package no.nav.foerstesidegenerator.consumer.metaforce.update;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
 * Sets the password for the Usernametoken WS-Security
 *
 * @author Joakim Bjørnstad, Visma Consulting
 */
public class SystemuserPasswordCallback implements CallbackHandler {

	private final String serviceuserPassword;

	public SystemuserPasswordCallback(String serviceuserPassword) {
		this.serviceuser***passord=gammelt_passord***;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WS***passord=gammelt_passord***];

		wsPasswordCallback.setPassword(serviceuserPassword);
	}
}
