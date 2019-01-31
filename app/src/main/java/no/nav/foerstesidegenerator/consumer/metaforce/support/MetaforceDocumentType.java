package no.nav.foerstesidegenerator.consumer.metaforce.support;

/**
 * TransferObject for org.datacontract.schemas._2004._07.metaforce_common.Format in metaforce webservice
 *
 * @author Hans Petter Simonsen - Visma Consulting AS
 */
public enum MetaforceDocumentType {
	NOTSET("NOTSET"),
	PDF("PDF"),
	XML("XML"),
	AFPDS("AFPDS"),
	POSTSCRIPT("Postscript"),
	RTF("RTF"),
	PREVIEWPDF("PreviewPDF");

	private final String value;

	MetaforceDocumentType(String v) {
		this.value = v;
	}

	public String value() {
		return this.value;
	}

	public static MetaforceDocumentType fromValue(String v) {
		for (MetaforceDocumentType c : values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
