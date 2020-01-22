package no.nav.foerstesidegenerator.domain;

import static no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype.NAV_INTERN;
import static no.nav.foerstesidegenerator.TestUtils.*;
import static no.nav.foerstesidegenerator.domain.FoerstesideMapper.TEMA_BIDRAG;
import static no.nav.foerstesidegenerator.domain.FoerstesideMapper.TEMA_FARSKAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.Arkivsaksystem;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class FoerstesideMapperTest {

	private static final String LOEPENUMMER = "***gammelt_fnr***01";

	private static HttpHeaders defaultHeaders = new HttpHeaders();

	@InjectMocks
	private FoerstesideMapper mapper;

	@BeforeAll
	static void setup() {
		defaultHeaders.add("Nav-Consumer-Id", "MockConsumer");
	}

	@Test
	void shouldMapRequestWithAdresse() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);

		assertEquals(ADR_LINJE_1, domain.getAdresselinje1());
		assertNull(domain.getAdresselinje2());
		assertNull(domain.getAdresselinje3());
		assertEquals(POSTNR, domain.getPostnummer());
		assertEquals(OSLO, domain.getPoststed());
		assertNull(domain.getNetsPostboks());
		assertEquals(AVSENDER, domain.getAvsenderId());
		assertEquals(NAVN, domain.getAvsenderNavn());
		assertEquals(BRUKER, domain.getBrukerId());
		assertEquals(BrukerType.PERSON.name(), domain.getBrukerType());
		assertNull(domain.getUkjentBrukerPersoninfo());
		assertEquals(TEMA_FORELDREPENGER, domain.getTema());
		assertEquals(BEHANDLINGSTEMA_AB1337, domain.getBehandlingstema());
		assertEquals(TITTEL, domain.getArkivtittel());
		assertEquals(SKJEMA_ID, domain.getNavSkjemaId());
		assertEquals(TITTEL, domain.getOverskriftstittel());
		assertEquals(Spraakkode.NB.name(), domain.getSpraakkode());
		assertEquals(Foerstesidetype.SKJEMA.name(), domain.getFoerstesidetype());
		assertEquals(String.join(";", VEDLEGG_1, VEDLEGG_2), domain.getVedleggListe());
		assertEquals(String.join(";", DOKUMENT_1, DOKUMENT_2), domain.getDokumentlisteFoersteside());
		assertEquals(ENHET_9999, domain.getEnhetsnummer());
		assertEquals(Arkivsaksystem.PSAK.name(), domain.getArkivsaksystem());
		assertEquals(SAK_REF, domain.getArkivsaksnummer());
		assertEquals("MockConsumer", domain.getFoerstesideOpprettetAv());
	}

	@Test
	void shouldMapRequestWithNetsPostboks() {
		PostFoerstesideRequest request = createRequestWithNetsPostboks();

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);
		assertNull(domain.getAdresselinje1());
		assertNull(domain.getAdresselinje2());
		assertNull(domain.getAdresselinje3());
		assertNull(domain.getPostnummer());
		assertNull(domain.getPoststed());
		assertEquals(NETS, domain.getNetsPostboks());
	}

	@Test
	void shouldMapFoerstesideTypeNAV_Intern(){
		PostFoerstesideRequest request = createRequestWithFoerstesideTypeNav_Intern();
		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);
		assertEquals(NAV_INTERN.name(),domain.getFoerstesidetype());
	}
	@Test
	void shouldMapUkjentBrukerPersoninfoIfBrukersIsAbsent() {
		PostFoerstesideRequest request = createRequestWithoutBruker("something");

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);
		assertNull(domain.getBrukerId());
		assertNull(domain.getBrukerType());
		assertNotNull(domain.getUkjentBrukerPersoninfo());
	}

	@Test
	void shouldMapEttersendelse() {
		PostFoerstesideRequest request = createRequestEttersendelse();

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);

		assertTrue(domain.getNavSkjemaId().startsWith("NAVe"));
	}

	@Test
	void shouldMapTemaBIDAsNull() {
		PostFoerstesideRequest request = createRequestWithTema(TEMA_BIDRAG);

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);

		assertNull(domain.getTema());
	}

	@Test
	void shouldMapTemaFARAsNull() {
		PostFoerstesideRequest request = createRequestWithTema(TEMA_FARSKAP);

		Foersteside domain = mapper.map(request, LOEPENUMMER, defaultHeaders);

		assertNull(domain.getTema());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Nav-Consumer-Id", "x_consumerId", "consumerId"})
	void shouldMapCommonConsumerIdHeadersToFoerstesideOpprettetAv(String headerName) {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(headerName, "MockConsumer");
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request, LOEPENUMMER, requestHeaders);

		assertEquals("MockConsumer", domain.getFoerstesideOpprettetAv());
	}


	@ParameterizedTest
	@ValueSource(strings = {"Nav-callid", "x-callid", "authorization"})
	void otherHeadersShouldNotMapToFoerstesideOpprettetAv(String headerName){
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(headerName, "MockConsumer");
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request, LOEPENUMMER, requestHeaders);

		assertNull(domain.getFoerstesideOpprettetAv());
	}

	@Test
	void emptyHeaderValueDoesNotMapOntoFoerstesideOpprettetAv() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Nav-Consumer-Id", "");
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request, LOEPENUMMER, requestHeaders);

		assertNull(domain.getFoerstesideOpprettetAv());
	}
}