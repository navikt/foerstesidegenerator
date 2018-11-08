package no.nav.foerstesidegenerator.exception;

/**
 * Technical exception from Metaforce
 *
 * @author Paul Magne Lunde, Visma Consulting
 */
public class MetaforceTechnicalException extends FoerstesideGeneratorTechnicalException {

	public MetaforceTechnicalException(String s) {
		super(s);
	}
	
	public MetaforceTechnicalException(String s, Throwable t) {
		super(s, t);
	}

}
