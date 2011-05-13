package jadex.bdi.examples.cleanerworld_classic;

public class Date {


	/** Tiempo transcurrido (en milisegundos) */
	private long millis;
	
	/** Duración del minuto simulado en milisegundos */
	private int minuteDuration;
	
	/** Duración del día simulado en milisegundos */
	private int dayDuration;
	
	/** Determina la duracion aproximada en milisegundos de medio dia (día o noche) */
	private final long HALF_DAY= 20000; 
	
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
		this.minuteDuration= (int)Math.rint(2*HALF_DAY /(60*24));
		this.dayDuration=this.minuteDuration*60*24;
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
	 * Devuelve la hora del día y sus minutos
	 * 
	 * @return String con la hora del día hh:mm	
	 */
	public synchronized String getTimeWatch(){
		int [] watch=hourAndMinutes(); 
		String decMin="";
		String decHour="";

		if (watch[0]<10){
			decMin="0";
		}if (watch[1]<10){
			decHour=" ";
		}
		return decHour+watch[1]+":"+decMin+watch[0];
		
	}
	/**
	 * Devuelve únicamente la hora 
	 * 
	 * @return int hora (formato de 24h)
	 */
	public synchronized int getHour(){
		int [] watch=hourAndMinutes(); 
		return watch[1];
	}
	
	/**
	 * Todos los resultados relevantes en un String
	 * 
	 * @return String con la información
	 */
	public synchronized String toString(){
		return "Son las "+getTimeWatch()+" del "+getDay()+" (han pasado "
				+this.millis+" milisegundos desde el inicio). ¿Es de dia? "+isDay();
	}

	/**
	 * Incrementa el tiempo transcurrido
	 * 
	 * @param time valor en milisegundos en que se quiere incrementar
	 */
	public synchronized void addTime(double time){
		this.millis+=time;
	}
	
	/**
	 * Devuelve el dï¿½a de la semana en base al tiempo transcurrido
	 * 
	 * @return String con el nombre del dï¿½a
	 */
	public synchronized int getDayNumber(){
		int day= (int)(this.millis/(this.dayDuration))%7;
		return day;
	}
	
	public synchronized String getDay(){
		int day= (int)(this.millis/(this.dayDuration))%7;
		return WEEK[day];
	}
	
	/**
	 * Indica si es día o noche
	 * 
	 * @return true si es de día
	 * 			false si es de noche
	 */
	public synchronized boolean isDay(){
		double dayPortion=this.millis%(this.dayDuration);
		boolean day=false; 
		if((dayPortion>this.dayDuration*DAWN)&&
				(dayPortion<this.dayDuration*TWILIGHT)){
			day = true;
		}
		return day;
	}
	/**
	 * Devuelve un array con el número de horas y minutos
	 * en formato de 24 horas.
	 * 
	 * @return int[] con [0]=> número de minutos
	 * 					 [1]=> número de horas
	 */
	private synchronized int[] hourAndMinutes(){
		double dayPortion=  this.millis%(this.dayDuration);
		int []result= new int[2];
		result[0]= (int)Math.rint(dayPortion/this.minuteDuration);
		result[1]= 0;
		
		if(result[0]>=60){
			result[1]=result[0]/60;
			result[0]=result[0]%60;
		}
		result[1]=result[1]%24;

		return result;
	}
}
