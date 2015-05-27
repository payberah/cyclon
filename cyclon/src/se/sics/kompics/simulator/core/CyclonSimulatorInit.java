package se.sics.kompics.simulator.core;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;
import se.sics.kompics.p2p.fd.ping.PingFailureDetectorConfiguration;
import se.sics.kompics.system.cyclon.CyclonConfiguration;
import se.sics.kompics.system.peer.PeerConfiguration;

public final class CyclonSimulatorInit extends Init {

	private final PeerConfiguration peerConfiguration;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final PingFailureDetectorConfiguration fdConfiguration;
	
	private final Address peer0Address;

//-------------------------------------------------------------------	
	public CyclonSimulatorInit(PeerConfiguration peerConfiguration,
			BootstrapConfiguration bootstrapConfiguration,
			CyclonConfiguration cyclonConfiguration, 
			PingFailureDetectorConfiguration fdConfiguration,
			Address peer0Address) {
		super();
		this.peerConfiguration = peerConfiguration;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.fdConfiguration = fdConfiguration;
		this.peer0Address = peer0Address;
	}

//-------------------------------------------------------------------	
	public PeerConfiguration getPeerConfiguration() {
		return peerConfiguration;
	}

//-------------------------------------------------------------------	
	public BootstrapConfiguration getBootstrapConfiguration() {
		return bootstrapConfiguration;
	}

//-------------------------------------------------------------------	
	public CyclonConfiguration getCyclonConfiguration() {
		return cyclonConfiguration;
	}
	
//-------------------------------------------------------------------	
	public PingFailureDetectorConfiguration getFdConfiguration() {
		return fdConfiguration;
	}

//-------------------------------------------------------------------	
	public Address getPeer0Address() {
		return peer0Address;
	}
}
