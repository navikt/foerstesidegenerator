package no.nav.foerstesidegenerator.consumer.metaforce.config;

import static se.metaforce.services.InteractOnlineProcess.SERVICE;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joakim Bjørnstad, Jbit AS
 */
@Setter
@ToString
@Slf4j
@Configuration
public class MetaforceTimeouts {

	@Value("${metaforce.defaultreceivetimeoutms:#{null}}")
	@Getter
	private Long defaultReceiveTimeoutms;

	@Value("${metaforce.gsCreateDocument.receivetimeoutms:#{null}}")
	private Long gsCreateDocumentReceiveTimeoutms;

	public Map<QName, Long> getOperationsTimeouts() {
		Map<QName, Long> map = new HashMap<>();
		map.put(new QName(SERVICE.getNamespaceURI(), "GS_CreateDocument"), gsCreateDocumentReceiveTimeoutms);
		return map;
	}

	@PostConstruct
	public void postConstruct() {
		log.info("Metaforce timeout values. " + this);
	}
}