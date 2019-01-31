package no.nav.foerstesidegenerator.consumer.metaforce.support;

import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Utility for doing Dom operations
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public final class DomUtil {

	private DomUtil() {
		//no-op - utility class
	}

	public static String elementToString(Element xmlElement) {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			Transformer transformer = factory.newTransformer();
			StringWriter stringWriter = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(xmlElement), new StreamResult(stringWriter));
			return stringWriter.toString();
		} catch (TransformerException e) {
			throw new IllegalStateException("Could not parse element to String " + xmlElement, e);
		}
	}


}
