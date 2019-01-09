package no.nav.foerstesidegenerator.consumer.metaforce.update;

import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.to.CreateDocumentResponseTo;

public interface Metaforce {
	CreateDocumentResponseTo createDocument(CreateDocumentRequestTo createDocumentRequestTo);
}
