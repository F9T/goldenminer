package goldminer.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;
import goldminercommon.config.SettingsGame;

public abstract class AbstractPanel extends JPanel implements FocusListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -2886545706704045267L;
	protected GameInfo gameInfo;
	protected PanelSlider<GameFrame> panelSlider;
	protected Image backgroundGame;
	protected final String name;
	
	public AbstractPanel(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		this.panelSlider=_panelSlider;
		this.gameInfo=_gameInfo;
		this.name=_name;
		this.backgroundGame=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/backgrounds/lightBackground.png"));
		this.setPreferredSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
		this.setSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
		this.addFocusListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void updateGameInfo(GameInfo _gameInfo) {
		this.gameInfo=_gameInfo;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2.drawImage(backgroundGame, 0, 0, SettingsGame.WIDTH, SettingsGame.HEIGHT, this);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
