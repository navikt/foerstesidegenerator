package no.nav.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.foerstesidegenerator.api.v1.code.AvsenderIdType;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Avsender {

	@Schema(description = "Identifikator for avsender. Formatet skal samsvare med avsenderIdType.",
			example = "01234567890")
	private String avsenderId;

	@Schema(description = "Sier hvilken type identifikator som er benyttet i avsenderID.",
			example = "FNR",
			requiredMode = NOT_REQUIRED)
	private AvsenderIdType avsenderIdType;

	@Schema(description = "Navn på avsender",
			example = "Per Hansen")
	private String avsenderNavn;
}
