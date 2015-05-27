package se.sics.kompics.simulator.scenarios;

import java.util.Random;

import se.sics.kompics.p2p.experiment.dsl.SimulationScenario;
import se.sics.kompics.simulator.core.CyclonExecutionMain;
import se.sics.kompics.simulator.core.CyclonSimulationMain;

/**
 * The <code>Scenario</code> class.
 * 
 * @author Amir Payberah <amir@sics.se>
 */
public class Scenario {
	private static Random random;
	protected SimulationScenario scenario;

//-------------------------------------------------------------------
	public Scenario(SimulationScenario scenario) {
		this.scenario = scenario;
		this.scenario.setSeed(System.currentTimeMillis());
		random = scenario.getRandom();
	}

//-------------------------------------------------------------------
	public void setSeed(long seed) {
		this.scenario.setSeed(seed);
	}

//-------------------------------------------------------------------
	public void execute() {
		this.scenario.execute(CyclonExecutionMain.class);
	}

//-------------------------------------------------------------------
	public void simulate() {
		this.scenario.simulate(CyclonSimulationMain.class);
	}

//-------------------------------------------------------------------
	public static Random getRandom() {
		return random;
	}

//-------------------------------------------------------------------
	public static void setRandom(Random r) {
		random = r;
	}
}
