package no.nav.foerstesidegenerator.consumer.metaforce;

import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.exception.MetaforceTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.BaseReturn;
import org.datacontract.schemas._2004._07.metaforce_common.Document;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentFormat;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.Format;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Element;
import se.metaforce.services.IGeneralService;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetaforceConsumerTest {

	@Mock
	private IGeneralService metaforceService;

	@InjectMocks
	private MetaforceConsumer metaforceConsumer;

	@BeforeEach
	void setUp() {
		when(metaforceService.gsCreateDocument(any(), any(), any(), any(), any(), any())).thenReturn(createResponse());
	}

	@Test
	void shouldCreateDocument() {
		CreateDocumentResponseTo responseTo = metaforceConsumer.createDocument(createRequest());

		assertNotNull(responseTo);
	}

	@Test
	void shouldThrowTechnicalExceptionWhenCreateDocumentFails() {
		when(metaforceService.gsCreateDocument(any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException(new UnknownHostException("Unknown host")));

		assertThrows(MetaforceTechnicalException.class, () -> metaforceConsumer.createDocument(createRequest()));
	}

	private CreateDocumentRequestTo createRequest() {
		return new CreateDocumentRequestTo("metafile", "document", mock(Element.class));
	}

	private DocumentReturn createResponse() {
		DocumentReturn documentReturn = new DocumentReturn();

		DocumentFormat documentFormat = new DocumentFormat();
		documentFormat.setDocFormat(Format.PDF);
		documentFormat.setExtension("extension");
		documentFormat.setMimeType("mimetype");

		Document document = new Document();
		document.setFormat(documentFormat);
		document.setBlob("hello".getBytes());
		document.setLength(5);

		documentReturn.setDocument(document);

		BaseReturn.Data data = new BaseReturn.Data();
		documentReturn.setData(data);

		return documentReturn;
	}
}