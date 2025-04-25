package no.nav.foerstesidegenerator.service.support;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.api.v1.Adresse;
import no.nav.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.foerstesidegenerator.api.v1.Avsender;
import no.nav.foerstesidegenerator.api.v1.Bruker;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.api.v1.code.BrukerType;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exception.BrukerIdIkkeValidException;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.foerstesidegenerator.api.v1.code.BrukerType.ORGANISASJON;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
import static no.nav.foerstesidegenerator.service.support.FoedselsnummerValidator.isValidPid;
import static no.nav.foerstesidegenerator.service.support.NpidValidator.isValidNpid;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Component
public class PostFoerstesideRequestValidator {

	public static final int ARKIVTITTEL_MAX_LENGTH = 500;
	private static final Pattern BRUKER_ID_ORGANISASJON_REGEX = Pattern.compile("[0-9]{9}");

	private static final Pattern NUMMER_REGEX = Pattern.compile("^[0-9]+$"); //Kun tall
	private static final Pattern NUMMER_OG_BOKSTAVER_REGEX = Pattern.compile("^[0-9A-Za-zÆØÅæøå]+$"); //Kun tall og bokstaver
	private static final Pattern BEHANDLINGSTEMA_REGEX = Pattern.compile("^[A-Za-zÆØÅæøå]{2}[0-9]{4}$"); //To bokstaver, så fire tall
	private static final Pattern NAV_SKJEMA_ID_REGEX = Pattern.compile("^[A-Za-zÆØÅæøå0-9 .-]+$"); //Kun bokstaver og tall, mellomrom, punktum og bindestrek

	//Basert på frekvensen av tegn i metadata
	private static final Pattern LOVLIGE_TEGN_REGEX = Pattern.compile("^[\\p{L}\\p{N}\\p{Zs}\\-./;()\":,–_'?&+’%#•@»«§]+$"); //L: bokstaver, N: nummer, Z: separatorer

	public void validate(PostFoerstesideRequest request, HttpHeaders headers) {
		if (request != null) {
			if (nonNull(request.getBruker())) {
				validateBruker(request);
			}
			validateRequiredFields(request);
			validateArkivtittel(request.getArkivtittel());
			validateAdresse(request.getAdresse());
			validateNetsPostboks(request.getNetsPostboks(), request.getAdresse());
			validateTema(request.getTema());
			validateConsumerId(headers);

			validateUkjentBrukerPersoninfo(request.getUkjentBrukerPersoninfo());
			validateBehandlingstema(request.getBehandlingstema());
			validateNavSkjemaId(request.getNavSkjemaId());
			validateAvsender(request.getAvsender());
			validateDokumentliste(request.getVedleggsliste(), "vedleggsliste");
			validateDokumentliste(request.getDokumentlisteFoersteside(), "dokumentlisteFoersteside");
			validateOverskriftstittel(request.getOverskriftstittel());
			validateEnhetsnummer(request.getEnhetsnummer());
			validateArkvisak(request.getArkivsak());
		}
	}

	private void validateArkivtittel(String arkivtittel) {
		if (arkivtittel != null && arkivtittel.length() > ARKIVTITTEL_MAX_LENGTH) {
			throw new InvalidRequestException("Arkivtittel kan ha maks lengde på %d tegn. Faktisk lengde=%d".formatted(ARKIVTITTEL_MAX_LENGTH, arkivtittel.length()));
		}

		if (inneholderUlovligeTegn(arkivtittel)) {
			throw new InvalidRequestException("Arkivtittel inneholder ulovlige tegn. Mottatt verdi=%s".formatted(arkivtittel));
		}
	}

	private void validateBruker(PostFoerstesideRequest request) {
		if (!isBrukerIdValid(request.getBruker())) {
			String brukerId = request.getBruker().getBrukerId();
			BrukerType brukerType = request.getBruker().getBrukerType();
			String feilmelding = format("Validering av ident feilet. brukerId=%s, brukerType=%s. Kunne ikke opprette førsteside.", brukerId, brukerType);
			log.warn(feilmelding);
			throw new BrukerIdIkkeValidException(feilmelding);
		}
	}

