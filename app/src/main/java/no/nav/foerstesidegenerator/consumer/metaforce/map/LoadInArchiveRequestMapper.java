package no.nav.foerstesidegenerator.consumer.metaforce.map;

import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveRequestTo;
import se.metaforce.services.GSLoadInArchive;

/**
 * Mapper which maps between domain and webservice requests for Metaforce loadInArchive
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public interface LoadInArchiveRequestMapper {
	/**
	 * Map from LoadInArchiveRequestTo to a parameter list
	 *
	 * @param requestTo transfer object
	 * @return list of parameters in a specific order
	 */
	GSLoadInArchive map(LoadInArchiveRequestTo requestTo);
}
