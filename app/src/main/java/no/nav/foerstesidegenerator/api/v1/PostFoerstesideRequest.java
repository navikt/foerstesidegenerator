package no.nav.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.foerstesidegenerator.api.v1.code.Foerstesidetype;
import no.nav.foerstesidegenerator.api.v1.code.Spraakkode;

import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static no.nav.foerstesidegenerator.api.v1.code.Spraakkode.NB;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFoerstesideRequest {

	@Schema(description = """
			Målformen førstesiden skal produseres på.
			
			Gyldige verdier er NB, NN og EN.
			
			Default verdi er NB
			""",
			requiredMode = REQUIRED,
			example = "NB")
	@NotNull(message = "PostFoerstesideRequest mangler Spraakkode")
	@Builder.Default
	private Spraakkode spraakkode = NB;

	@Schema(description = """
			Adressen som brukeren skal sende dokumentene til. Adressen blir trykket på førstesiden.
			Dersom dokumentene skal sendes til primærskanningleverandør (idag NETS), kan konsument velge å la adressefeltet stå blankt, og kun oppgi postboks. Resten av adressen blir da fylt ut automatisk.
			""")
	private Adresse adresse;

	@Schema(description = """
			Postboksen hos hovedskanningleverandør (idag NETS) som dokumentene skal sendes til.
			"NB: Dersom adresse ikke er oppgitt, er postboks påkrevd, og vil bli brukt til å generere en korrekt adresse.
			""",
			example = "1234")
	private String netsPostboks;

	@Schema(description = "Avsender av dokumentene")
	private Avsender avsender;

	@Schema(description = "Personen eller organisasjonen som dokumentene gjelder")
	private Bruker bruker;

	@Schema(description = """
			Kan settes dersom man ikke kjenner brukerens fødselsnummer, men har noe informasjon om brukeren som kan være relevant når en saksbehandler skal finne ut hvor saken skal behandles. Dette kan være brukerens navn eller informasjon om hvilket NAV-kontor han/hun har vært innom.
			ukjentBrukerPersoninfo trykkes nederst på førstesiden.
			""")
	private String ukjentBrukerPersoninfo;

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
			example = "Søknad om foreldrepenger ved fødsel")
	private String arkivtittel;

	@Schema(description = """
			Liste over vedlegg avsender skal sende inn.
			NB: Selve skjemaet skal ikke inngå i vedleggslisten.
			Arkivtittel på et vedlegg som skal sendes inn, for eksempel "Terminbekreftelse" eller "Dokumentasjon av inntekt".
			Tittel skal oppgis på norsk (bokmål).""",
			example = "[Terminbekreftelse, Dokumentasjon av inntekt]")
	@Builder.Default
	private List<String> vedleggsliste = new ArrayList<>();

	@Schema(description = """
			Identifikator på skjema som er valgt.
			"NAV-skjemaID skal oppgis på format "NAV 14.05-07" uavhengig av om forsendelsen er en søknad eller ettersendelse.
			""",
			example = "NAV 14.05-07")
	private String navSkjemaId;

	@NotNull(message = "PostFoerstesideRequest mangler Overskriftstittel")
	@Schema(description = "Teksten som skal trykkes som overskrift på førstesiden. Overskriften kan oppgis på brukers eget språk (bokmål, nynorsk eller engelsk).",
			requiredMode = REQUIRED,
			example = """
					"Søknad om foreldrepenger ved fødsel - NAV 14.05-07"
					""")
	private String overskriftstittel;

	@Schema(description = """
			Alt som skal trykkes på førstesiden under "Send inn følgende dokumenter".
			Tittel på et dokument som skal sendes inn, for eksempel "Søknad om foreldrepenger ved fødsel", "Terminbekreftelse" eller "Dokumentasjon av inntekt".
			Titlene kan oppgis på brukers eget språk (bokmål, nynorsk eller engelsk)""",
			example = "[Søknad om foreldrepenger ved fødsel, Terminbekreftelse, Dokumentasjon av inntekt]")
	@Builder.Default
	private List<String> dokumentlisteFoersteside = new ArrayList<>();

	@NotNull(message = "PostFoerstesideRequest mangler Foerstesidetype")
	@Schema(description = """
			Sier hvorvidt forsendelsen er et NAV-skjema, NAV-internt, en ettersendelse til et skjema, eller løspost (altså frittstående dokumentasjon som ikke er knyttet til et skjema).
			Foerstesidetypen styrer hvilken brevkode journalposten får i arkivet.
			""",
			requiredMode = REQUIRED,
			example = "SKJEMA")
	private Foerstesidetype foerstesidetype;

	@Schema(description = """
			NAV-enheten som dokumentene skal rutes til for journalføring og/eller saksbehandling.
			Feltet skal kun benyttes dersom det er behov for å overstyre fagsystemets egne rutingregler. Dette kan feks være dersom avsender vet bedre enn NAV hvilken enhet som skal motta dokumentene.
			""",
			example = "9999")
	private String enhetsnummer;

	@Schema(description = "Saken i GSAK/PSAK som journalposten skal knyttes til.")
	private Arkivsak arkivsak;
}
