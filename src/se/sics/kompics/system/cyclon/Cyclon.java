package se.sics.kompics.system.cyclon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.simulator.snapshot.Snapshot;
import se.sics.kompics.system.common.PeerAddress;
import se.sics.kompics.timer.CancelTimeout;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

public final class Cyclon extends ComponentDefinition {

	Negative<CyclonPort> cyclonPort = negative(CyclonPort.class);
	Positive<Network> networkPort = positive(Network.class);
	Positive<Timer> timerPort = positive(Timer.class);
	
	CyclonConfiguration cyclonConfiguration;

	private Logger logger;
	private PeerAddress self;

	private int shuffleLength;
	private long shufflePeriod;
	private long shuffleTimeout;

	private View cache;
	
	private boolean joining;

	private HashMap<UUID, PeerAddress> outstandingRandomShuffles;

//-------------------------------------------------------------------	
	public Cyclon() {
		outstandingRandomShuffles = new HashMap<UUID, PeerAddress>();

		subscribe(handleInit, control);

		subscribe(handleJoin, cyclonPort);

		subscribe(handleInitiateShuffle, timerPort);
		subscribe(handleShuffleTimeout, timerPort);

		subscribe(handleShuffleRequest, networkPort);
		subscribe(handleShuffleResponse, networkPort);
	}

//-------------------------------------------------------------------	
	Handler<CyclonInit> handleInit = new Handler<CyclonInit>() {
		public void handle(CyclonInit init) {
			cyclonConfiguration = init.getConfiguration();
			
			shuffleLength = cyclonConfiguration.getShuffleLength();
			shufflePeriod = cyclonConfiguration.getShufflePeriod();
			shuffleTimeout = cyclonConfiguration.getShuffleTimeout();
		}
	};

//-------------------------------------------------------------------	
	/**
	 * handles a request to join a Cyclon network using a set of introducer
	 * nodes provided in the Join event.
	 */
	Handler<Join> handleJoin = new Handler<Join>() {
		public void handle(Join event) {
			self = event.getSelf();
			cache = new View(cyclonConfiguration.getRandomViewSize(), self);
			
			logger = LoggerFactory.getLogger(getClass().getName() + "@" + self.getPeerId());

			LinkedList<PeerAddress> insiders = event.getCyclonInsiders();

			if (insiders.size() == 0) {
				// I am the first peer
				trigger(new JoinCompleted(self), cyclonPort);

				// schedule shuffling
				SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(shufflePeriod, shufflePeriod);
				spt.setTimeoutEvent(new InitiateShuffle(spt));
				trigger(spt, timerPort);
				return;
			}

			PeerAddress peer = insiders.poll();
			initiateShuffle(1, peer);
			joining = true;
		}
	};

//-------------------------------------------------------------------	
	/**
	 * initiates a shuffle of size <code>shuffleSize</code>. Called either
	 * during the join protocol with a <code>shuffleSize</code> of 1, or
	 * periodically to initiate regular shuffles.
	 * 
	 * @param shuffleSize
	 * @param randomPeer
	 */
	private void initiateShuffle(int shuffleSize, PeerAddress randomPeer) {
		// send the random view to a random peer
		ArrayList<CyclonDescriptor> randomDescriptors = cache.selectToSendAtActive(shuffleSize - 1, randomPeer);
		randomDescriptors.add(new CyclonDescriptor(self));
		DescriptorBuffer randomBuffer = new DescriptorBuffer(self, randomDescriptors);
		
		ScheduleTimeout rst = new ScheduleTimeout(shuffleTimeout);
		rst.setTimeoutEvent(new ShuffleTimeout(rst, randomPeer));
		UUID rTimeoutId = rst.getTimeoutEvent().getTimeoutId();

		outstandingRandomShuffles.put(rTimeoutId, randomPeer);
		ShuffleRequest rRequest = new ShuffleRequest(rTimeoutId, randomBuffer, self, randomPeer);

		trigger(rst, timerPort);
		trigger(rRequest, networkPort);

		logger.debug("Initiated Shuffle with {}", randomPeer);
	}

//-------------------------------------------------------------------	
	/**
	 * Periodically, will initiate regular shuffles. This is the first half of
	 * the "active thread" of the Cyclon specification.
	 */
	Handler<InitiateShuffle> handleInitiateShuffle = new Handler<InitiateShuffle>() {
		public void handle(InitiateShuffle event) {
			cache.incrementDescriptorAges();
			
			PeerAddress randomPeer = cache.selectPeerToShuffleWith();
			Snapshot.incSelectedTimes(randomPeer);
			
			if (randomPeer != null) {
				initiateShuffle(shuffleLength, randomPeer);
			}
		}
	};

//-------------------------------------------------------------------	
	Handler<ShuffleRequest> handleShuffleRequest = new Handler<ShuffleRequest>() {
		public void handle(ShuffleRequest event) {
			PeerAddress peer = event.getPeerSource();

			DescriptorBuffer receivedRandomBuffer = event.getRandomBuffer();
			
			DescriptorBuffer toSendRandomBuffer = new DescriptorBuffer(self, cache.selectToSendAtPassive(receivedRandomBuffer.getSize(), peer));

			cache.selectToKeep(peer, receivedRandomBuffer.getDescriptors());
			Snapshot.updatePartners(self, cache.getAll());
		
			logger.debug("SHUFFLE_REQ from {}. r={} s={}", new Object[] { peer, receivedRandomBuffer.getSize(), toSendRandomBuffer.getSize() });

			ShuffleResponse response = new ShuffleResponse(event.getRequestId(), toSendRandomBuffer, self, peer);
			
			trigger(response, networkPort);
		}
	};

//-------------------------------------------------------------------	
	Handler<ShuffleResponse> handleShuffleResponse = new Handler<ShuffleResponse>() {
		public void handle(ShuffleResponse event) {
			if (joining) {
				joining = false;
				trigger(new JoinCompleted(self), cyclonPort);

				// schedule shuffling
				SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(shufflePeriod, shufflePeriod);
				spt.setTimeoutEvent(new InitiateShuffle(spt));
				trigger(spt, timerPort);
			}

			// cancel shuffle timeout
			UUID shuffleId = event.getRequestId();
			if (outstandingRandomShuffles.containsKey(shuffleId)) {
				outstandingRandomShuffles.remove(shuffleId);
				CancelTimeout ct = new CancelTimeout(shuffleId);
				trigger(ct, timerPort);
			}

			PeerAddress peer = event.getPeerSource();

			//logger.debug("SHUFFLE_RESP from {}", peer);

			DescriptorBuffer receivedRandomBuffer = event.getRandomBuffer();
			
			cache.selectToKeep(peer, receivedRandomBuffer.getDescriptors());
			Snapshot.updatePartners(self, cache.getAll());
		}
	};

//-------------------------------------------------------------------	
	Handler<ShuffleTimeout> handleShuffleTimeout = new Handler<ShuffleTimeout>() {
		public void handle(ShuffleTimeout event) {
			//logger.warn("SHUFFLE TIMED OUT");
		}
	};
}
