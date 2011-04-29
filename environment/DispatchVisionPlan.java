package jadex.bdi.examples.cleanerworld_classic.environment;

import jadex.base.fipa.Done;
import jadex.bdi.examples.cleanerworld_classic.Cleaner;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.examples.cleanerworld_classic.RequestVision;
import jadex.bdi.examples.cleanerworld_classic.Vision;
import jadex.bdi.runtime.Plan;

/**
 *  The dispatch vision plan calculates the vision for a
 *  participant and send it back.
 */
public class DispatchVisionPlan extends Plan
{
	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public DispatchVisionPlan()
	{
		getLogger().info("Created: "+this);
	}

	//------ methods -------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		RequestVision rv = (RequestVision)getParameter("action").getValue();
		Cleaner cl = rv.getCleaner();
		Environment env = (Environment)getBeliefbase().getBelief("environment").getFact();
		Vision v = env.getVision(cl);

		rv.setVision(v);
		Done done = new Done();
		done.setAction(rv);
		getParameter("result").setValue(done);
	}
}
