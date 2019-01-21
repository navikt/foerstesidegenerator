package no.nav.foerstesidegenerator.itest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.Resources;
import no.nav.foerstesidegenerator.Application;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.itest.config.ApplicationTestConfig;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, ApplicationTestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0, httpsPort = 8443)
@ActiveProfiles("itest")
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public abstract class AbstractIT {

	ObjectMapper mapper = new ObjectMapper();

	@Inject
	protected TestRestTemplate testRestTemplate;

	@Autowired
	protected FoerstesideRepository foerstesideRepository;

	@BeforeEach
	void setUp() {
		stubFor(get(urlPathMatching("/DOKUMENTTYPEINFO_V4(.*)"))
				.willReturn(aResponse().withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", "application/json")
						.withBodyFile("dokkat/happy-response.json")));

		stubFor(post("/METAFORCE").willReturn(aResponse().withStatus(HttpStatus.OK.value())
				.withBodyFile("metaforce/metaforce_createDocument-happy.xml")));
	}

	@AfterEach
	void tearDown() {
		WireMock.reset();
		WireMock.resetAllRequests();
		WireMock.removeAllMappings();

		foerstesideRepository.deleteAll();
	}

	static String classpathToString(String path) {
		return resourceUrlToString(Resources.getResource(path));
	}

	private static String resourceUrlToString(URL url) {
		try {
			return Resources.toString(url, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Could not convert url to String" + url);
		}
	}

	Foersteside getFoersteside() {
		return foerstesideRepository.findAll().iterator().next();
	}

}
