package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoerstesideRepository extends CrudRepository<Foersteside, Long> {

	Foersteside findByFoerstesideId(Long foerstesideId);

	Optional<Foersteside> findByLoepenummer(String loepenummer);
}
