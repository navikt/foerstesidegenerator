package no.nav.foerstesidegenerator.consumer.dokkat.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DokumentProduksjonsInfoTo {

	private final Boolean vedlegg;
	private final Boolean eksternVedlegg;
	private final String ikkeRedigerbarMalId;
	private final String redigerbarMalId;
	private final String malLogikkFil;
	private final String malXsdReferanse;

}
