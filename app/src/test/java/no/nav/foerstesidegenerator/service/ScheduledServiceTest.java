package no.nav.foerstesidegenerator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import no.nav.foerstesidegenerator.TestUtils;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
class ScheduledServiceTest {

	private Foersteside foerstesideToMask = TestUtils.createFoersteside("1");
	private Foersteside foerstesideToMaskOld = TestUtils.createFoersteside("2");
	private Foersteside foersteside3MonthsPlus1Minute = TestUtils.createFoersteside("3");
	private Foersteside foerstesideNow = TestUtils.createFoersteside("4");

	@Mock
	private FoerstesideRepository repository;

	@InjectMocks
	private ScheduledService scheduledService;

	@BeforeEach
	void setUp() {
		LocalDateTime now = LocalDateTime.now();
		foerstesideToMask.setDatoOpprettet(now.minusMonths(3).minusSeconds(1));
		foerstesideToMask.setUthentet(0);

		foerstesideToMaskOld.setDatoOpprettet(now.minusYears(1));
		foerstesideToMaskOld.setUthentet(0);

		foersteside3MonthsPlus1Minute.setDatoOpprettet(now.minusMonths(3).plusSeconds(1));
		foersteside3MonthsPlus1Minute.setUthentet(0);

		foerstesideNow.setDatoOpprettet(now);
		foerstesideNow.setUthentet(0);

		when(repository.findFoerstesiderDueForMaskeringBrukerId()).thenReturn(Arrays.asList(foerstesideToMask, foerstesideToMaskOld));
		when(repository.findAll()).thenReturn(Arrays.asList(foerstesideToMask, foerstesideToMaskOld, foersteside3MonthsPlus1Minute, foerstesideNow));
	}

	@Test
	void shouldMaskBrukerIdForOldFoerstesider() {
		scheduledService.execute();

		Iterable<Foersteside> all = repository.findAll();

		AtomicInteger numberOfMaskedFoerstesider = new AtomicInteger();
		all.iterator().forEachRemaining(foersteside -> {
			if (foersteside.getBrukerId() == null) {
				numberOfMaskedFoerstesider.incrementAndGet();
			}
		});

		assertEquals(2, numberOfMaskedFoerstesider.get());
	}
}