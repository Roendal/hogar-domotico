package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Date;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.runtime.Plan;

public class UpdateEnvironmentPlan extends Plan {
	// -------- constructors --------

	private static int hour = -1;
	private static int day = -1;
	private static boolean ringedToday = false;

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
		Date date = ((Environment) getBeliefbase().getBelief("environment").getFact()).getDate();
		getBeliefbase().getBelief("current_date").setFact(date);
		if (ringedToday) {
			getBeliefbase().getBelief("alarmcondition").setFact(false);
		} else {
			if (Ambrosio.shouldActivateAlarm(date)) {
				getBeliefbase().getBelief("alarmcondition").setFact(true);
				ringedToday = true;
			}
		}
		if (hour != date.getHour()) {
			hour = date.getHour();
			if (day != date.getDayNumber()) {
				day = date.getDayNumber();
				ringedToday = false;
			}
			// Trazas
			System.out.println("Día: " + day + " hora: " + hour + ":00");
		}
		
		Ambrosio.setRoomPresence(((Environment) getBeliefbase().getBelief("environment").getFact()).getRoomPresence());
	}

}
