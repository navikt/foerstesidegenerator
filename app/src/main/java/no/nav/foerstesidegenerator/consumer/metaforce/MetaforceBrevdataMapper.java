package no.nav.foerstesidegenerator.consumer.metaforce;

import static net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isBlank;
import static net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isNotBlank;

import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.xml.jaxb.gen.AdresseType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrevdataType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrukerType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.BrukertypeType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.FagType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.FoerstesideTypeKode;
import no.nav.foerstesidegenerator.xml.jaxb.gen.SpraakKode;
import no.nav.foerstesidegenerator.xml.jaxb.gen.VedleggListeType;
import no.nav.foerstesidegenerator.xml.jaxb.gen.VedleggType;

import java.util.List;

public class MetaforceBrevdataMapper {

	public static final String DEFAULT_NETS_POSTBOKS = "8888";

	private static final String ALPHABET_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%";

	public BrevdataType map(Foersteside domain) {
		BrevdataType brevdata = new BrevdataType();

		FagType fag = new FagType();
		fag.setSpraakkode(SpraakKode.valueOf(domain.getSpraakkode()));
		mapAdresse(fag, domain);
		// HVIS adresse er satt OG netsPostboks er null => sett default NETS-postboks til 8888
		addNetsPostboks(fag, domain.getNetsPostboks());
		addBruker(fag, domain);
		fag.setUkjentBrukerPersoninfo(domain.getUkjentBrukerPersoninfo());
		fag.setArkivtittel(domain.getArkivtittel());
		fag.setOverskriftstittel(domain.getOverskriftstittel());
		fag.setFoerstesideType(FoerstesideTypeKode.valueOf(domain.getFoerstesidetype()));
		addVedleggListe(fag, domain.getVedleggListeAsList());
		fag.setLøpenummer(domain.getLoepenummer());
		fag.setStrekkode2(generateStrekkode2(domain));

		brevdata.setFag(fag);

		return brevdata;

	}

	private void mapAdresse(FagType fag, Foersteside domain) {
		if (isNotBlank(domain.getAdresselinje1()) && isNotBlank(domain.getPostnummer()) && isNotBlank(domain.getPoststed())) {
			AdresseType adresse = new AdresseType();
			adresse.setAdresselinje1(domain.getAdresselinje1());
			adresse.setAdresselinje2(domain.getAdresselinje2());
			adresse.setAdresselinje3(domain.getAdresselinje3());
			adresse.setPostNr(domain.getPostnummer());
			adresse.setPoststed(domain.getPoststed());
			fag.setAdresse(adresse);
		}
	}

	private void addNetsPostboks(FagType fag, String netsPostboks) {
		if (isBlank(netsPostboks) && fag.getAdresse() != null) {
			fag.setNETSPostboks(DEFAULT_NETS_POSTBOKS);
		} else if (isNotBlank(netsPostboks)) {
			fag.setNETSPostboks(netsPostboks);
		}
	}

	private void addBruker(FagType fag, Foersteside domain) {
		if (isNotBlank(domain.getBrukerId()) && isNotBlank(domain.getBrukerType())) {
			BrukerType bruker = new BrukerType();
			bruker.setBrukerID(domain.getBrukerId());
			bruker.setBrukertype(BrukertypeType.valueOf(domain.getBrukerType()));
			fag.setBruker(bruker);
		}
	}

	private void addVedleggListe(FagType fag, List<String> vedleggList) {
		if (!vedleggList.isEmpty()) {
			VedleggListeType vedleggListeType = new VedleggListeType();
			vedleggList.forEach(s -> {
				VedleggType vedlegg = new VedleggType();
				vedlegg.setTittel(s);
				vedleggListeType.getVedlegg().add(vedlegg);
			});
			fag.setVedleggListe(vedleggListeType);
		}
	}

	private String generateStrekkode2(Foersteside foersteside) {
		long loepenummer = Long.parseLong(foersteside.getLoepenummer());
		long c1 = loepenummer % 10;

		String postboks = foersteside.getNetsPostboks() != null ? foersteside.getNetsPostboks() : DEFAULT_NETS_POSTBOKS;

		String res = foersteside.getLoepenummer() + c1 + postboks;

		int total = 0;
		for (int i = 0; i < res.length(); i++) {
			total += ALPHABET_STRING.indexOf(res.charAt(i));
		}
		char c2 = ALPHABET_STRING.charAt(total % 43);

		return "*" + res + c2 + "*";
	}

}
