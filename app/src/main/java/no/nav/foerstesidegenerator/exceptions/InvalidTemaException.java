package no.nav.foerstesidegenerator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTemaException extends FoerstesideGeneratorFunctionalException {

	public InvalidTemaException(String message) {
		super(message);
	}
}
