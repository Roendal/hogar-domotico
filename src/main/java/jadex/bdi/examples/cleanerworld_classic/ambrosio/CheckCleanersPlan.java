package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.base.fipa.IDF;
import jadex.base.fipa.IDFComponentDescription;
import jadex.base.fipa.IDFServiceDescription;
import jadex.base.fipa.SFipa;
import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;
import jadex.bridge.ISearchConstraints;
import jadex.commons.service.SServiceProvider;

public class CheckCleanersPlan extends Plan {
	// -------- constructors --------

	/**
	 * Create a new plan.
	 */
	public CheckCleanersPlan() {
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		// LSIN *Alicia* Inicio - PRUEBA

		// Create a service description to search for.
		IDF df = (IDF) SServiceProvider.getService(
				getScope().getServiceProvider(), IDF.class).get(this);
		IDFServiceDescription sd = df.createDFServiceDescription(
				"service_cleaner", null, null);
		IDFComponentDescription dfadesc = df.createDFComponentDescription(null,
				sd);

		// A hack - default is 2! to reach more Agents, we have
		// to increase the number of possible results.
		ISearchConstraints constraints = df.createSearchConstraints(-1, 0);

		// Use a subgoal to search
		IGoal ft = createGoal("dfcap.df_search");
		ft.getParameter("description").setValue(dfadesc);
		ft.getParameter("constraints").setValue(constraints);

		dispatchSubgoalAndWait(ft);
		// Object result = ft.getResult();
		IDFComponentDescription[] cleaners = (IDFComponentDescription[]) ft
				.getParameterSet("result").getValues();
		// System.out.println(cleaners.length);
		if (cleaners != null && cleaners.length > 0) {
			for (int i = 0; i < cleaners.length; i++) {
				IMessageEvent mevent = createMessageEvent("request");
				mevent.getParameterSet(SFipa.RECEIVERS).addValue(
						cleaners[i].getName());
				IMessageEvent reply = sendMessageAndWait(mevent, 10000);
				// Y SI MUERE ?
				String messageContent = (String) reply.getParameter(
						SFipa.CONTENT).getValue();
				Ambrosio.cleanersStatus[i] = (String) reply.getParameter(
						SFipa.CONTENT).getValue();
			}
			System.out.println("Estado limpiadores: " + Ambrosio.cleanersStatus[0]
					+ ", " + Ambrosio.cleanersStatus[1] + ", "
					+ Ambrosio.cleanersStatus[2] + ", "
					+ Ambrosio.cleanersStatus[3] + "");
		}
		// LSIN *Alicia* Fin - PRUEBA
	}

}
