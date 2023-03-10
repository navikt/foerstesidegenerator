package no.nav.foerstesidegenerator.service.support;

import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exception.BrukerIdIkkeValidException;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static no.nav.dok.foerstesidegenerator.api.v1.code.Arkivsaksystem.PSAK;
import static no.nav.dok.foerstesidegenerator.api.v1.code.BrukerType.PERSON;
import static no.nav.dok.foerstesidegenerator.api.v1.code.Foerstesidetype.SKJEMA;
import static no.nav.dok.foerstesidegenerator.api.v1.code.Spraakkode.NB;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PostFoerstesideRequestValidatorTest {

	private static HttpHeaders defaultHeaders;

	@InjectMocks
	private PostFoerstesideRequestValidator validator;

	@BeforeAll
	static void setup() {
		defaultHeaders = new HttpHeaders();
		defaultHeaders.add("Nav-Consumer-Id", "MockConsumer");
	}

	@Test
	void shouldValidateOk() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		assertDoesNotThrow(() -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfSpraakkodeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(null)
				.build();

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfBrukerIdIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.bruker(Bruker.builder()
						.brukerId(null)
						.brukerType(PERSON).build())
				.build();
		assertThrows(BrukerIdIkkeValidException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfBrukerTypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.bruker(Bruker.builder()
						.brukerId(BRUKER_ID)
						.brukerType(PERSON).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfOverskriftstittelIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.overskriftstittel(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfFoerstesidetypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.overskriftstittel("tittel")
				.foerstesidetype(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfSaksystemIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.overskriftstittel("tittel")
				.foerstesidetype(SKJEMA)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(null)
						.arkivsaksnummer("ref").build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfArkivsaksnummerIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(NB)
				.overskriftstittel("tittel")
				.foerstesidetype(SKJEMA)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(PSAK)
						.arkivsaksnummer(null).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfTemaIsInvalid() {
		PostFoerstesideRequest request = createRequestWithTema("WWW");

		assertThrows(InvalidTemaException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfTemaIsNull() {
		PostFoerstesideRequest request = createRequestWithTema(null);

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfNetsPostboksAndAdresseIsNull() {
		PostFoerstesideRequest request = createRequestWithoutAdresseAndNetsPostboks();

		assertThrows(FoerstesideGeneratorFunctionalException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfAdresselinje1IsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse(null, null, null, "nr", "poststed");

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfPostNrIsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse("adresse", null, null, null, "poststed");

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfPoststedIsNull() {
		PostFoerstesideRequest request = createRequestWithAdresse("adresse", null, null, "nr", null);

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@ParameterizedTest
	@ValueSource(strings = {"Nav-Consumer-Id", "x_consumerId", "consumerId", "nav-consumerid"})
	void shouldValidateForCommonConsumerIdHeaders(String headerName) {
		PostFoerstesideRequest request = createRequestWithAdresse();

		HttpHeaders parameterizedHeader = new HttpHeaders();
		parameterizedHeader.add(headerName, "MockConsumer");

		assertDoesNotThrow(() -> validator.validate(request, parameterizedHeader));
	}

	@Test
	void shouldThrowExceptionIfMissingCommonConsumerIdHeaders() {
		PostFoerstesideRequest request = createRequestWithAdresse();
		HttpHeaders headers = new HttpHeaders();

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, headers));
	}
}