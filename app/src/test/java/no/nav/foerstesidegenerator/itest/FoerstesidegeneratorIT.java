package no.nav.foerstesidegenerator.itest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class FoerstesidegeneratorIT extends AbstractIT {

	private final static String POST_URL = "/api/foerstesidegenerator/v1/foersteside";

	@Test
	@DisplayName("POST førsteside - standard adresse")
	void happyPathStandardAdresse() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_standardadresse.json"), PostFoerstesideRequest.class);

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
		assertEquals("GSAK", foersteside.getSaksystem());
		assertEquals("ref", foersteside.getSaksreferanse());

	}

	@Test
	@DisplayName("POST førsteside - egendefinert adresse")
	void happyPathEgendefinertAdresse() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_egendefinertadresse.json"), PostFoerstesideRequest.class);

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
	void happyPathUkjentBrukerPersoninfo() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_ukjentbrukerpersoninfo.json"), PostFoerstesideRequest.class);

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
}
