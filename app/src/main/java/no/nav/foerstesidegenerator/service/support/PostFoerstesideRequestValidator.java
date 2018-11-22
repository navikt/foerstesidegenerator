package no.nav.foerstesidegenerator.service.support;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

//import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.domain.code.FagomradeCode;
import no.nav.foerstesidegenerator.exceptions.InvalidTemaException;

public class PostFoerstesideRequestValidator {

//	public static void validate(PostFoerstesideRequest request) {
//		if (isNotBlank(request.getTema())) {
//			validateTema(request.getTema());
//		}
//
//		// flere felter
//	}

	private static void validateTema(String tema) {
		try {
			FagomradeCode.valueOf(tema);
		} catch (IllegalArgumentException e) {
			throw new InvalidTemaException(String.format("Tema oppgitt validerer ikke mot kodeverk, tema=%s", tema));
		}
	}
}
