package goldminer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Cette classe a été pris sur stackoverflow, on l'a modifié afin d'obtenir ce que l'on voulait
 * Mais la majeure partie du code n'a ainsi pas été fait par nous-mêmes.
 * La version précédente permettant uniquement de transiter les panels les uns après les autres.
 * La modification apporté est que l'on peut choisir quel panel affiché grâce à un nom.
 * Ce nom a été ajouté également. Il était obligatoire de le faire pour pouvoir afficher le panel GameOver seulement
 * lorsque le joueur a perdu.
 *
 * @param <ParentType>
 */
public class PanelSlider<ParentType extends Container> {

	private static final int RIGHT = 0x01;
	private static final int LEFT = 0x02;
	private static final int TOP = 0x03;
	private static final int BOTTOM = 0x04;
	private final JPanel basePanel = new JPanel();
	private final ParentType parent;
	private final Object lock = new Object();
	private final ArrayList<Component> jPanels = new ArrayList<Component>();
	private boolean isSlideInProgress = false;

	private final JPanel glassPane;
	{
		glassPane = new JPanel();
		glassPane.setOpaque(false);
		glassPane.addMouseListener(new MouseAdapter() {
		});
		glassPane.addMouseMotionListener(new MouseMotionAdapter() {
		});
		glassPane.addKeyListener(new KeyAdapter() {
		});
	}

	public PanelSlider(final ParentType parent) {
		if (parent == null) {
			throw new RuntimeException("ProgramCheck: Parent can not be null.");
		}
		if ((parent instanceof JFrame) || (parent instanceof JDialog) || (parent instanceof JWindow)
				|| (parent instanceof JPanel)) {
		} else {
			throw new RuntimeException("ProgramCheck: Parent type not supported. " + parent.getClass().getSimpleName());
		}
		this.parent = parent;
		attach();
		basePanel.setSize(parent.getSize());
		basePanel.setLayout(new BorderLayout());
	}

	public JPanel getBasePanel() {
		return basePanel;
	}

	private void attach() {
		final ParentType w = this.parent;
		if (w instanceof JFrame) {
			final JFrame j = (JFrame) w;
			if (j.getContentPane().getComponents().length > 0) {
				throw new RuntimeException("ProgramCheck: Parent already contains content.");
			}
			j.getContentPane().add(basePanel);
		}
		if (w instanceof JDialog) {
			final JDialog j = (JDialog) w;
			if (j.getContentPane().getComponents().length > 0) {
				throw new RuntimeException("ProgramCheck: Parent already contains content.");
			}
			j.getContentPane().add(basePanel);
		}
		if (w instanceof JWindow) {
			final JWindow j = (JWindow) w;
			if (j.getContentPane().getComponents().length > 0) {
				throw new RuntimeException("ProgramCheck: Parent already contains content.");
			}
			j.getContentPane().add(basePanel);
		}
		if (w instanceof JPanel) {
			final JPanel j = (JPanel) w;
			if (j.getComponents().length > 0) {
				throw new RuntimeException("ProgramCheck: Parent already contains content.");
			}
			j.add(basePanel);
		}
	}

	public void addComponent(final Component component) {
		if (jPanels.contains(component)) {
		} else {
			jPanels.add(component);
			if (jPanels.size() == 1) {
				basePanel.add(component);
			}
			component.setSize(basePanel.getSize());
			component.setLocation(0, 0);
		}
	}

	public void removeComponent(final Component component) {
		if (jPanels.contains(component)) {
			jPanels.remove(component);
		}
	}

	public void slideLeft(final String _name) {
		slide(LEFT, _name);
	}

	public void slideRight(final String _name) {
		slide(RIGHT, _name);
	}

	public void slideTop(final String _name) {
		slide(TOP, _name);
	}

	public void slideBottom(final String _name) {
		slide(BOTTOM, _name);
	}

	private void enableUserInput(final ParentType w) {
		if (w instanceof JFrame) {
			((JFrame) w).getGlassPane().setVisible(false);
		}
		if (w instanceof JDialog) {
			((JDialog) w).getGlassPane().setVisible(false);
		}
		if (w instanceof JWindow) {
			((JWindow) w).getGlassPane().setVisible(false);
		}
	}

