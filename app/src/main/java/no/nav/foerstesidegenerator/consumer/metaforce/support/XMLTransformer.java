package no.nav.foerstesidegenerator.consumer.metaforce.support;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.exception.XMLTransformerException;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrevdataType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Slf4j
public final class XMLTransformer {

	private XMLTransformer() {
		//no-op
	}

	public static Element transformXML(BrevdataType brevdata) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			JAXBContext jaxbContext = JAXBContext.newInstance(BrevdataType.class);
			Marshaller marshaller = jaxbContext.createMarshaller();

			ObjectFactory objectFactory = new ObjectFactory();
			JAXBElement<BrevdataType> root = objectFactory.createBrevdata(brevdata);
			marshaller.marshal(root, document);

			return document.getDocumentElement();

		} catch (Exception e) {
			log.error("Kunne ikke konvertere XML med feilmelding={}", e.getMessage());
			throw new XMLTransformerException("Kunne ikke konvertere XML", e);
		}
	}
}
