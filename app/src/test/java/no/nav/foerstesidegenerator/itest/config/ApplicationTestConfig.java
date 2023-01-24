package no.nav.foerstesidegenerator.itest.config;

import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
import no.nav.foerstesidegenerator.consumer.metaforce.config.CxfTimeoutOutInterceptor;
import no.nav.foerstesidegenerator.consumer.metaforce.config.MetaforceTimeouts;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.metaforce.services.IGeneralService;

import javax.xml.ws.soap.SOAPBinding;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("itest")
public class ApplicationTestConfig {

	/**
	 * Due to strict security settings in metaforce.wsdl, the mocked metaforce endpoint should use metaforceTest.wsdl instead.
	 * This allows us to use http instead of https when connecting to the mocked endpoint.
	 */
	@Bean
	public IGeneralService metaforcews(@Value("${metaforceendpoint_url}") String endpointurl,
									   final ServiceuserAlias serviceuserAlias,
									   final MetaforceTimeouts timeouts) {
		JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
		clientFactory.setServiceClass(IGeneralService.class);
		clientFactory.setAddress(endpointurl);
		// removed WSAddressingFeature since we can't correlate with wiremock
		clientFactory.setOutInterceptors(Collections.singletonList(
				new CxfTimeoutOutInterceptor(timeouts.getDefaultReceiveTimeoutms(), timeouts.getOperationsTimeouts()))
		);
		clientFactory.setBindingId(SOAPBinding.SOAP12HTTP_BINDING);
		clientFactory.setProperties(cxfProperties(serviceuserAlias));
		return (IGeneralService) clientFactory.create();
	}

	private static Map<String, Object> cxfProperties(ServiceuserAlias serviceuserAlias) {
		Map<String, Object> props = new HashMap<>();
		props.put(SecurityConstants.USERNAME, serviceuserAlias.getUsername());
		props.put(SecurityConstants.PASSWORD, serviceuserAlias.getPassword());
		return props;
	}
}

