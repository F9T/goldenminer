package goldminercommon;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="LevelInfo")
public class LevelInfo {

	@XmlElementWrapper(name="Levels")
    @XmlElement(name = "Level")
	private ArrayList<Level> levels;
	
	@XmlTransient
	private int currentIndex;
	
	public LevelInfo() {
		this.levels=new ArrayList<Level>();
		this.currentIndex=-1;
	}
	
	/**
	 * Ajoute un niveau
	 * @param _level le niveau à ajouter
	 */
	public void addLevel(Level _level) {
		levels.add(_level);
	}
	
	/**
	 * Supprime un niveau
	 * @param _level le niveau à supprimer
	 */
	public void removeLevel(Level _level) {
		levels.remove(_level);
	}
	
	/**
	 * Getter de tous les niveaux
	 * @return liste des nieaux
	 */
	public Level[] getArrayLevel() {
		return levels.toArray(new Level[levels.size()]);
	}
	
	/**
	 * Remise à zéro de la classe
	 */
	public void reset() {
		this.currentIndex=-1;
	}

	/**
	 * Récupère le prochain niveau
	 * @return un niveau
	 */
	public Level nextLevel() {
		++currentIndex;
		if(currentIndex>=levels.size()) {
			return null;
		}
		return levels.get(currentIndex);
	}
	
	/**
	 * Recupère un niveau à l'index donné
	 * @param _levelNumber l'index du niveau
	 * @return un niveau
	 */
	public Level getLevelAtNumber(int _levelNumber) {
		if(_levelNumber<levels.size()) {
			currentIndex=_levelNumber;
			return levels.get(_levelNumber);
		}
		return null;
	}
	
	/**
	 * Getter du nombre de niveaux totals
	 * @return nombre de niveaux
	 */
	public int getLevelCount() {
		return levels.size();
	}
	
	/**
	 * Initialise les niveaux
	 */
	public void initLevels() {
		for(Level level : levels) {
			level.initItems();
		}
	}
}

