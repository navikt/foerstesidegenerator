package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoerstesideResponse {

	@Schema(
			description = "Avsender av dokumentene",
			required = false)
	private Avsender avsender;

	@Schema(
			description = "Personen eller organisasjonen som dokumentene gjelder",
			required = false)
	private Bruker bruker;

	@Schema(
			description = "Temaet for forsendelsen, for eksempel FOR (Foreldrepenger), SYK (Sykepenger) eller BID (bidrag).\n" +
					"Tjenesten vil validere at konsument oppgir et gyldig tema for arkivering.",
			required = false,
			example = "FOR")
	private String tema;

	@Schema(
			description = "Behandlingstema for forsendelsen, for eksempel ab0001 (Ordinære dagpenger).\n" +
					"NB: Koden skal oppgis, ikke dekoden.",
			required = false,
			example = "ab0001")
	private String behandlingstema;

	@Schema(
			description = "Tittelen det skannede dokumentet skal få i journalen. For eksempel \"Søknad om foreldrepenger ved fødsel\" eller \"Ettersendelse til søknad om foreldrepenger ved fødsel\".\n" +
					"Arkivtittelen vil, såfremt den ikke blir endret under journalføring, vises frem i brukers journal på nav.no, samt til saksbehandler i fagsystemer som Gosys og Modia.\n" +
					"Arkivtittel skal oppgis på norsk (bokmål).",
			required = false,
			example = "Søknad om foreldrepenger ved fødsel\n" +
					"Ettersendelse til søknad om foreldrepenger ved fødsel")
	private String arkivtittel;

	@Schema(
			description = "Liste over vedlegg avsender skal sende inn.\n" +
					"NB: Selve skjemaet skal ikke inngå i vedleggslisten.\n" +
					"Arkivtittel på et vedlegg som skal sendes inn, for eksempel \"Terminbekreftelse\" eller \"Dokumentasjon av inntekt\".\n" +
					"Tittel skal oppgis på norsk (bokmål).",
			required = false,
			example = "[Terminbekreftelse, Dokumentasjon av inntekt]")
	private List<String> vedleggsliste = new ArrayList<>();

	@Schema(
			description = "Identifikator på skjema som er valgt.\n" +
					"NAV-skjemaID skal oppgis på format \"NAV 14.05-07\" uavhengig av om forsendelsen er en søknad eller ettersendelse.",
			required = false,
			example = "NAV 14.05-07")
	private String navSkjemaId;

	@Schema(
			description = "NAV-enheten som dokumentene skal rutes til for journalføring og/eller saksbehandling.\n" +
					"Feltet skal kun benyttes dersom det er behov for å overstyre fagsystemets egne rutingregler. Dette kan feks være dersom avsender vet bedre enn NAV hvilken enhet som skal motta dokumentene.",
			required = false,
			example = "9999")
	private String enhetsnummer;

	@Schema(
			description = "Saken i GSAK/PSAK som journalposten skal knyttes til.",
			required = false)
	private Arkivsak arkivsak;

	@Schema(
			description = "Systemet som opprettet dokumentene",
			required = false,
			example = "GOSYS")
	private String foerstesideOpprettetAv;
}
