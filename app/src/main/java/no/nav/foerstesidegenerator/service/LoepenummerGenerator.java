package no.nav.foerstesidegenerator.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class LoepenummerGenerator {

	private static final int BOUND = 1_000_000_000;

	LoepenummerGenerator() {
		// no-op
	}

	/**
	 * Genererer et random løpenummer på 9 siffer
	 *
	 * @return
	 */
	public int generateLoepenummer() {
		SecureRandom secureRandom = new SecureRandom();
		return secureRandom.nextInt(BOUND);
	}
}
