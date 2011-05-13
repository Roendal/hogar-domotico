package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.runtime.Plan;

public class UpdateEnvironmentPlan extends Plan {
	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public UpdateEnvironmentPlan() {
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		Date date = ((Environment)getBeliefbase().getBelief("environment").getFact()).getDate();
	}

}
