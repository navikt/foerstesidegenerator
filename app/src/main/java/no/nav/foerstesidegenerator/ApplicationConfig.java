package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.nais.NaisContract;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@Configuration
@Import(value = {
		RepositoryConfig.class,
		NaisContract.class
})
@EnableAutoConfiguration
public class ApplicationConfig {


}
