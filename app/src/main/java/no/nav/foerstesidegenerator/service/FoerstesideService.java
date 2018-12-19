package no.nav.foerstesidegenerator.service;

import static java.lang.Integer.parseInt;

import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMapper;
import no.nav.foerstesidegenerator.exceptions.UgyldigLoepenummerException;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.foerstesidegenerator.service.support.GetFoerstesideResponseMapper;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FoerstesideService {

	// metaforce-consumer
	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final LoepenummerGenerator loepenummerGenerator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;
	private final GetFoerstesideResponseMapper getFoerstesideResponseMapper;

	private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final LoepenummerGenerator loepenummerGenerator,
							  final FoerstesideMapper foerstesideMapper,
							  final FoerstesideRepository foerstesideRepository,
							  final GetFoerstesideResponseMapper getFoerstesideResponseMapper) {
		this.postFoerstesideRequestValidator =  postFoerstesideRequestValidator;
		this.loepenummerGenerator = loepenummerGenerator;
		this.foerstesideMapper = foerstesideMapper;
		this.foerstesideRepository = foerstesideRepository;
		this.getFoerstesideResponseMapper = getFoerstesideResponseMapper;
	}

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
		validerLoepenummer(loepenummer);

		Optional<Foersteside> foersteside = foerstesideRepository.findByLoepenummer(loepenummer.substring(0, 9));
		if (foersteside.isPresent()) {
			Foersteside domain = foersteside.get();
			domain.setUthentet(true);
			domain.setDatoUthentet(LocalDateTime.now());
			return getFoerstesideResponseMapper.map(domain);
		} else {
			return null;
		}
	}

	private void validerLoepenummer(String loepenummer) {
		if (loepenummer.length() < 9 || loepenummer.length() > 10) {
			throw new UgyldigLoepenummerException("Løpenummer har ugyldig lengde");
		} else if (loepenummer.length() == 10) {
			int a = parseInt(loepenummer.substring(0, 9));
			int b = parseInt(loepenummer.substring(9, 10));
			if (a % 10 != b) {
				throw new UgyldigLoepenummerException("Kontrollsiffer oppgitt er feil");
			}
		}
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
