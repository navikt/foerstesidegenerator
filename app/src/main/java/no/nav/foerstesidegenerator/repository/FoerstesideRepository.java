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

	@Query(value = """
			select f.* from foersteside f
				inner join foersteside_metadata fm on f.foersteside_id = fm.foersteside_id 
				and fm.key = :metadataKeySomSkalMaskeres and fm.value is not null
				where f.DATO_OPPRETTET < add_months(sysdate, -6)
			""", nativeQuery = true)
	List<Foersteside> finnFoerstesiderSomSkalMaskeres(@Param("metadataKeySomSkalMaskeres") String metadataKeySomSkalMaskeres);

}