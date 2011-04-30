package jadex.bdi.examples.cleanerworld_classic.environment;

import jadex.base.fipa.Done;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.examples.cleanerworld_classic.RequestPickUpWaste;
import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.runtime.Plan;

/**
 *  Pick up some piece of waste.
 */
public class PickUpWastePlan extends Plan
{
	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public PickUpWastePlan()
	{
		getLogger().info("Created: "+this);
	}

	//------ methods -------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		RequestPickUpWaste op = (RequestPickUpWaste)getParameter("action").getValue();
		Waste waste = op.getWaste();

		Environment env = (Environment)getBeliefbase().getBelief("environment").getFact();
		boolean success = env.pickUpWaste(waste);

		if(!success)
			fail();

		Done done = new Done();
		done.setAction(op);
		getParameter("result").setValue(done);
	}

}
