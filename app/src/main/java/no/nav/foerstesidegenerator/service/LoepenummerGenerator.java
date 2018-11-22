package no.nav.foerstesidegenerator.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class LoepenummerGenerator {

	public LoepenummerGenerator() {
		// no-op
	}

	/**
	 * Genererer en random 128-bit, som konverteres til en 22
	 * @return
	 */
	public String generateLoepenummer() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] bytes  = new byte[16];
		secureRandom.nextBytes(bytes);
		Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
		return encoder.encodeToString(bytes);
	}
}
