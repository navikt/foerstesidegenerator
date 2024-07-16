package no.nav.foerstesidegenerator.domain;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.api.v1.Adresse;
import no.nav.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.foerstesidegenerator.api.v1.Avsender;
import no.nav.foerstesidegenerator.api.v1.Bruker;
import no.nav.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.foerstesidegenerator.api.v1.code.Foerstesidetype;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.join;
import static no.nav.foerstesidegenerator.api.v1.code.Foerstesidetype.ETTERSENDELSE;
import static no.nav.foerstesidegenerator.constants.NavHeaders.NAV_CONSUMER_ID;
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
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;

@Slf4j
@Component
public class FoerstesideMapper {

	private static final String NAV_PREFIX = "NAV ";

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
			mapBruker(foersteside, request.getBruker());
		}
		if (request.getUkjentBrukerPersoninfo() != null && request.getBruker() == null) {
			addMetadata(foersteside, UKJENT_BRUKER_PERSONINFO, request.getUkjentBrukerPersoninfo());
		}

		addMetadata(foersteside, TEMA, request.getTema());
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
		addMetadata(foersteside, BRUKER_ID, mapBrukerId(bruker.getBrukerId()));
		addMetadata(foersteside, BRUKER_TYPE, bruker.getBrukerType().name());
	}

	private String mapBrukerId(String brukerId) {
		return deleteWhitespace(brukerId);
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

	private void mapOpprettetAv(Foersteside foersteside, HttpHeaders headers) {
		var navConsumerId = headers.getFirst(NAV_CONSUMER_ID);

		if (navConsumerId != null) {
			addMetadata(foersteside, FOERSTESIDE_OPPRETTET_AV, navConsumerId);
		}
	}

	private void addMetadata(Foersteside foersteside, String key, String value) {
		if (isNotEmpty(value)) {
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, key, value));
		}
	}
}
