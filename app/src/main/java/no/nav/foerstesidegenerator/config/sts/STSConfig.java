package no.nav.foerstesidegenerator.config.sts;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author Ugur Alpay Cenar, Visma Consulting.
 */
@Component
public class STSConfig {
	
	@Value("${securityTokenService.url}")
	private String stsUrl;
	
	private final ServiceuserAlias serviceuserAlias;

	@Inject
	public STSConfig(ServiceuserAlias serviceuserAlias) {
		this.serviceuserAlias = serviceuserAlias;
	}

	public void configureSTS(Object port){
		Client client = ClientProxy.getClient(port);
		STSConfigUtil.configureStsRequestSamlToken(client, stsUrl, serviceuserAlias.getUsername(), serviceuserAlias.getPassword());
	}
}