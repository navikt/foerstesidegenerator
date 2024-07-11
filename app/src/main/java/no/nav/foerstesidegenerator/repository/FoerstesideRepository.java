package no.nav.foerstesidegenerator.repository;

import no.nav.foerstesidegenerator.domain.Foersteside;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoerstesideRepository extends CrudRepository<Foersteside, Long> {

	Optional<Foersteside> findByLoepenummer(String loepenummer);

	@Query("""
			select f from Foersteside f
				join FoerstesideMetadata fm on f.foerstesideId = fm.foersteside.foerstesideId
				and fm.key = :metadataKeySomSkalMaskeres and fm.value is not null
				where f.datoOpprettet < function('ADD_MONTHS', CURRENT_DATE, -6)
			""")
	List<Foersteside> finnFoerstesiderSomSkalMaskeres(@Param("metadataKeySomSkalMaskeres") String metadataKeySomSkalMaskeres);

}