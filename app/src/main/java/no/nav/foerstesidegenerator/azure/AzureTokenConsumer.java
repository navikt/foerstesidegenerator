package no.nav.foerstesidegenerator.azure;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

import static no.nav.foerstesidegenerator.config.cache.CacheConfig.AZURE_CLIENT_CREDENTIAL_TOKEN_CACHE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class AzureTokenConsumer {
	private final RestTemplate restTemplate;
	private final AzureProperties azureProperties;

	public AzureTokenConsumer(AzureProperties azureProperties,
                              RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(3))
				.setReadTimeout(Duration.ofSeconds(20))
				.build();
		this.azureProperties = azureProperties;
	}

	@Retryable(include = AzureTokenException.class, maxAttempts = 5, backoff = @Backoff(delay = 200))
	@Cacheable(AZURE_CLIENT_CREDENTIAL_TOKEN_CACHE)
	public TokenResponse getClientCredentialToken(String scope) {
		try {
			HttpHeaders headers = createHeaders();
			String form = "grant_type=client_credentials&scope=" + scope + "&client_id=" +
					azureProperties.getAppClientId() + "&client_secret=" + azureProperties.getAppClientSecret();
			HttpEntity<String> requestEntity = new HttpEntity<>(form, headers);

			return restTemplate.exchange(azureProperties.getOpenidConfigTokenEndpoint(), POST, requestEntity, TokenResponse.class)
					.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new AzureTokenException(String.format("Klarte ikke hente token fra Azure. Feilet med httpstatus=%s. Feilmelding=%s", e.getStatusCode(), e.getMessage()), e);
		}
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(APPLICATION_JSON));
		return headers;
	}
}