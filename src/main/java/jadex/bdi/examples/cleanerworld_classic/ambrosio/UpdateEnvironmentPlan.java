package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Date;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.runtime.IGoal;
import jadex.bdi.runtime.IMessageEvent;
import jadex.bdi.runtime.Plan;
import jadex.base.fipa.IDF;
import jadex.base.fipa.IDFComponentDescription;
import jadex.base.fipa.IDFServiceDescription;
import jadex.base.fipa.SFipa;
import jadex.bridge.ISearchConstraints;
import jadex.commons.service.SServiceProvider;

public class UpdateEnvironmentPlan extends Plan {
	// -------- constructors --------

	private static int hour = -1;
	private static int day = -1;
	private static boolean ringedToday = false;

	/**
	 * Create a new plan.
	 */
	public UpdateEnvironmentPlan() {
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		Date date = ((Environment) getBeliefbase().getBelief("environment").getFact()).getDate();
		// LSIN *Alicia* Inicio
		Ambrosio.setDaytime(date.isDay());
		// LSIN *Alicia* Fin
		getBeliefbase().getBelief("current_date").setFact(date);
		if (ringedToday) {
			getBeliefbase().getBelief("alarmcondition").setFact(false);
		} else {
			if (Ambrosio.shouldActivateAlarm(date)) {
				getBeliefbase().getBelief("alarmcondition").setFact(true);
				ringedToday = true;
			}
		}
		if (hour != date.getHour()) {
			
			sendPruebaMessage();
			hour = date.getHour();
			
			if (day != date.getDayNumber()) {
				
				day = date.getDayNumber();
				ringedToday = false;
			}
			// Trazas
			System.out.println("Día: " + day + " hora: " + hour + ":00");
		}
		
		Ambrosio.setRoomPresence(((Environment) getBeliefbase().getBelief("environment").getFact()).getRoomPresence());	

	}
	
	private void sendPruebaMessage(){
		// LSIN *Alicia* Inicio - PRUEBA

		// Create a service description to search for.
		IDF	df	= (IDF)SServiceProvider.getService(getScope().getServiceProvider(), IDF.class).get(this);
		IDFServiceDescription sd = df.createDFServiceDescription("service_cleaner", null, null);
		IDFComponentDescription dfadesc = df.createDFComponentDescription(null, sd);

		// A hack - default is 2! to reach more Agents, we have
		// to increase the number of possible results.
		ISearchConstraints constraints = df.createSearchConstraints(-1, 0);

		// Use a subgoal to search
		IGoal ft = createGoal("dfcap.df_search");
		ft.getParameter("description").setValue(dfadesc);
		ft.getParameter("constraints").setValue(constraints);

		dispatchSubgoalAndWait(ft);
		//Object result = ft.getResult();
		IDFComponentDescription[] cleaners = (IDFComponentDescription[])ft.getParameterSet("result").getValues();
		//System.out.println(cleaners.length);
		if(cleaners!=null && cleaners.length>0){
			for (int i=0; i<cleaners.length; i++){
				System.out.println("Mando un mensaje a :"+i);
				IMessageEvent mevent = createMessageEvent("request");
				mevent.getParameterSet(SFipa.RECEIVERS).addValue(cleaners[i].getName());
				mevent.getParameter(SFipa.CONTENT).setValue("HELLO!! Hour: "+ this.hour+ ". Day: "+this.day);
				
				IMessageEvent reply= sendMessageAndWait(mevent, 10000);
				String messageContent = (String)reply.getParameter(SFipa.CONTENT).getValue();
				System.out.println(messageContent);

			}
		    
		}
		// LSIN *Alicia* Fin - PRUEBA
	}

}
