package no.nav.foerstesidegenerator.service;

import static no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator.validate;

import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideResponse;
import org.springframework.stereotype.Service;

@Service
public class FoerstesideService {

	// metaforce-consumer
	// foerstesideRepository

	public FoerstesideService() {
	}

	// returner en response av noe slag
	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
		// valider
		validate(request);

		// transformer til domeneobjekt som kan persisteres


		// hvis valid, lagre til db og hent/generer id


		return null;
	}

	// returner et responseObjekt fra en key
	public Object getFoersteside(String key) {

		return null;
	}
}
