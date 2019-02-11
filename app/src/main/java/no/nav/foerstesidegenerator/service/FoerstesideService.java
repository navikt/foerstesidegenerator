package no.nav.foerstesidegenerator.service;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.leftPad;

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
import no.nav.foerstesidegenerator.exception.InvalidLoepenummerException;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import no.nav.foerstesidegenerator.service.support.GetFoerstesideResponseMapper;
import no.nav.foerstesidegenerator.service.support.PostFoerstesideRequestValidator;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class FoerstesideService {

	public static final String FOERSTESIDE_DOKUMENTTYPE_ID = "000124";
	private static final int LOEPENUMMER_LENGTH = 13;
	private static final int LOEPENUMMER_LENGTH_WITH_CONTROL_DIGIT = 14;

	private final PostFoerstesideRequestValidator postFoerstesideRequestValidator;
	private final FoerstesideMapper foerstesideMapper;
	private final FoerstesideRepository foerstesideRepository;
	private final GetFoerstesideResponseMapper getFoerstesideResponseMapper;
	private final DokumentTypeInfoConsumer dokumentTypeInfoConsumer;
	private final MetaforceConsumer metaforceConsumer;

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
	}

	public PostFoerstesideResponse createFoersteside(PostFoerstesideRequest request) {
		postFoerstesideRequestValidator.validate(request);
		log.info("Request validert OK");

		DokumentTypeInfoTo dokumentTypeInfoTo = dokumentTypeInfoConsumer.hentDokumenttypeInfo(FOERSTESIDE_DOKUMENTTYPE_ID);
		log.info("Har hentet metadata fra dokkat");

		int count = foerstesideRepository.findNumberOfFoerstesiderGeneratedToday();
		String loepenummer = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + leftPad(Integer.toString(count + 1), 5, "0");
		log.info("Nytt løpenummer={}", loepenummer);

		Foersteside foersteside = foerstesideMapper.map(request, loepenummer);

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

		Foersteside domain = foerstesideRepository.findByLoepenummer(loepenummer.substring(0, 13))
				.orElseThrow(() -> new FoerstesideNotFoundException(loepenummer));
		domain.setUthentet(true);
		domain.setDatoUthentet(LocalDateTime.now());
		return getFoerstesideResponseMapper.map(domain);

	}

	private void validerLoepenummer(String loepenummer) {
		if (loepenummer.length() < LOEPENUMMER_LENGTH || loepenummer.length() > LOEPENUMMER_LENGTH_WITH_CONTROL_DIGIT) {
			throw new InvalidLoepenummerException("Løpenummer har ugyldig lengde");
		} else if (loepenummer.length() == LOEPENUMMER_LENGTH_WITH_CONTROL_DIGIT) {
			int a = parseInt(loepenummer.substring(0, LOEPENUMMER_LENGTH));
			int b = parseInt(loepenummer.substring(LOEPENUMMER_LENGTH, LOEPENUMMER_LENGTH_WITH_CONTROL_DIGIT));
			if (a % 10 != b) {
				throw new InvalidLoepenummerException("Kontrollsiffer oppgitt er feil");
			}
		}
	}


}
