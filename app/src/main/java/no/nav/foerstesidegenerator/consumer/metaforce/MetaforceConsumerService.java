package no.nav.foerstesidegenerator.consumer.metaforce;

import no.nav.foerstesidegenerator.consumer.metaforce.map.CreateDocumentRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.map.CreateDocumentResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.exception.MetaforceTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.springframework.stereotype.Component;
import se.metaforce.services.GSCreateDocument;

import javax.inject.Inject;

/**
 * Metaforce consumer services
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
@Component
public class MetaforceConsumerService {

	private final MetaforceConsumer metaforceConsumer;
	private final CreateDocumentRequestMapper createDocumentRequestMapper;
	private final CreateDocumentResponseMapper createDocumentResponseMapper;

	@Inject
	public MetaforceConsumerService(MetaforceConsumer metaforceConsumer,
									CreateDocumentRequestMapper createDocumentRequestMapper,
									CreateDocumentResponseMapper createDocumentResponseMapper) {
		this.metaforceConsumer = metaforceConsumer;
		this.createDocumentRequestMapper = createDocumentRequestMapper;
		this.createDocumentResponseMapper = createDocumentResponseMapper;
	}

	public CreateDocumentResponseTo createDocument(CreateDocumentRequestTo requestTo) {
		GSCreateDocument gsCreateDocument = createDocumentRequestMapper.map(requestTo);
		DocumentReturn documentReturn = metaforceConsumer.createDocumentRequest(gsCreateDocument);
		if (!documentReturn.isIsOK()) {
			throw new MetaforceTechnicalException(documentReturn.getErrorDescription());
		}
		return createDocumentResponseMapper.map(documentReturn);
	}
}
