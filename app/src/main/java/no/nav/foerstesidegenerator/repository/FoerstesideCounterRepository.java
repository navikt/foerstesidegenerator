package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.domain.FoerstesideCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Repository
public interface FoerstesideCounterRepository extends JpaRepository<FoerstesideCounter, Long> {
//    @Query(value =
//            "select * " +
//                    "FROM " + FoerstesideCounter.TABLE_NAME + " " +
//                    "WHERE DATO = to_char(sysdate, 'YYYYMMDD')", nativeQuery = true)
    @Transactional
    @Query("SELECT fc FROM FoerstesideCounter fc WHERE fc.date = '20190801'")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    FoerstesideCounter getCounterForToday();
}
