package no.nav.foerstesidegenerator.consumer.metaforce.to;

//import lombok.Builder;
//import lombok.Data;
//import lombok.ToString;
import org.w3c.dom.Element;

import javax.validation.constraints.NotNull;

/**
 * Transferobject for se.metaforce.services.GSLoadInArchive request object for metaforce gsLoadInArchive ws operation
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
//@Data
//@Builder
//@ToString(exclude = "attachments")
public class LoadInArchiveRequestTo {
	@NotNull
	private String metaFile;
	@NotNull
	private String document;
	@NotNull
	private Element data;

	private String textRows;

	private Element attachments;

	public LoadInArchiveRequestTo(@NotNull String metaFile, @NotNull String document, @NotNull Element data, String textRows, Element attachments) {
		this.metaFile = metaFile;
		this.document = document;
		this.data = data;
		this.textRows = textRows;
		this.attachments = attachments;
	}

	public String getMetaFile() {
		return metaFile;
	}

	public void setMetaFile(String metaFile) {
		this.metaFile = metaFile;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Element getData() {
		return data;
	}

	public void setData(Element data) {
		this.data = data;
	}

	public String getTextRows() {
		return textRows;
	}

	public void setTextRows(String textRows) {
		this.textRows = textRows;
	}

	public Element getAttachments() {
		return attachments;
	}

	public void setAttachments(Element attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "LoadInArchiveRequestTo{" +
				"metaFile='" + metaFile + '\'' +
				", document='" + document + '\'' +
				", data=" + data +
				", textRows='" + textRows + '\'' +
				'}';
	}
}
