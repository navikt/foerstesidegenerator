package no.nav.foerstesidegenerator.repository;

import org.springframework.data.repository.CrudRepository;
import no.nav.foerstesidegenerator.domain.Foersteside;

public interface FoerstesideRepository extends CrudRepository<Foersteside, Long> {

	Foersteside findByFoerstesideId(Long foerstesideId);

	Foersteside save(Foersteside foersteside);
}
