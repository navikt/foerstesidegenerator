package no.nav.foerstesidegenerator.service.support;

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
import static org.springframework.util.StringUtils.delimitedListToStringArray;
import static org.springframework.util.StringUtils.isEmpty;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse.Spraakkode;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMetadata;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GetFoerstesideResponseMapper {

	public GetFoerstesideResponse map(Foersteside domain) {
		return new GetFoerstesideResponse()
				.withAdresse(mapAdresse(domain))
				.withNetsPostboks(getValueForKey(domain, NETS_POSTBOKS))
				.withAvsender(mapAvsender(domain))
				.withBruker(mapBruker(domain))
				.withUkjentBrukerPersoninfo(getValueForKey(domain, UKJENT_BRUKER_PERSONINFO))
				.withTema(getValueForKey(domain, TEMA))
				.withBehandlingstema(getValueForKey(domain, BEHANDLINGSTEMA))
				.withArkivtittel(getValueForKey(domain, ARKIVTITTEL))
				.withNavSkjemaId(getValueForKey(domain, NAV_SKJEMA_ID))
				.withOverskriftstittel(getValueForKey(domain, OVERSKRIFTSTITTEL))
				.withSpraakkode(Spraakkode.valueOf(getValueForKey(domain, SPRAAKKODE)))
				.withFoerstesidetype(GetFoerstesideResponse.Foerstesidetype.fromValue(getValueForKey(domain, FOERSTESIDETYPE)))
				.withVedleggsliste(mapVedlegg(domain))
				.withEnhetsnummer(getValueForKey(domain, ENHETSNUMMER))
				.withSak(mapSak(domain));
	}

	private Adresse mapAdresse(Foersteside domain) {
		if (isEmpty(getValueForKey(domain, ADRESSELINJE_1)) || isEmpty(getValueForKey(domain, POSTNUMMER)) || isEmpty(getValueForKey(domain, POSTSTED))) {
			return null;
		}
		return new Adresse()
				.withAdresselinje1(getValueForKey(domain, ADRESSELINJE_1))
				.withAdresselinje2(getValueForKey(domain, ADRESSELINJE_2))
				.withAdresselinje3(getValueForKey(domain, ADRESSELINJE_3))
				.withPostnummer(getValueForKey(domain, POSTNUMMER))
				.withPoststed(getValueForKey(domain, POSTSTED));
	}

	private Avsender mapAvsender(Foersteside domain) {
		String avsenderId = getValueForKey(domain, AVSENDER_ID);
		String avsenderNavn = getValueForKey(domain, AVSENDER_NAVN);
		if (isEmpty(avsenderId) && isEmpty(avsenderNavn)) {
			return null;
		}
		return new Avsender()
				.withAvsenderId(avsenderId)
				.withAvsenderNavn(avsenderNavn);
	}

	private Bruker mapBruker(Foersteside domain) {
		String brukerId = getValueForKey(domain, BRUKER_ID);
		String brukerType = getValueForKey(domain, BRUKER_TYPE);
		if (isEmpty(brukerId) || isEmpty(brukerType)) {
			return null;
		}
		return new Bruker()
				.withBrukerId(brukerId)
				.withBrukerType(Bruker.BrukerType.valueOf(brukerType));
	}

	private List<String> mapVedlegg(Foersteside domain) {
		String vedleggString = getValueForKey(domain, VEDLEGG_LISTE);
		String[] splitted = delimitedListToStringArray(vedleggString, ";");
		if (isEmpty(vedleggString) || splitted == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(splitted);
	}

	private Sak mapSak(Foersteside domain) {
		String saksystem = getValueForKey(domain, SAKSYSTEM);
		String saksreferanse = getValueForKey(domain, SAKSREFERANSE);
		if (isEmpty(saksystem) || isEmpty(saksreferanse)) {
			return null;
		}
		return new Sak()
				.withSaksystem(Sak.Saksystem.valueOf(saksystem))
				.withSaksreferanse(saksreferanse);
	}

	private String getValueForKey(Foersteside domain, String key) {
		return domain.getFoerstesideMetadata().stream().filter(a -> a.getKey().equals(key)).findFirst().map(FoerstesideMetadata::getValue).orElse(null);
	}

}
