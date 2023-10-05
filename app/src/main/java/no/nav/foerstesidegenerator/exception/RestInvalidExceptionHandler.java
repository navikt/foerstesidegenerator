package no.nav.foerstesidegenerator.exception;

import no.nav.foerstesidegenerator.azure.AzureTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestInvalidExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({InvalidRequestException.class, InvalidTemaException.class,
			BrukerIdIkkeValidException.class, InvalidLoepenummerException.class,
			DokkatConsumerFunctionalException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception e) {
		Map<String, Object> responseBody = new HashMap<>();
		logger.warn("Feilet funksjonell med feilmelding=" + e.getMessage(), e);
		responseBody.put("message", e.getMessage());
		if (e.getMessage().contains(NOT_FOUND.toString())) {
			responseBody.put("status", NOT_FOUND);
			return new ResponseEntity<>(responseBody, NOT_FOUND);
		}
		responseBody.put("status", BAD_REQUEST);
		return new ResponseEntity<>(responseBody, BAD_REQUEST);
	}

	@ExceptionHandler({FoerstesideNotFoundException.class, AzureTokenException.class
	})
	public ResponseEntity<Object> handleNotFoundException(Exception e) {
		Map<String, Object> responseBody = new HashMap<>();
		logger.warn("Feilet funksjonell med feilmelding=" + e.getMessage(), e);
		responseBody.put("message", e.getMessage());
		return new ResponseEntity<>(responseBody, NOT_FOUND);
	}

	@ExceptionHandler({FoerstesideGeneratorTechnicalException.class, Exception.class
	})
	public ResponseEntity<Object> handleTechnicalException(Exception e) {
		Map<String, Object> responseBody = new HashMap<>();
		logger.error("Feilet teknisk med feilmelding=" + e.getMessage(), e);
		responseBody.put("message", e.getMessage());
		return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
	}
}
