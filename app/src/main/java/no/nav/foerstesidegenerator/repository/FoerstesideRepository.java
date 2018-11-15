package no.nav.foerstesidegenerator.repository;

import org.springframework.data.repository.CrudRepository;
import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.stereotype.Repository;

@Repository
public interface FoerstesideRepository extends CrudRepository<Foersteside, Long> {

	Foersteside findByFoerstesideId(Long foerstesideId);

	Foersteside findByLoepenummer(Long loepenummer);
}
