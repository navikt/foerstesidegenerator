package no.nav.foerstesidegenerator.consumer.metaforce;

import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.UpdateDocReturn;
import se.metaforce.services.GSLoadInArchive;
import se.metaforce.services.GSOpenDocument;
import se.metaforce.services.IGeneralService;

import javax.inject.Inject;

/**
 * Consumer for Metaforce
 * <p>
 * Implemented methods
 * LoadInArchive
 * OpenDocument
 * <p>
 * Setup of metaforceEndpoint in MetaforceEndpointConfig
 * endpoint of type se.metaforce.services.IGeneralService
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class MetaforceConsumer {

	@Inject
	private IGeneralService metaforceEndpoint;

	/**
	 * GSLoadInArchive
	 * String metafile, String document, String textRows, Data data, Attachments attachments
	 */
	public UpdateDocReturn loadInArchiveRequest(GSLoadInArchive gsLoadInArchive) {
//		log.info(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)) + " kaller Metaforce:gsLoadInArchive for forsendelse med forsendelseMottaksId=" + MDCOperations
//				.getFromMDC(MDC_FORSENDELSE_MOTTAKSID));
//		Histogram.Timer requestTimer = requestLatency.labels(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)), "Metaforce::gsLoadInArchive")
//				.startTimer();
		try {
			return metaforceEndpoint.gsLoadInArchive(gsLoadInArchive.getMetaFile(),
					gsLoadInArchive.getDocument(),
					gsLoadInArchive.getTextRows(),
					gsLoadInArchive.getData(),
					gsLoadInArchive.getAttachments());
		} catch (Exception e) {
			throw new FoerstesideGeneratorTechnicalException("Technical error in Metaforce.loadInArchive", e);
		} finally {
//			requestTimer.observeDuration();
		}
	}

	/**
	 * GSOpenDocument
	 * Integer jobId, Integer docId, String printConfig
	 */
	public DocumentReturn openDocumentRequest(GSOpenDocument gsOpenDocument) {
//		log.info(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)) + " kaller Metaforce:gsOpenDocument for forsendelse med forsendelseMottaksId=" + MDCOperations
//				.getFromMDC(MDC_FORSENDELSE_MOTTAKSID));
//
//		Histogram.Timer requestTimer = requestLatency.labels(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)), "Metaforce::gsOpenDocument")
//				.startTimer();
		try {
			return metaforceEndpoint.gsOpenDocument(gsOpenDocument.getJobId(),
					gsOpenDocument.getDocId(),
					gsOpenDocument.getPrintConfiguration());
		} catch (Exception e) {
			throw new FoerstesideGeneratorTechnicalException("Technical error in Metaforce.openDocument", e);
		} finally {
//			requestTimer.observeDuration();
		}
	}
}
