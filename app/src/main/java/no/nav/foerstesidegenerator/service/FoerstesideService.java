package no.nav.foerstesidegenerator.service;

import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMapper;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class FoerstesideService {

	// metaforce-consumer
	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final LoepenummerGenerator loepenummerGenerator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;

	static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final LoepenummerGenerator loepenummerGenerator,
							  final FoerstesideMapper foerstesideMapper,
							  final FoerstesideRepository foerstesideRepository) {
		this.postFoerstesideRequestValidator =  postFoerstesideRequestValidator;
		this.loepenummerGenerator = loepenummerGenerator;
		this.foerstesideMapper = foerstesideMapper;
		this.foerstesideRepository = foerstesideRepository;
	}

	// returner en response av noe slag
	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
		// valider request
		postFoerstesideRequestValidator.validate(request);

		// generer loepenummer. stringify - String.format("%09d", loepenummer)
		int loepenummer = loepenummerGenerator.generateLoepenummer();

		// map til domeneobjekt
		Foersteside foersteside = foerstesideMapper.map(request);
		foersteside.setLoepenummer(String.format("%09d", loepenummer));

		// persister til db
		foerstesideRepository.save(foersteside);

		// kall metaforce:
		// byte[] document = metaforceService.createDocument()

		return new PostFoerstesideResponse()
				.withFoersteside(null);
	}

	public GetFoerstesideResponse getFoersteside(String loepenummer) {
		/* todo
		 * kall tjenesten med løpenummer (+ kontrollsiffer)
		 *
		 * hent ut fra db.
		 * Sett uthentet=true, og uthentetTidspunkt.
		 *
		 * returner response med metadata.
		 */
		return new GetFoerstesideResponse();
	}

	private String generateStrekkode(int loepenummer, String postboks) {
		int c1 = loepenummer % 10;
		String loepenummerString = String.format("%09d", loepenummer);

		String res = loepenummerString + c1 + postboks;

		int total = 0;
		for (int i = 0; i < res.length(); i++) {
			total += ALPHABET_STRING.indexOf(res.charAt(i));
		}
		char c2 = ALPHABET_STRING.charAt(total % 43);

		return "*" + res + c2 + "*";
	}
}
