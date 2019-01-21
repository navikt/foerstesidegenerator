package no.nav.foerstesidegenerator.itest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FoerstesidegeneratorIT extends AbstractIT {

	private final static String POST_URL = "/api/foerstesidegenerator/v1/foersteside";

	@Test
	@DisplayName("POST førsteside - standard adresse")
	void happyPathStandardAdresse() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_standardadresse.json"), PostFoerstesideRequest.class);

		PostFoerstesideResponse response = testRestTemplate.postForObject(POST_URL, request, PostFoerstesideResponse.class);

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		// assert stuff
	}

	@Test
	@DisplayName("POST førsteside - egendefinert adresse")
	void happyPathEgendefinertAdresse() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_egendefinertadresse.json"), PostFoerstesideRequest.class);

		PostFoerstesideResponse response = testRestTemplate.postForObject(POST_URL, request, PostFoerstesideResponse.class);

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		// assert stuff
	}

	@Test
	@DisplayName("POST førsteside - UkjentBrukerPersoninfo")
	void happyPathUkjentBrukerPersoninfo() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/input/happypath_ukjentbrukerpersoninfo.json"), PostFoerstesideRequest.class);

		PostFoerstesideResponse response = testRestTemplate.postForObject(POST_URL, request, PostFoerstesideResponse.class);

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		// assert stuff
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
