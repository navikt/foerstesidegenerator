package no.nav.foerstesidegenerator.rest;

import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/foerstesidegenerator/v1/foersteside")
public class FoerstesideRestController {

	private static final Logger log = LogManager.getLogger(FoerstesideRestController.class);

	private final FoerstesideService foerstesideService;

	@Inject
	public FoerstesideRestController(FoerstesideService foerstesideService) {
		this.foerstesideService = foerstesideService;
	}

	@GetMapping(value = "/{key}")
	@ResponseBody
	public Object getDataFromKey(@PathVariable String key) {
		log.info("foerstesidegenerator - mottatt GET-kall om å hente data fra key={}", key);
		// do stuff
		Object foersteside = foerstesideService.getFoersteside(key);

		// returner pdf
		return null;
	}

	@PostMapping
	@ResponseBody
	public PostFoerstesideResponse postNew(@RequestBody PostFoerstesideRequest request) {
		log.info("foerstesidegenerator - mottatt POST-kall for å opprette ny foersteside-key");
		// persister basert på request (hvis identiske metadata finnes => returner eksisterende?)
		PostFoerstesideResponse foersteside = foerstesideService.createFoersteside(request);

		return foersteside;
	}
}
