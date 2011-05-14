package jadex.bdi.examples.cleanerworld_classic;


/**
 * Editable Java class for concept Cleaner of cleaner-generated ontology.
 */
public class Ambrosio extends LocationObject {
	// -------- attributes ----------
	
	public static boolean alarm = false;
	
	// -------- constructors --------

	/**
	 * Create a new Cleaner.
	 */
	public Ambrosio() {
		// Empty constructor required for JavaBeans (do not remove).
	}
	
	public static void activateAlarm(){
		alarm =true;
	}
	
	public static void deactivateAlarm(){
		alarm=false;
	}
	
	// LSIN*Eduardo* Inicio
	public static boolean shouldActivateAlarm(Date date){
		boolean activate = false;
		if (date.getHour()==7){
			activate = true;
		}
		return activate;
	}
	// LSIN*Eduardo* Fin

}
