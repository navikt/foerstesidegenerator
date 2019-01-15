package no.nav.foerstesidegenerator.domain;

import static java.lang.String.join;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_2;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_3;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_NAVN;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BEHANDLINGSTEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_TYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ENHETSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDETYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NAV_SKJEMA_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NETS_POSTBOKS;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.OVERSKRIFTSTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTSTED;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SAKSREFERANSE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SAKSYSTEM;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SPRAAKKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.TEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.VEDLEGG_LISTE;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FoerstesideMapper {

	public Foersteside map(PostFoerstesideRequest request) {
		Foersteside foersteside = new Foersteside();
		foersteside.setDatoOpprettet(LocalDateTime.now());
		foersteside.setUthentet(false);

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
		addMetadata(foersteside, NAV_SKJEMA_ID, request.getNavSkjemaId());
		addMetadata(foersteside, OVERSKRIFTSTITTEL, request.getOverskriftstittel());
		addMetadata(foersteside, SPRAAKKODE, request.getSpraakkode().value());
		addMetadata(foersteside, FOERSTESIDETYPE, request.getFoerstesidetype().value());
		addMetadata(foersteside, VEDLEGG_LISTE, join(";", request.getVedleggsliste()));
		addMetadata(foersteside, ENHETSNUMMER, request.getEnhetsnummer());
		if (request.getSak() != null) {
			mapSak(foersteside, request.getSak());
		}

		return foersteside;
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
		addMetadata(foersteside, BRUKER_TYPE, bruker.getBrukerType().value());
	}

	private void mapSak(Foersteside foersteside, Sak sak) {
		addMetadata(foersteside, SAKSYSTEM, sak.getSaksystem().value());
		addMetadata(foersteside, SAKSREFERANSE, sak.getSaksreferanse());
	}

	private void addMetadata(Foersteside foersteside, String key, String value) {
		if (isNotEmpty(value)) {
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, key, value));
		}
	}
}
