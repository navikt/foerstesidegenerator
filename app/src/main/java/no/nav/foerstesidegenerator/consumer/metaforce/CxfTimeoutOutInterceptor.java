package no.nav.foerstesidegenerator.consumer.metaforce;

import org.apache.cxf.interceptor.MessageSenderInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * An {@link AbstractPhaseInterceptor} that applies a call-specific timeout based on the QName of the operation.
 *
 * @author Hans Petter Simonsen, Miles AS
 */
public class CxfTimeoutOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private final long defaultReceiveTimeoutForService;
	private static final long DEFAULT_RECEIVE_TIMEOUT_MS = 15_000;
	private static final long DEFAULT_CONNECT_TIMEOUT_MS = 3_000;

	/**
	 * Keep all timeouts per OperationName in milliseconds
	 */
	private Map<QName, Long> receiveTimeoutByOperationName;

	public CxfTimeoutOutInterceptor(Map<QName, Long> receiveTimeoutByOperationName) {
		this(DEFAULT_RECEIVE_TIMEOUT_MS, receiveTimeoutByOperationName);
	}

	public CxfTimeoutOutInterceptor(Long defaultReceiveTimeoutForService, Map<QName, Long> receiveTimeoutByOperationName) {
		super(Phase.PREPARE_SEND);
		addBefore(MessageSenderInterceptor.class.getName());
		if(defaultReceiveTimeoutForService == null) {
			this.defaultReceiveTimeoutForService = DEFAULT_RECEIVE_TIMEOUT_MS;
		} else {
			this.defaultReceiveTimeoutForService = defaultReceiveTimeoutForService;
		}
		this.receiveTimeoutByOperationName = receiveTimeoutByOperationName;
	}

	@Override
	public void handleMessage(Message message) {
		if (message.get(FoerstesidegeneratorConstants.PROPERTY_RECEIVE_TIMEOUT_OVERRIDE) == null) {
			final QName operationName = message.getExchange().getBindingOperationInfo().getName();
			final Long receiveTimeout = receiveTimeoutByOperationName.get(operationName);
			if (receiveTimeout != null) {
				message.put(Message.RECEIVE_TIMEOUT, receiveTimeout);
			} else {
				message.put(Message.RECEIVE_TIMEOUT, defaultReceiveTimeoutForService);
			}
			message.put(Message.CONNECTION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT_MS);
		}
	}
}
