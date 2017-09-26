package goldminer.shop;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import goldminer.items.EnumBonusItem;

public class ButtonBonus extends JButton {

	private static final long serialVersionUID = -188851094522848578L;
	private int price, available;
	private EnumBonusItem bonusType;
	private ImageIcon icon, outIcon, closeIcon;
	private boolean isClosed;

	public ButtonBonus(ImageIcon _icon, ImageIcon _outIcon, ImageIcon _closeIcon, EnumBonusItem _bonusType) {
		super(_icon);
		this.icon=_icon;
		this.outIcon=_outIcon;
		this.closeIcon=_closeIcon;
		this.bonusType=_bonusType;
		this.price=100;
		this.available=1;
		this.isClosed=false;
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setContentAreaFilled(false);
	}
	
	public EnumBonusItem getBonusType() {
		return bonusType;
	}
	
	public void setPrice(int _price) {
		this.price=_price;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setAvaible(int _available) {
		this.available=_available;
		this.isClosed=available==0;
	}
	
	public boolean use() {
		if(available==0) return false;
		available--;
		return true;
	}
	
	public void checkEnabled(int _amountMoney) {
		if(!isClosed) {
			if(_amountMoney>=price && available>0) {
				this.setIcon(icon);
			} else if(available==0) {
				this.setIcon(outIcon);
			} else if(_amountMoney<price && available>0) {
				this.setIcon(closeIcon);
			}
		}
	}
	
	public void close() {
		this.setAvaible(0);
		this.setIcon(outIcon);
	}
}
