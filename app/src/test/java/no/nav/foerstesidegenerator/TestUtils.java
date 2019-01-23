package no.nav.foerstesidegenerator;

import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
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

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.GetFoerstesideResponse;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Sak;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMetadata;

import java.util.Arrays;

public class TestUtils {

	public static final String ADR_LINJE_1 = "Vei 1";
	public static final String POSTNR = "0404";
	public static final String OSLO = "Oslo";

	public static final String NETS = "8899";

	public static final String AVSENDER = "avsenderId";
	public static final String NAVN = "Navn";

	public static final String BRUKER = "brukerId";
	public static final String BRUKER_PERSON = "PERSON";

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

	private static PostFoerstesideRequest createRequestWithoutAdresse(String netspostboks, String ukjent, PostFoerstesideRequest.Foerstesidetype type, String tema) {
		return new PostFoerstesideRequest()
				.withAdresse(null)
				.withNetsPostboks(netspostboks)
				.withAvsender(new Avsender()
						.withAvsenderId(AVSENDER)
						.withAvsenderNavn(NAVN))
				.withBruker(new Bruker()
						.withBrukerId(BRUKER)
						.withBrukerType(Bruker.BrukerType.PERSON))
				.withUkjentBrukerPersoninfo(ukjent)
				.withTema(tema)
				.withBehandlingstema(BEHANDLINGSTEMA_AB1337)
				.withArkivtittel(TITTEL)
				.withNavSkjemaId(SKJEMA_ID)
				.withOverskriftstittel(TITTEL)
				.withSpraakkode(PostFoerstesideRequest.Spraakkode.NB)
				.withFoerstesidetype(type)
				.withVedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.withEnhetsnummer(ENHET_9999)
				.withSak(new Sak()
						.withSaksystem(Sak.Saksystem.PSAK)
						.withSaksreferanse(SAK_REF));
	}

	public static PostFoerstesideRequest createRequestWithNetsPostboks() {
		return createRequestWithoutAdresse(NETS, null, PostFoerstesideRequest.Foerstesidetype.SKJEMA, TEMA_FAR);
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

	public static PostFoerstesideRequest createRequestWithoutAdresseAndNetsPostboks() {
		return createRequestWithoutAdresse(null, null, PostFoerstesideRequest.Foerstesidetype.SKJEMA, TEMA_FAR);
	}

	public static PostFoerstesideRequest createRequestWithTema(String tema) {
		return createRequestWithoutAdresse(NETS, null, PostFoerstesideRequest.Foerstesidetype.SKJEMA, tema);

	}

	public static PostFoerstesideRequest createRequestEttersendelse() {
		return createRequestWithoutAdresse(NETS, null, PostFoerstesideRequest.Foerstesidetype.ETTERSENDELSE, TEMA_FAR);
	}

//	Domeneobjekt-metoder

	public static Foersteside createFoersteside(String loepenummer) {
		return createFoersteside(loepenummer, ADR_LINJE_1, POSTNR, OSLO, null, TEMA_FAR, null, AVSENDER_ID, NAVN, BRUKER, BRUKER_PERSON);
	}

	public static Foersteside createFoerstesideWithoutAdresse(String loepenummer) {
		return createFoersteside(loepenummer, null, null, null, NETS, TEMA_FAR, null, AVSENDER_ID, NAVN, BRUKER, BRUKER_PERSON);
	}

	public static Foersteside createFoerstesideWithoutAvsenderAndBruker(String loepenummer) {
		return createFoersteside(loepenummer, ADR_LINJE_1, POSTNR, OSLO, NETS, TEMA_FAR, null, null, null, null, null);
	}

	public static Foersteside createFoersteside(String loepenummer, String adresse, String postnr, String poststed, String nets, String tema, String ukjent, String avsenderId, String avsendernavn, String brukerid, String brukertype) {
		Foersteside foersteside = new Foersteside();
		foersteside.setLoepenummer(loepenummer);
		createMetadata(foersteside, ADRESSELINJE_1, adresse);
		createMetadata(foersteside, POSTNUMMER, postnr);
		createMetadata(foersteside, POSTSTED, poststed);
		createMetadata(foersteside, NETS_POSTBOKS, nets);
		createMetadata(foersteside, AVSENDER_ID, avsenderId);
		createMetadata(foersteside, AVSENDER_NAVN, avsendernavn);
		createMetadata(foersteside, BRUKER_ID, brukerid);
		createMetadata(foersteside, BRUKER_TYPE, brukertype);
		createMetadata(foersteside, UKJENT_BRUKER_PERSONINFO, ukjent);
		createMetadata(foersteside, TEMA, tema);
		createMetadata(foersteside, BEHANDLINGSTEMA, BEHANDLINGSTEMA_AB1337);
		createMetadata(foersteside, ARKIVTITTEL, TITTEL);
		createMetadata(foersteside, NAV_SKJEMA_ID, SKJEMA_ID);
		createMetadata(foersteside, OVERSKRIFTSTITTEL, TITTEL);
		createMetadata(foersteside, SPRAAKKODE, GetFoerstesideResponse.Spraakkode.NB.value());
		createMetadata(foersteside, FOERSTESIDETYPE, GetFoerstesideResponse.Foerstesidetype.SKJEMA.value());
		createMetadata(foersteside, VEDLEGG_LISTE, VEDLEGG_1 + ";" + VEDLEGG_2);
		createMetadata(foersteside, ENHETSNUMMER, ENHET_9999);
		createMetadata(foersteside, SAKSYSTEM, Sak.Saksystem.PSAK.value());
		createMetadata(foersteside, SAKSREFERANSE, SAK_REF);
		return foersteside;
	}

	private static void createMetadata(Foersteside foersteside, String key, String value) {
		if (value != null) {
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, key, value));
		}
	}
}
