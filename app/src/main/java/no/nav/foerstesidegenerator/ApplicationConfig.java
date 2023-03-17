package no.nav.foerstesidegenerator;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.foerstesidegenerator.azure.AzureProperties;
import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.foerstesidegenerator.metrics.DokTimedAspect;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({
		ServiceuserAlias.class,
		AzureProperties.class
})
@Import(value = {
		RepositoryConfig.class
})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableJwtTokenValidation(ignore = {"org.springframework", "org.springdoc"})
public class ApplicationConfig {

	@Bean
	public DokTimedAspect timedAspect(MeterRegistry meterRegistry) {
		return new DokTimedAspect(meterRegistry);
	}
}
