package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.repository.FoerstesideCounterRepository;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
@EntityScan(basePackages = {"no.nav.foerstesidegenerator.domain"})
@EnableJpaRepositories(basePackageClasses = {FoerstesideRepository.class, FoerstesideCounterRepository.class})
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
@Configuration
public class RepositoryConfig {

	@Bean
	@Primary
	DataSource dataSource(final DataSourceProperties dataSourceProperties) throws SQLException {
		PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
		poolDataSource.setURL(dataSourceProperties.getUrl());
		poolDataSource.setUser(dataSourceProperties.getUsername());
		poolDataSource.setPassword(dataSourceProperties.getPassword());
		poolDataSource.setConnectionFactoryClassName(dataSourceProperties.getDriverClassName());
//
		Properties connProperties = new Properties();
		connProperties.setProperty("oracle.net.CONNECT_TIMEOUT", "3000");
		connProperties.setProperty("oracle.jdbc.thinForceDNSLoadBalancing", "true");
//		// Optimizing UCP behaviour https://docs.oracle.com/database/121/JJUCP/optimize.htm#JJUCP8143
		poolDataSource.setInitialPoolSize(2);
		poolDataSource.setMinPoolSize(2);
		poolDataSource.setMaxPoolSize(8);
		poolDataSource.setMaxConnectionReuseTime(300); // 5min
		poolDataSource.setMaxConnectionReuseCount(100);
		poolDataSource.setValidateConnectionOnBorrow(true);
		poolDataSource.setConnectionProperties(connProperties);
		return poolDataSource;
	}
}