package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

import no.nav.foerstesidegenerator.consumer.metaforce.map.CreateDocumentRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentRequestTo;
import se.metaforce.services.GSCreateDocument;

/**
 * Default implementation of CreateDocumentRequestMapper
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */

public class DefaultCreateDocumentRequestMapper implements CreateDocumentRequestMapper {

	@Override
	public GSCreateDocument map(CreateDocumentRequestTo requestTo) {
		GSCreateDocument gsCreateDocument = new GSCreateDocument();
		gsCreateDocument.setMetafile(requestTo.getMetafile());
		gsCreateDocument.setDocument(requestTo.getDocument());
		gsCreateDocument.setData(getCreateDocumentDataType(requestTo.getData()));
		gsCreateDocument.setTextRows(requestTo.getTextRows());
		gsCreateDocument.setPrintConfiguration(requestTo.getPrintConfiguration());
		return gsCreateDocument;
	}

	private GSCreateDocument.Data getCreateDocumentDataType(Object fletteData) {
		GSCreateDocument.Data data = new GSCreateDocument.Data();
		data.setAny(fletteData);
		return data;
	}
}
