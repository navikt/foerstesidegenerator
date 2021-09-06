package no.nav.foerstesidegenerator.service;

import lombok.extern.slf4j.Slf4j;
import no.nav.foerstesidegenerator.domain.FoerstesideCounter;
import no.nav.foerstesidegenerator.repository.FoerstesideCounterRepository;
import org.hibernate.exception.ConstraintViolationException;
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

    @SuppressWarnings("squid:S2142")
    String hentLoepenummer() {
        FoerstesideCounter currentCounter = repository.getCounterForToday();
        if (currentCounter == null) {
            FoerstesideCounter save = new FoerstesideCounter();
            log.info("Fant ingen FoerstesideCounter for {}. Lager ny!", save.getDate());
            try {
                repository.saveAndFlush(save);
            } catch (DataIntegrityViolationException e) {
                log.warn("FoerstesideCounter finnes allerede for dato {}", save.getDate());
            }
        }
        while(true) {
            try {
                FoerstesideCounter existingCounter = repository.getCounterForToday();
                existingCounter.count();
                existingCounter = repository.saveAndFlush(existingCounter);
                return existingCounter.generateLoepenummer();
            } catch (ObjectOptimisticLockingFailureException lockingException) {
                log.warn(lockingException.getMessage());
                log.warn("Tråd {} venter på tur", Thread.currentThread().getId());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    log.warn("Fikk ikke sove! " + interruptedException.getMessage());
                }
                repository.flush();
            } catch (ConstraintViolationException e) {
                log.error("En annen tråd har laget en ny counter som denne tråden ikke får benyttet: " + e.getMessage());
                throw e;
            } catch (Exception unknownException) {
                log.error("Ukjent feil: " + unknownException.getMessage());
            }
        }
    }
}
