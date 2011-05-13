package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Date;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.runtime.Plan;

public class UpdateEnvironmentPlan extends Plan {
	// -------- constructors --------

	private static int hour = -1;
	
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
		getBeliefbase().getBelief("current_date").setFact(date);
		getBeliefbase().getBelief("alarmcondition").setFact(Ambrosio.shouldActivateAlarm(date));
		if (hour!=date.getHour()){
			hour = date.getHour();
			System.out.println("Nueva hora: " + hour + ":00");			
		}
	}

}
