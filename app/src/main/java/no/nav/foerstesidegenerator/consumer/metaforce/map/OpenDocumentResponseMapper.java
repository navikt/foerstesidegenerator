package no.nav.foerstesidegenerator.consumer.metaforce.map;

import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentResponseTo;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;

/**
 * Mapper which maps between domain and webservice responses for Metaforce openDocument
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public interface OpenDocumentResponseMapper {
	/**
	 * Map from metaforce DocumentReturn to transfer object
	 *
	 * @param response The metaforce response
	 * @return transfer object
	 */
	OpenDocumentResponseTo map(DocumentReturn response);
}
