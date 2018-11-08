package no.nav.foerstesidegenerator.consumer.metaforce.to;

//import lombok.Builder;
//import lombok.Data;
//import lombok.ToString;

/**
 * Transfer object for LoadInArchiveResponse
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
//@Data
//@Builder
//@ToString
public class LoadInArchiveResponseTo {
	private Integer docId;
	private Integer jobId;

	public LoadInArchiveResponseTo(Integer docId, Integer jobId) {
		this.docId = docId;
		this.jobId = jobId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	public String toString() {
		return "LoadInArchiveResponseTo{" +
				"docId=" + docId +
				", jobId=" + jobId +
				'}';
	}
}
