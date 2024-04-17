package no.nav.foerstesidegenerator.consumer.metaforce.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.xml.datatype.Duration;

@Builder
@Getter
@AllArgsConstructor
public class CreateDocumentResponseTo {

	private MetaforceDocumentType docFormat;
	private String extension;
	private String mimeType;
	private int length;
	private byte[] documentData;

	private Duration executionTime;
	private Duration executionTimeInternal;
}
