package no.nav.foerstesidegenerator.rest;

import static org.apache.commons.lang3.StringUtils.isBlank;

import com.nimbusds.jwt.SignedJWT;
import no.nav.foerstesidegenerator.config.MDCConstants;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.TokenContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class MDCPopulationInterceptor extends HandlerInterceptorAdapter {

	private OIDCRequestContextHolder oidcRequestContextHolder;

	public MDCPopulationInterceptor(OIDCRequestContextHolder contextHolder) {
		oidcRequestContextHolder = contextHolder;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String callId = getHeaderValueFromRequest(request, UUID.randomUUID().toString(),
				"Nav-Callid", "Nav-CallId", "x_callId", "callId");
		addValueToMDC(callId, MDCConstants.MDC_CALL_ID);

		String consumerId = getHeaderValueFromRequest(request, "foerstesidegenerator",
				"consumerId", "x_consumerId", "Nav-Consumer-Id");
		addValueToMDC(consumerId, MDCConstants.MDC_CONSUMER_ID);

		String appId = getHeaderValueFromRequest(request, "foerstesidegenerator","appId");
		addValueToMDC(appId, MDCConstants.MDC_APP_ID);

		if(oidcRequestContextHolder.getOIDCValidationContext().hasValidToken() &&
				oidcRequestContextHolder.getOIDCValidationContext().getFirstValidToken().isPresent()) {
			TokenContext tokenContext = oidcRequestContextHolder.getOIDCValidationContext().getFirstValidToken().get();
			SignedJWT parsedToken = SignedJWT.parse(tokenContext.getIdToken());
			addValueToMDC(parsedToken.getJWTClaimsSet().getSubject(), MDCConstants.MDC_USER_ID);
		}
		return true;
	}

	/**
	 *
	 * @return Return the value in the first header that is defined in the request
	 */
	private String getHeaderValueFromRequest(HttpServletRequest request, String fallbackValue, String ... headerNames) {
		for(String headerName : headerNames) {
			String value = request.getHeader(headerName);
			if(!isBlank(value)) {
				return value;
			}
		}
		return fallbackValue;
	}

	private boolean addValueToMDC(String value, String key) {
		if(value != null && !value.isEmpty()) {
			MDC.put(key, value);
			return true;
		}
		return false;
	}
}
