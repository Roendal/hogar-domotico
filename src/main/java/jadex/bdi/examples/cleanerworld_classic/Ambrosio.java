package jadex.bdi.examples.cleanerworld_classic;

import java.awt.Rectangle;

import jadex.commons.SUtil;

/**
 * Editable Java class for concept Cleaner of cleaner-generated ontology.
 */
public class Ambrosio extends LocationObject {
	// -------- attributes ----------

	protected int hour;

	// -------- constructors --------

	/**
	 * Create a new Cleaner.
	 */
	public Ambrosio() {
		// Empty constructor required for JavaBeans (do not remove).
	}

	// LSIN*Eduardo* Inicio
	/**
	 * Create a new Cleaner. Modified.
	 */
	public Ambrosio(int hour) {
		this.hour = hour;
	}

	public void updateHour(int hour) {
		//?
	}
}
