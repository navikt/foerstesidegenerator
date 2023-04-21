package no.nav.foerstesidegenerator.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class XMLTransformerException extends FoerstesideGeneratorFunctionalException {
	public XMLTransformerException(String message, Throwable cause) {
		super(message, cause);
	}
}
