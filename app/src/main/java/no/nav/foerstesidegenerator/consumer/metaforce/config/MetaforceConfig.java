package no.nav.foerstesidegenerator.consumer.metaforce.config;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.metaforce.services.IGeneralService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING;
import static org.apache.wss4j.common.ConfigurationConstants.ACTION;
import static org.apache.wss4j.common.ConfigurationConstants.PASSWORD_TYPE;
import static org.apache.wss4j.common.ConfigurationConstants.PW_CALLBACK_REF;
import static org.apache.wss4j.common.ConfigurationConstants.USER;
import static org.apache.wss4j.common.ConfigurationConstants.USERNAME_TOKEN;
import static org.apache.wss4j.common.WSS4JConstants.PW_TEXT;

@Configuration
@Profile("nais")
class MetaforceConfig {
	@Bean
	public IGeneralService metaforcews(@Value("${metaforceendpoint_url}") String endpointurl,
									   final ServiceuserAlias serviceuserAlias,
									   final MetaforceTimeouts timeouts) {
		JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
		clientFactory.setServiceClass(IGeneralService.class);
		clientFactory.setAddress(endpointurl);
		clientFactory.setFeatures(Collections.singletonList(new WSAddressingFeature()));
		clientFactory.setOutInterceptors(Arrays.asList(
				new CxfTimeoutOutInterceptor(timeouts.getDefaultReceiveTimeoutms(), timeouts.getOperationsTimeouts()),
				wss4JOutInterceptor(serviceuserAlias))
		);

		clientFactory.setBindingId(SOAP12HTTP_BINDING);
		return (IGeneralService) clientFactory.create();
	}

	private WSS4JOutInterceptor wss4JOutInterceptor(ServiceuserAlias serviceuserAlias) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(ACTION, USERNAME_TOKEN);
		properties.put(PASSWORD_TYPE, PW_TEXT);
		properties.put(USER, serviceuserAlias.getUsername());
		properties.put(PW_CALLBACK_REF, new SystemuserPasswordCallback(serviceuserAlias.getPassword()));
		return new WSS4JOutInterceptor(properties);
	}
}
