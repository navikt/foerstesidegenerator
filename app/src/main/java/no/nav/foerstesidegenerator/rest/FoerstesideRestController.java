package no.nav.foerstesidegenerator.rest;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@Slf4j
@RestController
@RequestMapping("/api/foerstesidegenerator/v1")
public class FoerstesideRestController {

	private final FoerstesideService foerstesideService;

	@Inject
	public FoerstesideRestController(FoerstesideService foerstesideService) {
		this.foerstesideService = foerstesideService;
	}

	@GetMapping(value = "/foersteside/{loepenummer}")
	@ApiOperation("Hent metadata om generert førsteside")
	@ResponseBody
	public Object getFoerstesideDataFromLoepenummer(@PathVariable String loepenummer) {
		log.info("Har mottatt GET-kall om å hente foerstesidedata fra loepenummer={}", loepenummer);

		GetFoerstesideResponse foersteside = foerstesideService.getFoersteside(loepenummer);

		return foersteside;
	}

	@PostMapping(value = "/foersteside")
	@ApiOperation("Generer en ny førsteside")
	@ResponseBody
	public PostFoerstesideResponse postNew(@RequestBody PostFoerstesideRequest request) {
		log.info("foerstesidegenerator - mottatt POST-kall for å opprette ny foersteside-key");

		PostFoerstesideResponse foersteside = foerstesideService.createFoersteside(request);

		return foersteside;
	}
}
