package no.nav.foerstesidegenerator.consumer.metaforce;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metaforce.services.GSCreateDocument;
import se.metaforce.services.IGeneralService;

import java.net.UnknownHostException;

@ExtendWith(MockitoExtension.class)
class MetaforceConsumerTest {

	@Mock
	private IGeneralService metaforceService;

	@InjectMocks
	private MetaforceConsumer metaforceConsumer;

	@BeforeEach
	void setUp() {
		when(metaforceService.gsCreateDocument(any(), any(), any(), any(), any(), any())).thenReturn(new DocumentReturn());
	}

	@Test
	void shouldCreateDocument() {
		DocumentReturn documentReturn = metaforceConsumer.createDocumentRequest(new GSCreateDocument());

		assertNotNull(documentReturn);
	}

	@Test
	void shouldThrowTechnicalExceptionWhenCreateDocumentFails() {
		when(metaforceService.gsCreateDocument(any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException(new UnknownHostException("Unknown host")));

		assertThrows(FoerstesideGeneratorTechnicalException.class, () -> metaforceConsumer.createDocumentRequest(new GSCreateDocument()), "Technical error in Metaforce.createDocument");
	}
}