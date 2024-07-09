package no.nav.foerstesidegenerator.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Table(name = "FOERSTESIDE_METADATA")
public class FoerstesideMetadata {

	private static final String SEQUENCE_NAME = "FOERSTESIDE_METADATA_SEQ";

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
	@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
	@Column(name = "foersteside_metadata_id", unique = true, nullable = false, updatable = false)
	private Long foerstesideMetadataId;

	@ManyToOne(fetch = LAZY)
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

	public void setValue(String value) {
		this.value = value;
	}
}
