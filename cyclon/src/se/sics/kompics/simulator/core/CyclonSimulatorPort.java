package se.sics.kompics.simulator.core;

import se.sics.kompics.PortType;
import se.sics.kompics.p2p.experiment.dsl.events.TerminateExperiment;

public class CyclonSimulatorPort extends PortType {{
	positive(CyclonPeerJoin.class);
	positive(CyclonPeerFail.class);
	negative(TerminateExperiment.class);
}}
