package no.nav.foerstesidegenerator.service.support;

import no.nav.foerstesidegenerator.api.v1.Adresse;
import no.nav.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.foerstesidegenerator.api.v1.Avsender;
import no.nav.foerstesidegenerator.api.v1.Bruker;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exception.BrukerIdIkkeValidException;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Stream;

import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID_PERSON;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.createBaseRequest;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithBrukerId;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithTema;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutAdresseAndNetsPostboks;
import static no.nav.foerstesidegenerator.api.v1.code.Arkivsaksystem.PSAK;
import static no.nav.foerstesidegenerator.api.v1.code.BrukerType.PERSON;
import static no.nav.foerstesidegenerator.api.v1.code.Foerstesidetype.SKJEMA;
import static no.nav.foerstesidegenerator.api.v1.code.Spraakkode.NB;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
import static no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator.ARKIVTITTEL_MAX_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostFoerstesideRequestValidatorTest {

	private static HttpHeaders defaultHeaders;
	private final PostFoerstesideRequestValidator validator = new PostFoerstesideRequestValidator();

	@BeforeAll
	static void setup() {
		defaultHeaders = new HttpHeaders();
		defaultHeaders.add(NAV_CONSUMER_ID, "MockConsumer");
	}

	@Test
	void shouldValidateOk() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		assertDoesNotThrow(() -> validator.validate(request, defaultHeaders));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"01117400200", // fnr
			"58088349006", // dnr
			"13442157949", // hnr
			"02239969375", // npid
	})
	void shouldValidateOkIdents(String brukerId) {
		PostFoerstesideRequest request = createRequestWithBrukerId(brukerId);

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
						.brukerId(BRUKER_ID_PERSON)
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
	void shouldThrowExceptionIfLongArkivtittel() {
		int faktiskAntallTegn = ARKIVTITTEL_MAX_LENGTH + 1;
		PostFoerstesideRequest request = createBaseRequest()
				.arkivtittel("a".repeat(faktiskAntallTegn))
				.build();

		InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> validator.validate(request, defaultHeaders));
		assertThat(exception.getMessage()).isEqualTo("Arkivtittel kan ha maks lengde på %d tegn. Faktisk lengde=%d".formatted(ARKIVTITTEL_MAX_LENGTH, faktiskAntallTegn));
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

	@Test
	void shouldValidateForNavConsumerIdHeader() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		HttpHeaders parameterizedHeader = new HttpHeaders();
		parameterizedHeader.add(NAV_CONSUMER_ID, "MockConsumer");

		assertDoesNotThrow(() -> validator.validate(request, parameterizedHeader));
	}

	@Test
	void shouldThrowExceptionIfMissingCommonConsumerIdHeaders() {
		PostFoerstesideRequest request = createRequestWithAdresse();
		HttpHeaders headers = new HttpHeaders();

		assertThrows(InvalidRequestException.class, () -> validator.validate(request, headers));
	}

	@ParameterizedTest
	@MethodSource
	void shouldThrowExceptionOnUlovligeTegn(PostFoerstesideRequest request, String felt, String ulovligVerdi) {
		//arkivtittel
		//adresse.adresselinje1
		//adresse.adresselinje2
		//adresse.adresselinje3
		//adresse.poststed
		//overskriftstittel
		//vedleggsliste
		//dokumentlisteFoersteside
		//avsender.avsendernavn
		//ukjentBrukerPersoninfo

		assertThatExceptionOfType(InvalidRequestException.class)
				.isThrownBy(() -> validator.validate(request, defaultHeaders))
				.withMessage("%s inneholder ulovlige tegn. Mottatt verdi=%s", felt, ulovligVerdi);
	}

	private static Stream<Arguments> shouldThrowExceptionOnUlovligeTegn() {
		final Stream<String> ulovligeTegn = Stream.of("\\", "|", "*", "=");

		return ulovligeTegn.flatMap(tegn -> Stream.of(
				Arguments.of(createBaseRequest().arkivtittel(tegn).build(), "Arkivtittel", tegn),
				Arguments.of(createBaseRequest().adresse(Adresse.builder().adresselinje1(tegn).postnummer(POSTNR).poststed(OSLO).build()).build(), "Adresselinje1", tegn),
				Arguments.of(createBaseRequest().adresse(Adresse.builder().adresselinje1("a").adresselinje2(tegn).postnummer(POSTNR).poststed(OSLO).build()).build(), "Adresselinje2", tegn),
				Arguments.of(createBaseRequest().adresse(Adresse.builder().adresselinje1("a").adresselinje3(tegn).postnummer(POSTNR).poststed(OSLO).build()).build(), "Adresselinje3", tegn),
				Arguments.of(createBaseRequest().adresse(Adresse.builder().adresselinje1("a").postnummer(POSTNR).poststed(tegn).build()).build(), "Poststed", tegn),
				Arguments.of(createBaseRequest().overskriftstittel(tegn).build(), "Tittel", tegn),
				Arguments.of(createBaseRequest().vedleggsliste(List.of(tegn)).build(), "Ett eller flere vedlegg i vedleggsliste", "[%s]".formatted(tegn)),
				Arguments.of(createBaseRequest().dokumentlisteFoersteside(List.of(tegn)).build(), "Ett eller flere vedlegg i dokumentlisteFoersteside", "[%s]".formatted(tegn)),
				Arguments.of(createBaseRequest().avsender(Avsender.builder().avsenderNavn(tegn).build()).build(), "AvsenderNavn", tegn),
				Arguments.of(createBaseRequest().ukjentBrukerPersoninfo(tegn).build(), "UkjentBrukerPersoninfo", tegn)
		));
	}

	@ParameterizedTest
	@MethodSource
	void shouldThrowExceptionOnIkkeTallVerdi(PostFoerstesideRequest request, String felt, String ulovligVerdi) {
		//netsPostboks
		//adresse.postnummer
		//enhetsnummer
		//avsender.avsenderId

		assertThatExceptionOfType(InvalidRequestException.class)
				.isThrownBy(() -> validator.validate(request, defaultHeaders))
				.withMessage("%s kan kun inneholde siffer. Mottatt verdi=%s", felt, ulovligVerdi);
	}

	private static Stream<Arguments> shouldThrowExceptionOnIkkeTallVerdi() {
		final Stream<String> ulovligeTegn = Stream.of("a", "A", "*", "=");

		return ulovligeTegn.flatMap(tegn -> Stream.of(
				Arguments.of(createBaseRequest().netsPostboks(tegn).build(), "NETS-postboks", tegn),
				Arguments.of(createBaseRequest().adresse(Adresse.builder().adresselinje1("a").postnummer(tegn).poststed(OSLO).build()).build(), "Postnummer", tegn),
				Arguments.of(createBaseRequest().enhetsnummer(tegn).build(), "Enhetsnummer", tegn),
				Arguments.of(createBaseRequest().avsender(Avsender.builder().avsenderId(tegn).build()).build(), "AvsenderId", tegn)
		));
	}


	@ParameterizedTest
	@ValueSource(strings = {"\\", "|", "*", "="})
	void shouldThrowExceptionOnIkkeTallEllerBokstavVerdi(String ulovligVerdi) {
		PostFoerstesideRequest request = createBaseRequest()
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(PSAK)
						.arkivsaksnummer(ulovligVerdi)
						.build())
				.build();

		//arkivsak.arkivsaksnummer

		assertThatExceptionOfType(InvalidRequestException.class)
				.isThrownBy(() -> validator.validate(request, defaultHeaders))
				.withMessage("Arkivsaksnummer kan kun inneholde siffer og/eller bokstaver. Mottatt verdi=%s", ulovligVerdi);
	}

	@ParameterizedTest
	@ValueSource(strings = {"NAV", "NAV-1234", "NAV-1234-5678"})
	void shouldThrowExceptionOnInvalidBehandlingstema(String behandlingstema) {
		PostFoerstesideRequest request = createBaseRequest()
				.behandlingstema(behandlingstema)
				.build();

		assertThatExceptionOfType(InvalidRequestException.class)
				.isThrownBy(() -> validator.validate(request, defaultHeaders))
				.withMessage("Behandlingstema må være på formatet 'To bokstaver, så fire tall'. Eksempel: ab1234. Mottatt verdi=%s", behandlingstema);
	}

	@ParameterizedTest
	@ValueSource(strings = {"NAV*", "NAV/1234", "NAV123_"})
	void shouldThrowExceptionOnInvalidNavSkjemaId(String navSkjemaId) {
		PostFoerstesideRequest request = createBaseRequest()
				.navSkjemaId(navSkjemaId)
				.build();

		assertThatExceptionOfType(InvalidRequestException.class)
				.isThrownBy(() -> validator.validate(request, defaultHeaders))
				.withMessage("NAV-skjemaId kan kun inneholde bokstaver, tall, mellomrom, punktum og bindestrek. Mottatt verdi=%s", navSkjemaId);
	}
}