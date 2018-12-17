package no.nav.foerstesidegenerator.service.support;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exceptions.InvalidTemaException;
import org.springframework.stereotype.Component;

@Component
public class PostFoerstesideRequestValidator {

	public void validate(PostFoerstesideRequest request) {
		if (request != null) {
			validateTema(request.getTema());
		}
		// TODO: flere felter?
	}

	private static void validateTema(String tema) {
		if (isNotBlank(tema)){
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
