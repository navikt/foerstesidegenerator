package no.nav.dok.foerstesidegenerator.api.v1;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import no.nav.dok.foerstesidegenerator.api.v1.code.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.code.Spraakkode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFoerstesideRequest {

	@Schema(
			description = "Målformen førstesiden skal produseres på.\nGyldige verdier er NB, NN og EN.\nDefault verdi er NB",
			required = true,
			example = "NB")
	@NotNull(message = "PostFoerstesideRequest mangler Spraakkode")
	private Spraakkode spraakkode = Spraakkode.NB;

	@Schema(
			description = "Adressen som brukeren skal sende dokumentene til. Adressen blir trykket på førstesiden.\n" +
					"Dersom dokumentene skal sendes til primærskanningleverandør (idag NETS), kan konsument velge å la adressefeltet stå blankt, og kun oppgi postboks. Resten av adressen blir da fylt ut automatisk.",
			required = false)
	private Adresse adresse;

	@Schema(
			description = "Postboksen hos hovedskanningleverandør (idag NETS) som dokumentene skal sendes til.\n" +
					"NB: Dersom adresse ikke er oppgitt, er postboks påkrevd, og vil bli brukt til å generere en korrekt adresse.",
			required = false,
			example = "1234")
	private String netsPostboks;

	@Schema(
			description = "Avsender av dokumentene",
			required = false)
	private Avsender avsender;

	@Schema(
			description = "Personen eller organisasjonen som dokumentene gjelder",
			required = false)
	private Bruker bruker;

	@Schema(
			description = "Kan settes dersom man ikke kjenner brukerens fødselsnummer, men har noe informasjon om brukeren som kan være relevant når en saksbehandler skal finne ut hvor saken skal behandles. Dette kan være brukerens navn eller informasjon om hvilket NAV-kontor han/hun har vært innom.\n" +
					"ukjentBrukerPersoninfo trykkes nederst på førstesiden.",
			required = false)
	private String ukjentBrukerPersoninfo;

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

	@NotNull(message = "PostFoerstesideRequest mangler Overskriftstittel")
	@Schema(
			description = "Teksten som skal trykkes som overskrift på førstesiden.\n" +
					"Overskriften kan oppgis på brukers eget språk (bokmål, nynorsk eller engelsk)",
			required = true,
			example = "\"Søknad om foreldrepenger ved fødsel - NAV 14.05-07\", \"Ettersendelse til søknad om foreldrepenger ved fødsel - NAV 14.05-07\"")
	private String overskriftstittel;

	@Schema(
			description = "Alt som skal trykkes på førstesiden under \"Send inn følgende dokumenter\".\n" +
					"Tittel på et dokument som skal sendes inn, for eksempel \"Søknad om foreldrepenger ved fødsel\", \"Terminbekreftelse\" eller \"Dokumentasjon av inntekt\".\n" +
					"Titlene kan oppgis på brukers eget språk (bokmål, nynorsk eller engelsk)",
			required = false,
			example = "[Søknad om foreldrepenger ved fødsel, Terminbekreftelse, Dokumentasjon av inntekt]")
	private List<String> dokumentlisteFoersteside = new ArrayList<>();

	@NotNull(message = "PostFoerstesideRequest mangler Foerstesidetype")
	@Schema(
			description = "Sier hvorvidt forsendelsen er et NAV-skjema, NAV-internt, en ettersendelse til et skjema, eller løspost (altså frittstående dokumentasjon som ikke er knyttet til et skjema).\n" +
					"Foerstesidetypen styrer hvilken brevkode journalposten får i arkivet.",
			required = true,
			example = "SKJEMA")
	private Foerstesidetype foerstesidetype;

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
}
