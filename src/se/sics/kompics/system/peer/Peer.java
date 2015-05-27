package se.sics.kompics.system.peer;

import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Network;
import se.sics.kompics.p2p.bootstrap.BootstrapCompleted;
import se.sics.kompics.p2p.bootstrap.BootstrapRequest;
import se.sics.kompics.p2p.bootstrap.BootstrapResponse;
import se.sics.kompics.p2p.bootstrap.P2pBootstrap;
import se.sics.kompics.p2p.bootstrap.PeerEntry;
import se.sics.kompics.p2p.bootstrap.client.BootstrapClient;
import se.sics.kompics.p2p.bootstrap.client.BootstrapClientInit;
import se.sics.kompics.p2p.fd.ping.PingFailureDetector;
import se.sics.kompics.p2p.fd.ping.PingFailureDetectorInit;
import se.sics.kompics.system.common.PeerAddress;
import se.sics.kompics.system.cyclon.Cyclon;
import se.sics.kompics.system.cyclon.CyclonInit;
import se.sics.kompics.system.cyclon.CyclonPort;
import se.sics.kompics.system.cyclon.Join;
import se.sics.kompics.system.cyclon.JoinCompleted;
import se.sics.kompics.timer.Timer;

public final class Peer extends ComponentDefinition {
	Negative<PeerPort> peerPort = negative(PeerPort.class);
	Positive<Network> network = positive(Network.class);
	Positive<Timer> timer = positive(Timer.class);

	private Component cyclon;
	private Component fd, bootstrap;
	
	private Address self;
	private PeerAddress peerSelf;

	private int bootstrapRequestPeerCount;
	private boolean bootstrapped;
	private Logger logger;

//-------------------------------------------------------------------	
	public Peer() {
		cyclon = create(Cyclon.class);
		
		fd = create(PingFailureDetector.class);
		bootstrap = create(BootstrapClient.class);

		connect(network, cyclon.getNegative(Network.class));
		connect(network, fd.getNegative(Network.class));
		connect(network, bootstrap.getNegative(Network.class));

		connect(timer, cyclon.getNegative(Timer.class));
		connect(timer, fd.getNegative(Timer.class));
		connect(timer, bootstrap.getNegative(Timer.class));
		
		subscribe(handleInit, control);

		subscribe(handleJoin, peerPort);
		subscribe(handleJoinCompleted, cyclon.getPositive(CyclonPort.class));
		subscribe(handleBootstrapResponse, bootstrap.getPositive(P2pBootstrap.class));
	}

//-------------------------------------------------------------------	
	Handler<PeerInit> handleInit = new Handler<PeerInit>() {
		public void handle(PeerInit init) {
			peerSelf = init.getPeerSelf();
			self = peerSelf.getPeerAddress();

			logger = LoggerFactory.getLogger(getClass().getName() + "@"	+ self.getId());

			bootstrapRequestPeerCount = init.getCyclonConfiguration().getBootstrapRequestPeerCount();

			trigger(new CyclonInit(init.getCyclonConfiguration()), cyclon.getControl());
			trigger(new BootstrapClientInit(self, init.getBootstrapConfiguration()), bootstrap.getControl());
			trigger(new PingFailureDetectorInit(self, init.getFdConfiguration()), fd.getControl());
		}
	};

//-------------------------------------------------------------------	
	Handler<JoinPeer> handleJoin = new Handler<JoinPeer>() {
		public void handle(JoinPeer event) {
			BootstrapRequest request = new BootstrapRequest("Cyclon", bootstrapRequestPeerCount);
			trigger(request, bootstrap.getPositive(P2pBootstrap.class));
		}
	};

//-------------------------------------------------------------------	
	Handler<BootstrapResponse> handleBootstrapResponse = new Handler<BootstrapResponse>() {
		public void handle(BootstrapResponse event) {
			if (!bootstrapped) {
				logger.debug("Got BoostrapResponse {}, Bootstrap complete", event.getPeers().size());

				Set<PeerEntry> somePeers = event.getPeers();

				LinkedList<PeerAddress> cyclonInsiders = new LinkedList<PeerAddress>();
				
				for (PeerEntry peerEntry : somePeers)
					cyclonInsiders.add((PeerAddress) peerEntry.getOverlayAddress());
				
				trigger(new Join(peerSelf, cyclonInsiders), cyclon.getPositive(CyclonPort.class));
				bootstrapped = true;
			}
		}
	};

//-------------------------------------------------------------------	
	Handler<JoinCompleted> handleJoinCompleted = new Handler<JoinCompleted>() {
		public void handle(JoinCompleted event) {
			logger.debug("Join completed");
			
			// bootstrap completed
			trigger(new BootstrapCompleted("Cyclon", peerSelf), bootstrap.getPositive(P2pBootstrap.class));
		}
	};
}
