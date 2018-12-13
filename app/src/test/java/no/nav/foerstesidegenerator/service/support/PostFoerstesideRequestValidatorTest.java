package no.nav.foerstesidegenerator.service.support;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostFoerstesideRequestValidatorTest {

	@InjectMocks
	private PostFoerstesideRequestValidator validator;

	@Mock
	private PostFoerstesideRequest request;

	@Test
	void shouldValidateOk() {
		when(request.getTema()).thenReturn("FAR");

		validator.validate(request);
	}

	@Test
	void shouldThrowExceptionIfTemaIsInvalid() {
		when(request.getTema()).thenReturn("WWW");

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsNull() {
		when(request.getTema()).thenReturn(null);

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}
}