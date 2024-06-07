package no.nav.foerstesidegenerator.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ekstra konfigurasjon for databasen
 */
@Data
@ConfigurationProperties("database")
public class DataSourceAdditionalProperties {
	private String onshosts;
}
