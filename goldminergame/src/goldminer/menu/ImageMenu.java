package goldminer.menu;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import goldminercommon.config.SettingsGame;

public class ImageMenu {

	private int x, y, width, height;
	private BufferedImage image, pressedImage;
	private String name;
	private boolean entered, pressed, isEnabled;
	
	public ImageMenu(int _x, String _srcUrl, String _name) {
		this.x=_x;
		this.name=_name;
		this.entered=false;
		this.pressed=false;
		this.isEnabled=true;
		try {
			this.image=ImageIO.read(this.getClass().getResource(_srcUrl).openStream());
			this.pressedImage=ImageIO.read(this.getClass().getResource("/resources/images/menu/menu_pressed.png").openStream());
			this.width=image.getWidth();
			this.height=image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setY(int _y) {
		this.y=_y;
	}
	
	public String getName() {
		return name;
	}

	public boolean overlap(int _x, int _y) {
		return (_x>=x && _x<=x+width && _y>=y && _y<=y+height);
	}
	
	public void entered() {
		this.entered=true;
	}
	
	public void exited() {
		this.entered=false;
	}
	
	public void pressed() {
		this.pressed=true;
	}
	
	public void released() {
		this.pressed=false;
	}
	
	public void setEnabled(boolean _isEnabled) {
		this.isEnabled=_isEnabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void paint(Graphics g) {
		g.drawImage(image, x, y, width, height, null);
		if(isEnabled) {
			if(pressed) {
				g.drawImage(pressedImage, x, y, null);
			}
			if(entered) {
				Graphics2D g2=(Graphics2D)g;
				g2.setColor(SettingsGame.BORDER_MENU);
				g2.setStroke(new BasicStroke(4));
				g2.drawLine(x, y, x+width, y);
				g2.drawLine(x+width, y, x+width, y+height);
				g2.drawLine(x, y+height, x+width, y+height);
				g2.drawLine(x, y, x, y+height);
			}
		} else {
			g.drawImage(pressedImage, x, y, null);
		}
	}
}
