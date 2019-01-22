package no.nav.foerstesidegenerator.service.support;


import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.AVSENDER;
import static no.nav.foerstesidegenerator.TestUtils.BEHANDLINGSTEMA_AB1337;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER;
import static no.nav.foerstesidegenerator.TestUtils.ENHET_9999;
import static no.nav.foerstesidegenerator.TestUtils.NAVN;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.SAK_REF;
import static no.nav.foerstesidegenerator.TestUtils.SKJEMA_ID;
import static no.nav.foerstesidegenerator.TestUtils.TEMA_FAR;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithoutAdresse;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithoutAvsenderAndBruker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import no.nav.dok.foerstesidegenerator.api.v1.Bruker.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse.Spraakkode;
import no.nav.dok.foerstesidegenerator.api.v1.Sak.Saksystem;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class GetFoerstesideResponseMapperTest {

	@InjectMocks
	private GetFoerstesideResponseMapper mapper;

	@Test
	void shouldMapResponseWithAdresse() {
		Foersteside foersteside = createFoersteside("123456789");

		GetFoerstesideResponse response = mapper.map(foersteside);

		assertEquals(ADR_LINJE_1, response.getAdresse().getAdresselinje1());
		assertNull(response.getAdresse().getAdresselinje2());
		assertNull(response.getAdresse().getAdresselinje3());
		assertEquals(POSTNR, response.getAdresse().getPostnummer());
		assertEquals(OSLO, response.getAdresse().getPoststed());
		assertNull(response.getNetsPostboks());
		assertEquals(AVSENDER, response.getAvsender().getAvsenderId());
		assertEquals(NAVN, response.getAvsender().getAvsenderNavn());
		assertEquals(BRUKER, response.getBruker().getBrukerId());
		assertEquals(BrukerType.PERSON, response.getBruker().getBrukerType());
		assertNull(response.getUkjentBrukerPersoninfo());
		assertEquals(TEMA_FAR, response.getTema());
		assertEquals(BEHANDLINGSTEMA_AB1337, response.getBehandlingstema());
		assertEquals(TITTEL, response.getArkivtittel());
		assertEquals(SKJEMA_ID, response.getNavSkjemaId());
		assertEquals(TITTEL, response.getOverskriftstittel());
		assertEquals(Spraakkode.NB, response.getSpraakkode());
		assertEquals(Foerstesidetype.SKJEMA, response.getFoerstesidetype());
		assertEquals(Arrays.asList(VEDLEGG_1, VEDLEGG_2), response.getVedleggsliste());
		assertEquals(ENHET_9999, response.getEnhetsnummer());
		assertEquals(Saksystem.PSAK, response.getSak().getSaksystem());
		assertEquals(SAK_REF, response.getSak().getSaksreferanse());
	}

	@Test
	void shouldMapFoerstesideWithoutAdresse() {
		Foersteside foersteside = createFoerstesideWithoutAdresse("123");

		GetFoerstesideResponse response = mapper.map(foersteside);

		assertNull(response.getAdresse());
		assertNotNull(response.getNetsPostboks());
	}

	@Test
	void shouldMapFoerstesideWithoutAvsenderAndBruker() {
		Foersteside foersteside = createFoerstesideWithoutAvsenderAndBruker("1234");

		GetFoerstesideResponse response = mapper.map(foersteside);

		assertNull(response.getAvsender());
		assertNull(response.getBruker());
	}
}