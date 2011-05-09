package jadex.bdi.examples.cleanerworld_classic.ambrosio;


import jadex.bdi.examples.cleanerworld_classic.Chargingstation;
import jadex.bdi.examples.cleanerworld_classic.Cleaner;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.examples.cleanerworld_classic.MapPoint;
import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.examples.cleanerworld_classic.Wastebin;
import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bdi.runtime.IExpression;
import jadex.bdi.runtime.IGoal;
import jadex.bridge.ComponentTerminatedException;
import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.commons.IFuture;
import jadex.commons.concurrent.SwingDefaultResultListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 *  Panel for showing the cleaner world view.
 */
class AmbrosioPanel extends JPanel
{
	//-------- attributes --------
	
	/** The cleaner agent. */
	protected IExternalAccess	agent;
		
	/** The latest world view information. */
	protected DrawData	drawdata;
		
	/** Flag to indicate that the draw data is currently updated to avoid multiple updates in parallel. */
	protected boolean	updating;
		
	//-------- constructors --------

	/**
	 *  Create a cleaner panel.
	 */
	public AmbrosioPanel(IExternalAccess agent)
	{
		this.agent = agent;
	}
	
	//-------- JPanel methods --------

	/**
	 *  Paint the world view.
	 */
	protected void	paintComponent(Graphics g)
	{
		if(!updating)
		{
			updating	= true;
			try
			{
				IFuture	fut	= agent.scheduleStep(new UpdateStep());
				fut.addResultListener(new SwingDefaultResultListener()
				{
					public void customResultAvailable(Object source, Object result)
					{
						AmbrosioPanel.this.drawdata	= (DrawData)result;
						updating	= false;
					}
					public void customExceptionOccurred(Object source, Exception exception)
					{
//						exception.printStackTrace();
//						updating	= false;	// Keep to false to disable any more updates
					}
				});
			}
			catch(ComponentTerminatedException e) 
			{
				// Keep updating to false to disable any more updates
			}
		}
			
		if(drawdata!=null)
		{			
			// Paint background (dependent on daytime).
			Rectangle	bounds	= getBounds();
			g.setColor(Color.lightGray);
			g.fillRect(0, 0, bounds.width, bounds.height);
			//PINTAR
		}
	}
	
	//-------- helper methods --------

	/**
	 *  Get the on screen location for a location in  the world.
	 */
	protected static Point	onScreenLocation(Location loc, Rectangle bounds)
	{
		assert loc!=null;
		assert bounds!=null;
		return new Point((int)(bounds.width*loc.getX()),
			(int)(bounds.height*(1.0-loc.getY())));
	}
	
	//-------- helper classes --------
	
	/**
	 *  Component step to produce an uptodate draw data.
	 */
	public static class UpdateStep implements IComponentStep
	{
		public Object execute(IInternalAccess ia)
		{
			IBDIInternalAccess bia = (IBDIInternalAccess)ia;
			DrawData	drawdata	= new DrawData();
			
			return drawdata;
		}
	}

	/**
	 *  Data for drawing.
	 */
	public static class DrawData
	{
		// Allow object being transferred as XML using public fields.
		public static boolean XML_INCLUDE_FIELDS = true;
		
	}
}