package no.nav.foerstesidegenerator.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

/**
 * Ekstra konfigurasjon for databasen
 */
@Data
@ConfigurationProperties("foerstesidegeneratordb")
public class DataSourceAdditionalProperties {
	DatabaseCreds creds = new DatabaseCreds();
	@Data
	public static class DatabaseCreds {
		private String username;
		private String password;
	}
		@Name("config.jdbc.url")
		private String jdbcUrl;
		@Name("config.ons.host")
		private String onshosts;
}
