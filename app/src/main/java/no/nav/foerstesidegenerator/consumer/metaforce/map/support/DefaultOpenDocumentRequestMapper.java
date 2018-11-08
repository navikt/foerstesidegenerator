package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

import no.nav.foerstesidegenerator.consumer.metaforce.map.OpenDocumentRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentRequestTo;
import se.metaforce.services.GSOpenDocument;

/**
 * Implementation of {@link OpenDocumentRequestMapper}
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class DefaultOpenDocumentRequestMapper implements OpenDocumentRequestMapper {
	@Override
	public GSOpenDocument map(OpenDocumentRequestTo requestTo) {
		return new GSOpenDocument()
				.withJobId(requestTo.getJobId())
				.withDocId(requestTo.getDocId())
				.withPrintConfiguration(requestTo.getPrintConfiguration());
	}
}
