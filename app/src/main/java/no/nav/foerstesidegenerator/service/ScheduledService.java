package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.domain.Foersteside;
import no.nav.foerstesidegenerator.repository.FoerstesideRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

		List<Foersteside> foerstesiderDerBrukerIdSkalMaskeres = foerstesideRepository.finnFoerstesiderSomSkalMaskeres(BRUKER_ID);

		if (!foerstesiderDerBrukerIdSkalMaskeres.isEmpty()) {
			foerstesiderDerBrukerIdSkalMaskeres.forEach(Foersteside::clearBrukerId);
			log.info("Foerstesidegenerator - schedulert jobb: Har maskert brukerId-ene på {} førstesider", foerstesiderDerBrukerIdSkalMaskeres.size());
		}

		List<Foersteside> foerstesiderDerUkjentBrukerPersoninfoSkalMaskeres = foerstesideRepository.finnFoerstesiderSomSkalMaskeres(UKJENT_BRUKER_PERSONINFO);

		if (!foerstesiderDerUkjentBrukerPersoninfoSkalMaskeres.isEmpty()) {
			foerstesiderDerUkjentBrukerPersoninfoSkalMaskeres.forEach(Foersteside::clearUkjentBrukerPersoninfo);
			log.info("Foerstesidegenerator - schedulert jobb: Har maskert ukjent brukerinfo på {} førstesider", foerstesiderDerUkjentBrukerPersoninfoSkalMaskeres.size());
		}

		log.info("Avslutter automatisk jobb for maskering av førstesidemetadata");
	}
}
