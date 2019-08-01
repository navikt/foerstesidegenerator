package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.domain.FoerstesideCounter;
import no.nav.foerstesidegenerator.repository.FoerstesideCounterRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class FoerstesideCounterService {

    private final FoerstesideCounterRepository repository;

    @Inject
    public FoerstesideCounterService(final FoerstesideCounterRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("squid:S00108")
    String hentLoepenummer() {
        FoerstesideCounter currentCounter = repository.getCounterForToday();
        if (currentCounter == null) {
            FoerstesideCounter save = new FoerstesideCounter();
            log.info("Fant ingen FoerstesideCounter for {}. Lager ny!", save.getDate());
            try {
                repository.saveAndFlush(save);
            }
            catch (DataIntegrityViolationException e) {
                log.warn("FoerstesideCounter finnes allerede for dato "+ save.getDate());
            }
        }
        while(true) {
            try {
                FoerstesideCounter existingCounter = repository.getCounterForToday();
                existingCounter.count();
                existingCounter = repository.saveAndFlush(existingCounter);
                return existingCounter.generateLoepenummer();
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn(e.getMessage());
                log.warn("Tråd {} venter på tur", Thread.currentThread().getId());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) { }
                repository.flush();
            } catch (Exception e) {
                log.error("Ukjent feil!");
                log.error(e.getMessage(), e);
            }
        }
    }
}
