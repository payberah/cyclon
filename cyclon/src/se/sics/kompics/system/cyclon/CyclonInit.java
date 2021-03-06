package se.sics.kompics.system.cyclon;

import se.sics.kompics.Init;

public final class CyclonInit extends Init {

	private final CyclonConfiguration configuration;

//-------------------------------------------------------------------	
	public CyclonInit(CyclonConfiguration configuration) {
		super();
		this.configuration = configuration;
	}

//-------------------------------------------------------------------	
	public CyclonConfiguration getConfiguration() {
		return configuration;
	}
}