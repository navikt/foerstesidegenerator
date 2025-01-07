package no.nav.foerstesidegenerator.service;

import no.nav.foerstesidegenerator.domain.FoerstesideMetadata;
import no.nav.foerstesidegenerator.itest.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID_PERSON;
import static no.nav.foerstesidegenerator.TestUtils.UKJENT_BRUKER_PERSONINFO_PERSON;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithUkjent;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduledServiceIT extends AbstractIT {

	private static final Set<String> KEYS_MASKERES = Set.of(BRUKER_ID, UKJENT_BRUKER_PERSONINFO);

	@Autowired
	ScheduledService scheduledService;

	@Test
	void skalMaskereBrukerId() {
		stubLeaderElection();
		var foerstesideSomSkalMaskeres = createFoersteside("1");
		foerstesideSomSkalMaskeres.setDatoOpprettet(ELDRE_ENN_6_MAANEDER);
		var foerstesideSomIkkeSkalMaskeres = createFoersteside("2");
		foerstesideSomIkkeSkalMaskeres.setDatoOpprettet(NYERE_ENN_6_MAANEDER);

		foerstesideRepository.saveAll(List.of(foerstesideSomSkalMaskeres, foerstesideSomIkkeSkalMaskeres));
		commitAndBeginNewTransaction();

		scheduledService.execute();

		assertThat(foerstesideRepository.findByLoepenummer("1").get().getBrukerId()).isNull();
		List<FoerstesideMetadata> foerstesideMetadata = hentFoerstesidemetadataUtenomMaskerteKeys("1");
		assertThat(foerstesideMetadata.size()).isEqualTo(19);
		assertThat(foerstesideMetadata.stream().map(FoerstesideMetadata::getValue)).doesNotContainNull();
		assertThat(foerstesideRepository.findByLoepenummer("2").get().getBrukerId()).isEqualTo(BRUKER_ID_PERSON);
	}

	@Test
	void skalMaskereUkjentBrukerPersoninfo() {
		stubLeaderElection();
		var foerstesideSomSkalMaskeres = createFoerstesideWithUkjent("3");
		foerstesideSomSkalMaskeres.setDatoOpprettet(ELDRE_ENN_6_MAANEDER);
		var foerstesideSomIkkeSkalMaskeres = createFoerstesideWithUkjent("4");
		foerstesideSomIkkeSkalMaskeres.setDatoOpprettet(NYERE_ENN_6_MAANEDER);

		foerstesideRepository.saveAll(List.of(foerstesideSomSkalMaskeres, foerstesideSomIkkeSkalMaskeres));
		commitAndBeginNewTransaction();

		scheduledService.execute();

		assertThat(foerstesideRepository.findByLoepenummer("3").get().getUkjentBrukerPersoninfo()).isNull();
		List<FoerstesideMetadata> foerstesideMetadata = hentFoerstesidemetadataUtenomMaskerteKeys("3");
		assertThat(foerstesideMetadata.size()).isEqualTo(19);
		assertThat(foerstesideMetadata.stream().map(FoerstesideMetadata::getValue)).doesNotContainNull();
		assertThat(foerstesideRepository.findByLoepenummer("4").get().getUkjentBrukerPersoninfo()).isEqualTo(UKJENT_BRUKER_PERSONINFO_PERSON);
	}

	private List<FoerstesideMetadata> hentFoerstesidemetadataUtenomMaskerteKeys(String loepenummer) {
		return foerstesideRepository.findByLoepenummer(loepenummer).get().getFoerstesideMetadata()
				.stream().filter(fm -> !KEYS_MASKERES.contains(fm.getKey())).toList();
	}
}