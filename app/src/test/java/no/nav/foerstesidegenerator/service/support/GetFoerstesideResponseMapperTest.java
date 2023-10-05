package no.nav.foerstesidegenerator.service.support;


import no.nav.dok.foerstesidegenerator.api.v1.FoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static no.nav.dok.foerstesidegenerator.api.v1.code.Arkivsaksystem.PSAK;
import static no.nav.dok.foerstesidegenerator.api.v1.code.BrukerType.PERSON;
import static no.nav.foerstesidegenerator.TestUtils.AVSENDER;
import static no.nav.foerstesidegenerator.TestUtils.BEHANDLINGSTEMA_AB1337;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID;
import static no.nav.foerstesidegenerator.TestUtils.CONSUMER_ID;
import static no.nav.foerstesidegenerator.TestUtils.ENHET_9999;
import static no.nav.foerstesidegenerator.TestUtils.NAVN;
import static no.nav.foerstesidegenerator.TestUtils.SAK_REF;
import static no.nav.foerstesidegenerator.TestUtils.SKJEMA_ID;
import static no.nav.foerstesidegenerator.TestUtils.TEMA_FORELDREPENGER;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithTemaOKO;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithoutAvsenderAndBruker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class GetFoerstesideResponseMapperTest {

	@InjectMocks
	private FoerstesideResponseMapper mapper;

	@Test
	void shouldMapResponseWithAdresse() {
		Foersteside foersteside = createFoersteside("123456789");

		FoerstesideResponse response = mapper.map(foersteside);

		assertEquals(AVSENDER, response.getAvsender().getAvsenderId());
		assertEquals(NAVN, response.getAvsender().getAvsenderNavn());
		assertEquals(BRUKER_ID, response.getBruker().getBrukerId());
		assertEquals(PERSON, response.getBruker().getBrukerType());
		assertEquals(TEMA_FORELDREPENGER, response.getTema());
		assertEquals(BEHANDLINGSTEMA_AB1337, response.getBehandlingstema());
		assertEquals(TITTEL, response.getArkivtittel());
		assertEquals(SKJEMA_ID, response.getNavSkjemaId());
		assertEquals(Arrays.asList(VEDLEGG_1, VEDLEGG_2), response.getVedleggsliste());
		assertEquals(ENHET_9999, response.getEnhetsnummer());
		assertEquals(PSAK, response.getArkivsak().getArkivsaksystem());
		assertEquals(SAK_REF, response.getArkivsak().getArkivsaksnummer());
		assertEquals(CONSUMER_ID, response.getFoerstesideOpprettetAv());
	}

	@Test
	void shouldMapFoerstesideWithoutAvsenderAndBruker() {
		Foersteside foersteside = createFoerstesideWithoutAvsenderAndBruker("1234");

		FoerstesideResponse response = mapper.map(foersteside);

		assertNull(response.getAvsender());
		assertNull(response.getBruker());
	}

	@Test
	void shouldMapFoerstesideWithDomainTemaOKOtoResponseTemaSTO() {
		Foersteside foersteside = createFoerstesideWithTemaOKO("123456789");

		FoerstesideResponse response = mapper.map(foersteside);

		assertThat(response.getTema()).isEqualTo("STO");
	}
}