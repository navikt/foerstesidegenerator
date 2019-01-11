package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.config.ServiceuserAlias;
import no.nav.foerstesidegenerator.nais.NaisContract;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@EnableConfigurationProperties({
		ServiceuserAlias.class
})
@Configuration
@Import(value = {
		RepositoryConfig.class,
		NaisContract.class,
})
@EnableAutoConfiguration
public class ApplicationConfig {


}
