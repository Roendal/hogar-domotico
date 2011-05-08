package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.Chargingstation;
import jadex.bdi.examples.cleanerworld_classic.CleanerLocationManager;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;


/**
 *  Patrol along the patrol points.
 */
public class PatrolPlan extends Plan
{

	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public PatrolPlan()
	{
//		getLogger().info("Created: "+this);
	}

	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		//LSIN*Eduardo* Inicio
		IGoal findstation = createGoal("querychargingstation");
		dispatchSubgoalAndWait(findstation);
		Chargingstation station = (Chargingstation)findstation.getParameter("result").getValue();

		if(station!=null)
		{
			IGoal moveto = createGoal("achievemoveto");
			Location location = station.getLocation();
			location = location.randomRadius(0.001);			
			moveto.getParameter("location").setValue(location);
			dispatchSubgoalAndWait(moveto);
			double	charge	= ((Double)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue();

			location = (Location)getBeliefbase().getBelief("my_location").getFact();
			while(location.getDistance(station.getLocation())<0.01 && charge<1.0)
			{
				waitFor(100);
				charge	= ((Double)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue();
				charge	= Math.min(charge + 0.01, 1.0);
				getBeliefbase().getBelief("my_chargestate").setFact(new Double(charge));
				location = (Location)getBeliefbase().getBelief("my_location").getFact();
				IGoal dg = createGoal("get_vision_action");
				dispatchSubgoalAndWait(dg);
			}
		}
		
		//LSIN*Eduardo* Fin
	}
}
