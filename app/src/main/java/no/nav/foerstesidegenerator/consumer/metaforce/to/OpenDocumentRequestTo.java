package no.nav.foerstesidegenerator.consumer.metaforce.to;

//import lombok.Builder;
//import lombok.Data;
//import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Transfer object for OpenDocumentRequest
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
//@Data
//@Builder
//@ToString
public class OpenDocumentRequestTo {
	@NotNull
	private Integer jobId;
	@NotNull
	private Integer docId;
	@NotNull
	private String printConfiguration;

	public OpenDocumentRequestTo(@NotNull Integer jobId, @NotNull Integer docId, @NotNull String printConfiguration) {
		this.jobId = jobId;
		this.docId = docId;
		this.printConfiguration = printConfiguration;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getPrintConfiguration() {
		return printConfiguration;
	}

	public void setPrintConfiguration(String printConfiguration) {
		this.printConfiguration = printConfiguration;
	}

	@Override
	public String toString() {
		return "OpenDocumentRequestTo{" +
				"jobId=" + jobId +
				", docId=" + docId +
				", printConfiguration='" + printConfiguration + '\'' +
				'}';
	}
}
