package no.nav.foerstesidegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {ApplicationConfig.class})
public class ApplicationLocal {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLocal.class, args);
	}
}