	private void disableUserInput(final ParentType w) {
		if (w instanceof JFrame) {
			((JFrame) w).setGlassPane(glassPane);
		}
		if (w instanceof JDialog) {
			((JDialog) w).setGlassPane(glassPane);
		}
		if (w instanceof JWindow) {
			((JWindow) w).setGlassPane(glassPane);
		}
		glassPane.setVisible(true);
	}

	private void slide(final int slideType, final String _name) {
		if (!isSlideInProgress) {
			isSlideInProgress = true;
			final Thread t0 = new Thread(new Runnable() {
				@Override
				public void run() {
					parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					disableUserInput(parent);
					slide(true, slideType, _name);
					enableUserInput(parent);
					parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					isSlideInProgress = false;
				}
			});
			t0.setDaemon(true);
			t0.start();
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	private int getIndexComponentByName(final String _name) {
		for(int i=0;i<jPanels.size();i++) {
			Component c=jPanels.get(i);
			if(c.toString().equals(_name)) return i;
		}
		return -1;
	}

	private void slide(final boolean useLoop, final int slideType, final String _name) {
		int index=this.getIndexComponentByName(_name);
		if(index<=-1) {
			return;
		}
		if (jPanels.size() < 2) {
			System.err.println("Not enough panels");
			return;
		}
		synchronized (lock) {
			Component componentOld = null;
			Component componentNew = null;
			if ((slideType == LEFT) || (slideType == TOP)) {
				componentNew = jPanels.remove(index);
				componentOld = jPanels.get(0);
				jPanels.add(0, componentNew);
			}
			if ((slideType == RIGHT) || (slideType == BOTTOM)) {
				componentNew = jPanels.remove(index);
				componentOld = jPanels.get(0);
				jPanels.add(0, componentNew);
			}
			final int w = componentOld.getWidth();
			final int h = componentOld.getHeight();
			final Point p1 = componentOld.getLocation();
			final Point p2 = new Point(0, 0);
			if (slideType == LEFT) {
				p2.x += w;
			}
			if (slideType == RIGHT) {
				p2.x -= w;
			}
			if (slideType == TOP) {
				p2.y += h;
			}
			if (slideType == BOTTOM) {
				p2.y -= h;
			}
			componentNew.setLocation(p2);
			int step = 0;
			if ((slideType == LEFT) || (slideType == RIGHT)) {
				step = (int) (((float) parent.getWidth() / (float) Toolkit.getDefaultToolkit().getScreenSize().width)
						* 100.f);
			} else {
				step = (int) (((float) parent.getHeight() / (float) Toolkit.getDefaultToolkit().getScreenSize().height)
						* 1.f);
			}
			step = step < 5 ? 5 : step;
			basePanel.add(componentNew);
			basePanel.revalidate();
			if (useLoop) {
				final int max = (slideType == LEFT) || (slideType == RIGHT) ? w : h;
				for (int i = 0; i != (max / step); i++) {
					switch (slideType) {
					case LEFT: {
						p1.x -= step;
						componentOld.setLocation(p1);
						p2.x -= step;
						componentNew.setLocation(p2);
						break;
					}
					case RIGHT: {
						p1.x += step;
						componentOld.setLocation(p1);
						p2.x += step;
						componentNew.setLocation(p2);
						break;
					}
					case TOP: {
						p1.y -= step;
						componentOld.setLocation(p1);
						p2.y -= step;
						componentNew.setLocation(p2);
						break;
					}
					case BOTTOM: {
						p1.y += step;
						componentOld.setLocation(p1);
						p2.y += step;
						componentNew.setLocation(p2);
						break;
					}
					default:
						new RuntimeException("ProgramCheck").printStackTrace();
						break;
					}

					try {
						Thread.sleep(500 / (max / step));
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
			componentOld.setLocation(-10000, -10000);
			componentNew.setLocation(0, 0);
			componentNew.requestFocus();
		}
	}
}