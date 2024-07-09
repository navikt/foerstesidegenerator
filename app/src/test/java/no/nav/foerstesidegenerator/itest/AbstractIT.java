package no.nav.foerstesidegenerator.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import no.nav.foerstesidegenerator.ApplicationLocal;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.itest.config.ApplicationTestConfig;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback;
import no.nav.security.token.support.spring.test.EnableMockOAuth2Server;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static java.util.Collections.emptyMap;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
		classes = {ApplicationLocal.class, ApplicationTestConfig.class},
		webEnvironment = RANDOM_PORT
)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("itest")
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@EnableMockOAuth2Server
@AutoConfigureCache
public abstract class AbstractIT {

	public static final String MDC_CALL_ID = UUID.randomUUID().toString();
	public static final String MDC_CONSUMER_ID = "srvtest";

	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Autowired
	protected FoerstesideRepository foerstesideRepository;

	@Autowired
	private MockOAuth2Server server;

	private final ObjectMapper mapper = new ObjectMapper();

	protected static String classpathToString(String path) {
		return resourceUrlToString(Resources.getResource(path));
	}

	private static String resourceUrlToString(URL url) {
		try {
			return Resources.toString(url, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert url to String" + url);
		}
	}

	@BeforeEach
	void setUp() {
		stubFor(get(urlPathMatching("/DOKUMENTTYPEINFO_V4(.*)"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json")
						.withBodyFile("dokkat/happy-response.json")));

		stubFor(post("/METAFORCE")
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withBodyFile("metaforce/metaforce_createDocument-happy.xml")));

		stubAzureToken();
		foerstesideRepository.deleteAll();
	}

	protected static void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

	private String getToken() {
		return getToken(emptyMap());
	}

	protected String getToken(Map<String, Object> claims) {
		String issuerId = "azurev2";
		String audience = "skanmot";
		String subject = "srvtest";
		return server.issueToken(
				issuerId,
				"foerstesidegenerator",
				new DefaultOAuth2TokenCallback(
						issuerId,
						subject,
						"JWT",
						List.of(audience),
						claims,
						3600
				)
		).serialize();
	}

	protected HttpHeaders createHeadersWithTokenWithRole(String role) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken(Map.of("roles", role)));
		headers.add("Nav-Consumer-Id", MDC_CONSUMER_ID);
		headers.add("Nav-Callid", MDC_CALL_ID);
		return headers;
	}

	protected HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getToken());
		headers.add("Nav-Consumer-Id", MDC_CONSUMER_ID);
		headers.add("Nav-Callid", MDC_CALL_ID);
		return headers;
	}

	protected PostFoerstesideRequest createPostRequest(String filepath) {
		try {
			return mapper.readValue(classpathToString(filepath), PostFoerstesideRequest.class);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert filepath to PostFoerstesideRequest");
		}
	}

	Foersteside getFoersteside() {
		return foerstesideRepository.findAll().iterator().next();
	}

	String modifyCheckDigit(String checkDigit) {
		int c1 = Integer.parseInt(checkDigit);
		if (c1 > 0 && c1 < 10) {
			return String.valueOf(c1 - 1);
		} else {
			return String.valueOf(c1 + 1);
		}
	}

	protected void stubAzureToken() {
		stubFor(post("/azure_token")
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.withBodyFile("azure/token_response_dummy.json")));
	}
}
