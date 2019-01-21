package no.nav.foerstesidegenerator.service.support;

import static org.springframework.util.StringUtils.delimitedListToStringArray;
import static org.springframework.util.StringUtils.isEmpty;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse.Spraakkode;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.dok.foerstesidegenerator.api.v1.Sak.Saksystem;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GetFoerstesideResponseMapper {

	public GetFoerstesideResponse map(Foersteside domain) {
		return new GetFoerstesideResponse()
				.withAdresse(mapAdresse(domain))
				.withNetsPostboks(domain.getNetsPostboks())
				.withAvsender(mapAvsender(domain))
				.withBruker(mapBruker(domain))
				.withUkjentBrukerPersoninfo(domain.getUkjentBrukerPersoninfo())
				.withTema(domain.getTema())
				.withBehandlingstema(domain.getBehandlingstema())
				.withArkivtittel(domain.getArkivtittel())
				.withNavSkjemaId(domain.getNavSkjemaId())
				.withOverskriftstittel(domain.getOverskriftstittel())
				.withSpraakkode(Spraakkode.valueOf(domain.getSpraakkode()))
				.withFoerstesidetype(GetFoerstesideResponse.Foerstesidetype.fromValue(domain.getFoerstesidetype()))
				.withVedleggsliste(mapVedlegg(domain))
				.withEnhetsnummer(domain.getEnhetsnummer())
				.withSak(mapSak(domain));
	}

	private Adresse mapAdresse(Foersteside domain) {
		if (isEmpty(domain.getAdresselinje1()) || isEmpty(domain.getPostnummer()) || isEmpty(domain.getPoststed())) {
			return null;
		}
		return new Adresse()
				.withAdresselinje1(domain.getAdresselinje1())
				.withAdresselinje2(domain.getAdresselinje2())
				.withAdresselinje3(domain.getAdresselinje3())
				.withPostnummer(domain.getPostnummer())
				.withPoststed(domain.getPoststed());
	}

	private Avsender mapAvsender(Foersteside domain) {
		String avsenderId = domain.getAvsenderId();
		String avsenderNavn = domain.getAvsenderNavn();
		if (isEmpty(avsenderId) && isEmpty(avsenderNavn)) {
			return null;
		}
		return new Avsender()
				.withAvsenderId(avsenderId)
				.withAvsenderNavn(avsenderNavn);
	}

	private Bruker mapBruker(Foersteside domain) {
		String brukerId = domain.getBrukerId();
		String brukerType = domain.getBrukerType();
		if (isEmpty(brukerId) || isEmpty(brukerType)) {
			return null;
		}
		return new Bruker()
				.withBrukerId(brukerId)
				.withBrukerType(BrukerType.valueOf(brukerType));
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
		return new Sak()
				.withSaksystem(Saksystem.valueOf(saksystem))
				.withSaksreferanse(saksreferanse);
	}
}