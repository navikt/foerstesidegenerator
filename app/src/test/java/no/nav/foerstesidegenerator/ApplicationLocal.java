package no.nav.foerstesidegenerator;

import no.nav.security.spring.oidc.test.TokenGeneratorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {
		ApplicationConfig.class,
		TokenGeneratorConfiguration.class})
public class ApplicationLocal {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
