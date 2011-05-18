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
		double dice = Math.random();
		if (dice < 1.0) {
			ans.getParameter(SFipa.CONTENT).setValue(Status.OK);
		} else if (1.0 <= dice && dice < 1.0) {
			ans.getParameter(SFipa.CONTENT).setValue(Status.ERROR);
		} else {
			ans.getParameter(SFipa.CONTENT).setValue(Status.OFFLINE);
		}
		sendMessage(ans);
	}

}
