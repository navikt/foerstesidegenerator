package no.nav.foerstesidegenerator.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = Foersteside.TABLE_NAME)
public class Foersteside {

	public static final String TABLE_NAME = "FOERSTESIDE";
	private static final String SEQUENCE_NAME = TABLE_NAME + "_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
	@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 10)
	@Column(name = "foersteside_id", nullable = false, updatable = false, unique = true)
	private Long foerstesideId;

	@Column(name = "loepenummer", nullable = false, updatable = false)
	private Long loepenummer;

	@Column(name = "dato_opprettet", nullable = false, updatable = false)
	private LocalDateTime datoOpprettet;

	@Column(name = "uthentet", nullable = false, updatable = true)
	private Boolean uthentet;

	@Column(name = "dato_uthentet", nullable = true, updatable = true)
	private LocalDateTime datoUthentet;

	@OneToMany(mappedBy = "foersteside")
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE, CascadeType.DELETE, CascadeType.DETACH})
	private final Set<FoerstesideMetadata> foerstesideMetadata = new HashSet<>();

	public Long getFoerstesideId() {
		return foerstesideId;
	}

	public Long getLoepenummer() {
		return loepenummer;
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
		return foerstesideMetadata;
	}
}
