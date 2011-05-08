package jadex.bdi.examples.cleanerworld_classic;

import jadex.commons.SimplePropertyChangeSupport;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  The environment object for non distributed applications.
 */
public class Environment implements IEnvironment
{
	//-------- class attributes --------

	/** The singleton. */
	protected static Environment instance;

	//-------- attributes --------

	/** The daytime. */
	protected boolean daytime;

	/** The cleaners. */
	protected List cleaners;

	/** The wastes. */
	protected List wastes;

	/** The waste bins. */
	protected List wastebins;

	/** The charging stations. */
	protected List stations;

	/** The cleaner ages. */
	protected Map ages;
	
	//LSIN *Alicia* INICIO 
	/** Tiempo transcurrido (en milisegundos) */
	private long millis=0;
	
	/** Determina la duracion en milisegundos de medio dia (dï¿½a o noche) */
	public final long HALF_DAY= 20000;
	public final long DAY= 2*HALF_DAY;
	
	/** Duración de un minuto de la simulacion en ms reales */
	public final long MINUTE= DAY /(60*24);
	
	/** Duración de las etapas del dia (porcentaje) */
	public final double DAWN= 0.25; //hasta las 6 de la mañana
	public final double TWILIGHT= 0.83333333333333; //hasta las 8 de la noche
	
