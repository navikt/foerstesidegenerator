package no.nav.foerstesidegenerator.itest.config;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("itest")
@TestConfiguration
public class RepositoryTestConfig {

	///  I stedet for embedded DataSource fra `@AutoConfigureTestDatabase`
	///
	/// * `DB_CLOSE_DELAY`: hindrer at H2 dropper databasen hvis en kobling lukkes
	/// * `NON_KEYWORDS`: tabellen `FOERSTESIDE_METADATA` har reserverte keywords som kolonnenavn
	@Bean
	@Primary
	public DataSource dataSource() {
		JdbcDataSource jdbcDataSource = new JdbcDataSource();
		jdbcDataSource.setUrl("jdbc:h2:mem:test;MODE=Oracle;DB_CLOSE_DELAY=-1;NON_KEYWORDS=KEY,VALUE");
		return jdbcDataSource;
	}
}
