package se.sics.kompics.system.peer;

import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;
import se.sics.kompics.p2p.fd.ping.PingFailureDetectorConfiguration;
import se.sics.kompics.system.common.PeerAddress;
import se.sics.kompics.system.cyclon.CyclonConfiguration;

public final class PeerInit extends Init {

	private final PeerAddress peerSelf;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final PeerConfiguration peerConfiguration;
	private final PingFailureDetectorConfiguration fdConfiguration;

//-------------------------------------------------------------------	
	public PeerInit(PeerAddress peerSelf,
			PeerConfiguration peerConfiguration,
			BootstrapConfiguration bootstrapConfiguration,
			CyclonConfiguration cyclonConfiguration,
			PingFailureDetectorConfiguration fdConfiguration) {
		super();

		this.peerSelf = peerSelf;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.peerConfiguration = peerConfiguration;
		this.fdConfiguration = fdConfiguration;
	}

//-------------------------------------------------------------------	
	public PeerAddress getPeerSelf() {
		return peerSelf;
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
	public PeerConfiguration getPeerConfiguration() {
		return peerConfiguration; 
	}
	
//-------------------------------------------------------------------	
	public PingFailureDetectorConfiguration getFdConfiguration() {
		return fdConfiguration;
	}

}
