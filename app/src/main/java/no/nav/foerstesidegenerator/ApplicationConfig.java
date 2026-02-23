package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.config.properties.DataSourceAdditionalProperties;
import no.nav.foerstesidegenerator.config.properties.FoerstesidegeneratorProperties;
import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.resilience.annotation.EnableResilientMethods;

@Configuration
@EnableConfigurationProperties({
		ServiceuserAlias.class,
		DataSourceAdditionalProperties.class,
		FoerstesidegeneratorProperties.class
})
@Import(value = {
		RepositoryConfig.class
})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableResilientMethods
@EnableJwtTokenValidation(ignore = {"org.springframework", "org.springdoc"})
public class ApplicationConfig {
}
