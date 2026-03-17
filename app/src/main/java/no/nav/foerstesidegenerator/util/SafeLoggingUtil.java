package no.nav.foerstesidegenerator.util;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.left;

public class SafeLoggingUtil {
	private static final int STRING_MAXLENGTH = 500;
	private static final Pattern EVERYTHING_EXCEPT_SAFE_CHARS_REGEX = Pattern.compile("[^a-zA-Z0-9]");

	public static String removeUnsafeChars(String input) {
		return removeUnsafeChars(input, STRING_MAXLENGTH);
	}

	public static String removeUnsafeChars(String input, int maxLength) {
		if (input == null) {
			return null;
		}
		return left(EVERYTHING_EXCEPT_SAFE_CHARS_REGEX.matcher(input).replaceAll("_"), maxLength);
	}
}
