package jadex.bdi.examples.cleanerworld.cleaner;

import jadex.application.space.envsupport.environment.space2d.Space2D;
import jadex.application.space.envsupport.math.IVector1;
import jadex.application.space.envsupport.math.IVector2;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;

import java.util.List;


/**
 *  Plan to explore the map by going to the seldom visited positions.
 *  Uses the absolute quantity to go to positions that are not yet
 *  explored.
 */
public class ExploreMapPlan extends Plan
{
	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public ExploreMapPlan()
	{
//		getLogger().info("Created: "+this+" for goal "+getRootGoal());
	}

	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		// Use shortest not seen point
		Space2D env = (Space2D)getBeliefbase().getBelief("environment").getFact();
		IVector2 mypos = (IVector2)getBeliefbase().getBelief("my_location").getFact();

		List	mps = (List)getExpression("query_min_quantity").execute();
		MapPoint mp = (MapPoint)mps.get(0);
		IVector1 dist = env.getDistance(mypos, mp.getLocation());
		int cnt	= 1;
		
		for( ; cnt<mps.size(); cnt++)
		{
			MapPoint mp2 = (MapPoint)mps.get(cnt);
			if(mp.getSeen()!=mp2.getSeen())
				break;
			IVector1 dist2 = env.getDistance(mypos, mp2.getLocation());
			if(dist2.less(dist))
			{
				mp = mp2;
				dist = dist2;
			}
		}
		
//		mp = (MapPoint)mps.get((int)(Math.random()*cnt));
//		MapPoint[]	mps = (MapPoint[])getBeliefbase().getBeliefSet("visited_positions").getFacts();
//		MapPoint mp = mps[(int)(Math.random()*mps.length)];

		IVector2 dest = mp.getLocation();
		IGoal moveto = createGoal("achievemoveto");
		moveto.getParameter("location").setValue(dest);		
//		System.out.println("Created: "+dest+" "+this);
		dispatchSubgoalAndWait(moveto);
//		System.out.println("Reached: "+dest+" "+this);
	}
}
