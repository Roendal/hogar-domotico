package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.examples.cleanerworld_classic.Wastebin;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

public class EmptyTrashPlan extends Plan{

	/**
	 * Create a new plan.
	 */
	public EmptyTrashPlan() {
	}

	@Override
	public void body() {
		Ambrosio.setCleanUp(true);
		System.out.println("Se vacian todas las papeleras");
		Wastebin[] papeleras = (Wastebin[]) getBeliefbase().getBelief("waste_levels").getFact();
		//Empty all of trash, to enhance the travels to empty bins.
		for (Wastebin e : papeleras) {
			e.empty();
		}
		
		getBeliefbase().getBelief("cleanup").setFact(false);
	}
	
}
