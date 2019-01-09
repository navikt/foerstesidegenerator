//package no.nav.foerstesidegenerator.config.metaforce;
//
//import no.nav.foerstesidegenerator.config.properties.ServiceuserAlias;
//import org.apache.cxf.ws.security.SecurityConstants;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import se.metaforce.services.IGeneralService;
//import se.metaforce.services.InteractOnlineProcess;
//
//import javax.xml.namespace.QName;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Spring config for Metaforce CXF Endpoint
// *
// * @author Paul Magne Lunde, Visma Consulting
// */
//@Configuration
//public class MetaforceEndpointConfig extends AbstractCxfEndpointConfig {
//
//	public static final QName METAFORCE_PORT_QNAME = InteractOnlineProcess.GeneralServiceBinding;
//	public static final QName METAFORCE_SERVICE_QNAME = InteractOnlineProcess.SERVICE;
//	public static final String WSDL_URL = "metaforce.wsdl";
//
//	@Value("${client.timeout.metaforce:10000}")
//	private Integer timeout;
//
//	@Bean
//	public IGeneralService metaforceEndpoint(ServiceuserAlias serviceuserAlias,
//											 @Value("${metaforceendpoint_url}") String endpointUrl) {
//		setWsdlUrl(WSDL_URL);
//		setEndpointName(METAFORCE_PORT_QNAME);
//		setServiceName(METAFORCE_SERVICE_QNAME);
//		setAdress(endpointUrl);
//		setTimeout(timeout);
//		addProperties(cxfProperties(serviceuserAlias));
////		addOutInterceptor(new NewLineFixOutInterceptor());
//		enableMtom();
//
//		return createPort(IGeneralService.class);
//	}
//
//	private Map<String, Object> cxfProperties(ServiceuserAlias serviceuserAlias) {
//		Map<String, Object> props = new HashMap<>();
//		props.put(SecurityConstants.USERNAME, serviceuserAlias.getUsername());
//		props.put(SecurityConstants.PASSWORD, serviceuserAlias.getPassword());
//		props.put(SecurityConstants.MUST_UNDERSTAND, 0);
//		return props;
//	}
//
//}
