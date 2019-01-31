package no.nav.foerstesidegenerator.rest;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import no.nav.security.oidc.api.Protected;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

	// TODO: tilgangsstyring via abac?

	private final FoerstesideService foerstesideService;

	@Inject
	public FoerstesideRestController(FoerstesideService foerstesideService) {
		this.foerstesideService = foerstesideService;
	}

	@Transactional(readOnly = true)
	@GetMapping(value = "/foersteside/{loepenummer}")
	@ApiOperation("Hent metadata om generert førsteside")
	@ResponseBody
	public GetFoerstesideResponse getFoerstesideDataFromLoepenummer(@PathVariable String loepenummer) {
		log.info("Har mottatt GET-kall om å hente metadata om førsteside fra loepenummer={}", loepenummer);

		return foerstesideService.getFoersteside(loepenummer);
	}

	@Transactional
	@PostMapping(value = "/foersteside")
	@ApiOperation("Generer en ny førsteside")
	@ResponseBody
	public PostFoerstesideResponse postNew(@RequestBody PostFoerstesideRequest request) {
		log.info("Har mottatt POST-kall for å opprette ny førsteside");

		return foerstesideService.createFoersteside(request);
	}
}
