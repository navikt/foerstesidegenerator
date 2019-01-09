//package no.nav.foerstesidegenerator.consumer.metaforce.map.support;
////
////import static org.junit.jupiter.api.Assertions.assertEquals;
////import static org.junit.jupiter.api.Assertions.assertNotNull;
////import static org.junit.jupiter.api.Assertions.assertNull;
////
////import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentRequestTo;
////import org.junit.jupiter.api.Test;
////import se.metaforce.services.GSCreateDocument;
////
////class DefaultCreateDocumentRequestMapperTest {
////
////	private static final String METAFILE = "NAV";
////	private static final String MAL = "BREVMAL";
////	private static final String PRINTCONFIG = "PreviewPDF";
////
////	private DefaultCreateDocumentRequestMapper defaultCreateDocumentRequestMapper =
////			new DefaultCreateDocumentRequestMapper();
////
////	@Test
////	void shouldMapCreateDocumentRequest() {
////		GSCreateDocument gsCreateDocument = defaultCreateDocumentRequestMapper.map(createDocumentRequestTo());
////
////		assertEquals(METAFILE, gsCreateDocument.getMetafile());
////		assertEquals(MAL, gsCreateDocument.getDocument());
////		assertNotNull(gsCreateDocument.getData().getAny());
////		assertNull(gsCreateDocument.getAttachments());
////		assertEquals(PRINTCONFIG, gsCreateDocument.getPrintConfiguration());
////		assertNull(gsCreateDocument.getTextRows());
////	}
////
////	private CreateDocumentRequestTo createDocumentRequestTo() {
////		return new CreateDocumentRequestTo(METAFILE, MAL, DomUtil.stringToElement("<data></data>"));
////	}
////
////}