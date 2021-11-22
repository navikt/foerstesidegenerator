package no.nav.foerstesidegenerator;

import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSYSTEM;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_NAVN;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BEHANDLINGSTEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_TYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.DOKUMENT_LISTE_FOERSTESIDE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ENHETSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDETYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NAV_SKJEMA_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NETS_POSTBOKS;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.OVERSKRIFTSTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTSTED;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SPRAAKKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.TEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.VEDLEGG_LISTE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDE_OPPRETTET_AV;

import no.nav.dok.foerstesidegenerator.api.v1.Adresse;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsak;
import no.nav.dok.foerstesidegenerator.api.v1.Arkivsaksystem;
import no.nav.dok.foerstesidegenerator.api.v1.Avsender;
import no.nav.dok.foerstesidegenerator.api.v1.Bruker;
import no.nav.dok.foerstesidegenerator.api.v1.BrukerType;
import no.nav.dok.foerstesidegenerator.api.v1.Foerstesidetype;
import no.nav.dok.foerstesidegenerator.api.v1.PostFoerstesideRequest;
import no.nav.dok.foerstesidegenerator.api.v1.Spraakkode;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMetadata;
import no.nav.foerstesidegenerator.domain.code.MetadataConstants;

import java.util.Arrays;

public class TestUtils {

	public static final String ADR_LINJE_1 = "Vei 1";
	public static final String POSTNR = "0404";
	public static final String OSLO = "Oslo";

	public static final String NETS = "8899";

	public static final String AVSENDER = "avsenderId";
	public static final String NAVN = "Navn";

	public static final String BRUKER_ID = "14036609142";
	public static final String BRUKER_ID_INVALID = "00000000111";
	public static final String BRUKER_ID_10_DIGIT = "1403660914";
	public static final String BRUKER_PERSON = "PERSON";

	public static final String TEMA_FORELDREPENGER = "FOR";
	public static final String BEHANDLINGSTEMA_AB1337 = "ab1337";
	public static final String TITTEL = "tittel";
	public static final String SKJEMA_ID = "NAV 13.37";

	public static final String VEDLEGG_1 = "vedlegg1";
	public static final String VEDLEGG_2 = "vedlegg2";

	public static final String DOKUMENT_1 = "dokument 1";
	public static final String DOKUMENT_2 = "dokument 2";

	public static final String ENHET_9999 = "9999";

	public static final String SAK_REF = "saksRef";

	public static final String CONSUMER_ID = "consumer";

