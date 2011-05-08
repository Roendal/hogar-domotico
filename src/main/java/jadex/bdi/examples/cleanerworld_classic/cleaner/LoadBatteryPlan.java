package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.Chargingstation;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;


/**
 *  Go to the charging station and load the battery.
 */
public class LoadBatteryPlan extends Plan
{
	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public LoadBatteryPlan()
	{
//		getLogger().info("Created: "+this);
	}

	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IGoal findstation = createGoal("querychargingstation");
		dispatchSubgoalAndWait(findstation);
		Chargingstation station = (Chargingstation)findstation.getParameter("result").getValue();

		if(station!=null)
		{
			IGoal moveto = createGoal("achievemoveto");
			Location location = station.getLocation();
			moveto.getParameter("location").setValue(location);
			dispatchSubgoalAndWait(moveto);

			location = (Location)getBeliefbase().getBelief("my_location").getFact();
			double	charge	= ((Double)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue();

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
	}

}
