package no.nav.foerstesidegenerator.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Konfigurasjonsklasse for manuell Flyway-konfigurasjon.
 * <p>
 * Autoconfig via Spring boot 2.1.x forutsetter flyway-core 5.2.1, som på sin side forutsetter Oracle 12.2
 * <p>
 * For å disable autoconfig i <code>application.properties</code>:
 *
 * <pre>
 * spring.flyway.enabled=false
 * </pre>
 *
 * I tillegg må flyway-core deklareres i dependencyManagment i pom.xml med siste versjon på 5.1-grenen.
 */
@Configuration
public class FlywayConfig {

	@Bean(initMethod = "migrate")
	public Flyway flywayManualConfig(DataSource dataSource) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		return flyway;
	}

	/**
	 * Klassen deklarerer at JPA EntityManager-bønnen er avhengig av Flyway-bønnen. Dette for å unngå at Hibernate forsøker å
	 * benytte databaseskjemaet før Flyway har opprettet nødvendige objekter.
	 */
	@Configuration
	static class FlywayJpaDependencyConfiguration extends EntityManagerFactoryDependsOnPostProcessor {

		FlywayJpaDependencyConfiguration() {
			super("flywayManualConfig");
		}
	}
}