package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.runtime.Plan;
import jadex.base.fipa.SFipa;
import jadex.bdi.runtime.IMessageEvent;

public class PruebaPlan extends Plan{
	
	public PruebaPlan(){
		
	}
	
	public void body(){
		IMessageEvent message = (IMessageEvent)getReason();
		String messageContent = (String)message.getParameter(SFipa.CONTENT).getValue();
		System.out.println(messageContent);
		
	}

}
