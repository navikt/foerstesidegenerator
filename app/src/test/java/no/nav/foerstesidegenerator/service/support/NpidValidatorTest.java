package no.nav.foerstesidegenerator.service.support;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NpidValidatorTest {

	@ParameterizedTest
	@ValueSource(strings = {"02239969375", "19319718276", "28319443942"})
	void shouldValidateNpid(String npid) {
		assertTrue(NpidValidator.isValidNpid(npid));
	}

	@ParameterizedTest
	@ValueSource(strings = {"0223996937a", "02239969376", "193197182761", "32319443942", "32199443942", "58088349006", "01117400200"})
	void shouldNotValidateNpid(String ident) {
		assertFalse(NpidValidator.isValidNpid(ident));
	}
}