	public static PostFoerstesideRequest createRequestWithAdresse() {
		return PostFoerstesideRequest.builder()
				.adresse(Adresse.builder()
						.adresselinje1(ADR_LINJE_1)
						.adresselinje2(null)
						.adresselinje3(null)
						.postnummer(POSTNR)
						.poststed(OSLO).build())
				.netsPostboks(null)
				.avsender(Avsender.builder()
						.avsenderId(AVSENDER)
						.avsenderNavn(NAVN).build())
				.bruker(Bruker.builder()
						.brukerId(BRUKER_ID)
						.brukerType(BrukerType.PERSON).build())
				.ukjentBrukerPersoninfo(null)
				.tema(TEMA_FORELDREPENGER)
				.behandlingstema(BEHANDLINGSTEMA_AB1337)
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.enhetsnummer(ENHET_9999)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
						.arkivsaksnummer(SAK_REF).build())
				.build();
	}

	public static PostFoerstesideRequest createRequestWithFoerstesideTypeNav_Intern() {
		return PostFoerstesideRequest.builder()
				.adresse(Adresse.builder()
						.adresselinje1(ADR_LINJE_1)
						.adresselinje2(null)
						.adresselinje3(null)
						.postnummer(POSTNR)
						.poststed(OSLO).build())
				.netsPostboks(null)
				.avsender(Avsender.builder()
						.avsenderId(AVSENDER)
						.avsenderNavn(NAVN).build())
				.bruker(Bruker.builder()
						.brukerId(BRUKER_ID)
						.brukerType(BrukerType.PERSON).build())
				.ukjentBrukerPersoninfo(null)
				.tema(TEMA_FORELDREPENGER)
				.behandlingstema(BEHANDLINGSTEMA_AB1337)
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(Foerstesidetype.NAV_INTERN)
				.enhetsnummer(ENHET_9999)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
						.arkivsaksnummer(SAK_REF).build())
				.build();
	}

	private static PostFoerstesideRequest createRequestWithoutAdresse(String netspostboks, String ukjent, Foerstesidetype type, String tema) {
		return PostFoerstesideRequest.builder()
				.adresse(null)
				.netsPostboks(netspostboks)
				.avsender(Avsender.builder()
						.avsenderId(AVSENDER)
						.avsenderNavn(NAVN).build())
				.bruker(Bruker.builder()
						.brukerId(BRUKER_ID)
						.brukerType(BrukerType.PERSON).build())
				.ukjentBrukerPersoninfo(ukjent)
				.tema(tema)
				.behandlingstema(BEHANDLINGSTEMA_AB1337)
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(type)
				.enhetsnummer(ENHET_9999)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
						.arkivsaksnummer(SAK_REF).build())
				.build();
	}

	public static PostFoerstesideRequest createRequestWithNetsPostboks() {
		return createRequestWithoutAdresse(NETS, null, Foerstesidetype.SKJEMA, TEMA_FORELDREPENGER);
	}

	public static PostFoerstesideRequest createRequestWithoutBruker(String ukjentBrukerPersoninfo) {
		return PostFoerstesideRequest.builder()
				.adresse(null)
				.netsPostboks(NETS)
				.avsender(Avsender.builder()
						.avsenderId(AVSENDER)
						.avsenderNavn(NAVN).build())
				.bruker(null)
				.ukjentBrukerPersoninfo(ukjentBrukerPersoninfo)
				.tema(TEMA_FORELDREPENGER)
				.behandlingstema(BEHANDLINGSTEMA_AB1337)
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.enhetsnummer(ENHET_9999)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
						.arkivsaksnummer(SAK_REF).build())
				.build();
	}

	public static PostFoerstesideRequest createRequestWithoutAdresseAndNetsPostboks() {
		return createRequestWithoutAdresse(null, null, Foerstesidetype.SKJEMA, TEMA_FORELDREPENGER);
	}

	public static PostFoerstesideRequest createRequestWithAdresse(String adr1, String adr2, String adr3, String postnr, String poststed) {
		return PostFoerstesideRequest.builder()
				.adresse(Adresse.builder()
						.adresselinje1(adr1)
						.adresselinje2(adr2)
						.adresselinje3(adr3)
						.postnummer(postnr)
						.poststed(poststed).build())
				.netsPostboks(null)
				.bruker(Bruker.builder()
						.brukerId(BRUKER_ID)
						.brukerType(BrukerType.PERSON).build())
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.build();
	}

	public static PostFoerstesideRequest createRequestWithTema(String tema) {
		return createRequestWithoutAdresse(NETS, null, Foerstesidetype.SKJEMA, tema);

	}

	public static PostFoerstesideRequest createRequestEttersendelse() {
		return createRequestWithoutAdresse(NETS, null, Foerstesidetype.ETTERSENDELSE, TEMA_FORELDREPENGER);
	}

	public static PostFoerstesideRequest createRequestWithInvalidBrukerId(String brukerId) {
		return PostFoerstesideRequest.builder()
				.adresse(Adresse.builder()
						.adresselinje1(ADR_LINJE_1)
						.adresselinje2(null)
						.adresselinje3(null)
						.postnummer(POSTNR)
						.poststed(OSLO).build())
				.netsPostboks(null)
				.avsender(Avsender.builder()
						.avsenderId(AVSENDER)
						.avsenderNavn(NAVN).build())
				.bruker(Bruker.builder()
						.brukerId(brukerId)
						.brukerType(BrukerType.PERSON).build())
				.ukjentBrukerPersoninfo(null)
				.tema(TEMA_FORELDREPENGER)
				.behandlingstema(BEHANDLINGSTEMA_AB1337)
				.arkivtittel(TITTEL)
				.vedleggsliste(Arrays.asList(VEDLEGG_1, VEDLEGG_2))
				.navSkjemaId(SKJEMA_ID)
				.overskriftstittel(TITTEL)
				.dokumentlisteFoersteside(Arrays.asList(DOKUMENT_1, DOKUMENT_2))
				.spraakkode(Spraakkode.NB)
				.foerstesidetype(Foerstesidetype.SKJEMA)
				.enhetsnummer(ENHET_9999)
				.arkivsak(Arkivsak.builder()
						.arkivsaksystem(Arkivsaksystem.PSAK)
						.arkivsaksnummer(SAK_REF).build())
				.build();
	}

