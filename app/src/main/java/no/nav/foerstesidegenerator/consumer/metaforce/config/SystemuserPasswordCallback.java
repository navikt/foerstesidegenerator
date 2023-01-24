package no.nav.foerstesidegenerator.consumer.metaforce.config;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class SystemuserPasswordCallback implements CallbackHandler {

	private final String serviceuserPassword;

	public SystemuserPasswordCallback(String serviceuserPassword) {
		this.serviceuserPassword = serviceuserPassword;
	}

	@Override
	public void handle(Callback[] callbacks) {
		WSPasswordCallback wsPasswordCallback = (WSPasswordCallback) callbacks[0];

		wsPasswordCallback.setPassword(serviceuserPassword);
	}
}