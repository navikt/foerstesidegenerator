package no.nav.foerstesidegenerator.service;

import static no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator.validate;

//import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
//import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class FoerstesideService {

	// metaforce-consumer
	// foerstesideRepository
	private final LoepenummerGenerator loepenummerGenerator;

	@Inject
	public FoerstesideService(final LoepenummerGenerator loepenummerGenerator) {
		this.loepenummerGenerator = loepenummerGenerator;
	}

	// returner en response av noe slag
	public Object createFoersteside(Object request) {
		/*
		 * valider request []
		 * generer løpenummer []
		 * transformer til domeneobjekt som kan persisteres
		 * hvis valid, lagre til db
		 *
		 * kall metaforce
		 *
		 */

		// valider
//		validate(request);
//		String loepenummer = loepenummerGenerator.generateLoepenummer();

		return null;
	}

	// returner et responseObjekt fra en key
	public Object getFoersteside(String key) {

		return null;
	}
}
