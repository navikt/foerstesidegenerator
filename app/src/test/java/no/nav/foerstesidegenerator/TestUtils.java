package no.nav.foerstesidegenerator;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMapper;

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
	public static final String BEHANDLINGSTEMA_AB1337 = "ab1337";
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
				.withBehandlingstema(BEHANDLINGSTEMA_AB1337)
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
		return createRequest(PostFoerstesideRequest.Foerstesidetype.SKJEMA, TEMA_FAR);
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
				.withBehandlingstema(BEHANDLINGSTEMA_AB1337)
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
		return createRequest(PostFoerstesideRequest.Foerstesidetype.SKJEMA, tema);

	}

	public static PostFoerstesideRequest createRequestEttersendelse() {
		return createRequest(PostFoerstesideRequest.Foerstesidetype.ETTERSENDELSE, TEMA_FAR);
	}

	private static PostFoerstesideRequest createRequest(PostFoerstesideRequest.Foerstesidetype foerstesidetype, String tema) {
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
				.withBehandlingstema(BEHANDLINGSTEMA_AB1337)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(foerstesidetype)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}

	public static Foersteside createFoersteside(String loepenummer) {
		FoerstesideMapper foerstesideMapper = new FoerstesideMapper();
		Foersteside foersteside = foerstesideMapper.map(createRequestWithAdresse());
		foersteside.setLoepenummer(loepenummer);
		return foersteside;
	}


}
