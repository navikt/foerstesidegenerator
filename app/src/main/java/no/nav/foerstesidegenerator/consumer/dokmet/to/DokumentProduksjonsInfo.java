package no.nav.foerstesidegenerator.consumer.dokmet.to;

import lombok.Builder;

@Builder
public record DokumentProduksjonsInfo(
		Boolean vedlegg,
		Boolean eksternVedlegg,
		String ikkeRedigerbarMalId,
		String malLogikkFil,
		String malXsdReferanse
) {
}