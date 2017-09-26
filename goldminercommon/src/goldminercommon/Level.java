package goldminercommon;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import goldminercommon.items.Bomb;
import goldminercommon.items.Diamond;
import goldminercommon.items.DropItem;
import goldminercommon.items.Gold;
import goldminercommon.items.Stone;
import goldminercommon.items.SurpriseBag;

@XmlRootElement(name="Level")
public class Level {

	private int no, goal;
	
	@XmlElements({
	    @XmlElement(name = "Gold", type = Gold.class),
	    @XmlElement(name = "SurpriseBag", type = SurpriseBag.class),
	    @XmlElement(name = "Diamond", type = Diamond.class),
	    @XmlElement(name = "Stone", type = Stone.class),
	    @XmlElement(name = "Bomb", type = Bomb.class)
    })
	private ArrayList<DropItem> items;
	
	public Level() {
		this.items=new ArrayList<DropItem>();
	}
	
	public Level(int _no, int _goal) {
		this();
		this.no=_no;
		this.goal=_goal;
	}
	
	/**
	 * Initialisation des items
	 */
	public void initItems() {
		for(DropItem item : items) {
			if(item instanceof Stone) ((Stone)item).setWeight();
			else if(item instanceof Gold) ((Gold)item).setWeight();
			else if(item instanceof Bomb) item.initializePolygon("/res/coord_bomb.txt");
			else if(item instanceof Diamond) item.initializePolygon("/res/coord_diamond.txt");
			else if(item instanceof SurpriseBag) item.initializePolygon("/res/coord_surprise_bag.txt");
		}
	}
	
	/**
	 * Ajoute un item
	 * @param _item item à ajouter
	 */
	public void addItem(DropItem _item) {
		items.add(_item);
	}
	
	/**
	 * Supprime un item
	 * @param _item item à supprimer
	 */
	public void removeItem(DropItem _item) {
		items.remove(_item);
	}
	
	/**
	 * Getter de tous les items
	 * @return liste des items
	 */
	public ArrayList<DropItem> getItems() {
		return items;
	}
	
	/**
	 * Setter du nom du niveau
	 * @param _no le nom du niveau
	 */
	public void setNo(int _no) {
		this.no=_no;
	}

	/**
	 * Getter du nom du niveau
	 * @return le nom du niveau
	 */
	@XmlAttribute(name="no")
	public int getNo() {
		return no;
	}
	
	/**
	 * Setter de l'objectif à atteindre
	 * @param _goal l'objectif à atteindre
	 */
	public void setGoal(int _goal) {
		this.goal=_goal;
	}

	/**
	 * Getter de l'objectif à atteindre
	 * @return l'objectif à atteindre
	 */
	@XmlAttribute(name="goal")
	public int getGoal() {
		return goal;
	}
	
	/**
	 * Affiche tous les items
	 * @param g graphics
	 */
	public void paint(Graphics g) {
		for(DropItem item : items) {
			item.paint(g);
		}
	}
	
	/**
	 * Retourne le nom du niveau
	 */
	@Override
	public String toString() {
		return "Number : "+no;
	}
}