	private boolean isBrukerIdValid(Bruker bruker) {
		if (BrukerType.PERSON.equals(bruker.getBrukerType())) {
			boolean validPid = isValidPid(bruker.getBrukerId(), true);
			if(!validPid) {
				return isValidNpid(bruker.getBrukerId());
			}
			return validPid;
		} else if (ORGANISASJON.equals(bruker.getBrukerType())) {
			return BRUKER_ID_ORGANISASJON_REGEX.matcher(bruker.getBrukerId()).matches();
		}
		return false;
	}

	private void validateRequiredFields(PostFoerstesideRequest request) {
		if (request.getSpraakkode() == null) {
			throw new InvalidRequestException("Spraakkode kan ikke være null.");
		}

		if (request.getBruker() != null && (request.getBruker().getBrukerId() == null || request.getBruker().getBrukerType() == null)) {
			throw new InvalidRequestException("Hvis Bruker oppgis, må BrukerId og BrukerType være satt.");
		}

		if (request.getOverskriftstittel() == null) {
			throw new InvalidRequestException("Overskriftstittel kan ikke være null.");
		}

		if (request.getFoerstesidetype() == null) {
			throw new InvalidRequestException("Foerstesidetype kan ikke være null.");
		}

		if (request.getArkivsak() != null && (request.getArkivsak().getArkivsaksystem() == null || request.getArkivsak().getArkivsaksnummer() == null)) {
			throw new InvalidRequestException("Hvis Arkivsak oppgis, må Arkivsaksystem og Arkivsaksnummer være satt.");
		}

	}

	private void validateNetsPostboks(String netsPostboks, Adresse adresse) {
		if (isEmpty(netsPostboks) && adresse == null) {
			throw new InvalidRequestException("NETS-postboks og/eller Adresse må være satt");
		}

		if (inneholderIkkeBareNummer(netsPostboks)) {
			throw new InvalidRequestException("NETS-postboks kan kun inneholde tall. Mottatt verdi=%s".formatted(netsPostboks));
		}
	}

	private void validateAdresse(Adresse adresse) {
		if (adresse != null) {
			if (adresse.getAdresselinje1() == null || adresse.getPostnummer() == null || adresse.getPoststed() == null) {
				throw new InvalidRequestException("Hvis Adresse oppgis, må Adresselinje1, Postnummer og Poststed være satt.");
			}
			if (inneholderUlovligeTegn(adresse.getAdresselinje1())) {
				throw new InvalidRequestException("Adresselinje1 inneholder ulovlige tegn. Mottatt verdi=%s".formatted(adresse.getAdresselinje1()));
			}
			if (inneholderUlovligeTegn(adresse.getAdresselinje2())) {
				throw new InvalidRequestException("Adresselinje2 inneholder ulovlige tegn. Mottatt verdi=%s".formatted(adresse.getAdresselinje2()));
			}
			if (inneholderUlovligeTegn(adresse.getAdresselinje3())) {
				throw new InvalidRequestException("Adresselinje3 inneholder ulovlige tegn. Mottatt verdi=%s".formatted(adresse.getAdresselinje3()));
			}
			if (inneholderIkkeBareNummer(adresse.getPostnummer())) {
				throw new InvalidRequestException("Postnummer kan kun inneholde tall. Mottatt verdi=%s".formatted(adresse.getPostnummer()));
			}
			if (inneholderUlovligeTegn(adresse.getPoststed())) {
				throw new InvalidRequestException("Poststed inneholder ulovlige tegn. Mottatt verdi=%s".formatted(adresse.getPoststed()));
			}
		}
	}

	private void validateTema(String tema) {
		if (isNotBlank(tema)) {
			try {
				FagomradeCode.valueOf(tema);
			} catch (IllegalArgumentException e) {
				throw new InvalidTemaException(String.format("Tema oppgitt validerer ikke mot kodeverk, tema=%s", tema));
			}
		} else {
			throw new InvalidRequestException("Tema kan ikke være null");
		}
	}

