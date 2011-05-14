package jadex.bdi.examples.cleanerworld_classic.environment;

import jadex.bdi.examples.cleanerworld_classic.Chargingstation;
import jadex.bdi.examples.cleanerworld_classic.Cleaner;
import jadex.bdi.examples.cleanerworld_classic.Environment;
import jadex.bdi.examples.cleanerworld_classic.Location;
import jadex.bdi.examples.cleanerworld_classic.Waste;
import jadex.bdi.examples.cleanerworld_classic.Wastebin;
import jadex.bdi.runtime.IBDIExternalAccess;
import jadex.bdi.runtime.IBDIInternalAccess;
import jadex.bridge.IComponentListener;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.commons.SGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIDefaults;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The gui for the cleaner world example. Shows the world from the viewpoint of
 * the environment agent.
 */
public class EnvironmentGui extends JFrame {

	
	
	// LSIN*Eduardo* Inicio

	private int width = 585;
	private int height = 557;
	

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
			setLocation(new Point(SGUI.calculateMiddlePosition(EnvironmentGui.this).x,0));
			setVisible(true);
		}
		super.paint(g);
	}

	// LSIN*Eduardo* Fin
	// -------- constants --------

	/** The image icons. */
	private static UIDefaults icons = new UIDefaults(
			new Object[] {
					"waste",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/waste.png"),
					"wastebin",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/wastebin.png"),
					"wastebin_full",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/wastebin_full.png"),
					"chargingstation",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/chargingstation.png"),
					"cleaner",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/cleaner.png"),
					"background",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/background.png"),
					"background_night",
					SGUI
							.makeIcon(EnvironmentGui.class,
									"/jadex/bdi/examples/cleanerworld_classic/images/background_night.png") });

	// -------- constructors --------

	/**
	 * Create a new gui plan.
	 */
	public EnvironmentGui(final IBDIExternalAccess agent) {
		super(agent.getComponentIdentifier().getLocalName());

		agent.scheduleStep(new IComponentStep() {
			public Object execute(IInternalAccess ia) {
				IBDIInternalAccess bia = (IBDIInternalAccess) ia;

				bia.addComponentListener(new IComponentListener() {
					public void componentTerminating(
							jadex.commons.ChangeEvent ae) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								EnvironmentGui.this.dispose();
							}
						});
					}

					public void componentTerminated(jadex.commons.ChangeEvent ae) {
					}
				});

				final Environment env = (Environment) bia.getBeliefbase()
						.getBelief("environment").getFact();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// System.out.println("Now accessing env, GUIPlan: "+env.hashCode());

						// Option panel.
						JPanel options = new JPanel(new GridBagLayout());
						options.setBorder(new TitledBorder(new EtchedBorder(
								EtchedBorder.LOWERED), "Environment Control"));
						/*
						final JCheckBox daytime = new JCheckBox("", env
								.getDaytime());
						daytime.setHorizontalTextPosition(SwingConstants.LEFT);
						*/
						final JLabel wastecnt = new JLabel("0");

						final JComboBox wastebinchoice = new JComboBox();
						Wastebin[] wastebins = env.getWastebins();
						for (int i = 0; i < wastebins.length; i++)
							wastebinchoice.addItem(wastebins[i].getName());
						final JComboBox fillstate = new JComboBox(new String[] {
								"empty", "full" });
						JButton setfillstate = new JButton("Set fill-state");

						Insets insets = new Insets(2, 4, 4, 2);
						
						// PAR�METROS REUTILIZADOS M�S ADELANTE
						/*options.add(new JLabel("Toggle daytime"),
								new GridBagConstraints(0, 0, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						options.add(daytime, new GridBagConstraints(1, 0, 3, 1,
								1, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));*/
						options.add(new JLabel("Waste count:"),
								new GridBagConstraints(0, 1, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						options.add(wastecnt, new GridBagConstraints(1, 1, 3,
								1, 1, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));
						options.add(new JLabel("Wastebin:"),
								new GridBagConstraints(0, 2, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						options.add(wastebinchoice, new GridBagConstraints(1,
								2, 1, 1, 0, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));
						options.add(fillstate, new GridBagConstraints(2, 2, 1,
								1, 0, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));
						options.add(setfillstate, new GridBagConstraints(3, 2,
								1, 1, 1, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));

						final Image waste_image = ((ImageIcon) icons
								.getIcon("waste")).getImage();
						final Image wastebin_image = ((ImageIcon) icons
								.getIcon("wastebin")).getImage();
						final Image wastebin_full_image = ((ImageIcon) icons
								.getIcon("wastebin_full")).getImage();
						final Image chargingstation_image = ((ImageIcon) icons
								.getIcon("chargingstation")).getImage();
						final Image cleaner_image = ((ImageIcon) icons
								.getIcon("cleaner")).getImage();
						final Image background_image = ((ImageIcon) icons
								.getIcon("background")).getImage();
						final Image background_night_image = ((ImageIcon) icons
								.getIcon("background_night")).getImage();

						//LSIN *Alicia* INICIO
						final JLabel diaSemana= new JLabel("dummy",	JLabel.RIGHT);
						//LSIN *Alicia* FIN
						
						final JLabel waste = new JLabel(new ImageIcon(
								waste_image), JLabel.CENTER);
						final JLabel wastebin = new JLabel("dummy",
								new ImageIcon(wastebin_image), JLabel.CENTER);
						wastebin.setVerticalTextPosition(JLabel.BOTTOM);
						wastebin.setHorizontalTextPosition(JLabel.CENTER);
						final JLabel wastebin_full = new JLabel("dummy",
								new ImageIcon(wastebin_full_image),
								JLabel.CENTER);
						wastebin_full.setVerticalTextPosition(JLabel.BOTTOM);
						wastebin_full.setHorizontalTextPosition(JLabel.CENTER);
						final JLabel chargingstation = new JLabel("dummy",
								new ImageIcon(chargingstation_image),
								JLabel.CENTER);
						chargingstation.setVerticalTextPosition(JLabel.BOTTOM);
						chargingstation
								.setHorizontalTextPosition(JLabel.CENTER);
						final JLabel cleaner = new JLabel("dummy",
								new ImageIcon(cleaner_image), JLabel.CENTER);
						cleaner.setVerticalTextPosition(JLabel.CENTER);
						cleaner.setHorizontalTextPosition(JLabel.RIGHT);

						env
								.addPropertyChangeListener(new PropertyChangeListener() {
									public void propertyChange(
											PropertyChangeEvent ce) {
										String propertyname = ce
												.getPropertyName();
										/*
										if ("daytime".equals(propertyname)) {
											if (daytime.isSelected() != ((Boolean) ce
													.getNewValue())
													.booleanValue())
												daytime
														.setSelected(((Boolean) ce
																.getNewValue())
																.booleanValue());
										} else*/ 
											if ("wastebins"
												.equals(propertyname)) {
											if (ce.getNewValue() == null) {
												String name = ((Wastebin) ce
														.getOldValue())
														.getName();
												((DefaultComboBoxModel) wastebinchoice
														.getModel())
														.removeElement(name);
											} else {
												String name = ((Wastebin) ce
														.getNewValue())
														.getName();
												int size = wastebinchoice
														.getModel().getSize();
												boolean found = false;
												for (int i = 0; i < size
														&& !found; i++) {
													String wbname = (String) wastebinchoice
															.getModel()
															.getElementAt(i);
													if (wbname.equals(name))
														found = true;
												}
												if (!found)
													((DefaultComboBoxModel) wastebinchoice
															.getModel())
															.addElement(name);
											}
										} else if ("wastes"
												.equals(propertyname)) {
											wastecnt.setText(""
													+ env.getWastes().length);
										} else if ("chargingstations"
												.equals(propertyname)) {
											// implement me!
										}
									}
								});

						// Map panel.
						final JPanel map = new JPanel() {
							// overridden paint method.
							protected void paintComponent(Graphics g) {
								// Get world state from beliefs.
								boolean daytime = env.getDaytime();

								// Paint background (dependent on daytime).
								Rectangle bounds = getBounds();
								// g.setColor(daytime ? Color.lightGray :
								// Color.darkGray);
								// g.fillRect(0, 0, bounds.width,
								// bounds.height);
								Image image = daytime ? background_image
										: background_night_image;
								int w = image.getWidth(this);
								int h = image.getHeight(this);
								if (w > 0 && h > 0) {
									for (int y = 0; y < bounds.height; y += h) {
										for (int x = 0; x < bounds.width; x += w) {
											g.drawImage(image, x, y, this);
										}
									}
								}
								//LSIN *Alicia* INICIO
								
								//Pintar día de la semana
								diaSemana.setText(env.getDate().getDay());
								diaSemana.setForeground(daytime ? Color.black
											: Color.white);
								render(g, diaSemana, onScreenLocation(new Location(0.51,0.49), bounds));
								//LSIN *Alicia FIN
								
								// Paint charge Stations.
								Chargingstation[] stations = env
										.getChargingstations();
								for (int i = 0; i < stations.length; i++) {
									Point p = onScreenLocation(stations[i]
											.getLocation(), bounds);
									chargingstation.setText(stations[i]
											.getName());
									chargingstation
											.setForeground(daytime ? Color.black
													: Color.white);
									render(g, chargingstation, p);
								}

								// Paint waste bins.
								Wastebin[] wastebins = env.getWastebins();
								for (int i = 0; i < wastebins.length; i++) {
									Point p = onScreenLocation(wastebins[i]
											.getLocation(), bounds);
									JLabel renderer = wastebin;
									if (wastebins[i].isFull())
										renderer = wastebin_full;
									renderer.setText(wastebins[i].getName()
											+ " ("
											+ wastebins[i].getWastes().length
											+ "/" + wastebins[i].getCapacity()
											+ ")");
									renderer
											.setForeground(daytime ? Color.black
													: Color.white);
									render(g, renderer, p);
								}

								// Paint waste.
								Waste[] wastes = env.getWastes();
								for (int i = 0; i < wastes.length; i++) {
									Point p = onScreenLocation(wastes[i]
											.getLocation(), bounds);
									waste.setForeground(daytime ? Color.black
											: Color.white);
									render(g, waste, p);
								}
								// Hack ??? Update waste count label.
								wastecnt.setText("" + wastes.length);

								// Paint the cleaner visions.
								Cleaner[] cleaners = env.getCleaners();

								// System.out.println("cls: "+env.hashCode()+" "+cleaners.length);
								for (int i = 0; i < cleaners.length; i++) {
									Point p = onScreenLocation(cleaners[i]
											.getLocation(), bounds);
									w = (int) (cleaners[i].getVisionRange() * bounds.width);
									h = (int) (cleaners[i].getVisionRange() * bounds.height);
									g.setColor(new Color(255, 255, 0, Math.max(
											192 - env.getAge(cleaners[i]) * 4,
											0))); // Vision
									g.fillOval(p.x - w, p.y - h, w * 2, h * 2);
								}

								// Paint the cleaner agents.
								for (int i = 0; i < cleaners.length; i++) {
									int age = env.getAge(cleaners[i]);
									Point p = onScreenLocation(cleaners[i]
											.getLocation(), bounds);
									cleaner
											.setText("<html>"
													+ cleaners[i].getName()
													+ "<br>"
													+ "battery: "
													+ (int) (cleaners[i]
															.getChargestate() * 100.0)
													+ "%<br>"
													+ "waste: "
													+ (cleaners[i]
															.getCarriedWaste() != null ? "yes"
															: "no")
													+ "<br>"
													+ "room: "
													+ (cleaners[i]
															.getRoom()) + "</html>");
									cleaner.setForeground(daytime ? new Color(
											age * 2, age * 2, age * 2)
											: new Color(255 - age * 2,
													255 - age * 2,
													255 - age * 2));
									render(g, cleaner, new Point(p.x + 45, p.y)); // Hack!!!
								}
							}
						};

						// Set sizes.
						wastebinchoice.setPreferredSize(new Dimension(
								(int) wastebinchoice.getPreferredSize()
										.getWidth() + 10, (int) wastebinchoice
										.getPreferredSize().getHeight()));
						fillstate
								.setPreferredSize(new Dimension((int) fillstate
										.getPreferredSize().getWidth() + 10,
										(int) fillstate.getPreferredSize()
												.getHeight()));

						// Add listeners
						/*
						daytime.addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent ce) {
								env.setDaytime(daytime.isSelected());
							}
						});
						*/
						setfillstate.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Wastebin wb = env
										.getWastebin((String) wastebinchoice
												.getSelectedItem());

								if (fillstate.getSelectedItem().equals("empty"))
									wb.empty();
								if (fillstate.getSelectedItem().equals("full"))
									wb.fill();
							}
						});
						// Handle scaling of images.
						map.addComponentListener(new ComponentAdapter() {
							protected Rectangle _bounds;

							public void componentResized(ComponentEvent ce) {
								Rectangle bounds = map.getBounds();
								if (_bounds == null)
									_bounds = bounds;
								double scale = Math.min(bounds.width
										/ (double) _bounds.width, bounds.height
										/ (double) _bounds.height);
								// LSIN*Eduardo* Inicio
								scale = 1;
								// LSIN*Eduardo* Fin
								// Wastes
								((ImageIcon) waste.getIcon())
										.setImage(waste_image
												.getScaledInstance(
														(int) (waste_image
																.getWidth(map) * scale),
														(int) (waste_image
																.getHeight(map) * scale),
														Image.SCALE_DEFAULT));

								// Wastebin
								((ImageIcon) wastebin.getIcon())
										.setImage(wastebin_image
												.getScaledInstance(
														(int) (wastebin_image
																.getWidth(map) * scale),
														(int) (wastebin_image
																.getHeight(map) * scale),
														Image.SCALE_DEFAULT));

								// Full Wastebin
								((ImageIcon) wastebin_full.getIcon())
										.setImage(wastebin_full_image
												.getScaledInstance(
														(int) (wastebin_full_image
																.getWidth(map) * scale),
														(int) (wastebin_full_image
																.getHeight(map) * scale),
														Image.SCALE_DEFAULT));

								// Chargingstation
								((ImageIcon) chargingstation.getIcon())
										.setImage(chargingstation_image
												.getScaledInstance(
														(int) (chargingstation_image
																.getWidth(map) * scale),
														(int) (chargingstation_image
																.getHeight(map) * scale),
														Image.SCALE_DEFAULT));

								// Cleaner
								((ImageIcon) cleaner.getIcon())
										.setImage(cleaner_image
												.getScaledInstance(
														(int) (cleaner_image
																.getWidth(map) * scale),
														(int) (cleaner_image
																.getHeight(map) * scale),
														Image.SCALE_DEFAULT));
							}
						});

						map.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent me) {
								// Problem!!! Beliefbase is changed in
								// the gui thread.
								Point p = me.getPoint();
								Rectangle bounds = map.getBounds();
								final Location mouseloc = new Location(
										(double) p.x / (double) bounds.width,
										1.0 - (double) p.y
												/ (double) bounds.height);
								final double tol = 7 / (double) bounds.height;

								agent.scheduleStep(new IComponentStep() {
									public Object execute(IInternalAccess ia) {
										IBDIInternalAccess bia = (IBDIInternalAccess) ia;
										Environment env = (Environment) bia
												.getBeliefbase().getBelief(
														"environment")
												.getFact();

										Waste[] wastes = env.getWastes();
										Waste nearest = null;
										double dist = 0;
										for (int i = 0; i < wastes.length; i++) {
											if (nearest == null
													|| wastes[i].getLocation()
															.getDistance(
																	mouseloc) < dist) {
												nearest = wastes[i];
												dist = wastes[i].getLocation()
														.getDistance(mouseloc);
											}
										}
										Waste waste = null;
										if (dist < tol)
											waste = nearest;

										// If waste is near clicked position
										// remove the waste
										if (waste != null) {
											env.removeWaste(waste);
										}

										// If position is clean add a new waste
										else {
											env.addWaste(new Waste(mouseloc));
										}
										return null;
									}
								});
							}
						});

						addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								agent.killComponent();

								// IContextService cs =
								// (IContextService)agent.getPlatform().getService(IContextService.class);
								// if(cs!=null)
								// {
								// IContext[] contexts =
								// cs.getContexts(agent.getAgentIdentifier(),
								// IApplicationContext.class);
								// if(contexts!=null && contexts.length>0)
								// {
								// cs.deleteContext(contexts[0], null);
								// }
								// }
								// else
								// {
								// System.out.println("No context service found: Killing agents manually.");
								// // Todo: move to end goal.
								// Environment en =
								// (Environment)agent.getBeliefbase().getBelief("environment").getFact();
								// Cleaner[] cleaners = en.getCleaners();
								// for(int i=0; i<cleaners.length; i++)
								// {
								// try
								// {
								// // Hack!!! Should ignore remote cleaners.
								// IGoal kill =
								// agent.createGoal("cms_destroy_component");
								// //
								// System.out.println("killing: "+cleaners[i].getName());
								// IComponentIdentifier aid =
								// ((IAMS)agent.getPlatform().getService(IAMS.class))
								// .createAgentIdentifier(cleaners[i].getName(),
								// true);
								// kill.getParameter("componentidentifier").setValue(aid);
								// agent.dispatchTopLevelGoalAndWait(kill);
								// }
								// // catch(GoalFailureException gfe) {}
								// catch(Exception ex)
								// {
								// // There might be old cleaner entries in the
								// environment that can lead to exceptions
								// // because the agents cannot be killed.
								// //ex.printStackTrace();
								// }
								// }
								// agent.killAgent();
								// }
							}
						});

						// Show the gui.
						getContentPane().add(BorderLayout.SOUTH, options);
						getContentPane().add(BorderLayout.CENTER, map);
						setSize(width,height);
						// LSIN *Alicia* Inicio
						setUndecorated(true);
						// LSIN *Alicia* Fin
						// LSIN*Eduardo* Inicio
						setMaximumSize(new Dimension(width,height));				
						setMinimumSize(new Dimension(width,height));
						// LSIN*Eduardo* Fin

						setLocation(new Point(SGUI.calculateMiddlePosition(EnvironmentGui.this).x,30));
						setVisible(true);

						//LSIN *Alicia* INICIO
						final JLabel timePast= new JLabel(); 
						options.add(new JLabel("Time: "),
						new GridBagConstraints(0, 0, 1, 1, 0, 0,
								GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));
						options.add(timePast, new GridBagConstraints(1, 0, 3, 1,
								1, 0, GridBagConstraints.WEST,
								GridBagConstraints.NONE, insets, 0, 0));
						//options.add(tiempoTranscurrido);
						
						//LSIN*Ces* INICIO
						JCheckBox room0Presence = new JCheckBox("Presencia en la cocina");
						room0Presence.setName("0");
						options.add(room0Presence,
								new GridBagConstraints(2, 1, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						
						
						JCheckBox room1Presence = new JCheckBox("Presencia en el salon");
						room1Presence.setName("1");
						options.add(room1Presence,
								new GridBagConstraints(2, 0, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						
						
						JCheckBox room2Presence = new JCheckBox("Presencia en el baño");
						room2Presence.setName("2");
						options.add(room2Presence,
								new GridBagConstraints(3, 0, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						
						
						JCheckBox room3Presence = new JCheckBox("Presencia en el dormitorio");
						room3Presence.setName("3");
						options.add(room3Presence,
								new GridBagConstraints(3, 1, 1, 1, 0, 0,
										GridBagConstraints.WEST,
										GridBagConstraints.NONE, insets, 0, 0));
						
						
						ActionListener checkBoxHandler = new ActionListener() {							
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								//that's because you can't use roomXPresence in that inner class.
								JCheckBox cb= (JCheckBox) e.getSource();
								String name = cb.getName();
								int room = 0;
								if (name.equals("0")){
									room = 0;
								}
								if (name.equals("1")){
									room = 1;
								}
								if (name.equals("2")){
									room = 2;
								}
								if (name.equals("3")){
									room = 3;
								}
								
								env.setRoomPresence(room, cb.isSelected());	
							}
						};
						
						room0Presence.addActionListener(checkBoxHandler);
						room1Presence.addActionListener(checkBoxHandler);
						room2Presence.addActionListener(checkBoxHandler);
						room3Presence.addActionListener(checkBoxHandler);
						//LSIN*Ces* Fin
						
						Timer timer = new Timer(50, new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								env.updateDate(50);
								timePast.setText(""+env.getDate().getTimeWatch());
								map.invalidate();
								map.repaint();
								//env.getDirtyRooms();								
							}
						});
						//LSIN *Alicia* FIN
						timer.start();
					}
				});
				return null;
			}
		});
	}

	// -------- helper methods --------

	/**
	 * Get the on screen location for a location in the world.
	 */
	protected Point onScreenLocation(Location loc, Rectangle bounds) {
		return new Point((int) (bounds.width * loc.getX()),
				(int) (bounds.height * (1.0 - loc.getY())));
	}

	/**
	 * Render a component on screen.
	 * 
	 * @param g
	 *            The graphics object.
	 * @param comp
	 *            The component.
	 * @param p
	 *            The on screen location.
	 */
	protected void render(Graphics g, Component comp, Point p) {
		Dimension d = comp.getPreferredSize();
		Rectangle bounds = new Rectangle(p.x - d.width / 2, p.y - d.height / 2,
				d.width + 1, d.height);
		comp.setBounds(bounds);
		g.translate(bounds.x, bounds.y);
		comp.paint(g);
		g.translate(-bounds.x, -bounds.y);
	}

}
