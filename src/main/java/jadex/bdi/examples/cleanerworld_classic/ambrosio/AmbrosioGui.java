package jadex.bdi.examples.cleanerworld_classic.ambrosio;

import jadex.bdi.examples.cleanerworld_classic.Ambrosio;
import jadex.bdi.examples.cleanerworld_classic.environment.EnvironmentGui;
import jadex.bdi.runtime.IBDIExternalAccess;
import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bridge.IComponentListener;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.ChangeEvent;
import jadex.commons.SGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The gui for the cleaner world example. Shows the world from the viewpoint of
 * a single agent.
 */
public class AmbrosioGui extends JFrame {
	
	
	// LSIN*Eduardo* Inicio

	@Override
	public void paint(Graphics g) {
		Dimension d = getSize();
		Dimension m = getMaximumSize();
		boolean resize = d.width > m.width || d.height > m.height;
		d.width = Math.min(m.width, d.width);
		d.height = Math.min(m.height, d.height);
		if (resize) {
			setVisible(false);
			setSize(d);
			setLocation(new Point(SGUI.calculateMiddlePosition(AmbrosioGui.this).x,(int) (SGUI.calculateMiddlePosition(this).y*1.88)));
			setVisible(true);
		}
		super.paint(g);
	}
	// LSIN*Eduardo* Fin

	// -------- constructors --------

	/**
	 * Shows the gui, and updates it when beliefs change.
	 */
	public AmbrosioGui(final IBDIExternalAccess agent) {
		super(agent.getComponentIdentifier().getName());
		final JPanel map = new AmbrosioPanel(agent);

		getContentPane().add(BorderLayout.CENTER, map);
		setSize(1000, 120);
		
		// LSIN *Alicia* Inicio
		setMaximumSize(new Dimension(1000, 120));
		setMinimumSize(new Dimension(1000, 120));
		setUndecorated(true);
		// LSIN *Alicia* Fin
		
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
