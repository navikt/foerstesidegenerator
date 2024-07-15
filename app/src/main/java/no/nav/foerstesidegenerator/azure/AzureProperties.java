package no.nav.foerstesidegenerator.azure;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("azure")
public record AzureProperties (
	@NotEmpty String openidConfigTokenEndpoint,
	@NotEmpty String appClientId,
	@NotEmpty String appClientSecret
) {

	public static final String CLIENT_REGISTRATION_DOKMET = "azure-dokmet";
}