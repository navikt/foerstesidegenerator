package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.dok.foerstesidegenerator.api.v1.code.BrukerType;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bruker {

	@NotNull(message = "Bruker mangler BrukerId")
	@Schema(
			description = "Fødselsnummeret eller organisasjonsnummeret som dokumentene omhandler.\n" +
					"Fødselsnummeret vil bli trykket i klartekst på førstesiden.",
			required = true,
			example = "01234567890")
	private String brukerId;

	@NotNull(message = "Bruker mangler BrukerType")
	@Schema(
			description = "Typen bruker. Gyldige verdier er:\n" +
					"* PERSON\n" +
					"* ORGANISASJON",
			required = true,
			example = "PERSON")
	private BrukerType brukerType;
}
