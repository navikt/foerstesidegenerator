package no.nav.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.foerstesidegenerator.api.v1.code.BrukerType;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bruker {

	@NotNull(message = "Bruker mangler BrukerId")
	@Schema(description = "Fødselsnummeret eller organisasjonsnummeret som dokumentene omhandler. Fødselsnummeret vil bli trykket i klartekst på førstesiden. Kan kun inneholde siffer",
			requiredMode = REQUIRED,
			example = "01234567890")
	private String brukerId;

	@NotNull(message = "Bruker mangler BrukerType")
	@Schema(description = """
			Typen bruker. Gyldige verdier er:
			* PERSON
			* ORGANISASJON
			""",
			requiredMode = REQUIRED,
			example = "PERSON")
	private BrukerType brukerType;
}
