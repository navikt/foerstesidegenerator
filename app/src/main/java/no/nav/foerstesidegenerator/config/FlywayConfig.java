package no.nav.foerstesidegenerator.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Konfigurasjonsklasse flyway uten Spring Boot autoconfig.
 * Dette er grunnen til at spring.flyway.enabled=false
 * <p>
 * Flyway ikke er kompatibelt med alle Oracle versjoner.
 * Undersøk https://flywaydb.org/documentation/database/oracle kompatibilitet med produksjonsdatabasen før oppdatering
 * av flyway.
 */
@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flywayManualConfig(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
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