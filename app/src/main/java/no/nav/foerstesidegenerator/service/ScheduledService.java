package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class ScheduledService {

	private final FoerstesideRepository foerstesideRepository;

	@Inject
	public ScheduledService(final FoerstesideRepository foerstesideRepository) {
		this.foerstesideRepository = foerstesideRepository;
	}

	@Transactional
	@Scheduled(cron = "${maskering.fnr.rate}")
	public void execute() {
		List<Foersteside> foerstesiderDueForMaskering = foerstesideRepository.findFoerstesiderDueForMaskering();

		if (!foerstesiderDueForMaskering.isEmpty()) {
			foerstesiderDueForMaskering.forEach(Foersteside::clearBrukerId);
			log.info("Foerstesidegenerator - schedulert jobb: Har maskert {} brukerIder", foerstesiderDueForMaskering.size());
		}
	}
}
