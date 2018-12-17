package no.nav.foerstesidegenerator.service;

import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class FoerstesideService {

	// metaforce-consumer
	// foerstesideRepository
	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final LoepenummerGenerator loepenummerGenerator;

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final LoepenummerGenerator loepenummerGenerator) {
		this.postFoerstesideRequestValidator =  postFoerstesideRequestValidator;
		this.loepenummerGenerator = loepenummerGenerator;
	}

	// returner en response av noe slag
	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
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
		postFoerstesideRequestValidator.validate(request);

		// generer loepenummer. stringify - String.format("%09d", loepenummer)
		int loepenummer = loepenummerGenerator.generateLoepenummer();

		return null;
	}

	public Object getFoersteside(String key) {
		// todo: implement
		return null;
	}

	private String generateStrekkode(String loepenummer) {
		// String kontrollsiffer1 = loepenummer mod 10
		// String kontrollsiffer2 = (loepenummer + kontrollsiffer1 + postnummer) mod 43 ???
		// String strekkode = * + loepenummer + kontrollsiffer1 + postnummer + kontrollsiffer2 + *
		// totalt 16 tegn.
		return "";
	}
}
