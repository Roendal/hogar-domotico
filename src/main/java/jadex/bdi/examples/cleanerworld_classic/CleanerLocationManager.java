package jadex.bdi.examples.cleanerworld_classic;

public class CleanerLocationManager {
	
	/** The instance counter. */
	protected static int instancecnt = 0;
	
	private final static int TOTAL_CLEANERS = 4;
	
	public static void updateInstanceCount(){
		instancecnt = (++instancecnt)%TOTAL_CLEANERS;
	}
	
	public static Location newInitialLocation(){
		Location[] locations = {new Location(0,0),new Location(0.33,0.33),new Location(0.66,0.66),new Location(1,1)};
		return locations[instancecnt];
	}
	
	public static Location newUpperLeftCorner(){
		Location[] locations = {new Location(0,0),new Location(0,0),new Location(0,0),new Location(0,0)};
		return locations[instancecnt];
	}

	public static Location newBottomRightCorner(){		
		Location[] locations = {new Location(0.5,0.5),new Location(0.5,0.5),new Location(0.5,0.5),new Location(0.5,0.5)};
		updateInstanceCount();
		return locations[instancecnt];
	}
}
