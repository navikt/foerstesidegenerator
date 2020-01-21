package no.nav.foerstesidegenerator.service.support;

import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsaksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
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
				.spraakkode(Spraakkode.NB)
				.bruker(Bruker.builder()
						.brukerId(null)
						.brukerType(BrukerType.PERSON).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfBrukerTypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.bruker(Bruker.builder()
						.brukerId("abc")
						.brukerType(null).build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfOverskriftstittelIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfFoerstesidetypeIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(null)
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfSaksystemIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(null)
						.arkivsaksnummer("ref").build())
				.build();
		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@Test
	void shouldThrowExceptionIfArkivsaksnummerIsNull() {
		PostFoerstesideRequest request = PostFoerstesideRequest.builder()
				.spraakkode(Spraakkode.NB)
				.overskriftstittel("tittel")
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
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
		PostFoerstesideRequest request = createRequestWithAdresse("adresse", null, null, "nr",null);

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
	}

	@ParameterizedTest
	@ValueSource(strings = {"Nav-Consumer-Id", "x_consumerId", "consumerId","nav-consumerid"})
	void shouldValidateForCommonConsumerIdHeaders(String headerName){
		PostFoerstesideRequest request = createRequestWithAdresse();

		HttpHeaders parameterizedHeader = new HttpHeaders();
		parameterizedHeader.add(headerName, "MockConsumer");

		assertDoesNotThrow(() -> validator.validate(request, parameterizedHeader));
	}

	@Test
	void shouldThrowExceptionIfMissingCommonConsumerIdHeaders(){
		PostFoerstesideRequest request = createRequestWithAdresse();
		HttpHeaders headers = new HttpHeaders();

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, headers));
	}
}