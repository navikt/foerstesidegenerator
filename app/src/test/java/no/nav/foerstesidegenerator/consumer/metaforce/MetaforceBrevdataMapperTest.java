package no.nav.foerstesidegenerator.consumer.metaforce;

import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrevdataType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.FagType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.FoerstesideTypeKode;
import no.nav.foerstesidegenerator.xml.jaxb.gen.SpraakKode;
import org.junit.jupiter.api.Test;

import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID;
import static no.nav.foerstesidegenerator.TestUtils.DOKUMENT_1;
import static no.nav.foerstesidegenerator.TestUtils.DOKUMENT_2;
import static no.nav.foerstesidegenerator.TestUtils.NETS;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.TEMA_FORELDREPENGER;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.consumer.metaforce.MetaforceBrevdataMapper.DEFAULT_NETS_POSTBOKS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetaforceBrevdataMapperTest {

	private static final String LOEPENUMMER = "2019010200001";

	private MetaforceBrevdataMapper mapper = new MetaforceBrevdataMapper();

	@Test
	void shouldMapDefaultNetsPostboks() {
		Foersteside foersteside = createFoersteside(LOEPENUMMER);
		BrevdataType brevdata = mapper.map(foersteside);

		FagType fag = brevdata.getFag();

		assertEquals(SpraakKode.NB, brevdata.getFag().getSpraakkode());
		assertEquals(ADR_LINJE_1, fag.getAdresse().getAdresselinje1());
		assertEquals(POSTNR, fag.getAdresse().getPostNr());
		assertEquals(OSLO, fag.getAdresse().getPoststed());
		assertEquals(DEFAULT_NETS_POSTBOKS, fag.getNETSPostboks());
		assertEquals(BRUKER_ID, fag.getBruker().getBrukerID());
		assertEquals(TITTEL, fag.getOverskriftstittel());
		assertEquals(FoerstesideTypeKode.SKJEMA, fag.getFoerstesideType());
		assertTrue(fag.getLøpenummer().contains(LOEPENUMMER));
 		assertEquals(DOKUMENT_1, fag.getDokumentListe().getDokument().get(0).getDokumentTittel());
		assertEquals(DOKUMENT_2, fag.getDokumentListe().getDokument().get(1).getDokumentTittel());
		assertTrue(fag.getStrekkode2().contains("*" + LOEPENUMMER));
		assertTrue(fag.getStrekkode2().contains(DEFAULT_NETS_POSTBOKS));
		assertThat(fag.getTema()).isEqualTo(TEMA_FORELDREPENGER);
	}

	@Test
	void shouldMapGivenNetspostboks() {
		Foersteside foersteside = createFoersteside(LOEPENUMMER, NETS);
		BrevdataType brevdata = mapper.map(foersteside);

		FagType fag = brevdata.getFag();

		assertEquals(NETS, fag.getNETSPostboks());
		assertTrue(fag.getStrekkode2().contains("*" + LOEPENUMMER));
		assertTrue(fag.getStrekkode2().contains(NETS));
	}

}