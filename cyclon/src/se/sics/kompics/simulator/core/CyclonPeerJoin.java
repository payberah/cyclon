package se.sics.kompics.simulator.core;

import java.math.BigInteger;

import se.sics.kompics.Event;

public final class CyclonPeerJoin extends Event {

	private final BigInteger peerId;

//-------------------------------------------------------------------	
	public CyclonPeerJoin(BigInteger peerId) {
		this.peerId = peerId;
	}

//-------------------------------------------------------------------	
	public BigInteger getPeerId() {
		return peerId;
	}
}
