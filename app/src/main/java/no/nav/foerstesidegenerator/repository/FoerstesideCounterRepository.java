package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.domain.FoerstesideCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FoerstesideCounterRepository extends JpaRepository<FoerstesideCounter, Long> {
    @Query(value =
            "select * " +
                    "FROM " + FoerstesideCounter.TABLE_NAME + " " +
                    "WHERE DATO = to_char(sysdate, 'YYYYMMDD') FOR UPDATE ", nativeQuery = true)
    @Transactional
    FoerstesideCounter getCounterForToday();
}
