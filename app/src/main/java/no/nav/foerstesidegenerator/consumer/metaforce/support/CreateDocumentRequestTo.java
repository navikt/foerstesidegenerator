package no.nav.foerstesidegenerator.consumer.metaforce.support;

import lombok.Getter;
import org.w3c.dom.Element;

import jakarta.validation.constraints.NotNull;

@Getter
public class CreateDocumentRequestTo {
	@NotNull
	private final String metafile;
	@NotNull
	private final String document;
	@NotNull
	private final Object data;
	@NotNull
	private final String printConfiguration;
	private String textRows;
	private Object attachments;

	public CreateDocumentRequestTo(String metafile, String document, Element data) {
		this.metafile = metafile;
		this.document = document;
		this.data = data;
		this.printConfiguration = MetaforceDocumentType.PDF.value();
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
