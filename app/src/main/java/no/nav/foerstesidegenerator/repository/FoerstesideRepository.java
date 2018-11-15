package no.nav.foerstesidegenerator.repository;

import org.springframework.data.repository.CrudRepository;
import no.nav.foerstesidegenerator.domain.Foersteside;

public interface FoerstesideRepository extends CrudRepository<Foersteside, Long> {

	Foersteside findByFoerstesideId(Long foerstesideId);

	Foersteside findByLoepenummer(String loepenummer);

	Foersteside save(Foersteside foersteside);
}
