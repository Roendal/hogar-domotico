package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.examples.cleanerworld_classic.Wastebin;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.Plan;


/**
 *  Clean-up some waste.
 */
public class CleanUpWastePlan extends Plan
{

	//-------- constructors --------

	/**
	 *  Create a new plan.
	 */
	public CleanUpWastePlan()
	{
//		getLogger().info("Created: "+this);
	}

	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		//System.out.println("Clean-up waste plan started.");

		if(getBeliefbase().getBelief("carriedwaste").getFact()==null)
		{
			Waste waste = (Waste)getParameter("waste").getValue();
			// the following is only for debugging
			
//			if(waste==null)
//			{
//				RProcessGoal	proc	= (RProcessGoal)((ElementWrapper)getRootGoal()).unwrap();
//				IRGoal	orig	= proc.getProprietaryGoal();
//				throw new RuntimeException("Waste is null: " + proc.getParameter("waste") + ", "+orig.getParameter("waste"));
//			}
			//System.out.println("Pickup goal created.");
			IGoal pickup = createGoal("achievepickupwaste");
			pickup.getParameter("waste").setValue(waste);
			dispatchSubgoalAndWait(pickup);
		}

		boolean	dropped	= false;
		while(!dropped)
		{
			// Move to a not full waste-bin
			IGoal findbin = createGoal("querywastebin");
			dispatchSubgoalAndWait(findbin);
			Wastebin wastebin = (Wastebin)findbin.getParameter("result").getValue();

			// Retry, when wastebin is full in meantime.
			try
			{
				IGoal	drop = createGoal("achievedropwaste");
				drop.getParameter("wastebin").setValue(wastebin);
				dispatchSubgoalAndWait(drop);
				dropped	= true;
			}
//			catch(GoalFailureException e){}
			catch(Exception e)
			{
			}
		}
	}
}
