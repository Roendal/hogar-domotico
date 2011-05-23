package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Date;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.examples.cleanerworld_classic.Wastebin;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

public class UpdateEnvironmentPlan extends Plan {
	// -------- constructors --------

	private static int hour = -1;
	private static int day = -1;
	private static boolean ringedToday = false;
	
	private final int NUM_WASTES_UNTIL_CLEANUP = 18; 
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
		Date date = ((Environment) getBeliefbase().getBelief("environment")
				.getFact()).getDate();
		// LSIN *Alicia* Inicio
		Ambrosio.setDaytime(date.isDay());
		// LSIN *Alicia* Fin
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

			IGoal checkCleaners = createGoal("performcheckcleaners");
			dispatchTopLevelGoal(checkCleaners);

			if (day != date.getDayNumber()) {

				day = date.getDayNumber();
				ringedToday = false;
			}
			// Trazas
			System.out.println("Día: " + day + " hora: " + hour + ":00");
		}

		Ambrosio.setRoomPresence(((Environment) getBeliefbase().getBelief(
				"environment").getFact()).getRoomPresence());
		
		//LSIN*Ces* Inicio
		Wastebin[] papeleras = (Wastebin[]) getBeliefbase().getBelief("waste_levels").getFact();
		boolean triggerEmptyTrashPlan = false;
		for (Wastebin e : papeleras) {
			if(e.getNumWastes() > NUM_WASTES_UNTIL_CLEANUP) 
				triggerEmptyTrashPlan = true;
		}
		getBeliefbase().getBelief("cleanup").setFact(triggerEmptyTrashPlan);
		//LSIN*Ces* Fin

	}
}
