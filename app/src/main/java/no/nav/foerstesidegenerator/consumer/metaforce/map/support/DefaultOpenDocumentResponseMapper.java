package no.nav.foerstesidegenerator.consumer.metaforce.map.support;

//import lombok.Builder;
//import lombok.Data;
import no.nav.foerstesidegenerator.consumer.metaforce.map.OpenDocumentResponseMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.to.MetaforceDocumentType;
import no.nav.foerstesidegenerator.consumer.metaforce.to.OpenDocumentResponseTo;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentFormat;
import org.datacontract.schemas._2004._07.metaforce_common.DocumentReturn;
import org.datacontract.schemas._2004._07.metaforce_common.Format;
import org.w3c.dom.Element;

import java.nio.charset.StandardCharsets;

/**
 * Implementation of {@link OpenDocumentResponseMapper}
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class DefaultOpenDocumentResponseMapper implements OpenDocumentResponseMapper {
	private static InternalDocumentResult determineBlob(Format format, DocumentReturn response) {
		if (format.equals(Format.NOTSET)) {
			return new InternalDocumentResult(MetaforceDocumentType.XML, DomUtil.elementToString((Element) response.getData().getAny()).getBytes(StandardCharsets.UTF_8));
//			return InternalDocumentResult.builder()
//					.documentType(MetaforceDocumentType.XML)
//					.documentData(DomUtil.elementToString((Element) response.getData().getAny()).getBytes(StandardCharsets.UTF_8)).build();
		} else {
			return new InternalDocumentResult(MetaforceDocumentType.fromValue(format.name()), response.getDocument().getBlob());
//			return InternalDocumentResult.builder()
//					.documentType(MetaforceDocumentType.fromValue(format.name()))
//					.documentData(response.getDocument().getBlob()).build();
		}
	}

	@Override
	public OpenDocumentResponseTo map(DocumentReturn response) {
		DocumentFormat format = response.getDocument().getFormat();

		InternalDocumentResult documentResult = determineBlob(format.getDocFormat(), response);

		return new OpenDocumentResponseTo(documentResult.getDocumentData(),
				documentResult.getDocumentType(),
				format.getExtension(),
				format.getMimeType(),
				response.getDocument().getLength(),
				response.getExecutionTime(),
				response.getExecutionTimeInternal());

//		return OpenDocumentResponseTo.builder()
//				.documentData(documentResult.getDocumentData())
//				.documentFormat(documentResult.getDocumentType())
//				.extension(format.getExtension())
//				.mimeType(format.getMimeType())
//				.length(response.getDocument().getLength())
//				.executionTime(response.getExecutionTime())
//				.executionTimeInternal(response.getExecutionTimeInternal())
//				.build();
	}

//	@Data
//	@Builder
	private static final class InternalDocumentResult {
		private MetaforceDocumentType documentType;
		private byte[] documentData;

		public InternalDocumentResult(MetaforceDocumentType documentType, byte[] documentData) {
			this.documentType = documentType;
			this.documentData = documentData;
		}

		public MetaforceDocumentType getDocumentType() {
			return documentType;
		}

		public void setDocumentType(MetaforceDocumentType documentType) {
			this.documentType = documentType;
		}

		public byte[] getDocumentData() {
			return documentData;
		}

		public void setDocumentData(byte[] documentData) {
			this.documentData = documentData;
		}
	}
}
