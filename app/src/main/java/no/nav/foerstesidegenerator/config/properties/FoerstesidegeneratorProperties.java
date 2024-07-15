package no.nav.foerstesidegenerator.config.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "foerstesidegenerator")
public class FoerstesidegeneratorProperties {

	private final Endpoints endpoints = new Endpoints();

	@Data
	public static class Endpoints {
		@NotNull
		private AzureEndpoint dokmet;
	}

	@Data
	public static class AzureEndpoint {
		@NotEmpty
		private String url;

		@NotEmpty
		private String scope;
	}

}