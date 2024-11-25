package no.nav.foerstesidegenerator.itest;

import no.nav.foerstesidegenerator.api.v1.FoerstesideResponse;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.lang.String.format;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID_PERSON;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER_PERSON;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CALLID;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
import static no.nav.foerstesidegenerator.domain.code.FagomradeCode.BID;
import static no.nav.foerstesidegenerator.rest.FoerstesideRestController.ROLE_FOERSTESIDEGENERATOR_LES;
import static no.nav.foerstesidegenerator.service.support.LuhnCheckDigitHelper.calculateCheckDigit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class FoerstesidegeneratorIT extends AbstractIT {

	private final static String OPPRETT_NY_FOERSTESIDE_URL = "/api/foerstesidegenerator/v1/foersteside";
	private final static String HENT_FOERSTESIDE_URL = "/api/foerstesidegenerator/v1/foersteside/%s";
	private static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";

	@Autowired
	public WebTestClient webTestClient;

	@BeforeEach
	public void setUpStubs() {
		stubDokmet("dokmet/happy-response.json");
		stubMetaforce();
	}

	@Test
	void skalOppretteNyFoerstesideMedStandardAdresse() {
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getFoersteside()).isNotNull();
		assertThat(response.getLoepenummer()).isNotNull();

		assertThat(foerstesideRepository.findAll().iterator().hasNext()).isNotNull();

		Foersteside foersteside = getFoersteside();

		assertThat(foersteside.getAdresselinje1()).isNull();
		assertThat(foersteside.getAdresselinje2()).isNull();
		assertThat(foersteside.getAdresselinje3()).isNull();
		assertThat(foersteside.getPostnummer()).isNull();
		assertThat(foersteside.getPoststed()).isNull();

		assertThat(foersteside.getNetsPostboks()).isEqualTo("4444");
		assertThat(foersteside.getAvsenderId()).isEqualTo("99988812345");
		assertThat(foersteside.getAvsenderNavn()).isEqualTo("navn navnesen");
		assertThat(foersteside.getBrukerId()).isEqualTo(BRUKER_ID_PERSON);
		assertThat(foersteside.getBrukerType()).isEqualTo(BRUKER_PERSON);
		assertThat(foersteside.getUkjentBrukerPersoninfo()).isNull();
		assertThat(foersteside.getTema()).isEqualTo("FOR");
		assertThat(foersteside.getBehandlingstema()).isNull();
		assertThat(foersteside.getArkivtittel()).isEqualTo("joark-tittel");
		assertThat(foersteside.getNavSkjemaId()).isEqualTo("NAV 13.37");
		assertThat(foersteside.getOverskriftstittel()).isEqualTo("tittel som printes");
		assertThat(foersteside.getSpraakkode()).isEqualTo("NB");
		assertThat(foersteside.getFoerstesidetype()).isEqualTo("SKJEMA");
		assertThat(foersteside.getVedleggListe()).isEqualTo("tittel 1;tittel 2");
		assertThat(foersteside.getEnhetsnummer()).isEqualTo("9999");
		assertThat(foersteside.getArkivsaksystem()).isEqualTo("GSAK");
		assertThat(foersteside.getArkivsaksnummer()).isEqualTo("ref");
		assertThat(foersteside.getDokumentlisteFoersteside()).isEqualTo("første tittel;andre tittel");
		assertThat(foersteside.getFoerstesideOpprettetAv()).isEqualTo("srvtest");
	}

	@Test
	void skalOppretteNyFoerstesideMedEgendefinertAdresse() {
		var request = createPostRequest("__files/input/happypath_egendefinertadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getFoersteside()).isNotNull();
		assertThat(response.getLoepenummer()).isNotNull();

		assertThat(foerstesideRepository.findAll().iterator().hasNext()).isNotNull();

		Foersteside foersteside = getFoersteside();

		assertThat(foersteside.getAdresselinje1()).isEqualTo("gateveien");
		assertThat(foersteside.getAdresselinje2()).isNull();
		assertThat(foersteside.getAdresselinje3()).isNull();
		assertThat(foersteside.getPostnummer()).isEqualTo("1234");
		assertThat(foersteside.getPoststed()).isEqualTo("Oslo");
	}

	@Test
	void skalOppretteNyFoerstesideMedUkjentBrukerPersoninfo() {
		var request = createPostRequest("__files/input/happypath_ukjentbrukerpersoninfo.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getFoersteside()).isNotNull();
		assertThat(response.getLoepenummer()).isNotNull();

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		assertThat(foersteside.getUkjentBrukerPersoninfo()).isEqualTo("her kommer det masse info om personen på en linje");
		assertThat(foersteside.getFoerstesideOpprettetAv()).isEqualTo("srvtest");
	}

	@Test
	void skalOppretteNyFoerstesideMedBrukerIdUtenMellomromNaarBrukerIdIRequestHarMellomrom() {
		var request = createPostRequest("__files/input/happypath_brukerid_mellomrom.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getFoersteside()).isNotNull();
		assertThat(response.getLoepenummer()).isNotNull();

		assertTrue(foerstesideRepository.findAll().iterator().hasNext());

		Foersteside foersteside = getFoersteside();
		assertThat(foersteside.getBrukerId()).isEqualTo(BRUKER_ID_PERSON);
		assertThat(foersteside.getBrukerType()).isEqualTo(BRUKER_PERSON);
		assertThat(foersteside.getUkjentBrukerPersoninfo()).isNull();
	}

	@Test
	void skalReturnereBadRequestForUgyldigBrukerId() {
		var request = createPostRequest("__files/input/ugyldig_brukerid.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains("Validering av ident feilet.");
	}

	@Test
	void skalHenteFoerstesideGittLoepenummer() {
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		String loepenummer = response.getLoepenummer();

		var foerstesideResponse = webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, loepenummer))
				.headers(headers -> getHeadersWithClaim(headers, ROLE_FOERSTESIDEGENERATOR_LES))
				.exchange()
				.expectStatus().isOk()
				.expectBody(FoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(foerstesideResponse).isNotNull();
		assertThat(foerstesideResponse.getBruker()).isNotNull();
		assertThat(foerstesideResponse.getBruker().getBrukerId()).isEqualTo(BRUKER_ID_PERSON);
		assertThat(foerstesideResponse.getBruker().getBrukerType().name()).isEqualTo(BRUKER_PERSON);

		var foersteside = getFoersteside();
		assertThat(foersteside.getLoepenummer()).isEqualTo(loepenummer);
		assertThat(foersteside.getUthentet()).isEqualTo(1);
		assertThat(foersteside.getBrukerId()).isEqualTo(BRUKER_ID_PERSON);
		assertThat(foersteside.getDatoUthentet()).isNotNull();
	}

	@Test
	void skalHenteFoerstesideGittLoepenummerMedKontrollsiffer() {
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).isNotNull();
		var loepenummer = response.getLoepenummer();
		String loepenummerWithCheckDigit = loepenummer + calculateCheckDigit(loepenummer);

		webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, loepenummerWithCheckDigit))
				.headers(headers -> getHeadersWithClaim(headers, ROLE_FOERSTESIDEGENERATOR_LES))
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	void skalHenteFoerstesideMedTemaBIDHvisTemaErOpprettetSomBID() {
		var request = createPostRequest("__files/input/happypath_tema_bid.json");

		webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated();

		assertThat(foerstesideRepository.findAll().iterator().hasNext()).isNotNull();
		Foersteside foersteside = getFoersteside();

		String loepenummer = foersteside.getLoepenummer();
		assertThat(foersteside.getTema()).isEqualTo(BID.name());

		var foerstesideResponse = webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, loepenummer))
				.headers(headers -> getHeadersWithClaim(headers, ROLE_FOERSTESIDEGENERATOR_LES))
				.exchange()
				.expectStatus().isOk()
				.expectBody(FoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(foerstesideResponse).isNotNull();
		assertThat(foerstesideResponse.getTema()).isEqualTo(BID.name());
	}

	@Test
	void skalReturnereUnauthorizedVedHentingAvFoerstesideUtenRolle() {
		var response = webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, "123"))
				.headers(headers -> getHeadersWithClaim(headers, "INVALID_ROLE"))
				.exchange()
				.expectStatus().isUnauthorized()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains("Required claims not present in token. [roles=foerstesidegenerator_les]");
	}

	@Test
	void skalReturnereBadRequestHvisLoepenummerIkkeValiderer() {
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var foersteside = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(PostFoerstesideResponse.class)
				.returnResult()
				.getResponseBody();

		assertThat(foersteside).isNotNull();
		var loepenummer = foersteside.getLoepenummer();
		String checkDigit = calculateCheckDigit(loepenummer);
		String loepenummerWithWrongCheckDigit = loepenummer + modifyCheckDigit(checkDigit);

		var response = webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, loepenummerWithWrongCheckDigit))
				.headers(headers -> getHeadersWithClaim(headers, ROLE_FOERSTESIDEGENERATOR_LES))
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains("Kontrollsiffer oppgitt er feil løpenummer");
	}

	@Test
	void skalReturnereNotFoundHvisLoepenummerIkkeFinnes() {
		String loepenummer = "1234567890000";

		var response = webTestClient.get()
				.uri(format(HENT_FOERSTESIDE_URL, loepenummer))
				.headers(headers -> getHeadersWithClaim(headers, ROLE_FOERSTESIDEGENERATOR_LES))
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains(format("Kan ikke finne foersteside med loepenummer=%s", loepenummer));
	}

	@Test
	void skalReturnereInternalServerErrorHvisDokmetReturnererNotFound() {
		stubDokmet(NOT_FOUND);
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains(format("Fant ingen dokumenttypeInfo med dokumenttypeId=%s", FOERSTESIDE_DOKUMENTTYPE_ID));
	}

	@Test
	void skalReturnereInternalServerErrorHvisDokmetReturnererInternalServerError() {
		stubDokmet(INTERNAL_SERVER_ERROR);
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains(format("Dokmet feilet teknisk for dokumenttypeId=%s", FOERSTESIDE_DOKUMENTTYPE_ID));
	}

	@Test
	void skalReturnereInternalServerErrorHvisDataManglerFraDokmet() {
		stubDokmet("dokmet/tkat020-missing-dokumentproduksjonsinfo.json");
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains(format("Dokumentproduksjonsinfo mangler for dokument med dokumenttypeId=%s.", FOERSTESIDE_DOKUMENTTYPE_ID));
	}

	@Test
	void skalReturnereInternalServerErrorHvisMetaforceReturnererInternalServerError() {
		stubMetaforce(INTERNAL_SERVER_ERROR);
		var request = createPostRequest("__files/input/happypath_standardadresse.json");

		var response = webTestClient.post()
				.uri(OPPRETT_NY_FOERSTESIDE_URL)
				.headers(this::getHeaders)
				.bodyValue(request)
				.exchange()
				.expectStatus().is5xxServerError()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();

		assertThat(response).contains("Kall mot Metaforce:GS_CreateDocument feilet teknisk for ikkeRedigerbarMalId=Foersteside");
	}

	private void getHeaders(HttpHeaders headers) {
		headers.setBearerAuth(getToken());
		setNavHeaders(headers);
	}

	private void getHeadersWithClaim(HttpHeaders headers, String role) {
		headers.setBearerAuth(getTokenWithClaims(role));
		setNavHeaders(headers);
	}

	private static void setNavHeaders(HttpHeaders headers) {
		headers.add(NAV_CONSUMER_ID, MDC_CONSUMER_ID);
		headers.add(NAV_CALLID, MDC_CALL_ID);
	}

}