package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.bdi.runtime.Plan;
import jadex.base.fipa.SFipa;
import jadex.bdi.runtime.IMessageEvent;
import jadex.standalone.service.*;

public class PruebaPlan extends Plan{
	
	public PruebaPlan(){
		
	}
	
	public void body(){
		IMessageEvent message = (IMessageEvent)getReason();
		String messageContent = (String)message.getParameter(SFipa.CONTENT).getValue();
		System.out.println(messageContent);
		System.out.println("ConID Original: "+message.getParameter("conversation_id").getValue());
		
		//Responder
		IMessageEvent ans = getEventbase().createReply(message, "inform");
		System.out.println("ConID Respuesta: "+ans.getParameter("conversation_id").getValue());
		String str= "I received your greetings";
		ans.getParameter(SFipa.CONTENT).setValue(str);
		sendMessage(ans);
	}

}
