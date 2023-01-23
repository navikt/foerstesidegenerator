package no.nav.foerstesidegenerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MetaforceTechnicalException extends FoerstesideGeneratorTechnicalException {
	
	public MetaforceTechnicalException(String s, Throwable t) {
		super(s, t);
	}

}
