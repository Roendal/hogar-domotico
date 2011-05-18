package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.examples.cleanerworld_classic.Status;
import jadex.bdi.runtime.Plan;
import jadex.base.fipa.IDFComponentDescription;
import jadex.base.fipa.SFipa;
import jadex.bdi.runtime.IMessageEvent;
import jadex.standalone.service.*;

public class PruebaPlan extends Plan{
	
	public PruebaPlan(){
		
	}
	
	public void body(){
		IMessageEvent message = (IMessageEvent)getReason();
		String messageContent = (String)message.getParameter(SFipa.CONTENT).getValue();
		//Responder
		IMessageEvent ans = getEventbase().createReply(message, "inform");		
		ans.getParameter(SFipa.CONTENT).setValue(Status.OK);		
		sendMessage(ans);
	}

}
