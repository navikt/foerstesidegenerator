package no.nav.foerstesidegenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoepenummerGeneratorTest {

	private LoepenummerGenerator loepenummerGenerator = new LoepenummerGenerator();

	@Test
	void shouldBe24Chars() {
		String x = loepenummerGenerator.generateLoepenummer();

		assertEquals(22, x.length());
	}
}