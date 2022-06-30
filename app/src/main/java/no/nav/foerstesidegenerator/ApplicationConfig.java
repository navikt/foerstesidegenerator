package no.nav.foerstesidegenerator;

import io.micrometer.core.instrument.MeterRegistry;
import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.foerstesidegenerator.metrics.DokTimedAspect;
import no.nav.foerstesidegenerator.nais.NaisContract;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@ComponentScan
@EnableConfigurationProperties({
		ServiceuserAlias.class
})
@Configuration
@Import(value = {
		RepositoryConfig.class,
		NaisContract.class
})
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableJwtTokenValidation
public class ApplicationConfig {

	@Bean
	public DokTimedAspect timedAspect(MeterRegistry meterRegistry) {
		return new DokTimedAspect(meterRegistry);
	}

}
