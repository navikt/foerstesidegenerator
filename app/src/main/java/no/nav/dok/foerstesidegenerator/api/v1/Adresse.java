package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

	@NotNull(message = "Adresse mangler Adresselinje1")
	@Schema(
			description = "Trykkes på førstesiden",
			required = true,
			example = "Gateveien 1")
	private String adresselinje1;

	@Schema(
			description = "Trykkes på førstesiden",
			required = false)
	private String adresselinje2;

	@Schema(
			description = "Trykkes på førstesiden",
			required = false)
	private String adresselinje3;

	@NotNull(message = "Adresse mangler Postnummer")
	@Schema(
			description = "Trykkes på førstesiden",
			required = true,
			example = "1234")
	private String postnummer;

	@NotNull(message = "Adresse mangler Poststed")
	@Schema(
			description = "Trykkes på førstesiden",
			required = true,
			example = "Oslo")
	private String poststed;


}
