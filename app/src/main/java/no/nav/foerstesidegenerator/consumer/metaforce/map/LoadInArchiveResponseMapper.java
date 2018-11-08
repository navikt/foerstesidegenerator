package no.nav.foerstesidegenerator.consumer.metaforce.map;

import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveResponseTo;
import org.datacontract.schemas._2004._07.metaforce_common.UpdateDocReturn;

/**
 * Mapper which maps between domain and webservice responses for Metaforce loadInArchive
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public interface LoadInArchiveResponseMapper {

	/**
	 * Map from metaforce UpdateDocReturn to transfer object
	 *
	 * @param response The metaforce response
	 * @return transfer object
	 */
	LoadInArchiveResponseTo map(UpdateDocReturn response);
}
