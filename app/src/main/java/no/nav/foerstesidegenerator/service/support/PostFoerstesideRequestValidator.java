package no.nav.foerstesidegenerator.service.support;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exception.InvalidRequestException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class PostFoerstesideRequestValidator {

	public void validate(PostFoerstesideRequest request, HttpHeaders headers) {
		if (request != null) {
			validateRequiredFields(request);

			validateAdresseAndNetsPostboks(request.getNetsPostboks(), request.getAdresse());

			validateTema(request.getTema());

			validateConsumerId(headers);
		}
		// flere felter?
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

	private void validateConsumerId(HttpHeaders headers){
		if(headers.containsKey("Nav-Consumer-Id")){
			return;
		}
		if(headers.containsKey("x_consumerId")){
			return;
		}
		if(headers.containsKey("consumerId")){
			return;
		}
		if(headers.containsKey("nav-consumerid")){
			return;
		}
		throw new InvalidRequestException("Mangler Nav-Consumer-Id header");
	}
}