package no.nav.foerstesidegenerator.service.support;

import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import no.nav.foerstesidegenerator.exception.InvalidTemaException;
import org.springframework.stereotype.Component;

@Component
public class PostFoerstesideRequestValidator {

	public void validate(PostFoerstesideRequest request) {
		if (request != null) {
			validateAdresseFelter(request.getNetsPostboks(), request.getAdresse());

			validateTema(request.getTema());

		}
		// flere felter?
	}

	private void validateAdresseFelter(String netsPostboks, Adresse adresse) {
		if (isEmpty(netsPostboks) && adresse == null) {
			throw new FoerstesideGeneratorFunctionalException("NETS-postboks og/eller Adresse må være satt");
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
			throw new InvalidTemaException("Tema kan ikke være null");
		}
	}
}