package jadex.bdi.examples.cleanerworld;

import jadex.application.space.envsupport.environment.IEnvironmentSpace;
import jadex.application.space.envsupport.environment.ISpaceAction;
import jadex.application.space.envsupport.environment.ISpaceObject;
import jadex.application.space.envsupport.environment.space2d.Space2D;
import jadex.application.space.envsupport.math.IVector1;
import jadex.application.space.envsupport.math.IVector2;
import jadex.application.space.envsupport.math.Vector1Double;
import jadex.bridge.IComponentIdentifier;
import jadex.commons.SimplePropertyObject;

import java.util.Map;

/**
 *  Action for dropping a waste.
 */
public class DropWasteAction extends SimplePropertyObject implements ISpaceAction
{
	protected static final IVector1 TOLERANCE = new Vector1Double(0.05);
	
	/**
	 * Performs the action.
	 * @param parameters parameters for the action
	 * @param space the environment space
	 * @return action return value
	 */
	public Object perform(Map parameters, IEnvironmentSpace space)
	{	
		Space2D env = (Space2D)space;
		
		IComponentIdentifier owner = (IComponentIdentifier)parameters.get(ISpaceAction.ACTOR_ID);
		ISpaceObject wastebin = (ISpaceObject)parameters.get(ISpaceAction.OBJECT_ID);
		ISpaceObject waste = (ISpaceObject)parameters.get("waste");
		ISpaceObject avatar = env.getAvatar(owner);

		assert avatar.getProperty("waste")!=null: avatar;
		
		if(env.getDistance((IVector2)wastebin.getProperty(Space2D.PROPERTY_POSITION), (IVector2)avatar.getProperty(Space2D.PROPERTY_POSITION)).greater(TOLERANCE))
			throw new RuntimeException("Not near enough to wastebin: "+wastebin+" "+avatar);
			
//		System.out.println("drop: "+waste);
		if(((Boolean)wastebin.getProperty("full")).booleanValue())
			throw new RuntimeException("Wastebin already full: "+wastebin+" "+avatar);

		int wastes = ((Integer)wastebin.getProperty("wastes")).intValue();
		wastebin.setProperty("wastes", new Integer(wastes+1));
		env.destroySpaceObject(waste.getId());
		avatar.setProperty("waste", null);

//		System.out.println("pickup waste action "+parameters);

		return null;
	}
}
