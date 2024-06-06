package no.nav.foerstesidegenerator.config;

import no.nav.foerstesidegenerator.config.properties.DataSourceAdditionalProperties;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@EntityScan(basePackages = {"no.nav.foerstesidegenerator.domain"})
@EnableJpaRepositories(basePackageClasses = {FoerstesideRepository.class, FoerstesideCounterRepository.class})
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
@Configuration
public class RepositoryConfig {

	private static final int STATISK_POOL_SIZE = 20;

	@Bean
	@Primary
	DataSource dataSource(final DataSourceProperties dataSourceProperties,
						  final DataSourceAdditionalProperties dataSourceAdditionalProperties) throws SQLException {
		PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
		poolDataSource.setURL(dataSourceProperties.getUrl());
		poolDataSource.setUser(dataSourceProperties.getUsername());
		poolDataSource.setPassword(dataSourceProperties.getPassword());
		poolDataSource.setConnectionFactoryClassName(dataSourceProperties.getDriverClassName());

		if (isOracleFastConnectionFailoverSupported(dataSourceProperties.getUrl(), dataSourceAdditionalProperties.getOnshosts())) {
			poolDataSource.setFastConnectionFailoverEnabled(true);
			poolDataSource.setONSConfiguration("nodes=" + dataSourceAdditionalProperties.getOnshosts());
		} else {
			poolDataSource.setFastConnectionFailoverEnabled(false);
			poolDataSource.setONSConfiguration("");
		}

		Properties connProperties = new Properties();
		connProperties.setProperty("oracle.net.CONNECT_TIMEOUT", "3000");
		connProperties.setProperty("oracle.jdbc.thinForceDNSLoadBalancing", "true");
//		// Optimizing UCP behaviour https://docs.oracle.com/database/121/JJUCP/optimize.htm#JJUCP8143
		poolDataSource.setInitialPoolSize(STATISK_POOL_SIZE);
		poolDataSource.setMinPoolSize(STATISK_POOL_SIZE);
		poolDataSource.setMaxPoolSize(STATISK_POOL_SIZE);
		poolDataSource.setMaxConnectionReuseTime(300); // 5min
		poolDataSource.setMaxConnectionReuseCount(100);
		poolDataSource.setConnectionProperties(connProperties);
		return poolDataSource;
	}

	private boolean isOracleFastConnectionFailoverSupported(String jdbcurl, String onshosts) {
		return jdbcurl.toLowerCase().contains("failover") && isNotBlank(onshosts);
	}
}