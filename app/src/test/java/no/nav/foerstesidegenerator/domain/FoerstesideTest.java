package no.nav.foerstesidegenerator.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.AVSENDER_ID_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

class FoerstesideTest {

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"HPRNR", "ORGNR", "FNR"})
	void shouldGetAvsenderIdTypeFromMetadata(String avsenderIdType) {
		var foersteside = new Foersteside();
		foersteside.addFoerstesideMetadata(new FoerstesideMetadata(foersteside, AVSENDER_ID_TYPE, avsenderIdType));

		assertThat(foersteside.getAvsenderIdType()).isEqualTo(avsenderIdType);
	}

	@Test
	void shouldReturnNullWhenAvsenderIdTypeMetadataIsMissing() {
		var foersteside = new Foersteside();

		assertThat(foersteside.getAvsenderIdType()).isNull();
	}
}
