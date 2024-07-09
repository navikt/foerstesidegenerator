package no.nav.foerstesidegenerator.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.SEQUENCE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_1;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_2;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ADRESSELINJE_3;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVSAKSYSTEM;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ARKIVTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_NAVN;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BEHANDLINGSTEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_TYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.DOKUMENT_LISTE_FOERSTESIDE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.ENHETSNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDETYPE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.FOERSTESIDE_OPPRETTET_AV;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NAV_SKJEMA_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.NETS_POSTBOKS;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.OVERSKRIFTSTITTEL;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTNUMMER;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.POSTSTED;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.SPRAAKKODE;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.TEMA;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.VEDLEGG_LISTE;
import static org.hibernate.annotations.CascadeType.DELETE;
import static org.hibernate.annotations.CascadeType.DETACH;
import static org.hibernate.annotations.CascadeType.MERGE;
import static org.hibernate.annotations.CascadeType.PERSIST;
import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;
import static org.springframework.util.StringUtils.delimitedListToStringArray;

@Entity
@Getter
@Table(name = "FOERSTESIDE")
public class Foersteside {

	private static final String SEQUENCE_NAME = "FOERSTESIDE_SEQ";
	private static final int MAX_COUNTING_UTHENTET = 9;

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
	@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
	@Column(name = "foersteside_id", unique = true, nullable = false, updatable = false)
	private Long foerstesideId;

	@Column(name = "loepenummer", nullable = false, updatable = false)
	private String loepenummer;

	@Column(name = "dato_opprettet", nullable = false, updatable = false)
	private LocalDateTime datoOpprettet;

	@Column(name = "uthentet", nullable = false)
	private int uthentet;

	@Column(name = "dato_uthentet")
	private LocalDateTime datoUthentet;

	@OneToMany(fetch = EAGER, mappedBy = "foersteside")
	@Cascade({PERSIST, MERGE, SAVE_UPDATE, DELETE, DETACH})
	private final Set<FoerstesideMetadata> foerstesideMetadata = new HashSet<>();

	public void setLoepenummer(String loepenummer) {
		this.loepenummer = loepenummer;
	}

	public void setDatoOpprettet(LocalDateTime datoOpprettet) {
		this.datoOpprettet = datoOpprettet;
	}

	public void setUthentet(int uthentet) {
		this.uthentet = uthentet;
	}

	public void incrementUthenting() {
		if (uthentet < MAX_COUNTING_UTHENTET) uthentet++;
	}

	public void setDatoUthentet(LocalDateTime datoUthentet) {
		this.datoUthentet = datoUthentet;
	}

	public void addFoerstesideMetadata(FoerstesideMetadata metadata) {
		if (metadata != null) {
			foerstesideMetadata.add(metadata);
		}
	}

	// some helpers:

	private String getValueForKey(String key) {
		return foerstesideMetadata.stream()
				.filter(a -> a.getKey().equals(key))
				.findFirst()
				.map(FoerstesideMetadata::getValue)
				.orElse(null);
	}

	private void setValueForKey(String key, String value) {
		foerstesideMetadata.stream()
				.filter(a -> a.getKey().equals(key))
				.findFirst()
				.ifPresent(metadata -> metadata.setValue(value));
	}

	public String getAdresselinje1() {
		return getValueForKey(ADRESSELINJE_1);
	}

	public String getAdresselinje2() {
		return getValueForKey(ADRESSELINJE_2);
	}

	public String getAdresselinje3() {
		return getValueForKey(ADRESSELINJE_3);
	}

	public String getPostnummer() {
		return getValueForKey(POSTNUMMER);
	}

	public String getPoststed() {
		return getValueForKey(POSTSTED);
	}

	public String getNetsPostboks() {
		return getValueForKey(NETS_POSTBOKS);
	}

	public String getAvsenderId() {
		return getValueForKey(AVSENDER_ID);
	}

	public String getAvsenderNavn() {
		return getValueForKey(AVSENDER_NAVN);
	}

	public String getBrukerId() {
		return getValueForKey(BRUKER_ID);
	}

	public String getBrukerType() {
		return getValueForKey(BRUKER_TYPE);
	}

	public String getUkjentBrukerPersoninfo() {
		return getValueForKey(UKJENT_BRUKER_PERSONINFO);
	}

	public String getTema() {
		return getValueForKey(TEMA);
	}

	public String getBehandlingstema() {
		return getValueForKey(BEHANDLINGSTEMA);
	}

	public String getArkivtittel() {
		return getValueForKey(ARKIVTITTEL);
	}

	public String getNavSkjemaId() {
		return getValueForKey(NAV_SKJEMA_ID);
	}

	public String getOverskriftstittel() {
		return getValueForKey(OVERSKRIFTSTITTEL);
	}

	public String getSpraakkode() {
		return getValueForKey(SPRAAKKODE);
	}

	public String getFoerstesidetype() {
		return getValueForKey(FOERSTESIDETYPE);
	}

	public String getVedleggListe() {
		return getValueForKey(VEDLEGG_LISTE);
	}

	public String getDokumentlisteFoersteside() {
		return getValueForKey(DOKUMENT_LISTE_FOERSTESIDE);
	}

	public List<String> getDokumentlisteFoerstesideAsList() {
		String[] dokumentlisteStringArray = delimitedListToStringArray(getDokumentlisteFoersteside(), ";");
		return dokumentlisteStringArray.length == 0 ? Collections.emptyList() : Arrays.asList(dokumentlisteStringArray);
	}

	public String getEnhetsnummer() {
		return getValueForKey(ENHETSNUMMER);
	}

	public String getArkivsaksystem() {
		return getValueForKey(ARKIVSAKSYSTEM);
	}

	public String getArkivsaksnummer() {
		return getValueForKey(ARKIVSAKSNUMMER);
	}

	public String getFoerstesideOpprettetAv() {
		return getValueForKey(FOERSTESIDE_OPPRETTET_AV);
	}

	public void clearBrukerId() {
		setValueForKey(BRUKER_ID, null);
	}

	public void clearUkjentBrukerPersoninfo() {
		setValueForKey(UKJENT_BRUKER_PERSONINFO, null);
	}

}
