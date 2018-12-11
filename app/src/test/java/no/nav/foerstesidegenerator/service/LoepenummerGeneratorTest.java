package no.nav.foerstesidegenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LoepenummerGeneratorTest {

	private LoepenummerGenerator loepenummerGenerator = new LoepenummerGenerator();

	@Test
	void loepenummerShouldBeANumericString() {
		String x = loepenummerGenerator.generateLoepenummer();

		assertEquals(9, x.length());
		assertTrue(x.matches(".*[0-9].*"));
	}

}