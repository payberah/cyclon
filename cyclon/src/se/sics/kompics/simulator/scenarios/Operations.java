package se.sics.kompics.simulator.scenarios;

import java.math.BigInteger;

import se.sics.kompics.p2p.experiment.dsl.adaptor.Operation1;
import se.sics.kompics.simulator.core.CyclonPeerFail;
import se.sics.kompics.simulator.core.CyclonPeerJoin;

@SuppressWarnings("serial")
public class Operations {

//-------------------------------------------------------------------
	static Operation1<CyclonPeerJoin, BigInteger> cyclonPeerJoin = new Operation1<CyclonPeerJoin, BigInteger>() {
		public CyclonPeerJoin generate(BigInteger id) {
			return new CyclonPeerJoin(id);
		}
	};
	
//-------------------------------------------------------------------
	static Operation1<CyclonPeerFail, BigInteger> cyclonPeerFail = new Operation1<CyclonPeerFail, BigInteger>() {
		public CyclonPeerFail generate(BigInteger id) {
			return new CyclonPeerFail(id);
		}
	};
}
