package no.nav.foerstesidegenerator.consumer.metaforce.update;

import static se.metaforce.services.InteractOnlineProcess.SERVICE;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
@Setter
@ToString
@Slf4j
@Configuration
public class MetaforceTimeouts {
	@Value("${dokprod.metaforce.defaultreceivetimeoutms:#{null}}")
	@Getter
	private Long defaultReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetInstanceInfo.receivetimeoutms:#{null}}")
	private Long gsGetInstanceInfoReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsInitializeAndLeaseInstance.receivetimeoutms:#{null}}")
	private Long gsInitializeAndLeaseInstanceReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsPrintDXML.receivetimeoutms:#{null}}")
	private Long gsPrintDXMLReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetInstanceList.receivetimeoutms:#{null}}")
	private Long gsGetInstanceListReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetTempData.receivetimeoutms:#{null}}")
	private Long gsGetTempDataReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsInitializeInstance.receivetimeoutms:#{null}}")
	private Long gsInitializeInstanceReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsUpdateDocument2.receivetimeoutms:#{null}}")
	private Long gsUpdateDocument2ReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLoadPdfInArchive.receivetimeoutms:#{null}}")
	private Long gsLoadPdfInArchiveReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLoadInArchive.receivetimeoutms:#{null}}")
	private Long gsLoadInArchiveReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsCreateDocumentTmpData.receivetimeoutms:#{null}}")
	private Long gsCreateDocumentTmpDataReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsOpenDocument.receivetimeoutms:#{null}}")
	private Long gsOpenDocumentReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsInsertTempData.receivetimeoutms:#{null}}")
	private Long gsInsertTempDataReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsSearchFolder.receivetimeoutms:#{null}}")
	private Long gsSearchFolderReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLoadInArchiveDXML.receivetimeoutms:#{null}}")
	private Long gsLoadInArchiveDXMLReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetStartInstanceBrowseUrlSuffixed.receivetimeoutms:#{null}}")
	private Long gsGetStartInstanceBrowseUrlSuffixedReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsUpdateDocuments.receivetimeoutms:#{null}}")
	private Long gsUpdateDocumentsReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsDeleteTempData.receivetimeoutms:#{null}}")
	private Long gsDeleteTempDataReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLoadInArchiveTmpData.receivetimeoutms:#{null}}")
	private Long gsLoadInArchiveTmpDataReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsCreateDocument.receivetimeoutms:#{null}}")
	private Long gsCreateDocumentReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetFolder.receivetimeoutms:#{null}}")
	private Long gsGetFolderReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetStartInstanceBrowseUrl.receivetimeoutms:#{null}}")
	private Long gsGetStartInstanceBrowseUrlReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetInfoAndFonts.receivetimeoutms:#{null}}")
	private Long gsGetInfoAndFontsReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLeaseInstanceSuffixed.receivetimeoutms:#{null}}")
	private Long gsLeaseInstanceSuffixedReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsUpdateDocument.receivetimeoutms:#{null}}")
	private Long gsUpdateDocumentReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsDeleteDocument2.receivetimeoutms:#{null}}")
	private Long gsDeleteDocument2ReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetOnlineDocuments.receivetimeoutms:#{null}}")
	private Long gsGetOnlineDocumentsReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetStartDocBrowseUrl.receivetimeoutms:#{null}}")
	private Long gsGetStartDocBrowseUrlReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsDeleteDocument.receivetimeoutms:#{null}}")
	private Long gsDeleteDocumentReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsLeaseInstance.receivetimeoutms:#{null}}")
	private Long gsLeaseInstanceReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsUpdateDocuments2.receivetimeoutms:#{null}}")
	private Long gsUpdateDocuments2ReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsChangeInstanceStatus.receivetimeoutms:#{null}}")
	private Long gsChangeInstanceStatusReceiveTimeoutms;
	
	@Value("${dokprod.metaforce.gsGetFolderList.receivetimeoutms:#{null}}")
	private Long gsGetFolderListReceiveTimeoutms;
	
	public Map<QName, Long> getOperationsTimeouts() {
		Map<QName, Long> map = new HashMap<>();
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetInstanceInfo"), gsGetInstanceInfoReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_InitializeAndLeaseInstance"), gsInitializeAndLeaseInstanceReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_PrintDXML"), gsPrintDXMLReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetInstanceList"), gsGetInstanceListReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetTempData"), gsGetTempDataReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_InitializeInstance"), gsInitializeInstanceReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_UpdateDocument2"), gsUpdateDocument2ReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LoadPdfInArchive"), gsLoadPdfInArchiveReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LoadInArchive"), gsLoadInArchiveReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_CreateDocumentTmpData"), gsCreateDocumentTmpDataReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_OpenDocument"), gsOpenDocumentReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_InsertTempData"), gsInsertTempDataReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_SearchFolder"), gsSearchFolderReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LoadInArchiveDXML"), gsLoadInArchiveDXMLReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetStartInstanceBrowseUrlSuffixed"), gsGetStartInstanceBrowseUrlSuffixedReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_UpdateDocuments"), gsUpdateDocumentsReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_DeleteTempData"), gsDeleteTempDataReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LoadInArchiveTmpData"), gsLoadInArchiveTmpDataReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_CreateDocument"), gsCreateDocumentReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetFolder"), gsGetFolderReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetStartInstanceBrowseUrl"), gsGetStartInstanceBrowseUrlReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetInfoAndFonts"), gsGetInfoAndFontsReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LeaseInstanceSuffixed"), gsLeaseInstanceSuffixedReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_UpdateDocument"), gsUpdateDocumentReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_DeleteDocument2"), gsDeleteDocument2ReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetOnlineDocuments"), gsGetOnlineDocumentsReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetStartDocBrowseUrl"), gsGetStartDocBrowseUrlReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_DeleteDocument"), gsDeleteDocumentReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_LeaseInstance"), gsLeaseInstanceReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_UpdateDocuments2"), gsUpdateDocuments2ReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_ChangeInstanceStatus"), gsChangeInstanceStatusReceiveTimeoutms);
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_GetFolderList"), gsGetFolderListReceiveTimeoutms);
		return map;
	}
	
	@PostConstruct
	public void postConstruct() {
		log.info("Metaforce timeout values. " + this);
	}
}
