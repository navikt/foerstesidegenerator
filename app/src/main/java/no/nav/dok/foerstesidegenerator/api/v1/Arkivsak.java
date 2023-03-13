package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.dok.foerstesidegenerator.api.v1.code.Arkivsaksystem;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Arkivsak {

	@NotNull(message = "Arkivsak mangler arkivsaksystem")
	@Schema(
			description = "\"PSAK\" for forsendelser med tema PEN eller UFO\n" +
					"\"GSAK\" for alle andre tema",
			required = true,
			example = "GSAK")
	private Arkivsaksystem arkivsaksystem;

	@NotNull(message = "Arkivsak mangler arkivsaksnummer")
	@Schema(
			description = "Saksnummeret i GSAK eller PSAK",
			required = true,
			example = "abc123456")
	private String arkivsaksnummer;


}
