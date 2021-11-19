package no.nav.foerstesidegenerator.domain;

import lombok.extern.slf4j.Slf4j;
import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.exception.BrukerIdIkkeValidException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.String.join;
import static no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype.ETTERSENDELSE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_2;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_3;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSYSTEM;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_NAVN;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BEHANDLINGSTEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_TYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.DOKUMENT_LISTE_FOERSTESIDE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ENHETSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDETYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDE_OPPRETTET_AV;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NAV_SKJEMA_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NETS_POSTBOKS;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.OVERSKRIFTSTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTSTED;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SPRAAKKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.TEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.VEDLEGG_LISTE;
import static no.nav.foerstesidegenerator.service.support.FoedselsnummerValidator.isValidPid;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Slf4j
@Component
public class FoerstesideMapper {

	private static final Pattern BRUKER_ID_ORGANISASJON_REGEX = Pattern.compile("[0-9]{9}");
	private static final String NAV_PREFIX = "NAV ";
	static final String TEMA_BIDRAG = "BID";
	static final String TEMA_FARSKAP = "FAR";

	public Foersteside map(PostFoerstesideRequest request, String loepenummer, HttpHeaders headers) {
		Foersteside foersteside = new Foersteside();
		foersteside.setDatoOpprettet(LocalDateTime.now());
		foersteside.setUthentet(0);
		foersteside.setLoepenummer(loepenummer);

		if (request.getAdresse() != null) {
			mapAdresse(foersteside, request.getAdresse());
		}
		if (request.getNetsPostboks() != null) {
			addMetadata(foersteside, NETS_POSTBOKS, request.getNetsPostboks());
		}
		if (request.getAvsender() != null) {
			mapAvsender(foersteside, request.getAvsender());
		}
		if (request.getBruker() != null) {
			validateBruker(request);
			mapBruker(foersteside, request.getBruker());
		}
		if (request.getUkjentBrukerPersoninfo() != null && request.getBruker() == null) {
			addMetadata(foersteside, UKJENT_BRUKER_PERSONINFO, request.getUkjentBrukerPersoninfo());
		}
		if(TEMA_BIDRAG.equals(request.getTema()) || TEMA_FARSKAP.equals(request.getTema())) {
			log.info("Førsteside med tema bidrag/farskap forsøkt generert. Setter tema metadata til null da disse ikke skannes hos NETS enda.");
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, TEMA, null));
		} else {
			addMetadata(foersteside, TEMA, request.getTema());
		}
		addMetadata(foersteside, BEHANDLINGSTEMA, request.getBehandlingstema());
		addMetadata(foersteside, ARKIVTITTEL, request.getArkivtittel());
		addVedleggListeIfPresent(foersteside, request.getVedleggsliste());
		mapNavSkjemaId(foersteside, request.getFoerstesidetype(), request.getNavSkjemaId());
		addMetadata(foersteside, OVERSKRIFTSTITTEL, request.getOverskriftstittel());
		addDokumentListeIfPresent(foersteside, request.getDokumentlisteFoersteside());
		addMetadata(foersteside, SPRAAKKODE, request.getSpraakkode().name());
		addMetadata(foersteside, FOERSTESIDETYPE, request.getFoerstesidetype().name());
		addMetadata(foersteside, ENHETSNUMMER, request.getEnhetsnummer());
		mapOpprettetAv(foersteside, headers);
		if (request.getArkivsak() != null) {
			mapSak(foersteside, request.getArkivsak());
		}

		return foersteside;
	}

	private void addVedleggListeIfPresent(Foersteside foersteside, List<String> vedleggListe) {
		if (vedleggListe != null && !vedleggListe.isEmpty()) {
			addMetadata(foersteside, VEDLEGG_LISTE, join(";", vedleggListe));
		}
	}

	private void addDokumentListeIfPresent(Foersteside foersteside, List<String> dokumentListFoersteside) {
		if (dokumentListFoersteside != null && !dokumentListFoersteside.isEmpty()) {
			addMetadata(foersteside, DOKUMENT_LISTE_FOERSTESIDE, join(";", dokumentListFoersteside));
		}
	}

	private void mapAdresse(Foersteside foersteside, Adresse adresse) {
		addMetadata(foersteside, ADRESSELINJE_1, adresse.getAdresselinje1());
		addMetadata(foersteside, ADRESSELINJE_2, adresse.getAdresselinje2());
		addMetadata(foersteside, ADRESSELINJE_3, adresse.getAdresselinje3());
		addMetadata(foersteside, POSTNUMMER, adresse.getPostnummer());
		addMetadata(foersteside, POSTSTED, adresse.getPoststed());
	}

	private void mapAvsender(Foersteside foersteside, Avsender avsender) {
		addMetadata(foersteside, AVSENDER_ID, avsender.getAvsenderId());
		addMetadata(foersteside, AVSENDER_NAVN, avsender.getAvsenderNavn());
	}

	private void mapBruker(Foersteside foersteside, Bruker bruker) {
		addMetadata(foersteside, BRUKER_ID, bruker.getBrukerId());
		addMetadata(foersteside, BRUKER_TYPE, bruker.getBrukerType().name());
	}

	private void mapSak(Foersteside foersteside, Arkivsak sak) {
		addMetadata(foersteside, ARKIVSAKSYSTEM, sak.getArkivsaksystem().name());
		addMetadata(foersteside, ARKIVSAKSNUMMER, sak.getArkivsaksnummer());
	}

	private void mapNavSkjemaId(Foersteside foersteside, Foerstesidetype foerstesidetype, String navSkjemaId) {
		if (ETTERSENDELSE.equals(foerstesidetype) && navSkjemaId.startsWith(NAV_PREFIX)) {
			StringBuilder builder = new StringBuilder(navSkjemaId);
			builder.insert(3, 'e');
			navSkjemaId = builder.toString();
		}
		addMetadata(foersteside, NAV_SKJEMA_ID, navSkjemaId);
	}

	private void mapOpprettetAv(Foersteside foersteside, HttpHeaders headers){
		Stream.of("Nav-Consumer-Id", "x_consumerId", "consumerId","nav-consumerid")
				.filter(headers::containsKey)
				.map(headers::get)
				.findFirst()
				.ifPresent(header -> {
						if(!header.isEmpty()) {
							addMetadata(foersteside, FOERSTESIDE_OPPRETTET_AV, header.get(0));
						}
				});
	}

	private void addMetadata(Foersteside foersteside, String key, String value) {
		if (isNotEmpty(value)) {
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, key, value));
		}
	}

	private void validateBruker(PostFoerstesideRequest request) {
		if (!isBrukerIdValid(request.getBruker())) {
			log.warn("Ugyldig brukerId, Kunne ikke opprette forsteside");
			throw new BrukerIdIkkeValidException("Ugyldig brukerId, Kunne ikke opprette forsteside");
		}
	}

	private boolean isBrukerIdValid(Bruker bruker) {
		if (BrukerType.PERSON.equals(bruker.getBrukerType())) {
			return isValidPid(bruker.getBrukerId(), true);
		} else if (BrukerType.ORGANISASJON.equals(bruker.getBrukerType())) {
			return BRUKER_ID_ORGANISASJON_REGEX.matcher(bruker.getBrukerId()).matches();
		}
		return false;
	}
}
