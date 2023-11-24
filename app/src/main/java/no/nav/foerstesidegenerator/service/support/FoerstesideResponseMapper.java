package no.nav.foerstesidegenerator.service.support;

import no.nav.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.foerstesidegenerator.api.v1.Avsender;
import no.nav.foerstesidegenerator.api.v1.Bruker;
import no.nav.foerstesidegenerator.api.v1.FoerstesideResponse;
import no.nav.foerstesidegenerator.api.v1.code.Arkivsaksystem;
import no.nav.foerstesidegenerator.api.v1.code.BrukerType;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static no.nav.foerstesidegenerator.domain.code.FagomradeCode.STO;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.util.StringUtils.delimitedListToStringArray;

@Component
public class FoerstesideResponseMapper {

	public FoerstesideResponse map(Foersteside domain) {
		return FoerstesideResponse.builder()
				.avsender(mapAvsender(domain))
				.bruker(mapBruker(domain))
				.tema(mapTema(domain))
				.behandlingstema(domain.getBehandlingstema())
				.arkivtittel(domain.getArkivtittel())
				.navSkjemaId(domain.getNavSkjemaId())
				.vedleggsliste(mapVedlegg(domain))
				.enhetsnummer(domain.getEnhetsnummer())
				.arkivsak(mapArkivsak(domain))
				.foerstesideOpprettetAv(domain.getFoerstesideOpprettetAv())
				.build();
	}

	private static String mapTema(Foersteside domain) {
		if ("OKO".equals(domain.getTema())) {
			return STO.name();
		}
		return domain.getTema();
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
		if (isEmpty(vedleggString)) {
			return Collections.emptyList();
		}
		return Arrays.asList(splitted);
	}

	private Arkivsak mapArkivsak(Foersteside domain) {
		String saksystem = domain.getArkivsaksystem();
		String saksreferanse = domain.getArkivsaksnummer();
		if (isEmpty(saksystem) || isEmpty(saksreferanse)) {
			return null;
		}
		return Arkivsak.builder()
				.arkivsaksystem(Arkivsaksystem.valueOf(saksystem))
				.arkivsaksnummer(saksreferanse)
				.build();
	}
}