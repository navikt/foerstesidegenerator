package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
@Configuration
public class RestConfig {
	@Bean
	RestTemplate restTemplate(ServiceuserAlias serviceuserAlias,
							  RestTemplateBuilder restTemplateBuilder,
							  ClientHttpRequestFactory requestFactory) {
		return restTemplateBuilder
				.requestFactory(() -> requestFactory)
				.basicAuthentication(serviceuserAlias.getUsername(), serviceuserAlias.getPassword())
				.setConnectTimeout(Duration.ofMillis(5000))
				.setReadTimeout(Duration.ofMillis(5000)).build();
	}

	@Bean
	ClientHttpRequestFactory requestFactory(HttpClient httpClient) {
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	@Bean
	HttpClient httpClient() {
		return HttpClients.createDefault();
	}
}
