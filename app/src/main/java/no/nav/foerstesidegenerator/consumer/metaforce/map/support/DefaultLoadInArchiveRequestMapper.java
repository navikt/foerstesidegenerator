package no.nav.foerstesidegenerator.consumer.metaforce.map.support;


import no.nav.foerstesidegenerator.consumer.metaforce.map.LoadInArchiveRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveRequestTo;
import se.metaforce.services.GSLoadInArchive;

/**
 * Implementation of {@link LoadInArchiveRequestMapper}
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class DefaultLoadInArchiveRequestMapper implements LoadInArchiveRequestMapper {
	@Override
	public GSLoadInArchive map(LoadInArchiveRequestTo requestTo) {
		return new GSLoadInArchive()
				.withMetaFile(requestTo.getMetaFile())
				.withDocument(requestTo.getDocument())
				.withTextRows(requestTo.getTextRows())
				.withData(new GSLoadInArchive.Data().withAny(requestTo.getData()))
				.withAttachments(getCreateAttachmentType(requestTo.getAttachments()));
	}

	private GSLoadInArchive.Attachments getCreateAttachmentType(Object attachments) {
		if(attachments == null) {
			return null;
		}

		return new GSLoadInArchive.Attachments().withAny(attachments);
	}
}
