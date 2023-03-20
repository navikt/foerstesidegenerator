package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoerstesideResponse {

	@Schema(description = "Avsender av dokumentene"
	)
	private Avsender avsender;

	@Schema(description = "Personen eller organisasjonen som dokumentene gjelder")
	private Bruker bruker;

	@Schema(description = """
			Temaet for forsendelsen, for eksempel FOR (Foreldrepenger), SYK (Sykepenger) eller BID (bidrag).
			Tjenesten vil validere at konsument oppgir et gyldig tema for arkivering.
			""",
			example = "FOR")
	private String tema;

	@Schema(description = """
			Behandlingstema for forsendelsen, for eksempel ab0001 (Ordinære dagpenger).
			"NB: Koden skal oppgis, ikke dekoden.
			""",
			example = "ab0001")
	private String behandlingstema;

	@Schema(description = """
			Tittelen det skannede dokumentet skal få i journalen. For eksempel "Søknad om foreldrepenger ved fødsel" eller "Ettersendelse til søknad om foreldrepenger ved fødsel".
			Arkivtittelen vil, såfremt den ikke blir endret under journalføring, vises frem i brukers journal på nav.no, samt til saksbehandler i fagsystemer som Gosys og Modia.
			Arkivtittel skal oppgis på norsk (bokmål).
			""",
			example = "Ettersendelse til søknad om foreldrepenger ved fødsel")
	private String arkivtittel;

	@Schema(description = """
			Liste over vedlegg avsender skal sende inn.
			NB: Selve skjemaet skal ikke inngå i vedleggslisten.
			Arkivtittel på et vedlegg som skal sendes inn, for eksempel "Terminbekreftelse" eller "Dokumentasjon av inntekt".
			Tittel skal oppgis på norsk (bokmål).""",
			example = "[Terminbekreftelse, Dokumentasjon av inntekt]")
	private List<String> vedleggsliste = new ArrayList<>();

	@Schema(description = """
			Identifikator på skjema som er valgt.
			NAV-skjemaID skal oppgis på format "NAV 14.05-07" uavhengig av om forsendelsen er en søknad eller ettersendelse.
			""",
			example = "NAV 14.05-07")
	private String navSkjemaId;

	@Schema(description = """
			NAV-enheten som dokumentene skal rutes til for journalføring og/eller saksbehandling.
			Feltet skal kun benyttes dersom det er behov for å overstyre fagsystemets egne rutingregler. Dette kan feks være dersom avsender vet bedre enn NAV hvilken enhet som skal motta dokumentene.
			""",
			example = "9999")
	private String enhetsnummer;

	@Schema(description = "Saken i GSAK/PSAK som journalposten skal knyttes til.")
	private Arkivsak arkivsak;

	@Schema(description = "Systemet som opprettet dokumentene",
			example = "GOSYS")
	private String foerstesideOpprettetAv;
}
