package jadex.bdi.examples.cleanerworld_classic;


/**
 * Editable Java class for concept Cleaner of cleaner-generated ontology.
 */
public class Ambrosio extends LocationObject {
	
	// LSIN *Alicia* Inicio
	
	// -------- attributes ----------
	/** Indica si la alarma está sonando */ 
	private static boolean alarm = false;	
	private static boolean[] roomPresence = {false,false,false,false};
	
	// -------- getters and setters ------
	

	public static boolean[] getRoomPresence() {
		return roomPresence;
	}
	
	public static void setRoomPresence(boolean[] roomPresence) {
		Ambrosio.roomPresence = roomPresence;
	}
	
	/**
	 * Activar la alarma
	 */
	public static void activateAlarm(){
		alarm =true;
	}
	
	/**
	 * Desactivar la alarma
	 */
	public static void deactivateAlarm(){
		alarm=false;
	}
	
	/**
	 * Indica si la alarma está sonando
	 * @return boolean true si está sonando
	 */
	public static boolean isAlarmOn(){
		return alarm;
	}
	// LSIN *Alicia* Fin
	
	// -------- constructors --------

	/**
	 * Create a new Ambrosio.
	 */
	public Ambrosio() {
		// Empty constructor required for JavaBeans (do not remove).
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
