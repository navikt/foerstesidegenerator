package no.nav.foerstesidegenerator.consumer.metaforce.map;

import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentRequestTo;
import se.metaforce.services.GSOpenDocument;

/**
 * Mapper which maps between domain and webservice requests for Metaforce openDocument
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public interface OpenDocumentRequestMapper {

	/**
	 * Map from OpenDocumentRequestTo to a parameter list
	 *
	 * @param requestTo transfer object
	 * @return list of parameters in a specific order
	 */
	GSOpenDocument map(OpenDocumentRequestTo requestTo);
}
