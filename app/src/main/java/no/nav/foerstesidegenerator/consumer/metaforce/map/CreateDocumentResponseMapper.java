package no.nav.foerstesidegenerator.consumer.metaforce.map;

import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;

/**
 * Mapper that maps between domain and webservice responses for Metaforce createDocument
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public interface CreateDocumentResponseMapper {

	/**
	 * Map from metaforce DocumentReturn to transfer object
	 *
	 * @param response The metaforce response
	 * @return transfer object
	 */
	CreateDocumentResponseTo map(DocumentReturn response);
}
