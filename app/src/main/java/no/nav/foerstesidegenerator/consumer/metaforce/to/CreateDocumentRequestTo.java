package no.nav.foerstesidegenerator.consumer.metaforce.to;

import org.w3c.dom.Element;

import javax.validation.constraints.NotNull;

/**
 * TransterObject for se.metaforce.services.GSCreateDocument request in Metaforce GSCreateDocument webservice
 * @author Hans Petter Simonsen - Visma Consulting AS
 */
public class CreateDocumentRequestTo {
	@NotNull
	private String metafile;
	@NotNull
	private String document;
	@NotNull
	private Object data;
	@NotNull
	private String printConfiguration;
	private String textRows;
	private Object attachments;

	public CreateDocumentRequestTo(String metafile, String document, Element data) {
		this.metafile = metafile;
		this.document = document;
		this.data = data;
		this.printConfiguration = MetaforceDocumentType.PREVIEWPDF.value();
	}

	public CreateDocumentRequestTo(String metafile, String document, Element data, String printConfiguration) {
		this.metafile = metafile;
		this.document = document;
		this.data = data;
		this.printConfiguration = printConfiguration;
	}

	public String getMetafile() {
		return metafile;
	}

	public String getDocument() {
		return document;
	}

	public Object getData() {
		return data;
	}

	public String getTextRows() {
		return textRows;
	}

	public String getPrintConfiguration() {
		return printConfiguration;
	}

	public Object getAttachments() {
		return attachments;
	}

	@Override
	public String toString() {
		return "CreateDocumentRequestTo{" +
				"metafile='" + metafile + '\'' +
				", document='" + document + '\'' +
				", data=" + data +
				", printConfiguration='" + printConfiguration + '\'' +
				'}';
	}

}
