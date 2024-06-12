package no.nav.foerstesidegenerator.consumer.dokkat.to;

import lombok.Builder;
import lombok.Getter;
@Builder
public record DokumentProduksjonsInfoTo(Boolean vedlegg, Boolean eksternVedlegg, String ikkeRedigerbarMalId,
										String malLogikkFil, String malXsdReferanse) {

}
