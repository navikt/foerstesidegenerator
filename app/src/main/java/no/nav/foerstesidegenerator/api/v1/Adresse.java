package no.nav.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

	@NotNull(message = "Adresse mangler Adresselinje1")
	@Schema(description = "Trykkes på førstesiden",
			requiredMode = REQUIRED,
			example = "Gateveien 1")
	private String adresselinje1;

	@Schema(description = "Trykkes på førstesiden")
	private String adresselinje2;

	@Schema(description = "Trykkes på førstesiden")
	private String adresselinje3;

	@NotNull(message = "Adresse mangler Postnummer")
	@Schema(description = "Trykkes på førstesiden",
			requiredMode = REQUIRED,
			example = "1234")
	private String postnummer;

	@NotNull(message = "Adresse mangler Poststed")
	@Schema(description = "Trykkes på førstesiden",
			requiredMode = REQUIRED,
			example = "Oslo")
	private String poststed;


}
