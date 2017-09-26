package goldminer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ExplodeEffect {

	private ArrayList<BufferedImage> images;
	private int x, y, indexImage;
	private boolean painted;
	
	public ExplodeEffect(String _srcFolder, String _imageName, int _numberImage) {
		this.painted=false;
		this.images=new ArrayList<BufferedImage>();
		this.initializeImage(_srcFolder, _imageName, _numberImage);
	}
	
	public ExplodeEffect(int _x, int _y, String _srcFolder, String _imageName, int _numberImage) {
		this(_srcFolder, _imageName, _numberImage);
		this.x=_x;
		this.y=_y;
	}
	
	private void initializeImage(String _srcFolder, String _imageName, int _numberImage) {
		for(int i=1;i<=_numberImage;i++) {
			try {
				this.images.add(ImageIO.read(this.getClass().getResource(_srcFolder+"/"+_imageName+i+".png").openStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void explode() {
		this.painted=true;
		this.indexImage=0;
	}
	
	public void setX(int _x) {
		this.x=_x;
	}
	
	public void setY(int _y) {
		this.y=_y;
	}
	
	public int getWidth() {
		return images.get(indexImage).getWidth();
	}
	
	public int getHeight() {
		return images.get(indexImage).getHeight();
	}
	
	public boolean isExploded() {
		return painted;
	}
	
	public void paint(Graphics g) {
		if(painted) {
			g.drawImage(images.get(indexImage), x, y, null);
			if(++indexImage>=images.size()-1) {
				painted=false;
			}
		}
	}
}