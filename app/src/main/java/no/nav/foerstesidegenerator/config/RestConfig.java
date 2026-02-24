package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestConfig {
	@Bean
	RestTemplate restTemplate(ServiceuserAlias serviceuserAlias,
							  RestTemplateBuilder restTemplateBuilder,
							  ClientHttpRequestFactory requestFactory) {
		return restTemplateBuilder
				.requestFactory(() -> requestFactory)
				.basicAuthentication(serviceuserAlias.getUsername(), serviceuserAlias.getPassword())
				.build();
	}

	@Bean
	ClientHttpRequestFactory requestFactory(HttpClient httpClient) {
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	@Bean
	HttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(5, TimeUnit.SECONDS)
				.setResponseTimeout(5, TimeUnit.SECONDS)
				.build();
		return HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
	}
}
