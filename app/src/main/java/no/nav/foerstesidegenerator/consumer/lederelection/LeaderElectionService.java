package no.nav.foerstesidegenerator.consumer.lederelection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class LeaderElectionService {
	private final LeaderElectionClient leaderElectionClient;

	public LeaderElectionService(LeaderElectionClient leaderElectionClient) {
		this.leaderElectionClient = leaderElectionClient;
	}

	public boolean isLeader() {
		ElectorResponse electorResponse = leaderElectionClient.getLeader();
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			return hostname.equals(electorResponse.name());
		} catch (UnknownHostException e) {
			log.error("Kunne ikke bestemme lederpod", e);
			return false;
		}
	}
}
