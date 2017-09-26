package goldminercommon.items;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Diamond")
public class Diamond extends OreItem {

	public Diamond() {
		this.initialize();
	}
	
	public Diamond(int _x, int _y) {
		super(_x, _y);
		this.initialize();
		this.initializePolygon("/res/coord_diamond.txt");
	}
	
	/**
	 * Initialise les diamants
	 */
	private void initialize() {
		try {
			this.image=ImageIO.read(this.getClass().getResource("/res/images/diamond.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.density=1;
		this.gain=600;
		this.name="Diamond";
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
}
