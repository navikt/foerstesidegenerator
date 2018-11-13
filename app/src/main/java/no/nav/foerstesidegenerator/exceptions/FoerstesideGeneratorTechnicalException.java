package no.nav.foerstesidegenerator.exceptions;

public class FoerstesideGeneratorTechnicalException extends RuntimeException {

	public FoerstesideGeneratorTechnicalException(String message) {
		super(message);
	}

	public FoerstesideGeneratorTechnicalException(String message, Throwable cause) {
		super(message, cause);
	}
}
