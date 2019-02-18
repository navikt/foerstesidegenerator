package no.nav.foerstesidegenerator.consumer.metaforce;

import static no.nav.foerstesidegenerator.metrics.MetricLabels.CONSUMER;
import static no.nav.foerstesidegenerator.metrics.MetricLabels.DOK_REQUEST_CONSUMER;

import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.DomUtil;
import no.nav.foerstesidegenerator.consumer.metaforce.support.MetaforceDocumentType;
import no.nav.foerstesidegenerator.exception.MetaforceTechnicalException;
import no.nav.foerstesidegenerator.metrics.Metrics;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.Format;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import se.metaforce.services.GSCreateDocument;
import se.metaforce.services.IGeneralService;

import java.nio.charset.StandardCharsets;

@Component
public class MetaforceConsumer {
	private final IGeneralService metaforcews;

	MetaforceConsumer(IGeneralService metaforce) {
		this.metaforcews = metaforce;
	}

	@Metrics(value = DOK_REQUEST_CONSUMER, extraTags = {CONSUMER, "metaforce_createDocument"}, percentiles = {0.5, 0.95})
	public CreateDocumentResponseTo createDocument(CreateDocumentRequestTo createDocumentRequestTo) {
		String processCalled = "Metaforce:GS_CreateDocument";
		DocumentReturn documentReturn = null;
		try {
			documentReturn = metaforcews.gsCreateDocument(
					createDocumentRequestTo.getMetafile(),
					createDocumentRequestTo.getDocument(),
					getCreateDocumentDataType(createDocumentRequestTo.getData()),
					createDocumentRequestTo.getTextRows(),
					createDocumentRequestTo.getPrintConfiguration(),
					null);
			return CreateDocumentResponseTo.builder()
					.documentData(determineBlob(documentReturn))
					.docFormat(MetaforceDocumentType.fromValue(documentReturn.getDocument().getFormat().getDocFormat().name()))
					.extension(documentReturn.getDocument().getFormat().getExtension())
					.mimeType(documentReturn.getDocument().getFormat().getMimeType())
					.length(documentReturn.getDocument().getLength())
					.executionTime(documentReturn.getExecutionTime())
					.executionTimeInternal(documentReturn.getExecutionTimeInternal())
					.build();
		} catch (Exception e) {
			throw new MetaforceTechnicalException(String.format("Kall mot %s feilet teknisk for ikkeRedigerbarMalId=%s.%s",
					processCalled,
					createDocumentRequestTo.getMetafile(),
					documentReturn != null ? " ErrorDescription: " + documentReturn.getErrorDescription() : ""), e);
		}
	}

	private GSCreateDocument.Data getCreateDocumentDataType(Object fletteData) {
		GSCreateDocument.Data data = new GSCreateDocument.Data();
		data.setAny(fletteData);
		return data;
	}

	private byte[] determineBlob(DocumentReturn documentReturn) {
		if (documentReturn.getDocument().getFormat().getDocFormat() == Format.NOTSET) {
			return DomUtil.elementToString((Element) documentReturn.getData().getAny()).getBytes(StandardCharsets.UTF_8);
		}
		return documentReturn.getDocument().getBlob();
	}
}
