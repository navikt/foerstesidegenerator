package no.nav.foerstesidegenerator.consumer.metaforce.update;

import org.datacontract.schemas._2004._07.metaforce_common.InstanceErrorCodes;

/**
 * Exception caused by Metaforce
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public class MetaforceException extends RuntimeException {

	private InstanceErrorCodes errorCode;

	public MetaforceException(String route, String errorMessage) {
		super("Exception from Metaforce on route[" + route + "]: " + errorMessage);
	}

	public MetaforceException(String route, String errorMessage, InstanceErrorCodes errorCode) {
		this(route, errorMessage);
		this.errorCode = errorCode;
	}

	public InstanceErrorCodes getErrorCode() {
		return errorCode;
	}
}
