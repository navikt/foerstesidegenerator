package no.nav.foerstesidegenerator.consumer.metaforce;

import static net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isNotBlank;

import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.exception.FoerstesideGeneratorFunctionalException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

public class MetaforceMapper {

	private Document document;

	public Document map(Foersteside domain) {
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();

			Element brevdata = document.createElement("brevdata");

			Element fag = document.createElement("fag");
			brevdata.appendChild(fag);

			addSpraakkode(fag, domain.getSpraakkode());
			addAdresse(fag, domain);
			// TODO: HVIS adresse => sett default NETS-postboks til 8888
			addNetsPostboks(fag, domain.getNetsPostboks());
			addBruker(fag, domain);
			addUkjentBrukerPersoninfo(fag, domain.getUkjentBrukerPersoninfo());
			addArkivtittel(fag, domain.getArkivtittel());
			addOverskriftstittel(fag, domain.getOverskriftstittel());
			addFoerstesideType(fag, domain.getFoerstesidetype());
			addVedleggListe(fag, domain.getVedleggListeAsList());
			addLoepenummer(fag, domain.getLoepenummer());

			document.appendChild(brevdata);
			return document;
		} catch (ParserConfigurationException e) {
			throw new FoerstesideGeneratorFunctionalException("mapping feil");
		}
	}

	private void addSpraakkode(Element fag, String spraak) {
		Element spraakkode = document.createElement("spraakkode");
		spraakkode.setTextContent(spraak);
		fag.appendChild(spraakkode);
	}

	private void addAdresse(Element fag, Foersteside domain) {
		if (isNotBlank(domain.getAdresselinje1()) && isNotBlank(domain.getPostnummer()) && isNotBlank(domain.getPoststed())) {
			Element adresse = document.createElement("adresse");

			Element adresselinje1 = document.createElement("adresselinje1");
			adresselinje1.setTextContent(domain.getAdresselinje1());
			adresse.appendChild(adresselinje1);

			Element adresselinje2 = document.createElement("adresselinje2");
			adresselinje2.setTextContent(domain.getAdresselinje2());
			adresse.appendChild(adresselinje2);

			Element adresselinje3 = document.createElement("adresselinje3");
			adresselinje3.setTextContent(domain.getAdresselinje3());
			adresse.appendChild(adresselinje3);

			Element postNr = document.createElement("postNr");
			postNr.setTextContent(domain.getPostnummer());
			adresse.appendChild(postNr);

			Element poststed = document.createElement("poststed");
			poststed.setTextContent(domain.getPoststed());
			adresse.appendChild(poststed);

			fag.appendChild(adresse);
		}
	}

	private void addNetsPostboks(Element fag, String nets) {
		if (isNotBlank(nets)) {
			Element netsPostboks = document.createElement("NETS-postboks");
			netsPostboks.setTextContent(nets);
			fag.appendChild(netsPostboks);
		}
	}

	private void addBruker(Element fag, Foersteside domain) {
		if (isNotBlank(domain.getBrukerId()) && isNotBlank(domain.getBrukerType())) {
			Element bruker = document.createElement("bruker");

			Element brukerID = document.createElement("brukerID");
			brukerID.setTextContent(domain.getBrukerId());
			bruker.appendChild(brukerID);
			// TODO: brukerType ??

			fag.appendChild(bruker);
		}
	}

	private void addUkjentBrukerPersoninfo(Element fag, String ukjentbruker) {
		if (isNotBlank(ukjentbruker)) {
			Element ukjentBrukerPersoninfo = document.createElement("ukjentBrukerPersoninfo");
			ukjentBrukerPersoninfo.setTextContent(ukjentbruker);
			fag.appendChild(ukjentBrukerPersoninfo);
		}
	}

	private void addArkivtittel(Element fag, String arkiv) {
		if (isNotBlank(arkiv)) {
			Element arkivtittel = document.createElement("arkivtittel");
			arkivtittel.setTextContent(arkiv);
			fag.appendChild(arkivtittel);
		}
	}

	private void addOverskriftstittel(Element fag, String overskrift) {
		if (isNotBlank(overskrift)) {
			Element overskriftstittel = document.createElement("overskriftstittel");
			overskriftstittel.setTextContent(overskrift);
			fag.appendChild(overskriftstittel);
		}
	}

	private void addFoerstesideType(Element fag, String type) {
		Element foerstesideType = document.createElement("foerstesideType");
		foerstesideType.setTextContent(type);
		fag.appendChild(foerstesideType);
	}

	private void addLoepenummer(Element fag, String loepenummer) {
		Element loepenummerElement = document.createElement("loepenummer");
		loepenummerElement.setTextContent(loepenummer);
		fag.appendChild(loepenummerElement);
	}

	private void addVedleggListe(Element fag, List<String> vedleggTittelList) {
		if (!vedleggTittelList.isEmpty()) {
			Element vedleggListe = document.createElement("vedleggListe");
			vedleggTittelList.forEach(vedlegg -> {
				Element vedleggElement = document.createElement("vedlegg");
				Element tittel = document.createElement("tittel");
				tittel.setTextContent(vedlegg);
				vedleggElement.appendChild(tittel);
				vedleggListe.appendChild(vedleggElement);
			});
			fag.appendChild(vedleggListe);
		}
	}
}
