package no.nav.foerstesidegenerator.consumer.metaforce;

import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER;
import static no.nav.foerstesidegenerator.TestUtils.NETS;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.consumer.metaforce.MetaforceMapper.DEFAULT_NETS_POSTBOKS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class MetaforceMapperTest {

	private static final String LOEPENUMMER = "123546789";

	private MetaforceMapper mapper = new MetaforceMapper();

	@Test
	void shouldMapDefaultNetsPostboks() {
		Foersteside foersteside = createFoersteside(LOEPENUMMER);
		Document document = mapper.map(foersteside);

		Element documentElement = document.getDocumentElement();

		assertEquals(Spraakkode.NB.name(), documentElement.getElementsByTagName("spraakkode").item(0).getTextContent());
		assertEquals(ADR_LINJE_1, documentElement.getElementsByTagName("adresselinje1").item(0).getTextContent());
		assertEquals(POSTNR, documentElement.getElementsByTagName("postNr").item(0).getTextContent());
		assertEquals(OSLO, documentElement.getElementsByTagName("poststed").item(0).getTextContent());
		assertEquals(DEFAULT_NETS_POSTBOKS, documentElement.getElementsByTagName("NETS-postboks").item(0).getTextContent());
		assertEquals(BRUKER, documentElement.getElementsByTagName("brukerID").item(0).getTextContent());
		assertEquals(TITTEL, documentElement.getElementsByTagName("arkivtittel").item(0).getTextContent());
		assertEquals(TITTEL, documentElement.getElementsByTagName("overskriftstittel").item(0).getTextContent());
		assertEquals(Foerstesidetype.SKJEMA.name(), documentElement.getElementsByTagName("foerstesideType").item(0).getTextContent());
		assertEquals(LOEPENUMMER, documentElement.getElementsByTagName("loepenummer").item(0).getTextContent());
		assertEquals(VEDLEGG_1, documentElement.getElementsByTagName("tittel").item(0).getTextContent());
		assertEquals(VEDLEGG_2, documentElement.getElementsByTagName("tittel").item(1).getTextContent());
	}

	@Test
	void shouldMapGivenNetspostboks() {
		Foersteside foersteside = createFoersteside(LOEPENUMMER, NETS);
		Document document = mapper.map(foersteside);

		Element documentElement = document.getDocumentElement();

		assertEquals(NETS, documentElement.getElementsByTagName("NETS-postboks").item(0).getTextContent());
	}

}