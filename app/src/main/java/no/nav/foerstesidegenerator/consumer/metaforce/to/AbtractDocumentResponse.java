package no.nav.foerstesidegenerator.consumer.metaforce.to;

import javax.xml.datatype.Duration;

/**
 * Abstract class for DocumentResponse from Metaforce
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public abstract class AbtractDocumentResponse {
	private MetaforceDocumentType docFormat;
	private String extension;
	private String mimeType;
	private int length;

	private Duration executionTime;
	private Duration executionTimeInternal;

	protected AbtractDocumentResponse(MetaforceDocumentType docFormat, String extension, String mimeType, int length) {
		this.docFormat = docFormat;
		this.extension = extension;
		this.mimeType = mimeType;
		this.length = length;
	}

	public MetaforceDocumentType getDocFormat() {
		return docFormat;
	}

	public String getExtension() {
		return extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public int getLength() {
		return length;
	}

	public Duration getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Duration executionTime) {
		this.executionTime = executionTime;
	}

	public Duration getExecutionTimeInternal() {
		return executionTimeInternal;
	}

	public void setExecutionTimeInternal(Duration executionTimeInternal) {
		this.executionTimeInternal = executionTimeInternal;
	}
}
