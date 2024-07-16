package no.nav.foerstesidegenerator.rest;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_CALL_ID;
import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_CONSUMER_ID;
import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_USER_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class MDCPopulationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String callId = getHeaderValueFromRequest(request, UUID.randomUUID().toString(),
                "Nav-Callid", "callId", "x_callId");
        addValueToMDC(MDC_CALL_ID, callId);

        String consumerId = getHeaderValueFromRequest(request, "foerstesidegenerator",
                "nav-consumerid", "Nav-Consumer-Id", "x_consumerId", "consumerId");
        addValueToMDC(MDC_CONSUMER_ID, consumerId);

        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (isNotBlank(authorizationHeader)) {
            try {
                final String bearerToken = authorizationHeader.split(" ")[1];
                SignedJWT parsedToken = SignedJWT.parse(bearerToken);
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
