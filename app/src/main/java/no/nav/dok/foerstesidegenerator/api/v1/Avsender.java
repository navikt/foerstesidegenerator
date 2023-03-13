package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Avsender {

	@Schema(
			description = "Avsenders fødselsnummer eller personnummer",
			required = false,
			example = "01234567890")
	private String avsenderId;

	@Schema(
			description = "Navn på avsender",
			required = false,
			example = "Per Hansen")
	private String avsenderNavn;
}
