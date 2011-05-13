package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.runtime.Plan;

public class ActivateAlarmPlan extends Plan {
	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public ActivateAlarmPlan() {
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		System.out.println("RINGGGGGGGGGGGG!");
	}

}
