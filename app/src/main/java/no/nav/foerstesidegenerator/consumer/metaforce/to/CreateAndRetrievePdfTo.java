package no.nav.foerstesidegenerator.consumer.metaforce.to;

//import lombok.Builder;
//import lombok.Data;

/**
 * Transfer object for Metaforce call
 */

//@Data
//@Builder
public class CreateAndRetrievePdfTo {
	private final String malLogikkFil;
	private final String ikkeRedigerbarMalId;
	private byte[] dokument;

	public CreateAndRetrievePdfTo(String malLogikkFil, String ikkeRedigerbarMalId, byte[] dokument) {
		this.malLogikkFil = malLogikkFil;
		this.ikkeRedigerbarMalId = ikkeRedigerbarMalId;
		this.dokument = dokument;
	}

	public String getMalLogikkFil() {
		return malLogikkFil;
	}

	public String getIkkeRedigerbarMalId() {
		return ikkeRedigerbarMalId;
	}

	public byte[] getDokument() {
		return dokument;
	}

	public void setDokument(byte[] dokument) {
		this.dokument = dokument;
	}
}
