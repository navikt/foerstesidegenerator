package no.nav.foerstesidegenerator.service.support;

import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
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
		PostFoerstesideRequest request = createRequestWithTema("WWW");

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsNull() {
		PostFoerstesideRequest request = createRequestWithTema(null);

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfNetsPostboksAndAdresseIsNull() {
		PostFoerstesideRequest request = createRequestWithoutAdresseAndNetsPostboks();

		assertThrows(FoerstesideGeneratorFunctionalException.class, () -> validator.validate(request));
	}
}