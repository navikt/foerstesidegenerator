package no.nav.foerstesidegenerator.domain;

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
import static org.springframework.util.StringUtils.delimitedListToStringArray;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = Foersteside.TABLE_NAME)
public class Foersteside {

	static final String TABLE_NAME = "FOERSTESIDE";
	private static final String SEQUENCE_NAME = TABLE_NAME + "_SEQ";
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "foersteside")
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE, CascadeType.DETACH})
	private final Set<FoerstesideMetadata> foerstesideMetadata = new HashSet<>();
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
	@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
	@Column(name = "foersteside_id", unique = true, nullable = false, updatable = false)
	private Long foerstesideId;
	@Column(name = "loepenummer", nullable = false, updatable = false)
	private String loepenummer;
	@Column(name = "dato_opprettet", nullable = false, updatable = false)
	private LocalDateTime datoOpprettet;
	@Column(name = "uthentet", nullable = false, updatable = true)
	private Boolean uthentet;
	@Column(name = "dato_uthentet", nullable = true, updatable = true)
	private LocalDateTime datoUthentet;

	public Long getFoerstesideId() {
		return foerstesideId;
	}

	public String getLoepenummer() {
		return loepenummer;
	}

	public void setLoepenummer(String loepenummer) {
		this.loepenummer = loepenummer;
	}

	public LocalDateTime getDatoOpprettet() {
		return datoOpprettet;
	}

	public void setDatoOpprettet(LocalDateTime datoOpprettet) {
		this.datoOpprettet = datoOpprettet;
	}

	public Boolean getUthentet() {
		return uthentet;
	}

	public void setUthentet(Boolean uthentet) {
		this.uthentet = uthentet;
	}

	public LocalDateTime getDatoUthentet() {
		return datoUthentet;
	}

	public void setDatoUthentet(LocalDateTime datoUthentet) {
		this.datoUthentet = datoUthentet;
	}

	public Set<FoerstesideMetadata> getFoerstesideMetadata() {
		return Collections.unmodifiableSet(foerstesideMetadata);
	}

	public void addFoerstesideMetadata(FoerstesideMetadata metadata) {
		if (metadata != null) {
			foerstesideMetadata.add(metadata);
		}
	}

	// some helpers:

	private String getValueForKey(String key) {
		return foerstesideMetadata.stream().filter(a -> a.getKey().equals(key)).findFirst().map(FoerstesideMetadata::getValue).orElse(null);
	}

	private void setValueForKey(String key, String value) {
		foerstesideMetadata.stream().filter(a -> a.getKey().equals(key)).findFirst().ifPresent(metadata -> metadata.setValue(value));
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

	public List<String> getVedleggListeAsList() {
		String[] vedleggStringArray = delimitedListToStringArray(getVedleggListe(), ";");
		return vedleggStringArray.length == 0 ? Collections.emptyList() : Arrays.asList(vedleggStringArray);
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

	public void clearBrukerId() {
		setValueForKey(BRUKER_ID, null);
	}

	public void clearUkjentBrukerPersoninfo() {
		setValueForKey(UKJENT_BRUKER_PERSONINFO, null);
	}

}