//	Domeneobjekt-metoder

	public static Foersteside createFoersteside(String loepenummer) {
		return createFoersteside(loepenummer, ADR_LINJE_1, POSTNR, OSLO, null, TEMA_FORELDREPENGER, null, AVSENDER_ID, NAVN, BRUKER_ID, BRUKER_PERSON);
	}

	public static Foersteside createFoersteside(String loepenummer, String netspostboks) {
		return createFoersteside(loepenummer, ADR_LINJE_1, POSTNR, OSLO, netspostboks, TEMA_FORELDREPENGER, null, AVSENDER_ID, NAVN, BRUKER_ID, BRUKER_PERSON);
	}

	public static Foersteside createFoerstesideWithoutAdresse(String loepenummer) {
		return createFoersteside(loepenummer, null, null, null, NETS, TEMA_FORELDREPENGER, null, AVSENDER_ID, NAVN, BRUKER_ID, BRUKER_PERSON);
	}

	public static Foersteside createFoerstesideWithoutAvsenderAndBruker(String loepenummer) {
		return createFoersteside(loepenummer, ADR_LINJE_1, POSTNR, OSLO, NETS, TEMA_FORELDREPENGER, null, null, null, null, null);
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
		createMetadata(foersteside, MetadataConstants.BRUKER_ID, brukerid);
		createMetadata(foersteside, BRUKER_TYPE, brukertype);
		createMetadata(foersteside, UKJENT_BRUKER_PERSONINFO, ukjent);
		createMetadata(foersteside, TEMA, tema);
		createMetadata(foersteside, BEHANDLINGSTEMA, BEHANDLINGSTEMA_AB1337);
		createMetadata(foersteside, ARKIVTITTEL, TITTEL);
		createMetadata(foersteside, NAV_SKJEMA_ID, SKJEMA_ID);
		createMetadata(foersteside, OVERSKRIFTSTITTEL, TITTEL);
		createMetadata(foersteside, SPRAAKKODE, Spraakkode.NB.name());
		createMetadata(foersteside, FOERSTESIDETYPE, Foerstesidetype.SKJEMA.name());
		createMetadata(foersteside, VEDLEGG_LISTE, VEDLEGG_1 + ";" + VEDLEGG_2);
		createMetadata(foersteside, DOKUMENT_LISTE_FOERSTESIDE, DOKUMENT_1 + ";" + DOKUMENT_2);
		createMetadata(foersteside, ENHETSNUMMER, ENHET_9999);
		createMetadata(foersteside, ARKIVSAKSYSTEM, Arkivsaksystem.PSAK.name());
		createMetadata(foersteside, ARKIVSAKSNUMMER, SAK_REF);
		createMetadata(foersteside, FOERSTESIDE_OPPRETTET_AV, CONSUMER_ID);
		return foersteside;
	}

	private static void createMetadata(Foersteside foersteside, String key, String value) {
		if (value != null) {
			foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, key, value));
		}
	}
}
