package no.nav.foerstesidegenerator.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = FoerstesideMetadata.TABLE_NAME)
public class FoerstesideMetadata {

	public static final String TABLE_NAME = "FOERSTESIDE_METADATA";
	private static final String SEQUENCE_NAME = TABLE_NAME + "_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
	@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
	@Column(name = "foersteside_metadata_id", unique = true, nullable = false, updatable = false)
	private Long foerstesideMetadataId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foersteside_id", nullable = false)
	private Foersteside foersteside;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

	public FoerstesideMetadata() {
		// for hibernate
	}

	public FoerstesideMetadata(Foersteside foersteside, String key, String value) {
		this.foersteside = foersteside;
		this.key = key;
		this.value = value;
	}

	public Long getFoerstesideMetadataId() {
		return foerstesideMetadataId;
	}

	public Foersteside getFoersteside() {
		return foersteside;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
