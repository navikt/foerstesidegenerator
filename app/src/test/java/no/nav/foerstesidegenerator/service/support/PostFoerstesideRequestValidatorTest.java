package no.nav.foerstesidegenerator.service.support;

import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.dok.foerstesidegenerator.api.v1.Saksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
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
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfBrukerIdIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.bruker(Bruker.builder()
						.brukerId(null)
						.brukerType(BrukerType.PERSON).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfBrukerTypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.bruker(Bruker.builder()
						.brukerId("abc")
						.brukerType(null).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfOverskriftstittelIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfFoerstesidetypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfSaksystemIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.sak(Sak.builder()
						.saksystem(null)
						.saksreferanse("ref").build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfSaksreferanseIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.sak(Sak.builder()
						.saksystem(Saksystem.PSAK)
						.saksreferanse(null).build())
				.build();
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
		PostFoerstesideRequest request = createRequestWithAdresse(null, null, null, "nr", "poststed");

		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfPostNrIsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse("adresse", null, null, null, "poststed");

		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}

	@Test
	void shouldThrowExceptionIfPoststedIsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse("adresse", null, null, "nr",null);

		assertThrows(InvalidRequestException.class, () -> validator.validate(request));
	}
}