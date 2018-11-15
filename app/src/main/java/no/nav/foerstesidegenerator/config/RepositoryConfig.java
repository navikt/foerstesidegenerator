package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
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
@EnableJpaRepositories(basePackageClasses = {FoerstesideRepository.class})
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
@Configuration
public class RepositoryConfig {

	@Bean
	@Primary
	DataSource dataSource(final DataSourceProperties dataSourceProperties,
						  @Value("${dokmotds_onshosts:#{null}}") final String onsHosts) throws SQLException {
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setURL(dataSourceProperties.getUrl());
		dataSource.setUser(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());

//		PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
//		poolDataSource.setURL(dataSourceProperties.getUrl());
//		poolDataSource.setUser(dataSourceProperties.getUsername());
//		poolDataSource.setPassword(dataSourceProperties.getPassword());
//		poolDataSource.setConnectionFactoryClassName(dataSourceProperties.getDriverClassName());
		if(isOracleConnectionUrlFailover(dataSourceProperties.getUrl())) {
			if(onsHosts != null) {
				dataSource.setONSConfiguration("nodes=" + onsHosts);
//				poolDataSource.setONSConfiguration("nodes=" + onsHosts);
			}
			dataSource.setFastConnectionFailoverEnabled(true);
//			poolDataSource.setFastConnectionFailoverEnabled(true);
		}
//
		Properties connProperties = new Properties();
		connProperties.setProperty("oracle.net.CONNECT_TIMEOUT", "3000");
		connProperties.setProperty("oracle.jdbc.thinForceDNSLoadBalancing", "true");
//		// Optimizing UCP behaviour https://docs.oracle.com/database/121/JJUCP/optimize.htm#JJUCP8143
//		poolDataSource.setInitialPoolSize(5);
//		poolDataSource.setMinPoolSize(2);
//		poolDataSource.setMaxPoolSize(20);
//		poolDataSource.setMaxConnectionReuseTime(300); // 5min
//		poolDataSource.setMaxConnectionReuseCount(100);
//		poolDataSource.setConnectionProperties(connProperties);
//		return poolDataSource;

		dataSource.setConnectionProperties(connProperties);
		return dataSource;
	}

	private boolean isOracleConnectionUrlFailover(final String url) {
		return url.toLowerCase().contains("failover");
	}
}


