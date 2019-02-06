package no.nav.foerstesidegenerator.service.support;

import static org.springframework.util.StringUtils.delimitedListToStringArray;
import static org.springframework.util.StringUtils.isEmpty;

import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsaksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GetFoerstesideResponseMapper {

	public GetFoerstesideResponse map(Foersteside domain) {
		return GetFoerstesideResponse.builder()
				.avsender(mapAvsender(domain))
				.bruker(mapBruker(domain))
				.tema(domain.getTema())
				.behandlingstema(domain.getBehandlingstema())
				.arkivtittel(domain.getArkivtittel())
				.navSkjemaId(domain.getNavSkjemaId())
				.vedleggsliste(mapVedlegg(domain))
				.enhetsnummer(domain.getEnhetsnummer())
				.arkivsak(mapArkivsak(domain))
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