package no.nav.foerstesidegenerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FoerstesideGeneratorTechnicalException extends RuntimeException {

	public FoerstesideGeneratorTechnicalException(String message) {
		super(message);
	}

	public FoerstesideGeneratorTechnicalException(String message, Throwable cause) {
		super(message, cause);
	}
}
