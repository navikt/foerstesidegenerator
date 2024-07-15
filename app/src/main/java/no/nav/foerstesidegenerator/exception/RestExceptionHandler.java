package no.nav.foerstesidegenerator.exception;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({
			InvalidRequestException.class,
			InvalidTemaException.class,
			BrukerIdIkkeValidException.class,
			InvalidLoepenummerException.class
	})
	public ResponseEntity<Object> handleBadRequestException(Exception e) {
		log.warn("Feilet funksjonelt med feilmelding=" + e.getMessage(), e);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", e.getMessage());

		return new ResponseEntity<>(responseBody, BAD_REQUEST);
	}

	@ExceptionHandler({FoerstesideNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException(Exception e) {
		log.warn("Feilet funksjonelt med feilmelding=" + e.getMessage(), e);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", e.getMessage());

		return new ResponseEntity<>(responseBody, NOT_FOUND);
	}

	@ExceptionHandler({JwtTokenUnauthorizedException.class})
	public ResponseEntity<Object> handleUnauthorizedException(JwtTokenUnauthorizedException jwtTokenUnauthorizedException) {
		return new ResponseEntity<>(Map.of("message", jwtTokenUnauthorizedException.getCause().getMessage()), UNAUTHORIZED);
	}

	@ExceptionHandler({
			DokmetFunctionalException.class,
			ManglerDokumentproduksjonsinfoException.class
	})
	public ResponseEntity<Object> handleConsumerFunctionalException(Exception e) {
		log.error("Feilet funksjonelt mot ekstern tjeneste med feilmelding=" + e.getMessage(), e);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", "Funksjonell feil ved kall mot ekstern tjeneste med feilmelding=" + e.getMessage());

		return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({FoerstesideGeneratorTechnicalException.class})
	public ResponseEntity<Object> handleConsumerTechnicalException(Exception e) {
		log.error("Feilet teknisk mot ekstern tjeneste med feilmelding=" + e.getMessage(), e);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", "Teknisk feil ved kall mot ekstern tjeneste med feilmelding=" + e.getMessage());

		return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleException(Exception e) {
		log.error("Feilet med feilmelding=" + e.getMessage(), e);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", "Teknisk feil med feilmelding=" + e.getMessage());

		return new ResponseEntity<>(responseBody, INTERNAL_SERVER_ERROR);
	}

}