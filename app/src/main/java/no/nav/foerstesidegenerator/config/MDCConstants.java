package no.nav.foerstesidegenerator.config;

import java.util.Set;

public class MDCConstants {

	private MDCConstants() {
	}

	public static final String MDC_CALL_ID = "callId";
	public static final String MDC_USER_ID = "userId";
	public static final String MDC_CONSUMER_ID = "consumerId";

	public static Set<String> ALL_KEYS = Set.of(MDC_CALL_ID);
}
