package no.nav.foerstesidegenerator.service.support;

import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exceptions.InvalidTemaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostFoerstesideRequestValidatorTest {

	@InjectMocks
	private PostFoerstesideRequestValidator validator;

	@Test
	void shouldValidateOk() {
		PostFoerstesideRequest request = createRequestWithAdresse();
		assertDoesNotThrow(() -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsInvalid() {
		PostFoerstesideRequest request = createRequestWithAdresse();
		request.setTema("WWW");

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse();
		request.setTema(null);

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	// test på netsPostboks && adresse != null
	// test på netsPostboks && adresse == null
}