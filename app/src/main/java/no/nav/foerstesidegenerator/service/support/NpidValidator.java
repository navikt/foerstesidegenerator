package no.nav.foerstesidegenerator.service.support;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * <a href="https://pdl-docs.ansatt.nav.no/ekstern/index.html#_identifikatoren">Npid</a>
 * <a href="https://github.com/navikt/pdl/blob/1da8fe50d5c93259c8dbc65984ece06c800a3f4a/apps/ident-rekvisisjon/src/main/java/no/nav/pdl/apps/ident/rekvisisjon/npid/NpidGenerator.java#L20">Npid generator kode</a>
 */
public final class NpidValidator {
	private static final int[] KONTROLLSIFFERREKKE1 = {3, 7, 6, 1, 8, 9, 4, 5, 2};
	private static final int[] KONTROLLSIFFERREKKE2 = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

	private NpidValidator() {
		// ingen instansiering
	}

	public static boolean isValidNpid(String ident) {
		if (ident == null || ident.isEmpty()) {
			return false;
		}

		if (!isNumeric(ident) || ident.length() != 11) {
			return false;
		}

		// dag
		String dag = ident.substring(0, 2);
		if (!(parseInt(dag) < 32)) {
			return false;
		}

		// måned
		String maaned = ident.substring(2, 4);
		if (!(parseInt(maaned) > 20)) {
			return false;
		}

		String kontrollsiffer1 = ident.substring(9, 10);
		if (!kontrollsiffer1.equals(kontrollsiffer(KONTROLLSIFFERREKKE1, ident.substring(0, 9)))) {
			return false;
		}

		String kontrollsiffer2 = ident.substring(10);
		if (!kontrollsiffer2.equals(kontrollsiffer(KONTROLLSIFFERREKKE2, ident.substring(0, 10)))) {
			return false;
		}

		return true;
	}

	private static String kontrollsiffer(int[] kontrollrekke, String npid) {
		String[] siffer = npid.split("");
		int sum = 0;
		for (int i = 0; i < kontrollrekke.length; i++) {
			sum += (kontrollrekke[i] * parseInt(siffer[i]));
		}
		return String.valueOf(11 - (sum % 11));
	}
}
