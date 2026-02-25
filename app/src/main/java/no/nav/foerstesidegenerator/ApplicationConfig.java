package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.config.properties.DataSourceAdditionalProperties;
import no.nav.foerstesidegenerator.config.properties.FoerstesidegeneratorProperties;
import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.foerstesidegenerator.consumer.dokmet.DokmetClient;
import no.nav.foerstesidegenerator.consumer.lederelection.LeaderElectionClient;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.web.service.registry.ImportHttpServices;

@EnableConfigurationProperties({
		ServiceuserAlias.class,
		DataSourceAdditionalProperties.class,
		FoerstesidegeneratorProperties.class
})
@Import(value = {
		RepositoryConfig.class
})
@ImportHttpServices(group = "leaderelection", types = LeaderElectionClient.class)
@ImportHttpServices(group = "dokmet", types = DokmetClient.class)
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableResilientMethods
@EnableJwtTokenValidation(ignore = {"org.springframework", "org.springdoc"})
@Configuration
public class ApplicationConfig {
}
