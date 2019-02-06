package no.nav.foerstesidegenerator.itest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class FoerstesidegeneratorIT extends AbstractIT {

	private final static String POST_URL = "/api/foerstesidegenerator/v1/foersteside";

	@Test
	@DisplayName("POST førsteside - standard adresse")
	void happyPathStandardAdresse() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_standardadresse.json");

		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<PostFoerstesideResponse> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();

		assertNull(foersteside.getAdresselinje1());
		assertNull(foersteside.getAdresselinje2());
		assertNull(foersteside.getAdresselinje3());
		assertNull(foersteside.getPostnummer());
		assertNull(foersteside.getPoststed());

		assertEquals("4444", foersteside.getNetsPostboks());
		assertEquals("***gammelt_fnr***", foersteside.getAvsenderId());
		assertEquals("navn navnesen", foersteside.getAvsenderNavn());
		assertEquals("***gammelt_fnr***", foersteside.getBrukerId());
		assertEquals("PERSON", foersteside.getBrukerType());
		assertNull(foersteside.getUkjentBrukerPersoninfo());
		assertEquals("FOR", foersteside.getTema());
		assertNull(foersteside.getBehandlingstema());
		assertEquals("joark-tittel", foersteside.getArkivtittel());
		assertEquals("NAV 13.37", foersteside.getNavSkjemaId());
		assertEquals("tittel som printes", foersteside.getOverskriftstittel());
		assertEquals("NB", foersteside.getSpraakkode());
		assertEquals("SKJEMA", foersteside.getFoerstesidetype());
		assertEquals("tittel 1;tittel 2", foersteside.getVedleggListe());
		assertEquals("9999", foersteside.getEnhetsnummer());
		assertEquals("GSAK", foersteside.getArkivsaksystem());
		assertEquals("ref", foersteside.getArkivsaksnummer());

	}

	@Test
	@DisplayName("POST førsteside - egendefinert adresse")
	void happyPathEgendefinertAdresse() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_egendefinertadresse.json");

		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<PostFoerstesideResponse> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();

		assertEquals("gateveien", foersteside.getAdresselinje1());
		assertNull(foersteside.getAdresselinje2());
		assertNull(foersteside.getAdresselinje3());
		assertEquals("1234", foersteside.getPostnummer());
		assertEquals("Oslo", foersteside.getPoststed());

	}

	@Test
	@DisplayName("POST førsteside - UkjentBrukerPersoninfo")
	void happyPathUkjentBrukerPersoninfo() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_ukjentbrukerpersoninfo.json");

		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<PostFoerstesideResponse> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		assertEquals("her kommer det masse info om personen på en linje", foersteside.getUkjentBrukerPersoninfo());
	}

	@Test
	@DisplayName("GET førsteside - Ok")
	void shouldHentFoerstesideGivenLoepenummer() {
		// implement
	}

	@Test
	@DisplayName("GET førsteside - 404 not found")
	void shouldThrowExceptionWhenNoFoerstesideFoundForLoepenummer() {
		// implement
	}

	@Test
	@DisplayName("POST førsteside - Dokkat returns 404 not found")
	void shouldThrowExceptionIfDokkatReturns404NotFound() {
		stubFor(get(urlPathMatching("/DOKUMENTTYPEINFO_V4(.*)"))
				.willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value())
						.withHeader("Content-Type", "application/json")
						.withBody("Could not find dokumenttypeId: DOKTYPENOTFOUND in repository")));

		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_ukjentbrukerpersoninfo.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<FoerstesideGeneratorTechnicalException> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, FoerstesideGeneratorTechnicalException.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertTrue(response.getBody().getMessage().startsWith("TKAT020 feilet med statusKode=404 NOT_FOUND"));
	}

	@Test
	@DisplayName("POST førsteside - Dokkat returns 500 internal server error")
	void shouldThrowExceptionIfDokkatReturns500InternalServerError() {
		stubFor(get(urlPathMatching("/DOKUMENTTYPEINFO_V4(.*)"))
				.willReturn(aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.withHeader("Content-Type", "application/json")
						.withBody("Could not find dokumenttypeId: DOKTYPENOTFOUND in repository")));

		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_ukjentbrukerpersoninfo.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<FoerstesideGeneratorTechnicalException> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, FoerstesideGeneratorTechnicalException.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertTrue(response.getBody().getMessage().startsWith("TKAT020 feilet teknisk med statusKode=500 INTERNAL_SERVER_ERROR"));
	}

	@Test
	@DisplayName("POST førsteside - Metaforce returns 500 internal server error")
	void shouldThrowExceptionIfMetaforceReturns500InternalServerError() {
		stubFor(post("/METAFORCE")
				.willReturn(aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.withBody("Something went wrong")));

		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_ukjentbrukerpersoninfo.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<FoerstesideGeneratorTechnicalException> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, FoerstesideGeneratorTechnicalException.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertTrue(response.getBody().getMessage().startsWith("Kall mot Metaforce:GS_CreateDocument feilet teknisk for ikkeRedigerbarMalId=Foersteside"));
	}
}
