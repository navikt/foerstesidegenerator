package no.nav.foerstesidegenerator.service.support;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static no.nav.foerstesidegenerator.service.support.FoedselsnummerValidator.isValidPid;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoedselsnummerValidatorTest {

	@ParameterizedTest
	@ValueSource(strings = {"01117400200", "011174 00200", "27857798800"})
	void shouldValidateFnr(String fnr) {
		final boolean validPid = isValidPid(fnr);
		assertTrue(validPid);
	}

	@ParameterizedTest
	@ValueSource(strings = {"58088349006", "580883 49006"})
	void shouldValidateDnr(String dnr) {
		final boolean validPid = isValidPid(dnr);
		assertTrue(validPid);
	}

	@ParameterizedTest
	@ValueSource(strings = {"13442157949", "134421 57949"})
	void shouldValidateHnr(String hnr) {
		final boolean validPid = isValidPid(hnr);
		assertTrue(validPid);
	}
}