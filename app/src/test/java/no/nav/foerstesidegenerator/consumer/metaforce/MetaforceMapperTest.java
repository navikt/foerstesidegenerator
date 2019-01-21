package no.nav.foerstesidegenerator.consumer.metaforce;

import static no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest.Foerstesidetype.SKJEMA;
import static no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest.Spraakkode.NB;
import static no.nav.foerstesidegenerator.TestUtils.ADR_LINJE_1;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER;
import static no.nav.foerstesidegenerator.TestUtils.OSLO;
import static no.nav.foerstesidegenerator.TestUtils.POSTNR;
import static no.nav.foerstesidegenerator.TestUtils.TITTEL;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_1;
import static no.nav.foerstesidegenerator.TestUtils.VEDLEGG_2;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;

import no.nav.foerstesidegenerator.domain.Foersteside;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class MetaforceMapperTest {

	private static final String LOEPENUMMER = "123546789";

	private MetaforceMapper mapper = new MetaforceMapper();

	@Test
	void shouldMap() {
		Foersteside foersteside = createFoersteside(LOEPENUMMER);
		Document document = mapper.map(foersteside);

		Element documentElement = document.getDocumentElement();

		Assertions.assertEquals(NB.value(), documentElement.getElementsByTagName("spraakkode").item(0).getTextContent());
		Assertions.assertEquals(ADR_LINJE_1, documentElement.getElementsByTagName("adresselinje1").item(0).getTextContent());
		Assertions.assertEquals(POSTNR, documentElement.getElementsByTagName("postNr").item(0).getTextContent());
		Assertions.assertEquals(OSLO, documentElement.getElementsByTagName("poststed").item(0).getTextContent());
//		Assertions.assertEquals(NETS, documentElement.getElementsByTagName("NETS-postboks").item(0).getTextContent());
		Assertions.assertEquals(BRUKER, documentElement.getElementsByTagName("brukerID").item(0).getTextContent());
		Assertions.assertEquals(TITTEL, documentElement.getElementsByTagName("arkivtittel").item(0).getTextContent());
		Assertions.assertEquals(TITTEL, documentElement.getElementsByTagName("overskriftstittel").item(0).getTextContent());
		Assertions.assertEquals(SKJEMA.value(), documentElement.getElementsByTagName("foerstesideType").item(0).getTextContent());
		Assertions.assertEquals(LOEPENUMMER, documentElement.getElementsByTagName("loepenummer").item(0).getTextContent());
		Assertions.assertEquals(VEDLEGG_1, documentElement.getElementsByTagName("tittel").item(0).getTextContent());
		Assertions.assertEquals(VEDLEGG_2, documentElement.getElementsByTagName("tittel").item(1).getTextContent());
	}
}