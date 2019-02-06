package no.nav.foerstesidegenerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FoerstesideNotFoundException extends FoerstesideGeneratorFunctionalException {

	private static final String NOT_FOUND_MSG = "Kan ikke finne foersteside med loepenummer=";

	public FoerstesideNotFoundException(String message) {
		super(NOT_FOUND_MSG + message);
	}
}
