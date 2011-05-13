package jadex.bdi.examples.cleanerworld_classic;

public class Date {


	/** Tiempo transcurrido (en milisegundos) */
	private long millis;
	
	/** Determina la duracion en milisegundos de medio dia (dï¿½a o noche) */
	public final long HALF_DAY= 20000;
	public final long DAY= 2*HALF_DAY;
	
	/** Duración de un minuto de la simulacion en ms reales */
	public final long MINUTE= DAY /(60*24);
	
	/** Duración de las etapas del dia (porcentaje) */
	public final double DAWN= 0.25; //hasta las 6 de la mañana
	public final double TWILIGHT= 0.83333333333333; //hasta las 8 de la noche
	
	/** Array de dï¿½as de la semana*/
	public final String[] WEEK= {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
	

	/**
	 * Constructor
	 */
	public Date (){
	
		this.millis=0;
	}
	
	/**
	 * Devuelve el tiempo transcurrido en milisegundos
	 * 
	 * @return los milisegundos transcurridos hasta ese momento
	 */
	public synchronized long getMillis(){
		return this.millis;
	}
	
	/**
	 * Devuelve la hora del día
	 * 
	 * @return String con la hora del día
	 */
	public synchronized String getHour(){
		double parteDia=  this.millis%DAY;
		int min= (int)Math.rint(parteDia/MINUTE);
		int hora= 0;
		String minutosDecenas="";
		String horasDecenas="";

		if(min>=60){
			hora=min/60;
			min=min%60;
		}
		hora=hora%24;
		if (min<10){
			minutosDecenas="0";
		}if (hora<10){
			horasDecenas=" ";
		}
		return horasDecenas+hora+":"+minutosDecenas+min;
		
	}
	
	/**
	 * Incrementa el tiempo transcurrido
	 * 
	 * @param time valor en milisegundos en que se quiere incrementar
	 */
	public synchronized void addTime(double time){
		this.millis+=time;
		//controlaDiaNoche();
	}
	
	/**
	 * Devuelve el dï¿½a de la semana en base al tiempo transcurrido
	 * 
	 * @return String con el nombre del dï¿½a
	 */
	public synchronized String getDay(){
		int dia= (int)(this.millis/ (DAY))%7;
		return WEEK[dia];
	}
	
	/**
	 * Indica si es día o noche
	 * 
	 * @return true si es de día
	 * 			false si es de noche
	 */
	public synchronized boolean isDay(){
		double parteDia=this.millis%DAY;
		boolean day; 
		if (parteDia<DAY*DAWN){
			day = false;
		}else if(parteDia<DAY*TWILIGHT){
			day = true;
		}else{
			day =false;
		}
		return day;
	}
	
	
}
