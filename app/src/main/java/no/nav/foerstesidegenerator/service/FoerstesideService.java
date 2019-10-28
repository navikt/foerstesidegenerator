package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
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
import no.nav.foerstesidegenerator.service.support.GetFoerstesideResponseMapper;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrevdataType;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static no.nav.foerstesidegenerator.service.support.LuhnCheckDigitHelper.validateLoepenummerWithCheckDigit;
import static org.apache.commons.lang3.StringUtils.leftPad;

@Slf4j
@Service
public class FoerstesideService {

	private static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";
	private static final int LOEPENUMMER_LENGTH = 13;
	private static final int LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT = 14;

	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;
	private final GetFoerstesideResponseMapper getFoerstesideResponseMapper;
	private final DokumentTypeInfoConsumer dokumentTypeInfoConsumer;
	private final MetaforceConsumer metaforceConsumer;
	private final MetaforceBrevdataMapper metaforceBrevdataMapper;

	@Inject
	public FoerstesideService(final PostFoerstesideRequestValidator postFoerstesideRequestValidator,
							  final FoerstesideMapper foerstesideMapper,
							  final FoerstesideRepository foerstesideRepository,
							  final GetFoerstesideResponseMapper getFoerstesideResponseMapper,
							  final DokumentTypeInfoConsumer dokumentTypeInfoConsumer,
							  final MetaforceConsumer metaforceConsumer) {
		this.postFoerstesideRequestValidator = postFoerstesideRequestValidator;
		this.foerstesideMapper = foerstesideMapper;
		this.foerstesideRepository = foerstesideRepository;
		this.getFoerstesideResponseMapper = getFoerstesideResponseMapper;
		this.dokumentTypeInfoConsumer = dokumentTypeInfoConsumer;
		this.metaforceConsumer = metaforceConsumer;
		this.metaforceBrevdataMapper = new MetaforceBrevdataMapper();
	}

	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
		postFoerstesideRequestValidator.validate(request);
		log.info("Request validert OK");

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(FOERSTESIDE_DOKUMENTTYPE_ID);
		log.info("Har hentet metadata fra dokkat");

		int count = foerstesideRepository.findNumberOfFoerstesiderGeneratedToday();
		String loepenummer = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + leftPad(Integer.toString(count + 1), 5, "0");

		Foersteside foersteside = foerstesideMapper.map(request, loepenummer);
		foerstesideRepository.save(foersteside);

		CreateDocumentResponseTo document = genererPdfFraMetaforce(foersteside, dokumentTypeInfoTo);
		log.info("Førsteside generert vha Metaforce");

		log.info("Ny førsteside har løpenummer={}", loepenummer);
		return PostFoerstesideResponse.builder()
				.foersteside(document.getDocumentData().clone())
				.loepenummer(loepenummer)
				.build();
	}

	private CreateDocumentResponseTo genererPdfFraMetaforce(Foersteside foersteside, DokumentTypeInfoTo dokumentTypeInfoTo) {
		BrevdataType brevdata = metaforceBrevdataMapper.map(foersteside);

		CreateDocumentRequestTo metaforceRequest = new CreateDocumentRequestTo(
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getMalLogikkFil(),
				dokumentTypeInfoTo.getDokumentProduksjonsInfo().getIkkeRedigerbarMalId(),
				XMLTransformer.transformXML(brevdata));

		return metaforceConsumer.createDocument(metaforceRequest);
	}

	public GetFoerstesideResponse getFoersteside(String loepenummer) {
		validerLoepenummer(loepenummer);
		log.info("Loepenummer validert ok");

		Foersteside domain = foerstesideRepository.findByLoepenummer(loepenummer.substring(0, LOEPENUMMER_LENGTH))
				.orElseThrow(() -> new FoerstesideNotFoundException(loepenummer));
		GetFoerstesideResponse response = getFoerstesideResponseMapper.map(domain);
		domain.setUthentet(true);
		domain.setDatoUthentet(LocalDateTime.now());
		domain.clearBrukerId();
		domain.clearUkjentBrukerPersoninfo();

		return response;
	}

	private void validerLoepenummer(String loepenummer) {
		if (loepenummer.length() < LOEPENUMMER_LENGTH || loepenummer.length() > LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT) {
			throw new InvalidLoepenummerException("Løpenummer har ugyldig lengde");
		} else if (loepenummer.length() == LOEPENUMMER_LENGTH_WITH_CHECK_DIGIT && !validateLoepenummerWithCheckDigit(loepenummer)) {
		    throw new InvalidLoepenummerException("Kontrollsiffer oppgitt er feil: " + loepenummer);
		}
	}


}
