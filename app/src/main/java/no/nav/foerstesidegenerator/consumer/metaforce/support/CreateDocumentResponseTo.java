package no.nav.foerstesidegenerator.consumer.metaforce.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import javax.xml.datatype.Duration;

/**
 * Transferobject for the org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn
 * response from metaforce GSCreateDocument webservice
 *
 * @author Hans Petter Simonsen - Visma Consulting AS
 */
@Builder
@Getter
@AllArgsConstructor
public class CreateDocumentResponseTo {

	private MetaforceDocumentType docFormat;
	private String extension;
	private String mimeType;
	private int length;
	private byte[] documentData;

	private Duration executionTime;
	private Duration executionTimeInternal;

	public CreateDocumentResponseTo(byte[] documentData) {
		this.docFormat = MetaforceDocumentType.NOTSET;
		this.extension = null;
		this.mimeType = null;
		this.length = documentData.length;
		this.documentData = ArrayUtils.clone(documentData);
	}

	public CreateDocumentResponseTo(byte[] documentData, MetaforceDocumentType docFormat,
									String extension, String mimeType, int length) {
		this.documentData = ArrayUtils.clone(documentData);
		this.docFormat = docFormat;
		this.extension = extension;
		this.mimeType = mimeType;
		this.length = length;
	}

	public void setExecutionTime(Duration executionTime) {
		this.executionTime = executionTime;
	}

	public void setExecutionTimeInternal(Duration executionTimeInternal) {
		this.executionTimeInternal = executionTimeInternal;
	}
}
