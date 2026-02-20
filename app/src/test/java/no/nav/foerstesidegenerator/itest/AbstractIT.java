package no.nav.foerstesidegenerator.itest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
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
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(
		classes = {ApplicationLocal.class, ApplicationTestConfig.class},
		webEnvironment = RANDOM_PORT
)
@EnableWireMock
@ActiveProfiles("itest")
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@AutoConfigureTestRestTemplate
@AutoConfigureRestTestClient
@AutoConfigureWebTestClient
@Transactional
@EnableMockOAuth2Server
public abstract class AbstractIT {

	public static final String MDC_CALL_ID = UUID.randomUUID().toString();
	public static final String MDC_CONSUMER_ID = "srvtest";

	public static final LocalDateTime ELDRE_ENN_6_MAANEDER = LocalDateTime.now().minusMonths(6).minusDays(1);
	public static final LocalDateTime NYERE_ENN_6_MAANEDER = LocalDateTime.now().minusMonths(6).plusDays(1);

	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Autowired
	protected FoerstesideRepository foerstesideRepository;

	@Autowired
	private MockOAuth2Server server;

	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void cleanUp() {
		foerstesideRepository.deleteAll();
	}

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

	protected static void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

	protected String getToken() {
		return getToken(emptyMap());
	}

	protected String getTokenWithClaims(String role) {
		return getToken(Map.of("roles", role));
	}

	private String getToken(Map<String, Object> claims) {
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

	protected void stubDokmet(String bodyFile) {
		stubFor(get(urlPathMatching("/rest/dokumenttypeinfo/[0-9]*"))
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
						.withBodyFile(bodyFile)));
	}

	@SneakyThrows
	protected void stubLeaderElection() {
		stubFor(get("/leaderelection")
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
						.withBody("""
								{"name":"%s","last_update":"2023-12-13T09:46:08Z"}
								""".formatted(InetAddress.getLocalHost().getHostName()))));
	}

	protected void stubDokmet(HttpStatus httpStatus) {
		stubFor(get(urlPathMatching("/rest/dokumenttypeinfo/[0-9]*"))
				.willReturn(aResponse()
						.withStatus(httpStatus.value())));
	}

	protected void stubDokmetNotFound() {
		stubFor(get(urlPathMatching("/rest/dokumenttypeinfo/[0-9]*"))
				.willReturn(aResponse()
						.withBody("""
								{"timestamp":"2025-05-30T12:13:49.587+00:00","status":404,"error":"Not Found","message":"Fant ikke dokumenttypeId=000124","path":"/rest/basicauth/dokumenttypeinfo/000124"}
								""")
						.withStatus(NOT_FOUND.value())
				)
		);
	}

	protected void stubMetaforce() {
		stubFor(post("/metaforce")
				.willReturn(aResponse()
						.withStatus(OK.value())
						.withBodyFile("metaforce/metaforce_createDocument-happy.xml")));
	}

	protected void stubMetaforce(HttpStatus httpStatus) {
		stubFor(post("/metaforce")
				.willReturn(aResponse()
						.withStatus(httpStatus.value())
						.withBody("Something went wrong")));
	}

}