package no.nav.foerstesidegenerator.consumer.metaforce;


import no.nav.foerstesidegenerator.consumer.metaforce.map.LoadInArchiveRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.map.LoadInArchiveResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.map.OpenDocumentRequestMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.map.OpenDocumentResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.map.support.DomUtil;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateAndRetrievePdfTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.LoadInArchiveResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.MetaforceDocumentType;
import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentResponseTo;
import no.nav.foerstesidegenerator.exception.MetaforceTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.UpdateDocReturn;
import se.metaforce.services.GSLoadInArchive;
import se.metaforce.services.GSOpenDocument;

import javax.inject.Inject;

/**
 * Metaforce consumer services
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class MetaforceConsumerService {

	@Inject
	private LoadInArchiveRequestMapper loadInArchiveRequestMapper;
	@Inject
	private LoadInArchiveResponseMapper loadInArchiveResponseMapper;
	@Inject
	private OpenDocumentRequestMapper openDocumentRequestMapper;
	@Inject
	private OpenDocumentResponseMapper openDocumentResponseMapper;
	@Inject
	private MetaforceConsumer metaforceConsumer;

	public OpenDocumentResponseTo createAndRetrievePdf(CreateAndRetrievePdfTo createAndRetrievePdfTo) {
		LoadInArchiveResponseTo loadInArchiveResponseTo = loadInArchive(
				new LoadInArchiveRequestTo(createAndRetrievePdfTo.getMalLogikkFil(),
						createAndRetrievePdfTo.getIkkeRedigerbarMalId(),
						DomUtil.createDomElement(createAndRetrievePdfTo.getDokument()),
						null,
						null)
//				LoadInArchiveRequestTo.builder()
//				.data(DomUtil.createDomElement(createAndRetrievePdfTo.getDokument()))
//				.metaFile(createAndRetrievePdfTo.getMalLogikkFil())
//				.document(createAndRetrievePdfTo.getIkkeRedigerbarMalId())
//				.attachments(null)
//				.build()
		);

		OpenDocumentRequestTo openDocumentRequest = createOpenDocumentRequest(loadInArchiveResponseTo);
		return openDocument(openDocumentRequest);
	}

	public LoadInArchiveResponseTo loadInArchive(LoadInArchiveRequestTo requestTo) {
		GSLoadInArchive loadInArchive = loadInArchiveRequestMapper.map(requestTo);
		UpdateDocReturn updateDocReturn = metaforceConsumer.loadInArchiveRequest(loadInArchive);
		if (!updateDocReturn.isIsOK()) {
			throw new MetaforceTechnicalException(updateDocReturn.getErrorDescription());
		}
		return loadInArchiveResponseMapper.map(updateDocReturn);
	}

	public OpenDocumentResponseTo openDocument(OpenDocumentRequestTo requestTo) {
		GSOpenDocument gsOpenDocument = openDocumentRequestMapper.map(requestTo);
		DocumentReturn documentReturn = metaforceConsumer.openDocumentRequest(gsOpenDocument);
		if (!documentReturn.isIsOK()) {
			throw new MetaforceTechnicalException(documentReturn.getErrorDescription());
		}
		return openDocumentResponseMapper.map(documentReturn);
	}

	private OpenDocumentRequestTo createOpenDocumentRequest(LoadInArchiveResponseTo responseTo) {
		return new OpenDocumentRequestTo(responseTo.getJobId(), responseTo.getDocId(), MetaforceDocumentType.PDF.value());
//		return OpenDocumentRequestTo.builder()
//				.jobId(responseTo.getJobId())
//				.docId(responseTo.getDocId())
//				.printConfiguration(MetaforceDocumentType.PDF.value())
//				.build();
	}
}
