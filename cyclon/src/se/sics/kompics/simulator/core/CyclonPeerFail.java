package se.sics.kompics.simulator.core;

import java.math.BigInteger;

import se.sics.kompics.Event;

public final class CyclonPeerFail extends Event {

	private final BigInteger peerId;

//-------------------------------------------------------------------	
	public CyclonPeerFail(BigInteger peerId) {
		this.peerId = peerId;
	}

//-------------------------------------------------------------------	
	public BigInteger getPeerId() {
		return peerId;
	}

//-------------------------------------------------------------------	
	@Override
	public String toString() {
		return "Fail@" + peerId;
	}
}
