package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.foerstesidegenerator.nais.NaisContract;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan
@EnableConfigurationProperties({
		ServiceuserAlias.class
})
@Import(value = {
		NaisContract.class
})
@EnableAutoConfiguration
public class ApplicationConfig {


}
