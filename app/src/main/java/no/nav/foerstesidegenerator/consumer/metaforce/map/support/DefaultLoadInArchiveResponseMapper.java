package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

import no.nav.foerstesidegenerator.consumer.metaforce.map.LoadInArchiveResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveResponseTo;
import org.datacontract.schemas._2004._07.metaforce_common.UpdateDocReturn;

/**
 * Implementation of {@link LoadInArchiveResponseMapper}
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class DefaultLoadInArchiveResponseMapper implements LoadInArchiveResponseMapper {
	@Override
	public LoadInArchiveResponseTo map(UpdateDocReturn response) {
//		return LoadInArchiveResponseTo.builder()
//				.docId(response.getDocID())
//				.jobId(response.getJobID())
//				.build();
		return new LoadInArchiveResponseTo(response.getDocID(), response.getJobID());
	}
}
