package goldminer.menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import goldminercommon.config.SettingsGame;

public class Menu {

	private ArrayList<ImageMenu> menuImages;
	private Image background_menu;
	private int x, height, vgap;
	
	public Menu(int _widthImage, int _heightImage, int _vgap) {
		this.background_menu=null;
		this.menuImages=new ArrayList<ImageMenu>();
		this.x=(SettingsGame.WIDTH-_widthImage)/2;
		this.height=_heightImage;
		this.vgap=_vgap;
	}
	
	public Menu(String _srcBackground, int _widthImage, int _heightImage, int _vgap) {
		this(_widthImage, _heightImage, _vgap);
		this.background_menu=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(_srcBackground));
	}
	
	public void addMenuImage(String _srcUrl, String _name) {
		ImageMenu imageMenu=new ImageMenu(x, _srcUrl, _name);
		menuImages.add(imageMenu);
		this.organiseItem();
	}
	
	private void organiseItem() {
		int y=(SettingsGame.HEIGHT-((menuImages.size()*height)+((menuImages.size()-1)*vgap)))/2;
		for(ImageMenu image : menuImages) {
			image.setY(y);
			y+=height+vgap;
		}
	}
	
	public ImageMenu overlap(int _x, int _y) {
		for(ImageMenu image : menuImages) {
			if(image.overlap(_x, _y)) return image;
		}
		return null;
	}
	
	public void setEnabled(boolean _isEnabled, String _name) {
		for(ImageMenu image : menuImages) {
			if(image.getName().equals(_name)) {
				image.setEnabled(_isEnabled);
				break;
			}
		}
	}
	
	public void paint(Graphics g) {
		if(background_menu!=null) g.drawImage(background_menu, 0, 0, SettingsGame.WIDTH, SettingsGame.HEIGHT, null);
		for(ImageMenu image : menuImages) {
			image.paint(g);
		}
	}
}