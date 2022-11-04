package no.nav.foerstesidegenerator.azure;

import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorTechnicalException;

public class AzureTokenException extends FoerstesideGeneratorTechnicalException {
	public AzureTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