	private void validateConsumerId(HttpHeaders headers) {
		if (headers.containsKey(NAV_CONSUMER_ID)) {
			return;
		}

		throw new InvalidRequestException("Mangler Nav-Consumer-Id header");
	}

	private void validateArkvisak(Arkivsak arkivsak) {
		if (arkivsak != null) {
			if (isNotBlank(arkivsak.getArkivsaksnummer()) && !NUMMER_OG_BOKSTAVER_REGEX.matcher(arkivsak.getArkivsaksnummer()).matches()) {
				throw new InvalidRequestException("ArkivSaksnummer kan kun inneholde tall og/eller bokstaver. Mottatt verdi=%s".formatted(arkivsak.getArkivsaksnummer()));
			}
		}
	}

	private void validateEnhetsnummer(String enhetsnummer) {
		if (inneholderIkkeBareNummer(enhetsnummer)) {
			throw new InvalidRequestException("Enhetsnummer kan kun inneholde tall. Mottatt verdi=%s".formatted(enhetsnummer));
		}
	}

	private void validateOverskriftstittel(String tittel) {
		if (inneholderUlovligeTegn(tittel)) {
			throw new InvalidRequestException("Tittel inneholder ulovlige tegn. Mottatt verdi=%s".formatted(tittel));
		}
	}

	private void validateDokumentliste(List<String> liste, String felt) {
		var vedleggMedUlovligeTegn = liste.stream()
				.filter(this::inneholderUlovligeTegn)
				.toList();

		if (!vedleggMedUlovligeTegn.isEmpty()) {
			throw new InvalidRequestException("Ett eller flere vedlegg i %s inneholder ulovlige tegn. Mottatt verdi=%s".formatted(felt, vedleggMedUlovligeTegn));
		}
	}

	private void validateAvsender(Avsender avsender) {
		if (avsender != null) {
			if (inneholderIkkeBareNummer(avsender.getAvsenderId())) {
				throw new InvalidRequestException("AvsenderId kan kun inneholde tall. Mottatt verdi=%s".formatted(avsender.getAvsenderId()));
			}
			if (inneholderUlovligeTegn(avsender.getAvsenderNavn())) {
				throw new InvalidRequestException("AvsenderNavn inneholder ulovlige tegn. Mottatt verdi=%s".formatted(avsender.getAvsenderNavn()));
			}
		}
	}

	private void validateNavSkjemaId(String navSkjemaId) {
		if (isNotBlank(navSkjemaId)) {
			if (!NAV_SKJEMA_ID_REGEX.matcher(navSkjemaId).matches()) {
				throw new InvalidRequestException("NAV-skjemaId kan kun inneholde bokstaver, tall, mellomrom, punktum og bindestrek. Mottatt verdi=%s".formatted(navSkjemaId));
			}
		}
	}

	private void validateBehandlingstema(String behandlingstema) {
		if (isNotBlank(behandlingstema)) {
			if (!BEHANDLINGSTEMA_REGEX.matcher(behandlingstema).matches()) {
				throw new InvalidRequestException("Behandlingstema må være på formatet 'To bokstaver, så fire tall'. Eksempel: AB1234. Mottatt verdi=%s".formatted(behandlingstema));
			}
		}
	}

	private void validateUkjentBrukerPersoninfo(String ukjentBrukerPersoninfo) {
		if (inneholderUlovligeTegn(ukjentBrukerPersoninfo)) {
			throw new InvalidRequestException("UkjentBrukerPersoninfo inneholder ulovlige tegn. Mottatt verdi=%s".formatted(ukjentBrukerPersoninfo));
		}
	}

	private boolean inneholderUlovligeTegn(String verdi) {
		return isNotBlank(verdi) && !LOVLIGE_TEGN_REGEX.matcher(verdi).matches();
	}

	private boolean inneholderIkkeBareNummer(String verdi) {
		return isNotBlank(verdi) && !NUMMER_REGEX.matcher(verdi).matches();
	}
}