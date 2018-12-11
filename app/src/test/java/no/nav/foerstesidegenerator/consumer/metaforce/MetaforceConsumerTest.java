package no.nav.foerstesidegenerator.consumer.metaforce;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metaforce.services.GSCreateDocument;
import se.metaforce.services.IGeneralService;

@ExtendWith(MockitoExtension.class)
public class MetaforceConsumerTest {

	@Mock
	private IGeneralService metaforceService;

	@InjectMocks
	private MetaforceConsumer metaforceConsumer;

	@BeforeEach
	public void setUp() {
		when(metaforceService.gsCreateDocument(any(), any(), any(), any(), any(), any())).thenReturn(new DocumentReturn());
	}

	@Test
	void shouldCreateDocument() {
		DocumentReturn documentReturn = metaforceConsumer.createDocumentRequest(new GSCreateDocument());

		assertNotNull(documentReturn);
	}

//	@Test
//	public void shouldThrowTechnicalExceptionWhenCreateDocumentFails() {
//		when(metaforceService.gsCreateDocument(anyString(), anyString(), any(), anyString(), anyString(), any())).thenThrow(new RuntimeException(new UnknownHostException("Unknown host")));
//
//		assertThrows(FoerstesideGeneratorTechnicalException.class, () -> metaforceConsumer.createDocumentRequest(new GSCreateDocument()), "Technical error in Metaforce.createDocument");
//	}
}