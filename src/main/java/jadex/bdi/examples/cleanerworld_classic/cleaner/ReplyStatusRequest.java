package jadex.bdi.examples.cleanerworld_classic.cleaner;

import jadex.base.fipa.SFipa;
import jadex.bdi.examples.cleanerworld_classic.Status;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;

public class ReplyStatusRequest extends Plan {

	public ReplyStatusRequest() {

	}

	public void body() {
		IMessageEvent message = (IMessageEvent) getReason();
		// Responder
		IMessageEvent ans = getEventbase().createReply(message, "inform");
		if (Math.random() < 0.75) {
			ans.getParameter(SFipa.CONTENT).setValue(Status.OK);
		} else {
			ans.getParameter(SFipa.CONTENT).setValue(Status.ERROR);
		}
		sendMessage(ans);
	}

}
