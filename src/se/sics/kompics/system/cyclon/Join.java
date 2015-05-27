package se.sics.kompics.system.cyclon;

import java.util.LinkedList;

import se.sics.kompics.Event;
import se.sics.kompics.system.common.PeerAddress;

public final class Join extends Event {

	private final PeerAddress peerSelf;
	private final LinkedList<PeerAddress> insiders;

//-------------------------------------------------------------------	
	public Join(PeerAddress self, LinkedList<PeerAddress> cyclonInsiders) {
		super();
		this.peerSelf = self;
		this.insiders = cyclonInsiders;
	}

//-------------------------------------------------------------------	
	public final PeerAddress getSelf() {
		return peerSelf;
	}

//-------------------------------------------------------------------	
	public LinkedList<PeerAddress> getCyclonInsiders() {
		return insiders;
	}

//-------------------------------------------------------------------	
	@Override
	public String toString() {
		return "Join(" + peerSelf + ", " + insiders + ")";
	}
}
