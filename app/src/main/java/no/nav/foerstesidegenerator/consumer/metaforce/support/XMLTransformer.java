package no.nav.foerstesidegenerator.consumer.metaforce.support;

import no.nav.foerstesidegenerator.foerstesidebrev.BrevdataType;
import no.nav.foerstesidegenerator.foerstesidebrev.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
			throw new IllegalArgumentException("Could not convert xml", e);
		}
	}
}
