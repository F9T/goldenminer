package goldminer.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;

public class HelpPanel extends AbstractPanel {

	private static final long serialVersionUID = 3333248638473203148L;
	private final Image background;
	private BufferedImage cross, crossSelected;
	private final int posX, posY;
	private boolean isSelected;
	
	public HelpPanel(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		super(_panelSlider, _gameInfo, _name);
		this.background=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/backgrounds/helpBackground.png"));
		try {
			this.cross=ImageIO.read(this.getClass().getResource("/resources/images/cross.png").openStream());
			this.crossSelected=ImageIO.read(this.getClass().getResource("/resources/images/crossSelected.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.posX=this.getWidth()-cross.getWidth()-15;
		this.posY=15;
		this.isSelected=false;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
		g.drawImage(cross, posX, posY, this);
		if(isSelected)
			g.drawImage(crossSelected, posX, posY, this);
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		panelSlider.slideTop("game");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(new Ellipse2D.Float(posX, posY, cross.getWidth(), cross.getHeight()).contains(e.getPoint())) {
			isSelected=true;
		} else {
			isSelected=false;
		}
		repaint();
	}
}
