package no.nav.foerstesidegenerator.itest;

import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.exception.DokkatConsumerFunctionalException;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.transaction.TestTransaction;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static no.nav.foerstesidegenerator.service.support.LuhnCheckDigitHelper.calculateCheckDigit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoerstesidegeneratorIT extends AbstractIT {

	private final static String POST_URL = "/api/foerstesidegenerator/v1/foersteside";
	private final static String GET_URL = "/api/foerstesidegenerator/v1/foersteside/";

	@Test
	@DisplayName("POST førsteside - standard adresse")
	void happyPathStandardAdresse() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_standardadresse.json");

		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<PostFoerstesideResponse> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
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
		assertEquals("første tittel;andre tittel", foersteside.getDokumentlisteFoersteside());
		assertEquals("srvtest", foersteside.getFoerstesideOpprettetAv());
	}

	@Test
	@DisplayName("POST førsteside - egendefinert adresse")
	void happyPathEgendefinertAdresse() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_egendefinertadresse.json");

		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());

		ResponseEntity<PostFoerstesideResponse> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
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

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		assertEquals("her kommer det masse info om personen på en linje", foersteside.getUkjentBrukerPersoninfo());
		assertEquals("srvtest", foersteside.getFoerstesideOpprettetAv());
	}

	@Test
	@DisplayName("GET førsteside - Ok")
	void shouldHentFoerstesideGivenLoepenummer() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_standardadresse.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());
		ResponseEntity<PostFoerstesideResponse> postResponse = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());
		Foersteside foersteside = getFoersteside();
		String loepenummer = foersteside.getLoepenummer();

		ResponseEntity<GetFoerstesideResponse> getResponse = testRestTemplate.exchange(GET_URL + loepenummer, HttpMethod.GET, new HttpEntity<>(createHeaders()), GetFoerstesideResponse.class);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
		assertNotNull(getResponse.getBody().getBruker(), "Bruker skal være satt");
		assertEquals("***gammelt_fnr***", getResponse.getBody().getBruker().getBrukerId());
		assertEquals("PERSON", getResponse.getBody().getBruker().getBrukerType().name());

		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();

		foersteside = getFoersteside();
		assertEquals(loepenummer, foersteside.getLoepenummer());
		assertTrue(foersteside.getUthentet());
		assertEquals("***gammelt_fnr***", foersteside.getBrukerId());
		assertNotNull(foersteside.getDatoUthentet());
	}

	@Test
	@DisplayName("GET førsteside - Ok (løpenummer med kontrollsiffer)")
	void shouldHentFoerstesideGivenLoepenummerWithCheckDigit() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_standardadresse.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());
		ResponseEntity<PostFoerstesideResponse> postResponse = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());
		Foersteside foersteside = getFoersteside();

		String loepenummer = foersteside.getLoepenummer();
		String checkDigit = calculateCheckDigit(loepenummer);
		String loepenummerWithCheckDigit = loepenummer + checkDigit;

		ResponseEntity<GetFoerstesideResponse> getResponse = testRestTemplate.exchange(GET_URL + loepenummerWithCheckDigit, HttpMethod.GET, new HttpEntity<>(createHeaders()), GetFoerstesideResponse.class);

		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
	}

	@Test
	@DisplayName("GET førsteside tema BID - Ok (tema = null)")
	void shouldHentFoerstesideWithTemaNullWhenTemaOpprettetAsBID() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_tema_bid.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());
		ResponseEntity<PostFoerstesideResponse> postResponse = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());
		Foersteside foersteside = getFoersteside();

		String loepenummer = foersteside.getLoepenummer();
		assertNull(foersteside.getTema());

		ResponseEntity<GetFoerstesideResponse> getResponse = testRestTemplate.exchange(GET_URL + loepenummer, HttpMethod.GET, new HttpEntity<>(createHeaders()), GetFoerstesideResponse.class);
		assertEquals(HttpStatus.OK, getResponse.getStatusCode());
		assertNotNull(getResponse.getBody());
		assertNull(getResponse.getBody().getTema());
	}

	@Test
	@DisplayName("GET førsteside - 400 Løpenummer validerer ikke")
	void shouldThrowExceptionIfGivenLoepenummerDoesNotValidate() {
		PostFoerstesideRequest request = createPostRequest("__files/input/happypath_standardadresse.json");
		HttpEntity<PostFoerstesideRequest> requestHttpEntity = new HttpEntity<>(request, createHeaders());
		ResponseEntity<PostFoerstesideResponse> postResponse = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, PostFoerstesideResponse.class);

		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
		assertTrue(foerstesideRepository.findAll().iterator().hasNext());
		Foersteside foersteside = getFoersteside();

		String loepenummer = foersteside.getLoepenummer();
		String checkDigit = calculateCheckDigit(loepenummer);
		String loepenummerWithWrongCheckDigit = loepenummer + modifyCheckDigit(checkDigit);

		ResponseEntity<GetFoerstesideResponse> getResponse = testRestTemplate.exchange(GET_URL + loepenummerWithWrongCheckDigit, HttpMethod.GET, new HttpEntity<>(createHeaders()), GetFoerstesideResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, getResponse.getStatusCode());
	}

	@Test
	@DisplayName("GET førsteside - 404 not found")
	void shouldThrowExceptionWhenNoFoerstesideFoundForLoepenummer() {
		String loepenummer = "***gammelt_fnr***00";

		ResponseEntity<GetFoerstesideResponse> getResponse = testRestTemplate.exchange(GET_URL + loepenummer, HttpMethod.GET, new HttpEntity<>(createHeaders()), GetFoerstesideResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
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

		ResponseEntity<DokkatConsumerFunctionalException> response = testRestTemplate.postForEntity(POST_URL, requestHttpEntity, DokkatConsumerFunctionalException.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertNotNull(response.getBody());
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
		assertNotNull(response.getBody());
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
		assertNotNull(response.getBody());
		assertTrue(response.getBody().getMessage().startsWith("Kall mot Metaforce:GS_CreateDocument feilet teknisk for ikkeRedigerbarMalId=Foersteside"));
	}
}
