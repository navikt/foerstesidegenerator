package no.nav.foerstesidegenerator.config.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.validation.annotation.Validated;

/**
 * Ekstra konfigurasjon for databasen
 */
@Data
@Validated
@ConfigurationProperties("foerstesidegeneratordb")
public class DataSourceAdditionalProperties {
	DatabaseCreds creds = new DatabaseCreds();
	@Data
	public static class DatabaseCreds {
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
	}
		@Name("config.jdbc.url")
		@NotEmpty
		private String jdbcUrl;
		@Name("config.ons.host")
		private String onshosts;
}
