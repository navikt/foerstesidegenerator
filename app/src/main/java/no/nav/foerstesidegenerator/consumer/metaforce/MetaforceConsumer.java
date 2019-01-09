//package no.nav.foerstesidegenerator.consumer.metaforce;
//
//import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;
//import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
//import org.springframework.stereotype.Component;
//import se.metaforce.services.GSCreateDocument;
//import se.metaforce.services.IGeneralService;
//
//import javax.inject.Inject;
//
///**
// * Consumer for Metaforce
// * <p>
// * Implemented methods
// * LoadInArchive
// * OpenDocument
// * <p>
// * Setup of metaforceEndpoint in MetaforceEndpointConfig
// * endpoint of type se.metaforce.services.IGeneralService
// *
// * @author Paul Magne Lunde, Visma Consulting
// */
//@Component
//public class MetaforceConsumer {
//
//	private final IGeneralService metaforceEndpoint;
//
//	@Inject
//	public MetaforceConsumer(IGeneralService metaforceEndpoint) {
//		this.metaforceEndpoint = metaforceEndpoint;
//	}
//
//	/**
//	 * GSCreateDocument
//	 * String metafile, String document, String textRows, Data data, String printConfig, Attachments attachments
//	 */
//	public DocumentReturn createDocumentRequest(GSCreateDocument gsCreateDocument) {
////		log.info(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)) + " kaller Metaforce:createDocument for forsendelse med forsendelseMottaksId=" + MDCOperations
////				.getFromMDC(MDC_FORSENDELSE_MOTTAKSID));
////
////		Histogram.Timer requestTimer = requestLatency.labels(getProcessName(MDCOperations.getFromMDC(MDC_APP_ID)), "Metaforce::gsCreateDocument")
////				.startTimer();
//		try {
//			return metaforceEndpoint.gsCreateDocument(
//					gsCreateDocument.getMetafile(),
//					gsCreateDocument.getDocument(),
//					gsCreateDocument.getData(),
//					gsCreateDocument.getTextRows(),
//					gsCreateDocument.getPrintConfiguration(),
//					gsCreateDocument.getAttachments()
//			);
//		} catch (Exception e) {
//			throw new FoerstesideGeneratorTechnicalException("Technical error in Metaforce.createDocument", e);
//		}
//	}
//}
