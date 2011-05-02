package jadex.bdi.examples.cleanerworld;

import jadex.application.space.envsupport.environment.AbstractTask;
import jadex.application.space.envsupport.environment.IEnvironmentSpace;
import jadex.application.space.envsupport.environment.ISpaceObject;
import jadex.application.space.envsupport.environment.space2d.Space2D;
import jadex.application.space.envsupport.math.IVector2;
import jadex.commons.service.clock.IClockService;

/**
 *  Move an object towards a destination.
 */
public class MoveTask extends AbstractTask
{
	//-------- constants --------

	/** The destination property. */
	public static final String	PROPERTY_TYPENAME = "move";
	
	/** The destination property. */
	public static final String	PROPERTY_DESTINATION = "destination";

	/** The speed property (units per second). */
	public static final String	PROPERTY_SPEED	= "speed";
	
	/** The vision property (radius in units). */
	public static final String	PROPERTY_VISION	= "vision";
	
	/** The energy charge state. */
	public static final String	PROPERTY_CHARGESTATE	= "chargestate";
	
	//-------- AbstractTask methods --------
	
	/**
	 *  Executes the task.
	 *  Handles exceptions. Subclasses should implement doExecute() instead.
	 *  @param space	The environment in which the task is executing.
	 *  @param obj	The object that is executing the task.
	 *  @param progress	The time that has passed according to the environment executor.
	 */
	public void execute(IEnvironmentSpace space, ISpaceObject obj, long progress, IClockService clock)
	{
		IVector2 destination = (IVector2)getProperty(PROPERTY_DESTINATION);
		
		double speed = ((Number)obj.getProperty(PROPERTY_SPEED)).doubleValue();
		double maxdist = progress*speed*0.001;
		double energy = ((Double)obj.getProperty(PROPERTY_CHARGESTATE)).doubleValue();
		IVector2 loc = (IVector2)obj.getProperty(Space2D.PROPERTY_POSITION);
		
		// Todo: how to handle border conditions!?
		IVector2 newloc	= ((Space2D)space).getDistance(loc, destination).getAsDouble()<=maxdist? 
			destination : destination.copy().subtract(loc).normalize().multiply(maxdist).add(loc);

		if(energy>0)
		{
			energy = Math.max(energy-maxdist/5, 0);
			obj.setProperty(PROPERTY_CHARGESTATE, new Double(energy));
			((Space2D)space).setPosition(obj.getId(), newloc);
		}
		else
		{
			throw new RuntimeException("Energy too low.");
		}
		
		if(newloc==destination)
			setFinished(space, obj, true);
	}
	
	/**
	 *  Get the string representation.
	 *  @return The string representation.
	 * /
	public String toString()
	{
		return SReflect.getUnqualifiedClassName(this.getClass());
	}*/
	
	/**
	 * 
	 */
	public static boolean isGreater(IVector2 pos, IVector2 pos1, IVector2 pos2)
	{
		return pos.getDistance(pos1).greater(pos.getDistance(pos2));
	}
}
