package no.nav.foerstesidegenerator.consumer.metaforce.to;

//import lombok.Builder;
//import lombok.Data;

import javax.xml.datatype.Duration;

/**
 * Transfer object for OpenDocumentResponse
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
//@Data
//@Builder
public class OpenDocumentResponseTo {
	private byte[] documentData;
	private MetaforceDocumentType documentFormat;
	private String extension;
	private String mimeType;
	private int length;

	private Duration executionTime;
	private Duration executionTimeInternal;

	public OpenDocumentResponseTo(byte[] documentData, MetaforceDocumentType documentFormat, String extension, String mimeType, int length, Duration executionTime, Duration executionTimeInternal) {
		this.documentData = documentData;
		this.documentFormat = documentFormat;
		this.extension = extension;
		this.mimeType = mimeType;
		this.length = length;
		this.executionTime = executionTime;
		this.executionTimeInternal = executionTimeInternal;
	}

	public byte[] getDocumentData() {
		return documentData;
	}

	public void setDocumentData(byte[] documentData) {
		this.documentData = documentData;
	}

	public MetaforceDocumentType getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(MetaforceDocumentType documentFormat) {
		this.documentFormat = documentFormat;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
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
