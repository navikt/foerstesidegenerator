package no.nav.foerstesidegenerator.service.support;

import static org.springframework.util.StringUtils.delimitedListToStringArray;
import static org.springframework.util.StringUtils.isEmpty;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.dok.foerstesidegenerator.api.v1.Saksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GetFoerstesideResponseMapper {

	public GetFoerstesideResponse map(Foersteside domain) {
		return GetFoerstesideResponse.builder()
				.adresse(mapAdresse(domain))
				.netsPostboks(domain.getNetsPostboks())
				.avsender(mapAvsender(domain))
				.bruker(mapBruker(domain))
				.ukjentBrukerPersoninfo(domain.getUkjentBrukerPersoninfo())
				.tema(domain.getTema())
				.behandlingstema(domain.getBehandlingstema())
				.arkivtittel(domain.getArkivtittel())
				.navSkjemaId(domain.getNavSkjemaId())
				.overskriftstittel(domain.getOverskriftstittel())
				.spraakkode(Spraakkode.valueOf(domain.getSpraakkode()))
				.foerstesidetype(Foerstesidetype.valueOf(domain.getFoerstesidetype()))
				.vedleggsliste(mapVedlegg(domain))
				.enhetsnummer(domain.getEnhetsnummer())
				.sak(mapSak(domain))
				.build();
	}

	private Adresse mapAdresse(Foersteside domain) {
		if (isEmpty(domain.getAdresselinje1()) || isEmpty(domain.getPostnummer()) || isEmpty(domain.getPoststed())) {
			return null;
		}
		return Adresse.builder()
				.adresselinje1(domain.getAdresselinje1())
				.adresselinje2(domain.getAdresselinje2())
				.adresselinje3(domain.getAdresselinje3())
				.postnummer(domain.getPostnummer())
				.poststed(domain.getPoststed())
				.build();
	}

	private Avsender mapAvsender(Foersteside domain) {
		String avsenderId = domain.getAvsenderId();
		String avsenderNavn = domain.getAvsenderNavn();
		if (isEmpty(avsenderId) && isEmpty(avsenderNavn)) {
			return null;
		}
		return Avsender.builder()
				.avsenderId(avsenderId)
				.avsenderNavn(avsenderNavn)
				.build();
	}

	private Bruker mapBruker(Foersteside domain) {
		String brukerId = domain.getBrukerId();
		String brukerType = domain.getBrukerType();
		if (isEmpty(brukerId) || isEmpty(brukerType)) {
			return null;
		}
		return Bruker.builder()
				.brukerId(brukerId)
				.brukerType(BrukerType.valueOf(brukerType))
				.build();
	}

	private List<String> mapVedlegg(Foersteside domain) {
		String vedleggString = domain.getVedleggListe();
		String[] splitted = delimitedListToStringArray(vedleggString, ";");
		if (isEmpty(vedleggString) || splitted == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(splitted);
	}

	private Sak mapSak(Foersteside domain) {
		String saksystem = domain.getSaksystem();
		String saksreferanse = domain.getSaksreferanse();
		if (isEmpty(saksystem) || isEmpty(saksreferanse)) {
			return null;
		}
		return Sak.builder()
				.saksystem(Saksystem.valueOf(saksystem))
				.saksreferanse(saksreferanse)
				.build();
	}
}