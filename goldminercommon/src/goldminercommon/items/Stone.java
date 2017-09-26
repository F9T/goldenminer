package goldminercommon.items;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import goldminercommon.enums.OreWeightEnum;

@XmlRootElement(name="Stone")
public class Stone extends OreItem {

	@XmlAttribute(name="weight")
	private OreWeightEnum weight;
	
	public Stone() {}
	
	public Stone(OreWeightEnum _weight) {
		this.weight=_weight;
		this.initializeWeight(_weight);
	}
	
	public Stone(int _x, int _y, OreWeightEnum _weight) {
		super(_x, _y);
		this.weight=_weight;
		this.initializeWeight(_weight);
	}
	
	/**
	 * Initialise les pierres
	 */
	private void initializeWeight(OreWeightEnum _weight) {
		String srcImage="";
		switch(_weight) {
			default:
			case LITTLE:
				this.initializePolygon("/res/coord_stone_little.txt");
				srcImage="/res/images/stone_little.png";
				this.density=3;
				this.gain=20;
				this.name="Little stone";
			break;
			case BIG:
				this.initializePolygon("/res/coord_stone_big.txt");
				srcImage="/res/images/stone_big.png";
				this.density=3;
				this.gain=30;
				this.name="Big stone";
			break;
		}
		try {
			this.image=ImageIO.read(this.getClass().getResource(srcImage).openStream());
			this.width=image.getWidth();
			this.height=image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Grandeur des pierres
	 */
	public void setWeight() {
		this.initializeWeight(weight);
	}
}
