package no.nav.foerstesidegenerator.service.support;

import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
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
	void shouldThrowExceptionIfSpraakkodeIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(null);
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfBrukerIdIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withBruker(new Bruker()
						.withBrukerId(null)
						.withBrukerType(Bruker.BrukerType.PERSON));
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfBrukerTypeIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withBruker(new Bruker()
						.withBrukerId("abc")
						.withBrukerType(null));
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfOverskriftstittelIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withOverskriftstittel(null);
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfFoerstesidetypeIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withOverskriftstittel("tittel")
				.withFoerstesidetype(null);
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfSaksystemIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withOverskriftstittel("tittel")
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withSak(new Sak()
						.withSaksystem(null)
						.withSaksreferanse("ref"));
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfSaksreferanseIsNull() {
		PostFoerstesideRequest request = new PostFoerstesideRequest()
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withOverskriftstittel("tittel")
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(null));
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsInvalid() {
		PostFoerstesideRequest request = createRequestWithTema("WWW");

		assertThrows(InvalidTemaException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfTemaIsNull() {
		PostFoerstesideRequest request = createRequestWithTema(null);

		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfNetsPostboksAndAdresseIsNull() {
		PostFoerstesideRequest request = createRequestWithoutAdresseAndNetsPostboks();

		assertThrows(FoerstesideGeneratorFunctionalException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfAdresselinje1IsNull() {
		PostFoerstesideRequest request = createRequestWithoutAdresseAndNetsPostboks().withAdresse(new Adresse()
				.withAdresselinje1(null)
				.withPostnummer("nr")
				.withPoststed("oslo"));
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}
}