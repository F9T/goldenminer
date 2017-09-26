package goldminercommon.items;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import goldminercommon.enums.OreWeightEnum;

@XmlRootElement(name="Gold")
public class Gold extends OreItem {

	@XmlAttribute(name="weight")
	private OreWeightEnum weight;
	
	public Gold() {	}
	
	public Gold(OreWeightEnum _weight) {
		this.weight=_weight;
		this.initializeWeight(_weight);
	}
	
	public Gold(int _x, int _y, OreWeightEnum _weight) {
		super(_x, _y);
		this.weight=_weight;
		this.initializeWeight(_weight);
	}
	
	/**
	 * Initialise les pépites d'or
	 */
	private void initializeWeight(OreWeightEnum _weight) {
		String srcImage="";
		switch(_weight) {
			default:
			case LITTLE:
				this.initializePolygon("/res/coord_gold_little.txt");
				srcImage="/res/images/gold_little.png";
				this.density=1;
				this.gain=50;
				this.name="Little gold";
			break;
			case MEDIUM:
				this.initializePolygon("/res/coord_gold_medium.txt");
				srcImage="/res/images/gold_medium.png";
				this.density=1;
				this.gain=150;
				this.name="Medium gold";
			break;
			case BIG:
				this.initializePolygon("/res/coord_gold_big.txt");
				srcImage="/res/images/gold_big.png";
				this.density=2;
				this.gain=400;
				this.name="Big gold";
			break;
		}
		try {
			this.image=ImageIO.read(this.getClass().getResource(srcImage).openStream());
			this.width=image.getWidth();
			this.height=image.getHeight();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter de la grandeur de l'or
	 */
	public void setWeight() {
		this.initializeWeight(weight);
	}
}