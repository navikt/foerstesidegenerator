package no.nav.foerstesidegenerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Technical exception from Metaforce
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MetaforceTechnicalException extends FoerstesideGeneratorTechnicalException {

	public MetaforceTechnicalException(String s) {
		super(s);
	}
	
	public MetaforceTechnicalException(String s, Throwable t) {
		super(s, t);
	}

}
