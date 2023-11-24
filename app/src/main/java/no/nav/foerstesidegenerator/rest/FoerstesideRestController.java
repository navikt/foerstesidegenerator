package no.nav.foerstesidegenerator.rest;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.api.v1.FoerstesideResponse;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.service.FoerstesideService;
import no.nav.security.token.support.core.api.Protected;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/foerstesidegenerator/v1")
@Protected
public class FoerstesideRestController {

	private final FoerstesideService foerstesideService;

	public FoerstesideRestController(FoerstesideService foerstesideService) {
		this.foerstesideService = foerstesideService;
	}

	@Transactional
	@GetMapping(value = "/foersteside/{loepenummer}")
	@ResponseBody
	public FoerstesideResponse getFoerstesideDataFromLoepenummer(@PathVariable String loepenummer) {
		log.info("Har mottatt GET-kall om å hente metadata om førsteside fra løpenummer={}", loepenummer);
		return foerstesideService.getFoersteside(loepenummer);
	}

	@Transactional
	@PostMapping(value = "/foersteside")
	@ResponseBody
	public ResponseEntity<PostFoerstesideResponse> postNew(
			@RequestBody PostFoerstesideRequest request,
			@RequestHeader HttpHeaders headers
	) {
		log.info("Har mottatt POST-kall for å opprette ny førsteside");

		PostFoerstesideResponse foersteside = foerstesideService.createFoersteside(request, headers);
		return new ResponseEntity<>(foersteside, HttpStatus.CREATED);
	}

}
