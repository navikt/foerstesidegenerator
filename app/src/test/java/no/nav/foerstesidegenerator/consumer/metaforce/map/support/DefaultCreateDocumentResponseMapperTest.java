package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.MetaforceDocumentType;
import org.datacontract.schemas._2004._07.metaforce_common.Document;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentFormat;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

class DefaultCreateDocumentResponseMapperTest {

	private static final byte[] DOCUMENT_DATA = "Pdf".getBytes();
	private static final String EXTENSION = ".pdf";
	private static final String MIMETYPE = "application/pdf";
	private Duration executionTime;
	private Duration executionTimeInternal;

	private DefaultCreateDocumentResponseMapper defaultCreateDocumentResponseMapper =
			new DefaultCreateDocumentResponseMapper();

	@BeforeEach
	void setUp() throws Exception {
		executionTime = DatatypeFactory.newInstance().newDuration(100);
		executionTimeInternal = DatatypeFactory.newInstance().newDuration(200);
	}

	@Test
	void shouldMapCreateDocumentResponse() {
		CreateDocumentResponseTo responseTo = defaultCreateDocumentResponseMapper.map(createDocumentReturn());

		assertArrayEquals(DOCUMENT_DATA, responseTo.getDocumentData());
		assertEquals(MetaforceDocumentType.PDF, responseTo.getDocFormat());
		assertEquals(EXTENSION, responseTo.getExtension());
		assertEquals(MIMETYPE, responseTo.getMimeType());
		assertEquals(DOCUMENT_DATA.length, responseTo.getLength());
		assertEquals(executionTime, responseTo.getExecutionTime());
		assertEquals(executionTimeInternal, responseTo.getExecutionTimeInternal());
	}

	private DocumentReturn createDocumentReturn() {
		DocumentReturn documentReturn = new DocumentReturn();
		documentReturn.setDocument(createDocument());
		documentReturn.setExecutionTime(executionTime);
		documentReturn.setExecutionTimeInternal(executionTimeInternal);
		return documentReturn;
	}

	private Document createDocument() {
		Document document = new Document();
		document.setBlob(DOCUMENT_DATA);
		document.setLength(DOCUMENT_DATA.length);
		document.setFormat(createDocumentFormat());
		return document;
	}

	private DocumentFormat createDocumentFormat() {
		DocumentFormat documentFormat = new DocumentFormat();
		documentFormat.setDocFormat(Format.PDF);
		documentFormat.setExtension(EXTENSION);
		documentFormat.setMimeType(MIMETYPE);
		return documentFormat;
	}
}