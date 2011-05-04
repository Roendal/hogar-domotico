package jadex.bdi.examples.cleanerworld_classic;

public class CleanerLocationManager {
	
	/** The instance counter. */
	protected static int instancecnt = 0;
	
	private final static int TOTAL_CLEANERS = 4;
	public final static int UPPER_LEFT = 0;
	public final static int BOTTOM_RIGHT = 0;
	public final static int CENTER = 0;	
	
	public final static Location[][] rooms = {
		//{upper_left,bottom_right,center}
		{new Location(0.026,0.382),new Location(0.632,0.033),new Location(0.334,0.205)},
		{new Location(0.026,0.967),new Location(0.632,0.567),new Location(0.334,0.779)},
		{new Location(0.683,0.967),new Location(0.974,0.567),new Location(0.822,0.779)},
		{new Location(0.683,0.382),new Location(0.974,0.033),new Location(0.822,0.205)},		
	};
	
	private static int updateInstanceCount(){
		int temp = instancecnt;
		instancecnt = (++instancecnt)%TOTAL_CLEANERS;
		return temp;
	}
	
	public static int getInstanceId(){
		return updateInstanceCount();
	}
	
	public static Location newInitialLocation(){
		Location[] locations = {rooms[0][2],rooms[1][2],rooms[2][2],rooms[3][2]};
		return locations[instancecnt];
	}
		
	public static boolean isInsideRoom(int room, Location location){
		if ((location.getX()>rooms[room][0].getX())&&(rooms[room][1].getX()>location.getX())){
			if ((location.getY()<rooms[room][0].getY())&&(rooms[room][1].getY()<location.getY())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isOutsideRoom(int room, Location location){
		return !isInsideRoom(room, location);
	}
	
	public static Location rectify(int room, Location location){
		//TODO
		return null;
	}
	
	
}
