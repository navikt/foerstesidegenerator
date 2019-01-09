package no.nav.foerstesidegenerator.consumer.metaforce.update;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.datacontract.schemas._2004._07.metaforce_common.InstanceErrorCodes;
import org.springframework.util.ReflectionUtils;
import se.metaforce.services.GSGetInfoAndFontsResponse;
import se.metaforce.services.GSLoadInArchiveResponse;
import se.metaforce.services.GSOpenDocumentResponse;

import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interceptor that throws exceptions if a message is not ok.
 * Simplifies routes consuming metaforce.
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
@Slf4j
public class MetaforceErrorInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final String GET_ERROR_DESCRIPTION = "getErrorDescription";
	private static final String GET_ERROR_CODE = "getErrCode";
	private static final String IS_OK = "isIsOK";

	private static final Map<Class, Method> IS_OK_METHOD_CACHE = new HashMap<>();

	public MetaforceErrorInterceptor() {
		super(Phase.POST_LOGICAL);
	}

	@Override
	@SneakyThrows
	public void handleMessage(Message message) {
		Object response = getResponse(message);
		Class<?> responseClass = response.getClass();
		Method isOKMethod = geIsOKMethod(responseClass);
		if (isOKMethod == null) {
			log.warn(responseClass + " does not have isOk()");
			return;
		} else if ((Boolean) isOKMethod.invoke(response)) {
			return;
		}
		throwError(message, response, responseClass);
	}

	private Object getResponse(Message message) {
		List content = message.getContent(List.class);
		Object response = content.get(0);
		if (response instanceof GSGetInfoAndFontsResponse) {
			response = ((GSGetInfoAndFontsResponse) response).getGSGetInfoAndFontsResult();
		} else if(response instanceof GSLoadInArchiveResponse) {
			response = ((GSLoadInArchiveResponse) response).getGSLoadInArchiveResult();
		} else if(response instanceof GSOpenDocumentResponse) {
			response = ((GSOpenDocumentResponse) response).getGSOpenDocumentResult();
		}
		return response;
	}

	private void throwError(Message message, Object response, Class<?> responseClass) throws IllegalAccessException, InvocationTargetException {
		Method errorDescMethod = ReflectionUtils.findMethod(responseClass, GET_ERROR_DESCRIPTION);
		Method errorCodeMethod = ReflectionUtils.findMethod(responseClass, GET_ERROR_CODE);
		String error = (String) errorDescMethod.invoke(response);
		InstanceErrorCodes errorCode = errorCodeMethod == null ? null : (InstanceErrorCodes) errorCodeMethod.invoke(response);
		String operation = ((QName) message.get(Message.WSDL_OPERATION)).getLocalPart();
		String service = ((QName) message.get(Message.WSDL_PORT)).getLocalPart();
		throw new MetaforceException(operation, "(" + service + "): " + error, errorCode);
	}

	private Method geIsOKMethod(Class clazz) {
		// Cache isok reflection lookup, as these methods as are used a lot
		if (!IS_OK_METHOD_CACHE.containsKey(clazz)) {
			IS_OK_METHOD_CACHE.put(clazz, ReflectionUtils.findMethod(clazz, IS_OK));
		}
		return IS_OK_METHOD_CACHE.get(clazz);
	}
}
