package no.nav.foerstesidegenerator.config.properties;

import jakarta.validation.constraints.NotEmpty;
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
		@NotEmpty
		private String dokmetUrl;
	}

}