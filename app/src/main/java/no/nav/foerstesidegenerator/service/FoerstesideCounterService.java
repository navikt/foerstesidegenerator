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

    String hentLoepenummer() {
        FoerstesideCounter currentCounter = repository.getCounterForToday();
        if (currentCounter == null) {
            FoerstesideCounter save = new FoerstesideCounter();
            log.info("No FoerstesideCounter found, making new for date "+ save.getDate());
            try {
                repository.save(save);
            }
            catch (DataIntegrityViolationException e) {
                log.warn("FoerstesideCounter already exists for date "+ save.getDate());
            }
        }
        while(true) {
            try {
                currentCounter = repository.getCounterForToday();
                currentCounter.count();
                repository.save(currentCounter);
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn(e.getMessage());
                log.warn("Thread {} racing, trying again", Thread.currentThread().getId());
            }
        }
        return currentCounter.generateLoepenummer();
    }
}
