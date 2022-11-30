package no.nav.foerstesidegenerator;

import no.nav.foerstesidegenerator.itest.config.LocalTestCacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {ApplicationConfig.class, LocalTestCacheConfig.class})
public class ApplicationLocal {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLocal.class, args);
	}
}
