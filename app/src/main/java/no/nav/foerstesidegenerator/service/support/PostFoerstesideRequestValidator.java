package no.nav.foerstesidegenerator.service.support;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.domain.Adresse;
import no.nav.foerstesidegenerator.domain.Bruker;
import no.nav.foerstesidegenerator.domain.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.code.BrukerType;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exception.BrukerIdIkkeValidException;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static no.nav.foerstesidegenerator.domain.code.BrukerType.ORGANISASJON;
import static no.nav.foerstesidegenerator.service.support.FoedselsnummerValidator.isValidPid;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Component
public class PostFoerstesideRequestValidator {

	private static final Pattern BRUKER_ID_ORGANISASJON_REGEX = Pattern.compile("[0-9]{9}");

	public void validate(PostFoerstesideRequest request, HttpHeaders headers) {
		if (request != null) {
			if (nonNull(request.getBruker())) {
				validateBruker(request);
			}
			validateRequiredFields(request);

			validateAdresseAndNetsPostboks(request.getNetsPostboks(), request.getAdresse());

			validateTema(request.getTema());

			validateConsumerId(headers);
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
			return isValidPid(bruker.getBrukerId(), true);
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

	private void validateAdresseAndNetsPostboks(String netsPostboks, Adresse adresse) {
		if (isEmpty(netsPostboks) && adresse == null) {
			throw new InvalidRequestException("NETS-postboks og/eller Adresse må være satt");
		}

		if (adresse != null && (adresse.getAdresselinje1() == null || adresse.getPostnummer() == null || adresse.getPoststed() == null)) {
			throw new InvalidRequestException("Hvis Adresse oppgis, må Adresselinje1, Postnummer og Poststed være satt.");
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
		if (headers.containsKey("Nav-Consumer-Id")) {
			return;
		}
		if (headers.containsKey("x_consumerId")) {
			return;
		}
		if (headers.containsKey("consumerId")) {
			return;
		}
		if (headers.containsKey("nav-consumerid")) {
			return;
		}
		throw new InvalidRequestException("Mangler Nav-Consumer-Id header");
	}
}