package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.Chargingstation;
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
				//Esto estaba en LoadBatteryPlan. Mueren aunque lo descomente.
				//IGoal dg = createGoal("get_vision_action");
				//dispatchSubgoalAndWait(dg);
			}
			//Una vez llegados a este punto nos encontramos en el caso:
			//*Es de noche
			//*La carga es del 100%
			//Debemos indicarle que lo único que debe hacer es permanecer a la espera hasta que vuelva a ser de día.
			//Actualmente llegados a este punto desaparecen, pero no paran de repetir:
			System.out.println("Me muero");
		}
		//LSIN*Eduardo* Fin
	}
}
