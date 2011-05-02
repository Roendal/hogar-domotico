package jadex.bdi.examples.cleanerworld.cleaner;

import jadex.application.space.envsupport.math.IVector2;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;


/**
 *  Patrol along the patrol points.
 */
public class PatrolPlan extends Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IVector2[] loci = (IVector2[])getBeliefbase().getBeliefSet("patrolpoints").getFacts();

		for(int i=0; i<loci.length; i++)
		{
			IGoal moveto = createGoal("achievemoveto");
			moveto.getParameter("location").setValue(loci[i]);
//			System.out.println("Created: "+loci[i]+" "+this);
			dispatchSubgoalAndWait(moveto);
//			System.out.println("Reached: "+loci[i]+" "+this);
		}
	}
}
