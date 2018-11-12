package no.nav.foerstesidegenerator.consumer.metaforce.to;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Transferobject for the org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn
 * response from metaforce GSCreateDocument webservice
 *
 * @author Hans Petter Simonsen - Visma Consulting AS
 */
public class CreateDocumentResponseTo extends AbtractDocumentResponse {

	private byte[] documentData;

	public CreateDocumentResponseTo(byte[] documentData) {
		super(MetaforceDocumentType.NOTSET, null, null, documentData.length);
		this.documentData = ArrayUtils.clone(documentData);
	}

	public CreateDocumentResponseTo(byte[] documentData, MetaforceDocumentType docFormat,
									String extension, String mimeType, int length) {
		super(docFormat, extension, mimeType, length);
		this.documentData = ArrayUtils.clone(documentData);
	}

	public byte[] getDocumentData() {
		return ArrayUtils.clone(documentData);
	}
}
