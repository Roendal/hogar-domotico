package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.CleanerLocationManager;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.examples.cleanerworld_classic.MapPoint;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

import java.util.List;

/**
 * Plan to explore the map by going to the seldom visited positions. Uses the
 * absolute quantity to go to positions that are not yet explored.
 */
public class ExploreMapPlan extends Plan {
	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public ExploreMapPlan() {
		// getLogger().info("Created: "+this+" for goal "+getRootGoal());
	}

	// -------- methods --------

	/**
	 * The plan body. LSIN*Eduardo*
	 */
	public void body() {
		// Select randomly one of the seldom visited locations.
		List mps = (List) getExpression("query_min_quantity").execute();
		MapPoint mp = (MapPoint) mps.get(0);
		int cnt = 1;
		for (; cnt < mps.size(); cnt++) {
			MapPoint mp2 = (MapPoint) mps.get(cnt);
			if (mp.getSeen() != mp2.getSeen())
				break;
		}
		mp = (MapPoint) mps.get((int) (Math.random() * cnt));
		Location dest = mp.getLocation();
		
		//LSIN*Eduardo* Inicio
		//dest = CleanerLocationManager.rectify(((Number)getBeliefbase().getBelief("my_room").getFact()).intValue(), dest);
		//LSIN*Eduardo* Fin
		
		IGoal moveto = createGoal("achievemoveto");
		moveto.getParameter("location").setValue(dest);
		// System.out.println("Created: "+dest+" "+this);
		dispatchSubgoalAndWait(moveto);
		// System.out.println("Reached: "+dest+" "+this);
	}
}
