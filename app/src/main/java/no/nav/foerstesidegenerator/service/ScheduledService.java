package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.BRUKER_ID;
import static no.nav.foerstesidegenerator.domain.code.MetadataConstants.UKJENT_BRUKER_PERSONINFO;

@Slf4j
@Component
@EnableScheduling
public class ScheduledService {

	private final FoerstesideRepository foerstesideRepository;

	public ScheduledService(final FoerstesideRepository foerstesideRepository) {
		this.foerstesideRepository = foerstesideRepository;
	}

	@Transactional
	@Scheduled(cron = "${maskering.fnr.rate}")
	public void execute() {
		log.info("Starter automatisk jobb for maskering av førstesidemetadata");

		masker(BRUKER_ID);
		masker(UKJENT_BRUKER_PERSONINFO);

		log.info("Avslutter automatisk jobb for maskering av førstesidemetadata");
	}

	private void masker(String metadataKey) {
		int antallOppdatert = foerstesideRepository.maskerFoerstesideMetadataByKey(metadataKey);
		log.info("Foerstesidegenerator - schedulert jobb: Har maskert {} på antall={} førstesider", metadataKey, antallOppdatert);
	}
}
