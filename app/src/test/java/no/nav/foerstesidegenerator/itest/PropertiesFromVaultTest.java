package no.nav.foerstesidegenerator.itest;

import jakarta.annotation.Resource;
import no.nav.foerstesidegenerator.config.properties.DataSourceAdditionalProperties;
import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesFromVaultTest extends AbstractIT {

	@Resource
	DataSourceAdditionalProperties dataSourceAdditionalProperties;
	@Resource
	ServiceuserAlias serviceuserAlias;

	@Test
	public void test() {
		assertThat(serviceuserAlias.getPassword()).isEqualTo("password1");
		assertThat(serviceuserAlias.getUsername()).isEqualTo("username1");

		assertThat(dataSourceAdditionalProperties.getCreds().getPassword()).isEqualTo("password2");
		assertThat(dataSourceAdditionalProperties.getCreds().getUsername()).isEqualTo("username2");
		assertThat(dataSourceAdditionalProperties.getJdbcUrl()).isEqualTo("jdbc::something");
		assertThat(dataSourceAdditionalProperties.getOnshosts()).isEqualTo("onshostconfig");
	}
}
