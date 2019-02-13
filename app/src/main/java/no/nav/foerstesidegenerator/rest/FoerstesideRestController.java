package no.nav.foerstesidegenerator.rest;

import static no.nav.foerstesidegenerator.metrics.MetricLabels.DOK_REQUEST;
import static no.nav.foerstesidegenerator.metrics.MetricLabels.PROCESS_CODE;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.metrics.Metrics;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import no.nav.security.oidc.api.Protected;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@ConditionalOnProperty(value = {"swagger.enabled"}, havingValue = "true")
@Slf4j
@RestController
@RequestMapping("/api/foerstesidegenerator/v1")
@Protected
public class FoerstesideRestController {

	private final FoerstesideService foerstesideService;

	@Inject
	public FoerstesideRestController(FoerstesideService foerstesideService) {
		this.foerstesideService = foerstesideService;
	}

	@Transactional(readOnly = true)
	@GetMapping(value = "/foersteside/{loepenummer}")
	@ApiOperation("Hent metadata om generert førsteside")
	@Metrics(value = DOK_REQUEST, extraTags = {PROCESS_CODE, "get-foersteside"}, percentiles = {0.5, 0.95})
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Hentet metadata fra førsteside", response = GetFoerstesideResponse.class),
			@ApiResponse(code = 400, message = "Ugyldig løpenummer"),
			@ApiResponse(code = 404, message = "Kan ikke finne førsteside med løpenummer={loepenummer}"),
			@ApiResponse(code = 500, message = "Internal server error")})
	public GetFoerstesideResponse getFoerstesideDataFromLoepenummer(@PathVariable String loepenummer) {
		log.info("Har mottatt GET-kall om å hente metadata om førsteside fra løpenummer={}", loepenummer);

		return foerstesideService.getFoersteside(loepenummer);
	}

	@Transactional
	@PostMapping(value = "/foersteside")
	@ApiOperation("Generer en ny førsteside")
	@Metrics(value = DOK_REQUEST, extraTags = {PROCESS_CODE, "post-foersteside"}, percentiles = {0.5, 0.95})
	@ResponseBody
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Opprettet førsteside", response = PostFoerstesideResponse.class),
			@ApiResponse(code = 400, message = "Request validerer ikke"),
			@ApiResponse(code = 500, message = "Internal server error")})
	public ResponseEntity<PostFoerstesideResponse> postNew(@RequestBody PostFoerstesideRequest request) {
		log.info("Har mottatt POST-kall for å opprette ny førsteside");

		PostFoerstesideResponse foersteside = foerstesideService.createFoersteside(request);
		return new ResponseEntity<>(foersteside, HttpStatus.CREATED);
	}
}
