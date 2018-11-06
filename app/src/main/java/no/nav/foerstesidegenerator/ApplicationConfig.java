package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.nais.NaisContract;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan
@Import(value = {
		NaisContract.class
})
@EnableAutoConfiguration
public class ApplicationConfig {


}
