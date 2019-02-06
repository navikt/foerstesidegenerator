package no.nav.foerstesidegenerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLoepenummerException extends FoerstesideGeneratorFunctionalException {
	public InvalidLoepenummerException(String message) {
		super(message);
	}
}
