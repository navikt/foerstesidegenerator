package no.nav.foerstesidegenerator.rest;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.config.MDCConstants;
import no.nav.security.oidc.context.OIDCRequestContextHolder;
import no.nav.security.oidc.context.TokenContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MDCPopulationInterceptor extends HandlerInterceptorAdapter {

	private OIDCRequestContextHolder oidcRequestContextHolder;

	public MDCPopulationInterceptor(OIDCRequestContextHolder contextHolder) {
		oidcRequestContextHolder = contextHolder;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String callId = getHeaderValueFromRequest(request, UUID.randomUUID().toString(),
				"Nav-Callid", "callId", "x_callId","Nav-CallId");
		addValueToMDC(callId, MDCConstants.MDC_CALL_ID);

		String consumerId = getHeaderValueFromRequest(request, "foerstesidegenerator",
				"nav-consumerid","Nav-Consumer-Id" , "x_consumerId", "consumerId");
		addValueToMDC(consumerId, MDCConstants.MDC_CONSUMER_ID);

		String appId = getHeaderValueFromRequest(request, "foerstesidegenerator","appId");
		addValueToMDC(appId, MDCConstants.MDC_APP_ID);
		String userId = null;
		if(oidcRequestContextHolder.getOIDCValidationContext().hasValidToken() &&
				oidcRequestContextHolder.getOIDCValidationContext().getFirstValidToken().isPresent()) {
			TokenContext tokenContext = oidcRequestContextHolder.getOIDCValidationContext().getFirstValidToken().get();
			SignedJWT parsedToken = SignedJWT.parse(tokenContext.getIdToken());
			userId = parsedToken.getJWTClaimsSet().getSubject();
			addValueToMDC(userId, MDCConstants.MDC_USER_ID);
		}

		String consumerToken = getConsumerToken(request);
		if(isNotBlank(consumerToken)){
			SignedJWT parsedToken = SignedJWT.parse(consumerToken);
			addValueToMDC(parsedToken.getJWTClaimsSet().getSubject(),MDCConstants.MDC_CONSUMER_ID);
		} else if (userId!=null && userId.startsWith("srv")){
			addValueToMDC(userId,MDCConstants.MDC_CONSUMER_ID);
		} else {
			String message = "OIDC token på Authorization header må tilhøre en Servicebruker når Nav-Consumer-Token header ikke er satt";
			log.warn(message);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
			return false;
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

	public String getConsumerToken(HttpServletRequest request){
		return Optional.ofNullable(request.getHeader("nav-consumer-token"))
				.filter(e-> e.startsWith("Bearer "))
				.map(e -> e.replaceFirst("Bearer ",""))
				.orElse(null);
	}
}
