package no.nav.foerstesidegenerator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LoepenummerGeneratorTest {

	private static final int BOUND = 1_000_000_000;
	private LoepenummerGenerator loepenummerGenerator = new LoepenummerGenerator();

	@Test
	void loepenummerShouldBeWithinBounds() {
		int loepenummer = loepenummerGenerator.generateLoepenummer();

		assertTrue(loepenummer < BOUND);
		assertTrue(loepenummer > 0);
	}

}