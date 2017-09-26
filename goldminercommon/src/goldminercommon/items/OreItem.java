package goldminercommon.items;

import javax.xml.bind.annotation.XmlTransient;

public abstract class OreItem extends DropItem {

	protected int gain;
	
	public OreItem() {}
	
	public OreItem(int _x, int _y) {
		super(_x, _y);
	}
	
	/**
	 * Getter du gain à gagner de l'item
	 * @return
	 */
	@XmlTransient
	public int getGain() {
		return gain;
	}
}
