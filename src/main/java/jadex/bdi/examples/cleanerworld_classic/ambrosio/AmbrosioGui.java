package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.CleanerLocationManager;
import jadex.bdi.examples.cleanerworld_classic.environment.EnvironmentGui;
import jadex.bdi.runtime.IBDIExternalAccess;
import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bridge.IComponentListener;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.ChangeEvent;
import jadex.commons.SGUI;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The gui for the cleaner world example. Shows the world from the viewpoint of
 * a single agent.
 */
public class AmbrosioGui extends JFrame {
	/** The instance counter. */

	// -------- constructors --------

	/**
	 * Shows the gui, and updates it when beliefs change.
	 */
	public AmbrosioGui(final IBDIExternalAccess agent) {
		super(agent.getComponentIdentifier().getName());
		final JPanel map = new AmbrosioPanel(agent);

		getContentPane().add(BorderLayout.CENTER, map);
		setSize(1000, 120);
		// LSIN*Eduardo* Inicio
		setLocation(new Point(SGUI.calculateMiddlePosition(this).x,(int) (SGUI.calculateMiddlePosition(this).y*1.88)));
		// LSIN*Eduardo* Fin
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				agent.killComponent();
			}
		});

		agent.scheduleStep(new IComponentStep() {
			public Object execute(IInternalAccess ia) {
				IBDIInternalAccess bia = (IBDIInternalAccess) ia;
				bia.addComponentListener(new IComponentListener() {
					public void componentTerminating(ChangeEvent ae) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								dispose();
							}
						});
					}

					public void componentTerminated(ChangeEvent ae) {
					}
				});
				return null;
			}
		});

		Timer timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				map.invalidate();
				map.repaint();
			}
		});
		timer.start();
	}
}