	/** Array de dï¿½as de la semana*/
	public final String[] SEMANA= {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
	
	//LSIN *Alicia* FIN 
	
	/** The helper object for bean events. */
	public SimplePropertyChangeSupport pcs;
	
	//-------- constructors --------

	/**
	 *  Create a new environment.
	 */
	public Environment()
	{

		this.daytime = false;
		this.cleaners = new ArrayList();
		this.wastes = new ArrayList();
		this.wastebins = new ArrayList();
		this.stations = new ArrayList();
		this.ages = new HashMap();
		this.pcs = new SimplePropertyChangeSupport(this);

		// Add some things to our world.
		//LSIN*Csar* Inicio
		addWaste(new Waste(CleanerLocationManager.randomLocationInRoomWithMargin(0)));
		addWaste(new Waste(CleanerLocationManager.randomLocationInRoomWithMargin(1)));
		addWaste(new Waste(CleanerLocationManager.randomLocationInRoomWithMargin(2)));
		addWaste(new Waste(CleanerLocationManager.randomLocationInRoomWithMargin(3)));
		//LSIN*Csar* Fin
		
		//LSIN*Eduardo* Inicio
		addWastebin(new Wastebin(new Location(0.214, 0.075), 20));
		addWastebin(new Wastebin(new Location(0.214, 0.935), 20));
		addWastebin(new Wastebin(new Location(0.87, 0.075), 20));
		addWastebin(new Wastebin(new Location(0.87, 0.935), 20));
		addChargingStation(new Chargingstation(new Location(0.254, 0.075)));
		addChargingStation(new Chargingstation(new Location(0.254, 0.935)));
		addChargingStation(new Chargingstation(new Location(0.91, 0.075)));
		addChargingStation(new Chargingstation(new Location(0.91, 0.935)));
		//LSIN*Eduardo* Fin
	}

	/**
	 *  Get the singleton.
	 *  @return The environment.
	 */
	public static synchronized Environment getInstance()
	{
		if(instance==null)
		{
			instance = new Environment();
		}
		return instance;
	}

	//-------- cleaner actions from IEnvironment --------

	/**
	 *  Get the current vision.
	 *  @param cleaner The cleaner.
	 *  @return The current vision, null if failure.
	 */
	public synchronized Vision getVision(Cleaner cleaner)
	{
		// Update cleaner
		if(cleaners.contains(cleaner))
			cleaners.remove(cleaner);
		cleaners.add(cleaner);
		// reset age for cleaner
		ages.put(cleaner, new Integer(0));

		//System.out.println("getVision: "+this.hashCode()+" "+cleaners.size());

		Location cloc = cleaner.getLocation();
		double range = cleaner.getVisionRange();

		ArrayList nearwastes = new ArrayList();
		for(int i=0; i<wastes.size(); i++)
		{
			LocationObject	obj	= (LocationObject)wastes.get(i);
			if(obj.getLocation().isNear(cloc, range))
				nearwastes.add(obj.clone());
		}

		ArrayList nearwastebins = new ArrayList();
		for(int i=0; i<wastebins.size(); i++)
		{
			LocationObject	obj	= (LocationObject)wastebins.get(i);
			if(obj.getLocation().isNear(cloc, range))
				nearwastebins.add(obj.clone());
		}

		ArrayList nearstations = new ArrayList();
		for(int i=0; i<stations.size(); i++)
		{
			LocationObject	obj	= (LocationObject)stations.get(i);
			if(obj.getLocation().isNear(cloc, range))
				nearstations.add(obj.clone());
		}

		ArrayList nearcleaners = new ArrayList();
		for(int i=0; i<cleaners.size(); i++)
		{
			LocationObject	obj	= (LocationObject)cleaners.get(i);
			if(obj.getLocation().isNear(cloc, range))
				nearcleaners.add(obj.clone());
		}

		// Construct the ontology vision object.
		Vision v = new Vision(nearwastes, nearwastebins, nearstations, nearcleaners, getDaytime());

		return v;
	}

	/**
	 *  Try to pick up some piece of waste.
	 *  @param waste The waste.
	 *  @return True if the waste could be picked up.
	 */
	public synchronized boolean pickUpWaste(Waste waste)
	{
		// Todo: Implement random failure?
		boolean ret	= wastes.remove(waste);

//		if(!ret)
//		{
//			System.out.println("Failed to pick up: "+waste);
//			System.out.println("Wastes: "+wastes);
//		}

		return ret;
	}

	/**
	 *  Drop a piece of waste.
	 */
	public synchronized boolean dropWasteInWastebin(Waste waste, Wastebin wastebin)
	{
		assert waste!=null;
		
//		if(waste==null)
//			System.out.println("here");
		
		boolean ret = false;

		wastebin	= getWastebin(wastebin);
		if(!wastebin.contains(waste) && !wastebin.isFull())
		{
			wastebin.addWaste((Waste)waste.clone());
			ret = true;
		}
		return ret;
	}

	/**
	 *  Get a wastebin for a template.
	 *  @return The wastebin.
	 */
	protected synchronized Wastebin	getWastebin(Wastebin wb)
	{
		Wastebin ret = null;
		for(int i=0; i<wastebins.size() && ret==null; i++)
		{
			if(wb.equals(wastebins.get(i)))
				ret = (Wastebin)wastebins.get(i);
		}
		return ret;
	}

	//-------- methods --------

	//LSIN *Alicia* INICIO
	/**
	 * Devuelve el tiempo transcurrido en milisegundos
	 * 
	 * @return los milisegundos transcurridos hasta ese momento
	 */
	public synchronized long getTiempo(){
		return this.millis;
	}
	
	/**
	 * Devuelve la hora del día
	 * 
	 * @return String con la hora del día
	 */
	public synchronized String getHora(){
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
	 * Incrementa el tiempo transcurrido en 50ms
	 */
	public synchronized void addTiempo(){
		this.millis+=50;
		controlaDiaNoche();
	}
	
	/**
	 * Devuelve el dï¿½a de la semana en base al tiempo transcurrido
	 * @return String con el nombre del dï¿½a
	 */
	public synchronized String getDia(){
		int dia= (int)(this.millis/ (DAY))%7;
		return SEMANA[dia];
	}
	
	/**
	 * Cambia a dï¿½a o noche segï¿½n el tiempo transcurrido
	 */
	private synchronized void controlaDiaNoche(){
		double parteDia=this.millis%DAY;
		if (parteDia<DAY*DAWN){
			setDaytime(false);
		}else if(parteDia<DAY*TWILIGHT){
			setDaytime(true);
		}else{
			setDaytime(false);
		}
	}
	
	//LSIN *Alicia* FIN
	//LSIN*Csar* INICIO
	/**
	 * Waste generator (better implementations are coming)
	 */
	public synchronized void getDirtyRooms(){
		//generate one waste in each room, every day
		if (((this.millis%(HALF_DAY*2)) > -25) && ((this.millis%(HALF_DAY*2)) <= 25)){
			for (int i=0; i < CleanerLocationManager.TOTAL_ROOMS; i++){
				addWaste(new Waste(CleanerLocationManager.randomLocationInRoomWithMargin(i)));
			}
		}
	}
	//LSIN*Csar* FIN
	/**
	 *  Get the complete vision.
	 *  @return The current vision, null if failure.
	 */
	public synchronized Vision getCompleteVision()
	{
		// Construct the ontology vision object.
		return new Vision(wastes, wastebins, stations, cleaners, getDaytime());
	}

	/**
	 *  Get the daytime.
	 *  @return The current vision.
	 */
	public synchronized boolean getDaytime()
	{
		return daytime;
	}

	/**
	 *  Set the daytime.
	 *  @param daytime The daytime.
	 */
	public synchronized void setDaytime(boolean daytime)
	{
		this.daytime = daytime;
		this.pcs.firePropertyChange("daytime", null, new Boolean(daytime));
	}

	/**
	 *  Add a cleaner.
	 *  @param cleaner The cleaner.
	 */
	public synchronized void addCleaner(Cleaner cleaner)
	{
		cleaners.add(cleaner);
		this.pcs.firePropertyChange("cleaners", null, cleaner);
	}

	/**
	 *  Remove a cleaner.
	 *  @param cleaner The cleaner.
	 */
	public synchronized void removeCleaner(Cleaner cleaner)
	{
		cleaners.remove(cleaner);
		this.pcs.firePropertyChange("cleaners", null, cleaner);
	}

	/**
	 *  Add a piece of waste.
	 *  @param waste The new piece of waste.
	 */
	public synchronized void addWaste(Waste waste)
	{
		wastes.add(waste);
		this.pcs.firePropertyChange("wastes", null, waste);
	}

	/**
	 *  Remove a piece of waste.
	 *  @param waste The piece of waste.
	 */
	public synchronized void removeWaste(Waste waste)
	{
		wastes.remove(waste);
		this.pcs.firePropertyChange("wastes", waste, null);
	}

	/**
	 *  Add a wastebin.
	 *  @param wastebin The new waste bin.
	 */
	public synchronized void addWastebin(Wastebin wastebin)
	{
		wastebins.add(wastebin);
		this.pcs.firePropertyChange("wastebins", null, wastebin);
	}

	/**
	 *  Add a charging station.
	 *  @param station The new charging station.
	 */
	public synchronized void addChargingStation(Chargingstation station)
	{
		stations.add(station);
		this.pcs.firePropertyChange("chargingstations", null, station);
	}

	/**
	 *  Get all wastes.
	 *  @return All wastes.
	 */
	public synchronized Waste[] getWastes()
	{
		return (Waste[])wastes.toArray(new Waste[wastes.size()]);
	}

	/**
	 *  Get all wastebins.
	 *  @return All wastebins.
	 */
	public synchronized Wastebin[] getWastebins()
	{
		return (Wastebin[])wastebins.toArray(new Wastebin[wastebins.size()]);
	}

	/**
	 *  Get all charging stations.
	 *  @return All stations.
	 */
	public synchronized Chargingstation[] getChargingstations()
	{
		return (Chargingstation[])stations.toArray(new Chargingstation[stations.size()]);
	}

	/**
	 *  Get all cleaners.
	 *  @return All cleaners.
	 */
	public synchronized Cleaner[] getCleaners()
	{
		Cleaner[] cls = (Cleaner[])cleaners.toArray(new Cleaner[cleaners.size()]);
		// Increase ages of cleaners
		for(int i=0; i<cls.length; i++)
		{
			if(!ages.containsKey(cls[i]))
			{
				ages.put(cls[i], new Integer(0));
			}
			else
			{
				int age = ((Integer)ages.get(cls[i])).intValue();
				if(age>100)
				{
					removeCleaner(cls[i]);
					ages.remove(cls[i]);
				}
				else
				{
					ages.put(cls[i], new Integer(age+1));
				}
			}
		}
		return (Cleaner[])cleaners.toArray(new Cleaner[cleaners.size()]);
	}

	/**
	 *  Get a wastebin for a name.
	 *  @return The wastebin.
	 */
	public synchronized Wastebin getWastebin(String name)
	{
		Wastebin ret = null;
		for(int i=0; i<wastebins.size() && ret==null; i++)
		{
			if(((Wastebin)wastebins.get(i)).getName().equals(name))
				ret = (Wastebin)wastebins.get(i);
		}
		return ret;
	}

	/**
	 *  Clear the environment.
	 */
	public synchronized void clear()
	{
		cleaners.clear();
		wastes.clear();
		wastebins.clear();
		stations.clear();
		daytime = true;
	}

	/**
	 *  Get the age of a cleaner.
	 *  @return The age.
	 */
	public synchronized int getAge(Cleaner cleaner)
	{
		int ret = 0;
		Integer age = (Integer)ages.get(cleaner);
		if(age!=null)
			ret = age.intValue();
		return ret;
	}

	//-------- property methods --------

	/**
     *  Add a PropertyChangeListener to the listener list.
     *  The listener is registered for all properties.
     *  @param listener  The PropertyChangeListener to be added.
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
	{
		pcs.addPropertyChangeListener(listener);
    }

    /**
     *  Remove a PropertyChangeListener from the listener list.
     *  This removes a PropertyChangeListener that was registered
     *  for all properties.
     *  @param listener  The PropertyChangeListener to be removed.
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener)
	{
		pcs.removePropertyChangeListener(listener);
    }

}
