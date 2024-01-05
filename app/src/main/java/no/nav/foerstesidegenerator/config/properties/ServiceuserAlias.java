package no.nav.foerstesidegenerator.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@Data
@ConfigurationProperties("serviceuser")
@Validated
public class ServiceuserAlias {
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}
