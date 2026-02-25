package no.nav.foerstesidegenerator.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Map;

import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_CONSUMER_ID;
import static no.nav.foerstesidegenerator.config.MDCConstants.MDC_USER_ID;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
import static no.nav.foerstesidegenerator.rest.MDCPopulationInterceptor.CONSUMER_ID_FALLBACK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ExtendWith(MockitoExtension.class)
class MDCPopulationInterceptorTest {

    static final String APP_TOKEN = "Bearer eyJraWQiOiI0NTI0NzczMy0xNGI4LTRmMWEtYTc2NS1iMjUwN2M0ODdiNWYiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzcnZnb3N5cy1uYWlzIiwiYXVkIjpbInNydmdvc3lzLW5haXMiLCJwcmVwcm9kLmxvY2FsIl0sInZlciI6IjEuMCIsIm5iZiI6MTU3OTAwNTAyNCwiYXpwIjoic3J2Z29zeXMtbmFpcyIsImlkZW50VHlwZSI6IlN5c3RlbXJlc3N1cnMiLCJhdXRoX3RpbWUiOjE1NzkwMDUwMjQsImlzcyI6Imh0dHBzOlwvXC9zZWN1cml0eS10b2tlbi1zZXJ2aWNlLXQ0Lm5haXMucHJlcHJvZC5sb2NhbCIsImV4cCI6MTU3OTAwODYyNCwiaWF0IjoxNTc5MDA1MDI0LCJqdGkiOiI5YWI5NGM1Ny04NzczLTQyZDMtOGJiMC03NzVmZDhkZjU4ODUifQ.jdfcrgGZAq8sQ-KFB-McL3CrARROdoOMlIiSVQRregwSIbL_AR4x6ZQUDwMN_RRHmYdCv28Ap14VFZpW9qR7KDhgzEHtRmPcpzcMOvlsxf3D8F1heC-whtJZgIamsF0b9vTkWPB2PVCOWNDdgGPqaZ55SGZTtn0IR9iDECgcMGRxEYuE1HEb1PiZaemb-63kKYl_zOexi0wzNkm1Bf4TLE5adlkStP3lY8vAcYkSAhVRNRZDNQbaEDbqR0dMFP_gqi9XrzGmYPFvhO-iHxa_qH9szUkdVd84bZmtsGClTo2q64OgDVCaA_gZupZRoS6du7eekTDMndOnX3kqc46sjQ";
    static final String USER_TOKEN = "Bearer eyAidHlwIjogIkpXVCIsICJraWQiOiAiaG9ReWxXNDZpYWoyY1VjN3d4TFF2b3pLVTMwPSIsICJhbGciOiAiUlMyNTYiIH0.eyAiYXRfaGFzaCI6ICJoelZmd0djWjZSVHJiSE0zZnFoMm13IiwgInN1YiI6ICJaOTkwNzYxIiwgImF1ZGl0VHJhY2tpbmdJZCI6ICI5MzczMzZjNC0yM2FmLTRkYTMtODk2ZS02OGNjNGU4NDAyZjgtOTA1ODYyIiwgImlzcyI6ICJodHRwczovL2lzc28tdC5hZGVvLm5vOjQ0My9pc3NvL29hdXRoMiIsICJ0b2tlbk5hbWUiOiAiaWRfdG9rZW4iLCAiYXVkIjogImlkYS10IiwgImNfaGFzaCI6ICI2eEpnamxOUENuOHAtbklOMHE5bmVRIiwgIm9yZy5mb3JnZXJvY2sub3BlbmlkY29ubmVjdC5vcHMiOiAiNDg1Mjg3YzItZjFlNC00Njg2LTliODYtYjE5ZjVlMmFmNjJhIiwgImF6cCI6ICJpZGEtdCIsICJhdXRoX3RpbWUiOiAxNTUzODUxMTE1LCAicmVhbG0iOiAiLyIsICJleHAiOiAxNTUzODU0NzE2LCAidG9rZW5UeXBlIjogIkpXVFRva2VuIiwgImlhdCI6IDE1NTM4NTExMTYgfQ.stf6Ta_TLeiIDQgw3nYcvNx-jDeMnSDioV96nLghE4rorvrf2Eyexkk1J0DYx2lDLFCrfX1Q1ERTxpO32qKvzbKfpQjAng_L4tUOeNtYKgmsIzWzxS_AHxi1HqtlJouCmVqPOfwHsK31qMgDOim55iZMC9bmnhdOzC7WkuNNp5vOnzFgJbeglwu3OYgv5AUqxxq9lw-TiFozwAct866qNsp0C6FNTfm1svFW9wFGpvmWHj0fe1II1HGGILdA5kr8qksdu-dmccN0nY0M2xt7ejK_Sqp5xu_OVp58jMVyDo57_u4hb9bBd1jy2bJIDMDRFuyOkeLG0AvAbYsHFmC5PA";
    static final String CONSUMER_ID = "srvgosys-nais";
    static final String USER_ID = "Z990761";

    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    MDCPopulationInterceptor mdcPopulationInterceptor;

    @Test
    public void validateToReturnStandardValues() {
        doReturn(CONSUMER_ID).when(servletRequest).getHeader(NAV_CONSUMER_ID);
        doReturn(USER_TOKEN).when(servletRequest).getHeader(AUTHORIZATION);

        mdcPopulationInterceptor.preHandle(servletRequest, servletResponse, null);

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        assertThat(copyOfContextMap.get(MDC_CONSUMER_ID)).isEqualTo(CONSUMER_ID);
        assertThat(copyOfContextMap.get(MDC_USER_ID)).isEqualTo(USER_ID);
    }

    @Test
    public void shouldUseUserIdAsConsumerIdIfConsumerTokenIsNullAndUserIsServiceUser() {
        when(servletRequest.getHeader(anyString())).thenAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(0).equals(AUTHORIZATION)) {
                return APP_TOKEN;
            }
            return null;
        });

        mdcPopulationInterceptor.preHandle(servletRequest, servletResponse, null);

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        assertThat(copyOfContextMap.get(MDC_CONSUMER_ID)).isEqualTo(CONSUMER_ID_FALLBACK);
        assertThat(copyOfContextMap.get(MDC_USER_ID)).isEqualTo(CONSUMER_ID);
    }


    @Test
    public void shouldReturnDefaultValues() {
        when(servletRequest.getHeader(anyString())).thenAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(0).equals(AUTHORIZATION)) {
                return APP_TOKEN;
            }
            return null;
        });
        doReturn(APP_TOKEN).when(servletRequest).getHeader(AUTHORIZATION);

        mdcPopulationInterceptor.preHandle(servletRequest, servletResponse, null);

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        assertThat(copyOfContextMap.get(MDC_CONSUMER_ID)).isEqualTo(CONSUMER_ID_FALLBACK);
    }

}