package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.examples.cleanerworld_classic.Status;
import jadex.bdi.examples.cleanerworld_classic.environment.EnvironmentGui;
import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bridge.ComponentTerminatedException;
import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.commons.IFuture;
import jadex.commons.SGUI;
import jadex.commons.concurrent.SwingDefaultResultListener;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIDefaults;

/**
 * Panel for showing the cleaner world view.
 */
class AmbrosioPanel extends JPanel {
	// -------- attributes --------

	/** The Ambrosio agent. */
	protected IExternalAccess agent;

	/** The latest world view information. */
	protected DrawData drawdata;

	/** Para lograr que el despertador parpadee */
	protected int blink;

	/** Periodo del parpadeo del despertador */
	protected final int BLINK_PERIOD = 6;

	/**
	 * Flag to indicate that the draw data is currently updated to avoid
	 * multiple updates in parallel.
	 */
	protected boolean updating;

	// LSIN *Alicia* Inicio
	// -------- constants --------
	/** The image icons. */
	private static UIDefaults icons = new UIDefaults(
			new Object[] {
					"background",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/interfaz-HAL.png"),
					"alarm-on",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/alarm-on.png"),
					"alarm-off",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/alarm-off.png"),
					"room-empty",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/room-empty-dark.png"),
					"room-full-dark",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/room-full-dark.png"),
					"room-full-light",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/room-full-light.png"),
					"cleaner-ok",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/cleaner-ok.png"),
					"cleaner-error",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/cleaner-fail.png"),
					"cleaner-offline",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/cleaner-off.png"),
					"cleaner-unknown",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/cleaner-unknown.png") });

	// LSIN *Alicia* Fin

	// -------- constructors --------

	/**
	 * Create an Ambrosio panel.
	 */
	public AmbrosioPanel(IExternalAccess agent) {
		this.agent = agent;
		// LSIN *Alicia* Inicio
		this.blink = 0;
		// LSIN *Alicia* Fin
	}

	// -------- JPanel methods --------

	/**
	 * Paint the world view.
	 */
	protected void paintComponent(Graphics g) {

		if (!updating) {
			updating = true;
			try {
				IFuture fut = agent.scheduleStep(new UpdateStep());
				fut.addResultListener(new SwingDefaultResultListener() {
					public void customResultAvailable(Object source,
							Object result) {
						AmbrosioPanel.this.drawdata = (DrawData) result;
						updating = false;
					}

					public void customExceptionOccurred(Object source,
							Exception exception) {
						// exception.printStackTrace();
						// updating = false; // Keep to false to disable any
						// more updates
					}
				});
			} catch (ComponentTerminatedException e) {
				// Keep updating to false to disable any more updates
			}
		}

		if (drawdata != null) {
			// Paint background
			Rectangle bounds = getBounds();

			// LSIN *Alicia* Inicio
			Image image = ((ImageIcon) icons.getIcon("background")).getImage();
			int w = image.getWidth(this);
			int h = image.getHeight(this);
			if (w > 0 && h > 0) {
				g.drawImage(image, 0, 0, this);
			}

			// Paint the alarm clock when the alarm sounds
			Image alarmOn = ((ImageIcon) icons.getIcon("alarm-on")).getImage();
			Image alarmOff = ((ImageIcon) icons.getIcon("alarm-off"))
					.getImage();
			g.drawImage(alarmOff, 30, 30, this);
			if (Ambrosio.isAlarmOn()) {
				if (this.blink < (BLINK_PERIOD / 2)) {
					g.drawImage(alarmOn, 30, 30, this);
				} else if (this.blink == BLINK_PERIOD) {
					this.blink = -1;
				}
				this.blink++;
			}
			String[] roomIconString = { "room-empty", "room-empty",
					"room-empty", "room-empty" };
			boolean[] roomPresence = Ambrosio.getRoomPresence();
			Image[] roomIcon = new Image[4];
			for (int i = 0; i < roomIcon.length; i++) {
				if (roomPresence[i]) {
					roomIconString[i] = "room-full-dark";
					if (!Ambrosio.isDaytime() && (i != 3)) {
						roomIconString[i] = "room-full-light";
					}
				}
				roomIcon[i] = ((ImageIcon) icons.getIcon(roomIconString[i]))
						.getImage();
			}
			g.drawImage(roomIcon[0], 120, 62, this);
			g.drawImage(roomIcon[1], 120, 15, this);
			g.drawImage(roomIcon[2], 197, 15, this);
			g.drawImage(roomIcon[3], 197, 62, this);

			Image[] cleanerStateIcon = new Image[4];
			for (int i = 0; i < Ambrosio.cleanersStatus.length; i++) {
				if (Ambrosio.cleanersStatus[i].equals(Status.OK)) {
					cleanerStateIcon[i] = ((ImageIcon) icons
							.getIcon("cleaner-ok")).getImage();
				} else if (Ambrosio.cleanersStatus[i].equals(Status.OFFLINE)) {
					cleanerStateIcon[i] = ((ImageIcon) icons
							.getIcon("cleaner-offline")).getImage();
				}else if (Ambrosio.cleanersStatus[i].equals(Status.ERROR)) {
					cleanerStateIcon[i] = ((ImageIcon) icons
							.getIcon("cleaner-error")).getImage();
				} else {
					cleanerStateIcon[i] = ((ImageIcon) icons
							.getIcon("cleaner-unknown")).getImage();
				}
			}
			g.drawImage(cleanerStateIcon[0], 300, 62, this);
			g.drawImage(cleanerStateIcon[1], 300, 30, this);
			g.drawImage(cleanerStateIcon[2], 362, 30, this);
			g.drawImage(cleanerStateIcon[3], 362, 62, this);
			// LSIN *Alicia* Fin
		}
	}

	// -------- helper methods --------

	/**
	 * Get the on screen location for a location in the world.
	 */
	protected static Point onScreenLocation(Location loc, Rectangle bounds) {
		assert loc != null;
		assert bounds != null;
		return new Point((int) (bounds.width * loc.getX()),
				(int) (bounds.height * (1.0 - loc.getY())));
	}

	// -------- helper classes --------

	/**
	 * Component step to produce an uptodate draw data.
	 */
	public static class UpdateStep implements IComponentStep {
		public Object execute(IInternalAccess ia) {
			IBDIInternalAccess bia = (IBDIInternalAccess) ia;
			DrawData drawdata = new DrawData();

			return drawdata;
		}
	}

	/**
	 * Data for drawing.
	 */
	public static class DrawData {
		// Allow object being transferred as XML using public fields.
		public static boolean XML_INCLUDE_FIELDS = true;

	}
}