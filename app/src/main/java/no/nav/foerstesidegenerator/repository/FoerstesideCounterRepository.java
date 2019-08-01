package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.domain.FoerstesideCounter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoerstesideCounterRepository extends CrudRepository<FoerstesideCounter, Long> {
    @Query(value =
            "select * " +
                    "FROM " + FoerstesideCounter.TABLE_NAME + " " +
                    "WHERE DATO = to_char(sysdate, 'YYYYMMDD')", nativeQuery = true)
    FoerstesideCounter getCounterForToday();
}
