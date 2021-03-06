package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.CleanerLocationManager;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

/**
 * Wander around randomly.
 */
public class RandomWalkPlan extends Plan {

	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public RandomWalkPlan() {
		// getLogger().info("Created: "+this+" for goal "+getRootGoal());
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		// LSIN*Eduardo* Inicio
		// double x_dest = Math.random();
		// double y_dest = Math.random();
		// Location dest = new Location(x_dest, y_dest);
		// dest =
		// CleanerLocationManager.rectify(((Number)getBeliefbase().getBelief("my_room").getFact()).intValue(),
		// dest);
		// LSIN*Eduardo* Fin
		Location dest = CleanerLocationManager.randomLocationInRoom(((Number)getBeliefbase().getBelief("my_room").getFact()).intValue());
		IGoal moveto = createGoal("achievemoveto");
		moveto.getParameter("location").setValue(dest);
		// System.out.println("Created: "+dest+" "+this);
		dispatchSubgoalAndWait(moveto);
		// System.out.println("Reached: "+dest+" "+this);
		// getLogger().info("Reached point: "+dest);
	}
}
