package no.nav.foerstesidegenerator;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;

import java.util.Arrays;

public class TestUtils {

	public static final String ADR_LINJE_1 = "Vei 1";
	public static final String POSTNR = "0404";
	public static final String OSLO = "Oslo";

	public static final String NETS = "8899";

	public static final String AVSENDER = "avsenderId";
	public static final String NAVN = "Navn";

	public static final String BRUKER = "brukerId";

	public static final String TEMA_FAR = "FAR";
	public static final String TITTEL = "tittel";
	public static final String SKJEMA_ID = "NAV 13.37";

	public static final String VEDLEGG_1 = "vedlegg1";
	public static final String VEDLEGG_2 = "vedlegg2";

	public static final String ENHET_9999 = "9999";

	public static final String SAK_REF = "saksRef";

	public static PostFoerstesideRequest createRequestWithAdresse() {
		return new PostFoerstesideRequest()
				.withAdresse(new Adresse()
						.withAdresselinje1(ADR_LINJE_1)
						.withAdresselinje2(null)
						.withAdresselinje3(null)
						.withPostnummer(POSTNR)
						.withPoststed(OSLO))
				.withNetsPostboks(null)
				.withAvsender(new Avsender()
						.withAvsenderId(AVSENDER)
						.withAvsenderNavn(NAVN))
				.withBruker(new Bruker()
						.withBrukerId(BRUKER)
						.withBrukerType(Bruker.BrukerType.PERSON))
				.withUkjentBrukerPersoninfo(null)
				.withTema(TEMA_FAR)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}

	public static PostFoerstesideRequest createRequestWithNetsPostboks() {
		return new PostFoerstesideRequest()
				.withAdresse(null)
				.withNetsPostboks(NETS)
				.withAvsender(new Avsender()
						.withAvsenderId(AVSENDER)
						.withAvsenderNavn(NAVN))
				.withBruker(new Bruker()
						.withBrukerId(BRUKER)
						.withBrukerType(Bruker.BrukerType.PERSON))
				.withUkjentBrukerPersoninfo(null)
				.withTema(TEMA_FAR)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}

	public static PostFoerstesideRequest createRequestWithoutBruker(String ukjentBrukerPersoninfo) {
		return new PostFoerstesideRequest()
				.withAdresse(null)
				.withNetsPostboks(NETS)
				.withAvsender(new Avsender()
						.withAvsenderId(AVSENDER)
						.withAvsenderNavn(NAVN))
				.withBruker(null)
				.withUkjentBrukerPersoninfo(ukjentBrukerPersoninfo)
				.withTema(TEMA_FAR)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}

	public static PostFoerstesideRequest createRequestWithTema(String tema) {
		return new PostFoerstesideRequest()
				.withAdresse(null)
				.withNetsPostboks(NETS)
				.withAvsender(new Avsender()
						.withAvsenderId(AVSENDER)
						.withAvsenderNavn(NAVN))
				.withBruker(new Bruker()
						.withBrukerId(BRUKER)
						.withBrukerType(Bruker.BrukerType.PERSON))
				.withUkjentBrukerPersoninfo(null)
				.withTema(tema)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(PostFoerstesideRequest.Foerstesidetype.SKJEMA)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}
}
