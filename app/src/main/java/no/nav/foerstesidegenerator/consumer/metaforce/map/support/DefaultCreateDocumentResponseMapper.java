package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

import no.nav.foerstesidegenerator.consumer.metaforce.map.CreateDocumentResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.MetaforceDocumentType;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentFormat;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.springframework.stereotype.Component;

/**
 * Default implementation of CreateDocumentResponseMapper
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
@Component
public class DefaultCreateDocumentResponseMapper implements CreateDocumentResponseMapper {

	@Override
	public CreateDocumentResponseTo map(DocumentReturn response) {
		DocumentFormat format = response.getDocument().getFormat();

		CreateDocumentResponseTo responseTo = new CreateDocumentResponseTo(response.getDocument().getBlob(),
				MetaforceDocumentType.fromValue(format.getDocFormat().name()),
				format.getExtension(),
				format.getMimeType(),
				response.getDocument().getLength());
		responseTo.setExecutionTime(response.getExecutionTime());
		responseTo.setExecutionTimeInternal(response.getExecutionTimeInternal());
		return responseTo;
	}
}
