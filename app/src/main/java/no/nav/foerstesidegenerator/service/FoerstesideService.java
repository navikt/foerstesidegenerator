package no.nav.foerstesidegenerator.service;

import static java.lang.Integer.parseInt;

import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.consumer.dokkat.DokumentTypeInfoConsumer;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.consumer.metaforce.MetaforceConsumer;
import no.nav.foerstesidegenerator.consumer.metaforce.MetaforceMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMapper;
import no.nav.foerstesidegenerator.exception.FoerstesideNotFoundException;
import no.nav.foerstesidegenerator.exception.UgyldigLoepenummerException;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.foerstesidegenerator.service.support.GetFoerstesideResponseMapper;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Slf4j
@Service
public class FoerstesideService {

	private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

	public static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";

	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final LoepenummerGenerator loepenummerGenerator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;
	private final GetFoerstesideResponseMapper getFoerstesideResponseMapper;
	private final DokumentTypeInfoConsumer dokumentTypeInfoConsumer;
	private final MetaforceConsumer metaforceConsumer;

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final LoepenummerGenerator loepenummerGenerator,
							  final FoerstesideMapper foerstesideMapper,
							  final FoerstesideRepository foerstesideRepository,
							  final GetFoerstesideResponseMapper getFoerstesideResponseMapper,
							  final DokumentTypeInfoConsumer dokumentTypeInfoConsumer,
							  final MetaforceConsumer metaforceConsumer) {
		this.postFoerstesideRequestValidator =  postFoerstesideRequestValidator;
		this.loepenummerGenerator = loepenummerGenerator;
		this.foerstesideMapper = foerstesideMapper;
		this.foerstesideRepository = foerstesideRepository;
		this.getFoerstesideResponseMapper = getFoerstesideResponseMapper;
		this.dokumentTypeInfoConsumer = dokumentTypeInfoConsumer;
		this.metaforceConsumer = metaforceConsumer;
	}

	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
		postFoerstesideRequestValidator.validate(request);
		log.info("Request validert OK");

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(FOERSTESIDE_DOKUMENTTYPE_ID);
		log.info("Har hentet metadata fra dokkat");

		int loepenummer = loepenummerGenerator.generateLoepenummer();

		Foersteside foersteside = foerstesideMapper.map(request);
		foersteside.setLoepenummer(String.format("%09d", loepenummer));

		foerstesideRepository.save(foersteside);
		log.info("Har validert request og generert loepenummer for ny foersteside");

		CreateDocumentResponseTo document = genererPdfFraMetaforce(foersteside, dokumentTypeInfoTo);
		log.info("Har generert ny foersteside vha Metaforce");

		return PostFoerstesideResponse.builder()
				.foersteside(document.getDocumentData().clone())
				.build();
	}

	private CreateDocumentResponseTo genererPdfFraMetaforce(Foersteside foersteside, DokumentTypeInfoTo dokumentTypeInfoTo) {
		MetaforceMapper metaforceMapper = new MetaforceMapper();
		Document doc = metaforceMapper.map(foersteside);

		CreateDocumentRequestTo metaforceRequest = new CreateDocumentRequestTo(
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getMalLogikkFil(),
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(),
				doc.getDocumentElement());

		return metaforceConsumer.createDocument(metaforceRequest);
	}

	public GetFoerstesideResponse getFoersteside(String loepenummer) {
		validerLoepenummer(loepenummer);
		log.info("Loepenummer validert ok");

		Foersteside domain = foerstesideRepository.findByLoepenummer(loepenummer.substring(0, 9))
				.orElseThrow(() -> new FoerstesideNotFoundException(String.format("Kan ikke finne foersteside med loepenummer=%s", loepenummer)));
		domain.setUthentet(true);
		domain.setDatoUthentet(LocalDateTime.now());
		return getFoerstesideResponseMapper.map(domain);

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
