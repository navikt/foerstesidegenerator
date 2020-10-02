package no.nav.foerstesidegenerator.rest;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.config.MDCConstants;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class MDCPopulationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String callId = getHeaderValueFromRequest(request, UUID.randomUUID().toString(),
                "Nav-Callid", "callId", "x_callId");
        addValueToMDC(callId, MDCConstants.MDC_CALL_ID);

        String consumerId = getHeaderValueFromRequest(request, "foerstesidegenerator",
                "nav-consumerid", "Nav-Consumer-Id", "x_consumerId", "consumerId");
        addValueToMDC(consumerId, MDCConstants.MDC_CONSUMER_ID);

        String appId = getHeaderValueFromRequest(request, "foerstesidegenerator", MDCConstants.MDC_APP_ID);
        addValueToMDC(appId, MDCConstants.MDC_APP_ID);
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isNotBlank(authorizationHeader)) {
            try {
                final String bearerToken = authorizationHeader.split(" ")[1];
                SignedJWT parsedToken = SignedJWT.parse(bearerToken);
                addValueToMDC(parsedToken.getJWTClaimsSet().getSubject(), MDCConstants.MDC_USER_ID);
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

    private void addValueToMDC(String value, String key) {
        if (value != null && !value.isEmpty()) {
            MDC.put(key, value);
        }
    }
}
