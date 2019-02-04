package no.nav.foerstesidegenerator.domain;

import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.AVSENDER;
import static no.nav.foerstesidegenerator.TestUtils.BEHANDLINGSTEMA_AB1337;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER;
import static no.nav.foerstesidegenerator.TestUtils.ENHET_9999;
import static no.nav.foerstesidegenerator.TestUtils.NAVN;
import static no.nav.foerstesidegenerator.TestUtils.NETS;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.SAK_REF;
import static no.nav.foerstesidegenerator.TestUtils.SKJEMA_ID;
import static no.nav.foerstesidegenerator.TestUtils.TEMA_FAR;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createRequestEttersendelse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithNetsPostboks;
import static no.nav.foerstesidegenerator.TestUtils.createRequestWithoutBruker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Saksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoerstesideMapperTest {

	@InjectMocks
	private FoerstesideMapper mapper;

	@Test
	void shouldMapRequestWithAdresse() {
		PostFoerstesideRequest request = createRequestWithAdresse();

		Foersteside domain = mapper.map(request);

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
		assertEquals(TEMA_FAR, domain.getTema());
		assertEquals(BEHANDLINGSTEMA_AB1337, domain.getBehandlingstema());
		assertEquals(TITTEL, domain.getArkivtittel());
		assertEquals(SKJEMA_ID, domain.getNavSkjemaId());
		assertEquals(TITTEL, domain.getOverskriftstittel());
		assertEquals(Spraakkode.NB.name(), domain.getSpraakkode());
		assertEquals(Foerstesidetype.SKJEMA.name(), domain.getFoerstesidetype());
		assertEquals(String.join(";", VEDLEGG_1, VEDLEGG_2), domain.getVedleggListe());
		assertEquals(ENHET_9999, domain.getEnhetsnummer());
		assertEquals(Saksystem.PSAK.name(), domain.getSaksystem());
		assertEquals(SAK_REF, domain.getSaksreferanse());
	}

	@Test
	void shouldMapRequestWithNetsPostboks() {
		PostFoerstesideRequest request = createRequestWithNetsPostboks();

		Foersteside domain = mapper.map(request);
		assertNull(domain.getAdresselinje1());
		assertNull(domain.getAdresselinje2());
		assertNull(domain.getAdresselinje3());
		assertNull(domain.getPostnummer());
		assertNull(domain.getPoststed());
		assertEquals(NETS, domain.getNetsPostboks());
	}

	@Test
	void shouldMapUkjentBrukerPersoninfoIfBrukersIsAbsent() {
		PostFoerstesideRequest request = createRequestWithoutBruker("something");

		Foersteside domain = mapper.map(request);
		assertNull(domain.getBrukerId());
		assertNull(domain.getBrukerType());
		assertNotNull(domain.getUkjentBrukerPersoninfo());
	}

	@Test
	void shouldMapEttersendelse() {
		PostFoerstesideRequest request = createRequestEttersendelse();

		Foersteside domain = mapper.map(request);

		assertTrue(domain.getNavSkjemaId().startsWith("NAVe"));
	}
}