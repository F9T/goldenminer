package goldminercommon.items;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SurpriseBag")
public class SurpriseBag extends DropItem {

	public SurpriseBag() {
		this.initialize();
	}
	
	public SurpriseBag(int _x, int _y) {
		super(_x, _y);
		this.initialize();
		this.initializePolygon("/res/coord_surprise_bag.txt");
	}
	
	/**
	 * Initialise les sacs surprises
	 */
	private void initialize() {
		try {
			this.image=ImageIO.read(this.getClass().getResource("/res/images/surprise_bag.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.density=1;
		this.name="Surprise bag";
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
	
	/**
	 * Random pour définir si on a un gain ou une perte d'argent
	 * @return le nombre de gain et -1 si c'est la perte qui a été tirée
	 */
	public int randomSurprise() {
		Random rand=new Random();
		int max=1000;
		int min=0;
		int randomNumber=rand.nextInt(max+1-min)+min;
		if(randomNumber>=max/2) {
			max=700;
			min=400;
			randomNumber=rand.nextInt(max+1-min)+min;
			return randomNumber;
		}
		return -1;
	}
}
