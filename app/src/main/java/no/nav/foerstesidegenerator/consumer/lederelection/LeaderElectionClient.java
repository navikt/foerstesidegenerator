package no.nav.foerstesidegenerator.consumer.lederelection;

import org.springframework.web.service.annotation.GetExchange;

public interface LeaderElectionClient {

	@GetExchange
	ElectorResponse getLeader();
}
