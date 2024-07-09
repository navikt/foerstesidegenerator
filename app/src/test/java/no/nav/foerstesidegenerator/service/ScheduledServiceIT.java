package no.nav.foerstesidegenerator.service;

import no.nav.foerstesidegenerator.itest.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.time.LocalDateTime.now;
import static no.nav.foerstesidegenerator.TestUtils.BRUKER_ID;
import static no.nav.foerstesidegenerator.TestUtils.UKJENT_BRUKER_PERSONINFO;
import static no.nav.foerstesidegenerator.TestUtils.createFoersteside;
import static no.nav.foerstesidegenerator.TestUtils.createFoerstesideWithUkjent;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduledServiceIT extends AbstractIT {

	@Autowired
	ScheduledService scheduledService;

	@Test
	void skalSensurereBrukerId() {
		var foerstesideSomSkalMaskeres = createFoersteside("1");
		foerstesideSomSkalMaskeres.setDatoOpprettet(now().minusMonths(7));
		var foerstesideSomIkkeSkalMaskeres = createFoersteside("2");
		foerstesideSomIkkeSkalMaskeres.setDatoOpprettet(now().minusMonths(5));

		foerstesideRepository.saveAll(List.of(foerstesideSomSkalMaskeres, foerstesideSomIkkeSkalMaskeres));
		commitAndBeginNewTransaction();

		scheduledService.execute();

		assertThat(foerstesideRepository.findByLoepenummer("1").get().getBrukerId()).isNull();
		assertThat(foerstesideRepository.findByLoepenummer("2").get().getBrukerId()).isEqualTo(BRUKER_ID);
	}

	@Test
	void skalSensurereUkjentBrukerPersoninfo() {
		var foerstesideSomSkalMaskeres = createFoerstesideWithUkjent("1");
		foerstesideSomSkalMaskeres.setDatoOpprettet(now().minusMonths(7));
		var foerstesideSomIkkeSkalMaskeres = createFoerstesideWithUkjent("2");
		foerstesideSomIkkeSkalMaskeres.setDatoOpprettet(now().minusMonths(5));

		foerstesideRepository.saveAll(List.of(foerstesideSomSkalMaskeres, foerstesideSomIkkeSkalMaskeres));
		commitAndBeginNewTransaction();

		scheduledService.execute();

		assertThat(foerstesideRepository.findByLoepenummer("1").get().getUkjentBrukerPersoninfo()).isNull();
		assertThat(foerstesideRepository.findByLoepenummer("2").get().getUkjentBrukerPersoninfo()).isEqualTo(UKJENT_BRUKER_PERSONINFO);
	}
}