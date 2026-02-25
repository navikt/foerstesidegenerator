package no.nav.foerstesidegenerator.consumer.dokmet;

import no.nav.dokmet.api.tkat020.DokumenttypeInfoTo;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface DokmetClient {

	@Retryable(maxRetries = 3, delay = 1000)
	@GetExchange("/{dokumenttypeId}")
	DokumenttypeInfoTo getDokumenttypeInfo(@PathVariable String dokumenttypeId);
}
