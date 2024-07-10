package no.nav.foerstesidegenerator.consumer.dokmet.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DokumentTypeInfo {
	private String dokumentTypeId;
	private String dokumentTittel;
	private String dokumentKategori;
	private String behandlingsTema;
	private String arkivsystem;
	private Boolean sensitivt;
	private List<Pair<String, String>> tilleggsopplysninger;
	private DokumentProduksjonsInfo dokumentProduksjonsInfo;

}
