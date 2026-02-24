package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.config.RepositoryConfig;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.domain.FoerstesideMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TestTransaction;

import java.time.LocalDateTime;
import java.util.List;

import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.itest.AbstractIT.ELDRE_ENN_6_MAANEDER;
import static no.nav.foerstesidegenerator.itest.AbstractIT.NYERE_ENN_6_MAANEDER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = {RepositoryConfig.class})
@ActiveProfiles("itest")
class FoerstesideRepositoryTest {

	@Autowired
	private FoerstesideRepository foerstesideRepository;

	@Test
	public void skalMaskereBrukerIdIFoerstesideMetadata() {
		var eldreEnn6MaanederMedBrukerId = lagFoerstesideMedMetadata("1123", ELDRE_ENN_6_MAANEDER, BRUKER_ID, "01234567890");
		var eldreEnn6MaanederUtenBrukerId = lagFoerstesideUtenMetadata("1234", ELDRE_ENN_6_MAANEDER);
		var nyereEnn6MaanederMedBrukerId = lagFoerstesideMedMetadata("1345", NYERE_ENN_6_MAANEDER, BRUKER_ID, "01234567890");
		var nyereEnn6MaanederUtenBrukerId = lagFoerstesideUtenMetadata("1456", NYERE_ENN_6_MAANEDER);

		foerstesideRepository.saveAll(List.of(eldreEnn6MaanederMedBrukerId, eldreEnn6MaanederUtenBrukerId, nyereEnn6MaanederMedBrukerId, nyereEnn6MaanederUtenBrukerId));
		commitAndBeginNewTransaction();

		int antallOppdatert = foerstesideRepository.maskerFoerstesideMetadataByKey(BRUKER_ID);

		assertThat(antallOppdatert).isEqualTo(1);
		assertThat(foerstesideRepository.findByLoepenummer("1123").get().getBrukerId()).isNull();
	}

	@Test
	public void skalMaskereUkjentBrukerPersoninfoIFoerstesideMetadata() {
		var eldreEnn6MaanederMedUkjentBrukerPersoninfo = lagFoerstesideMedMetadata("2123", ELDRE_ENN_6_MAANEDER, UKJENT_BRUKER_PERSONINFO, "Anders And");
		var eldreEnn6MaanederUtenUkjentBrukerPersoninfo = lagFoerstesideUtenMetadata("2234", ELDRE_ENN_6_MAANEDER);
		var nyereEnn6MaanederMedUkjentBrukerPersoninfo = lagFoerstesideMedMetadata("2345", NYERE_ENN_6_MAANEDER, UKJENT_BRUKER_PERSONINFO, "Anders And");
		var nyereEnn6MaanederUtenUkjentBrukerPersoninfo = lagFoerstesideUtenMetadata("2456", NYERE_ENN_6_MAANEDER);

		foerstesideRepository.saveAll(List.of(eldreEnn6MaanederMedUkjentBrukerPersoninfo, eldreEnn6MaanederUtenUkjentBrukerPersoninfo,
				nyereEnn6MaanederMedUkjentBrukerPersoninfo, nyereEnn6MaanederUtenUkjentBrukerPersoninfo));
		commitAndBeginNewTransaction();

		int antallOppdatert = foerstesideRepository.maskerFoerstesideMetadataByKey(UKJENT_BRUKER_PERSONINFO);

		assertThat(antallOppdatert).isEqualTo(1);
		assertThat(foerstesideRepository.findByLoepenummer("2123").get().getBrukerId()).isNull();
	}

	private Foersteside lagFoerstesideUtenMetadata(String loepenummer, LocalDateTime datoOpprettet) {
		var foersteside = new Foersteside();
		foersteside.setLoepenummer(loepenummer);
		foersteside.setUthentet(0);
		foersteside.setDatoOpprettet(datoOpprettet);
		foersteside.setDatoUthentet(null);

		return foersteside;
	}

	private Foersteside lagFoerstesideMedMetadata(String loepenummer, LocalDateTime datoOpprettet, String metadataKey, String metadataValue) {
		var foersteside = new Foersteside();
		foersteside.setLoepenummer(loepenummer);
		foersteside.setUthentet(0);
		foersteside.setDatoOpprettet(datoOpprettet);
		foersteside.setDatoUthentet(null);

		var metadata = new FoerstesideMetadata(foersteside, metadataKey, metadataValue);
		foersteside.addFoerstesideMetadata(metadata);

		return foersteside;
	}

	private static void commitAndBeginNewTransaction() {
		TestTransaction.flagForCommit();
		TestTransaction.end();
		TestTransaction.start();
	}

}