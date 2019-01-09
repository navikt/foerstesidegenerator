package no.nav.foerstesidegenerator.consumer.metaforce.update;

import no.nav.foerstesidegenerator.consumer.metaforce.map.support.DomUtil;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.MetaforceDocumentType;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import se.metaforce.services.GSCreateDocument;
import se.metaforce.services.IGeneralService;

import java.nio.charset.StandardCharsets;

@Component
public class MetaforceConsumer implements Metaforce {
	private final IGeneralService metaforcews;

	MetaforceConsumer(IGeneralService metaforce) {
		this.metaforcews = metaforce;
	}

	@Override
	public CreateDocumentResponseTo createDocument(CreateDocumentRequestTo createDocumentRequestTo) {
		String processCalled = "Metaforce:GS_CreateDocument";
//		final Histogram.Timer requestTimer = startTimer(processCalled);
		try {
			DocumentReturn documentReturn = metaforcews.gsCreateDocument(
					createDocumentRequestTo.getMetafile(),
					createDocumentRequestTo.getDocument(),
					getCreateDocumentDataType(createDocumentRequestTo.getData()),
					createDocumentRequestTo.getTextRows(),
					createDocumentRequestTo.getPrintConfiguration(),
					null
			);
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
			throw new FoerstesideGeneratorTechnicalException("Kall mot " + processCalled + " feilet teknisk for ikkeRedigerbarMalId=" + createDocumentRequestTo.getMetafile(), e);
		}
//		finally {
//			requestTimer.observeDuration();
//		}
	}

//	private Histogram.Timer startTimer(final String processCalled) {
//		return requestLatency.labels("", "", processCalled).startTimer();
//	}

	private GSCreateDocument.Data getCreateDocumentDataType(Object fletteData) {
		GSCreateDocument.Data data = new GSCreateDocument.Data();
		data.setAny(fletteData);
		return data;
	}

	private byte[] determineBlob(DocumentReturn documentReturn) {
		switch (documentReturn.getDocument().getFormat().getDocFormat()) {
			case NOTSET:
				return DomUtil.elementToString((Element) documentReturn.getData().getAny()).getBytes(StandardCharsets.UTF_8);
			default:
				return documentReturn.getDocument().getBlob();
		}
	}
}
