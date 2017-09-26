package goldminer.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;
import goldminercommon.config.SettingsGame;

public class PanelFinishLevel extends AbstractDisplayPanel {

	private static final long serialVersionUID = -440279112562050729L;
	private BufferedImage arrowNextImage, arrowNextSelectedImage;
	private int inTheShopTime, posX, posY, widthText;
	private final String textShop="Boutique";
	private Timer timeTimer;
	private boolean isSelected;
	
	public PanelFinishLevel(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		super(_panelSlider, _gameInfo, _name);
		try {
			this.arrowNextImage=ImageIO.read(this.getClass().getResource("/resources/images/arrowNext.png").openStream());
			this.arrowNextSelectedImage=ImageIO.read(this.getClass().getResource("/resources/images/arrowNextSelected.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.posX=this.getWidth()-arrowNextImage.getWidth()-50;
		this.posY=(this.getHeight()/2)-(arrowNextImage.getHeight()/2);
		this.isSelected=false;
		this.inTheShopTime=10;
		this.timeTimer=new Timer(1000, timeTick);
		this.widthText=this.getFontMetrics(SettingsGame.DEFAULT_FONT_GAME).stringWidth(textShop);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(arrowNextImage, posX, posY, this);
		if(isSelected)
			g.drawImage(arrowNextSelectedImage, posX, posY, this);
		g.setFont(SettingsGame.DEFAULT_FONT_GAME);
		g.setColor(Color.WHITE);
		g.drawString(textShop, posX+arrowNextImage.getWidth()/2-widthText/2, posY+arrowNextImage.getHeight()+35);
		g.setFont(SettingsGame.FONT_LEVEL_FINISH);
		final int x=50, y=150;
		g.drawString("Niveau terminé", x, y);
		gameInfo.paintResume(g, x, y);
		g.drawString("Entrez dans la boutique dans "+inTheShopTime+" s", this.getWidth()/2, this.getHeight()-50);
	}
	
	private void showShop() {
		timeTimer.stop();
		inTheShopTime=10;
		panelSlider.slideLeft("shop");
	}
	
	private ActionListener timeTick=new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			inTheShopTime-=1;
			if(inTheShopTime==0) {
				showShop();
			}
			repaint();
		}
	};

	@Override
	public void focusGained(FocusEvent e) {
		timeTimer.start();
	}

	@Override
	public void focusLost(FocusEvent e) {
		
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
		if(isSelected) {
			showShop();
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(new Ellipse2D.Float(posX, posY, arrowNextImage.getWidth(), arrowNextImage.getHeight()).contains(e.getPoint())) {
			isSelected=true;
		} else {
			isSelected=false;
		}
		repaint();
	}
}
