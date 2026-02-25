package no.nav.foerstesidegenerator.rest;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_CONSUMER_ID;
import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_USER_ID;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class MDCPopulationInterceptor implements HandlerInterceptor {

	public static final String CONSUMER_ID_FALLBACK = "Ukjent system";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String consumerId = getHeaderValueFromRequest(request, CONSUMER_ID_FALLBACK,
				NAV_CONSUMER_ID);
		addValueToMDC(MDC_CONSUMER_ID, consumerId);

		final String authorizationHeader = request.getHeader(AUTHORIZATION);
		if (isNotBlank(authorizationHeader)) {
			try {
				final String bearerToken = authorizationHeader.split(" ")[1];
				SignedJWT parsedToken = SignedJWT.parse(bearerToken);
				// TODO: Fiks slik at det er NAV-ident som blir satt i userId
				addValueToMDC(MDC_USER_ID, parsedToken.getJWTClaimsSet().getSubject());
			} catch (Exception e) {
				// noop
			}
		}

		return true;
	}

	/**
	 * @return Return the value in the first header that is defined in the request
	 */
	private String getHeaderValueFromRequest(HttpServletRequest request, String fallbackValue, String... headerNames) {
		for (String headerName : headerNames) {
			String value = request.getHeader(headerName);
			if (!isBlank(value)) {
				return value;
			}
		}
		return fallbackValue;
	}

	private void addValueToMDC(String key, String value) {
		if (value != null && !value.isEmpty()) {
			MDC.put(key, value);
		}
	}
}
