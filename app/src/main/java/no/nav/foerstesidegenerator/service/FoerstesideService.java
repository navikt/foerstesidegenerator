package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.FoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideResponse;
import no.nav.foerstesidegenerator.consumer.dokkat.DokumentTypeInfoConsumer;
import no.nav.foerstesidegenerator.consumer.dokkat.to.DokumentTypeInfoTo;
import no.nav.foerstesidegenerator.consumer.metaforce.MetaforceBrevdataMapper;
import no.nav.foerstesidegenerator.consumer.metaforce.MetaforceConsumer;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentRequestTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.CreateDocumentResponseTo;
import no.nav.foerstesidegenerator.consumer.metaforce.support.XMLTransformer;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMapper;
import no.nav.foerstesidegenerator.exception.FoerstesideNotFoundException;
import no.nav.foerstesidegenerator.exception.InvalidLoepenummerException;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.foerstesidegenerator.service.support.FoerstesideResponseMapper;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrevdataType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static no.nav.foerstesidegenerator.service.support.LuhnCheckDigitHelper.validateLoepenummerWithCheckDigit;

@Slf4j
@Service
public class FoerstesideService {

	private static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";
	private static final int LOEPENUMMER_LENGTH = 13;
	private static final int LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT = 14;

	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;
	private final FoerstesideResponseMapper foerstesideResponseMapper;
	private final DokumentTypeInfoConsumer dokumentTypeInfoConsumer;
	private final MetaforceConsumer metaforceConsumer;
	private final MetaforceBrevdataMapper metaforceBrevdataMapper;
	private final FoerstesideCounterService foerstesideCounterService;

	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final FoerstesideMapper foerstesideMapper,
							  final FoerstesideRepository foerstesideRepository,
							  final FoerstesideResponseMapper foerstesideResponseMapper,
							  final DokumentTypeInfoConsumer dokumentTypeInfoConsumer,
							  final FoerstesideCounterService foerstesideCounterService,
							  final MetaforceConsumer metaforceConsumer) {
		this.postFoerstesideRequestValidator = postFoerstesideRequestValidator;
		this.foerstesideMapper = foerstesideMapper;
		this.foerstesideRepository = foerstesideRepository;
		this.foerstesideResponseMapper = foerstesideResponseMapper;
		this.dokumentTypeInfoConsumer = dokumentTypeInfoConsumer;
		this.metaforceConsumer = metaforceConsumer;
		this.foerstesideCounterService = foerstesideCounterService;
		this.metaforceBrevdataMapper = new MetaforceBrevdataMapper();
	}

	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request, HttpHeaders headers) {
		postFoerstesideRequestValidator.validate(request, headers);

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(FOERSTESIDE_DOKUMENTTYPE_ID);
		Foersteside foersteside = incrementLoepenummerAndPersist(request, headers);
		CreateDocumentResponseTo document = genererPdfFraMetaforce(foersteside, dokumentTypeInfoTo);

		log.info("Ny førsteside med løpenummer={} og dokumenttypeId={} har blitt generert vha Metaforce", foersteside.getLoepenummer(), FOERSTESIDE_DOKUMENTTYPE_ID);
		return PostFoerstesideResponse.builder()
				.foersteside(document.getDocumentData().clone())
				.loepenummer(foersteside.getLoepenummer())
				.build();
	}

	private Foersteside incrementLoepenummerAndPersist(PostFoerstesideRequest request, HttpHeaders headers) {
		String loepenummer = foerstesideCounterService.hentLoepenummer();
		Foersteside foersteside = foerstesideMapper.map(request, loepenummer, headers);
		foerstesideRepository.save(foersteside);
		return foersteside;
	}

	private CreateDocumentResponseTo genererPdfFraMetaforce(Foersteside foersteside, DokumentTypeInfoTo dokumentTypeInfoTo) {
		BrevdataType brevdata = metaforceBrevdataMapper.map(foersteside);

		CreateDocumentRequestTo metaforceRequest = new CreateDocumentRequestTo(
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getMalLogikkFil(),
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(),
				XMLTransformer.transformXML(brevdata));

		log.info("Mottatt kall til å generere førsteside med løpenummer={} vha metaforce", foersteside.getLoepenummer());
		return metaforceConsumer.createDocument(metaforceRequest);
	}

	public FoerstesideResponse getFoersteside(String loepenummer) {
		validerLoepenummer(loepenummer);
		log.info("Løpenummer validert ok");

		Foersteside domain = foerstesideRepository.findByLoepenummer(loepenummer.substring(0, LOEPENUMMER_LENGTH))
				.orElseThrow(() -> new FoerstesideNotFoundException(loepenummer));
		FoerstesideResponse response = foerstesideResponseMapper.map(domain);
		domain.incrementUthenting();
		domain.setDatoUthentet(LocalDateTime.now());

		return response;
	}

	private void validerLoepenummer(String loepenummer) {
		if (loepenummer.length() < LOEPENUMMER_LENGTH || loepenummer.length() > LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT) {
			log.warn("Løpenummer har ugyldig lengde");
			throw new InvalidLoepenummerException("Løpenummer har ugyldig lengde");
		} else if (loepenummer.length() == LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT && !validateLoepenummerWithCheckDigit(loepenummer)) {
			log.warn("Kontrollsiffer oppgitt er feil loepenummer={} " + loepenummer);
			throw new InvalidLoepenummerException("Kontrollsiffer oppgitt er feil loepenummer=: " + loepenummer);
		}
	}
}
