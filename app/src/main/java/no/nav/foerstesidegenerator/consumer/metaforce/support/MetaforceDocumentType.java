package no.nav.foerstesidegenerator.consumer.metaforce.support;

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
