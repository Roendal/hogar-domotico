package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.runtime.Plan;

import java.awt.Toolkit;

public class ActivateAlarmPlan extends Plan {
	// -------- constructors --------

	private int alarm_time = 120;
	private int alarm_times = 4;
	private int alarm_wave_wait = 250;
	private int alarm_waves = 2;

	/**
	 * Create a new plan.
	 */
	public ActivateAlarmPlan() {
	}

	// -------- methods --------

	/**
	 * The plan body.
	 */
	public void body() {
		System.out.println("RINGGGGGGGGGGGG!");
		for (int j = 0; j < alarm_waves; j++) {
			for (int i = 0; i < alarm_times; i++) {
				Toolkit.getDefaultToolkit().beep();
				waitFor(alarm_time);
			}
			waitFor(alarm_wave_wait);
		}

		getBeliefbase().getBelief("alarmcondition").setFact(false);
	}

}
