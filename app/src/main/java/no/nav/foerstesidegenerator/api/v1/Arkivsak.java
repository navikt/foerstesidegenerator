package no.nav.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.foerstesidegenerator.api.v1.code.Arkivsaksystem;

import jakarta.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Arkivsak {

	@NotNull(message = "Arkivsak mangler arkivsaksystem")
	@Schema(description = """
			"PSAK" for forsendelser med tema PEN eller UFO. "GSAK" for alle andre tema.
			""",
			requiredMode = REQUIRED,
			example = "GSAK")
	private Arkivsaksystem arkivsaksystem;

	@NotNull(message = "Arkivsak mangler arkivsaksnummer")
	@Schema(description = "Saksnummeret i GSAK eller PSAK",
			requiredMode = REQUIRED,
			example = "abc123456")
	private String arkivsaksnummer;


}
