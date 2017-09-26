package goldminer.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;

public class AbstractDisplayPanel extends AbstractPanel {
	
	private Image backgroundBlack;

	public AbstractDisplayPanel(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		super(_panelSlider, _gameInfo, _name);
		this.backgroundBlack=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/backgrounds/background_black.png"));
	}

	private static final long serialVersionUID = -5153118202404189915L;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundBlack, 0, 0, backgroundBlack.getWidth(this), backgroundBlack.getHeight(this), this);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
}
