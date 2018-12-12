package no.nav.foerstesidegenerator.service;

import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideRequest;
import no.nav.dok.tjenester.foerstesidegenerator.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.consumer.metaforce.MetaforceConsumerService;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class FoerstesideService {

	// foerstesideRepository
	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final LoepenummerGenerator loepenummerGenerator;
	private final MetaforceConsumerService metaforceConsumerService;

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final LoepenummerGenerator loepenummerGenerator,
							  final MetaforceConsumerService metaforceConsumerService) {
		this.postFoerstesideRequestValidator = postFoerstesideRequestValidator;
		this.loepenummerGenerator = loepenummerGenerator;
		this.metaforceConsumerService = metaforceConsumerService;
	}

	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {

		// valider request
		postFoerstesideRequestValidator.validate(request);

		// generer loepenummer. stringify - String.format("%09d", loepenummer)
		int loepenummer = loepenummerGenerator.generateLoepenummer();

		// Transformer til domeneobjekt. Persister

		// Kall metaforce:
		metaforceConsumerService.createDocument(null);

		return null;
	}

	public Object getFoersteside(String key) {
		// todo: implement
		return null;
	}

//	private String generateStrekkode(String loepenummer) {
//		// String kontrollsiffer1 = loepenummer mod 10
//		// String kontrollsiffer2 = (loepenummer + kontrollsiffer1 + postnummer) mod 43 ???
//		// String strekkode = * + loepenummer + kontrollsiffer1 + postnummer + kontrollsiffer2 + *
//		// totalt 16 tegn.
//		return "";
//	}
}
