package jadex.bdi.examples.cleanerworld_classic;

public class CleanerLocationManager {

	/** The instance counter. */
	protected static int instancecnt = 0;

	private final static int TOTAL_CLEANERS = 4;
	public final static int UPPER_LEFT = 0;
	public final static int BOTTOM_RIGHT = 1;
	public final static int CENTER = 2;

	public final static Location[][] rooms = {
			// {upper_left,bottom_right,center}
			{ new Location(0.026, 0.382), new Location(0.632, 0.033),
					new Location(0.334, 0.205) },
			{ new Location(0.026, 0.967), new Location(0.632, 0.567),
					new Location(0.334, 0.779) },
			{ new Location(0.683, 0.967), new Location(0.974, 0.567),
					new Location(0.822, 0.779) },
			{ new Location(0.683, 0.382), new Location(0.974, 0.033),
					new Location(0.822, 0.205) }, };

	private static int updateInstanceCount() {
		int temp = instancecnt;
		instancecnt = (++instancecnt) % TOTAL_CLEANERS;
		return temp;
	}

	public static int getInstanceId() {
		return updateInstanceCount();
	}

	public static Location newInitialLocation() {
		Location[] locations = { rooms[0][2], rooms[1][2], rooms[2][2],
				rooms[3][2] };
		return locations[instancecnt];
	}

	public static boolean isInsideRoom(int room, Location location) {
		if ((location.getX() > rooms[room][0].getX())
				&& (rooms[room][1].getX() > location.getX())) {
			if ((location.getY() < rooms[room][0].getY())
					&& (rooms[room][1].getY() < location.getY())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isOutsideRoom(int room, Location location) {
		return !isInsideRoom(room, location);
	}

	public static Location rectify(int room, Location location) {
		Location upper_left = rooms[room][UPPER_LEFT];
		Location bottom_right = rooms[room][BOTTOM_RIGHT];
		Location rectified = (Location) location.clone();
		double width = bottom_right.getX() - upper_left.getX();
		double height = upper_left.getY() - bottom_right.getY();

		if (rectified.getX() < upper_left.getX()) {
			// rectified.setX(upper_left.getX());
			double delta = upper_left.getX() - rectified.getX();
			rectified.setX(upper_left.getX() + Math.min(delta, 0));
		} else if (rectified.getX() > bottom_right.getX()) {
			// rectified.setX(bottom_right.getX());
			double delta = rectified.getX() - bottom_right.getX();
			rectified.setX(bottom_right.getX() - Math.min(delta, 0));
		}

		if (rectified.getY() > upper_left.getY()) {
			// rectified.setY(upper_left.getY());
			double delta = rectified.getY() - upper_left.getY();
			rectified.setY(upper_left.getY() - Math.min(delta, 0));
		} else if (rectified.getY() < bottom_right.getY()) {
			// rectified.setY(bottom_right.getY());
			double delta = bottom_right.getY() - rectified.getY();
			rectified.setY(bottom_right.getY() + Math.min(delta, 0));
		}
		// System.out.println("Rectificando: " + location.toString() + " to "
		// + rectified.toString());
		return rectified;
	}

	public static Location randomLocationInRoom(int room) {
		Location upper_left = rooms[room][UPPER_LEFT];
		Location bottom_right = rooms[room][BOTTOM_RIGHT];
		double width = bottom_right.getX() - upper_left.getX();
		double height = upper_left.getY() - bottom_right.getY();

		double x_dest = Math.random() * width + upper_left.getX();
		double y_dest = Math.random() * height + bottom_right.getY();

		return new Location(x_dest, y_dest);
	}
}
