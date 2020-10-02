package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.config.WebSecurityConfiguration;
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {ApplicationConfig.class, TokenGeneratorConfiguration.class, WebSecurityConfiguration.class})
public class ApplicationLocal {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLocal.class, args);
    }
}
