package no.nav.foerstesidegenerator.itest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled
public class FoerstesidegeneratorIT extends AbstractIT {

	private final static String POST_URL = "/api/foerstesidegenerator/v1/foersteside";

	@Test
	@DisplayName("Ny førsteside - standardutfylt adresse")
	void happyPathStandardAdresse() throws Exception {
		PostFoerstesideRequest request = mapper.readValue(classpathToString("__files/tmot010/input/happy_input.json"), PostFoerstesideRequest.class);

		PostFoerstesideResponse response = testRestTemplate.postForObject(POST_URL, request, PostFoerstesideResponse.class);

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

	}

	@Test
	@DisplayName("Ny førsteside - egendefinert adresse")
	void happyPathEgendefinertAdresse() {

	}

	@Test
	@DisplayName("Ny førsteside - UkjentBrukerPersoninfo")
	void happyPathUkjentBrukerPersoninfo() {

	}


}
