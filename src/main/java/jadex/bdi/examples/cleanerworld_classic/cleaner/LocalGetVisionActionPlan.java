package jadex.bdi.examples.cleanerworld_classic.cleaner;

import java.awt.Rectangle;

import jadex.bdi.examples.cleanerworld_classic.Cleaner;
import jadex.bdi.examples.cleanerworld_classic.IEnvironment;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.examples.cleanerworld_classic.Vision;
import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.runtime.Plan;


/**
 *  Pick up a piece of waste in the environment.
 */
public class LocalGetVisionActionPlan extends	Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		IEnvironment	environment	= (IEnvironment)getBeliefbase().getBelief("environment").getFact();
		Cleaner cl = new Cleaner((Location)getBeliefbase().getBelief("my_location").getFact(),
			(Location)getBeliefbase().getBelief("room_upper_left_corner").getFact(),
			(Location)getBeliefbase().getBelief("room_bottom_right_corner").getFact(),
			getComponentName(),
			(Waste)getBeliefbase().getBelief("carriedwaste").getFact(),
			((Number)getBeliefbase().getBelief("my_vision").getFact()).doubleValue(),
			((Number)getBeliefbase().getBelief("my_chargestate").getFact()).doubleValue());

		Vision	vision	= (Vision)environment.getVision(cl).clone();
//		Vision	vision	= (Vision)environment.getVision(cl);

		getParameter("vision").setValue(vision);
	}
}
