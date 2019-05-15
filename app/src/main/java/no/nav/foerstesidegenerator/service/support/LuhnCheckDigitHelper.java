package no.nav.foerstesidegenerator.service.support;

import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

/**
 * Hjelpeklasse som bruker Luhn-algoritmen (https://en.wikipedia.org/wiki/Luhn_algorithm)
 * (mod 10) for å beregne kontrollsiffer for løpenummer.
 */
public class LuhnCheckDigitHelper {

    private LuhnCheckDigitHelper() {
        //no-op
    }

    public static String calculateCheckDigit(String loepenummer) {
        LuhnCheckDigit luhnCheckDigit = new LuhnCheckDigit();
        try {
            return luhnCheckDigit.calculate(loepenummer);
        } catch (CheckDigitException e) {
            throw new FoerstesideGeneratorFunctionalException("Noe uventet feilet ved generering av kontrollsiffer", e);
        }
    }

    public static boolean validateLoepenummerWithCheckDigit(String loepenummer) {
        LuhnCheckDigit luhnCheckDigit = new LuhnCheckDigit();
        return luhnCheckDigit.isValid(loepenummer);
    }
}
