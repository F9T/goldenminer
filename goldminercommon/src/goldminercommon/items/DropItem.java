package goldminercommon.items;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

public abstract class DropItem {

	protected BufferedImage image;
	protected String name;
	protected int x, y, width, height, density;
	protected Polygon polygon;
	
	/*
	 * Constructeur
	 */
	public DropItem() {}
	
	/**
	 * Constructeur d'un item
	 * @param _x
	 * @param _y
	 */
	public DropItem(int _x, int _y) {
		this.x=_x;
		this.y=_y;
	}
	
	/**
	 * Affiche l'image de l'item
	 * @param g Graphics
	 */
	public void paint(Graphics g) {
		if(image!=null) g.drawImage(image, x, y, width, height, null);
	}
	
	/**
	 * Initialise le polygon entourant l'image depuis un fichier contenant toutes les coordonnées
	 * @param _srcFile le lien du fichier contenant les coordonnées
	 */
	public void initializePolygon(String _srcFile) {
		InputStream is = this.getClass().getResourceAsStream(_srcFile);
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		ArrayList<Integer> xPoints=new ArrayList<Integer>();
		ArrayList<Integer> yPoints=new ArrayList<Integer>();
	    String line;
	    try {
			while ((line = input.readLine()) != null) {
				if(line.contains(";")) {
			    	String coord[]=line.split(";");
			    	if(coord.length>=2) {
			    		xPoints.add(x+Integer.parseInt(coord[0]));
			    		yPoints.add(y+Integer.parseInt(coord[1]));
			    	}
				}
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		polygon=new Polygon(xPoints.stream().mapToInt(Integer::intValue).toArray(), yPoints.stream().mapToInt(Integer::intValue).toArray(), xPoints.size());
	}
	
	/**
	 * Vérifie si un rectangle touche l'item entier (rectangle)
	 * @param _intersectRect le rectangle à vérifier
	 * @return vrai si une collision a lieu
	 */
	public boolean intersect(Rectangle _intersectRect) {
		Rectangle rect=new Rectangle(x, y, width, height);
		return rect.intersects(_intersectRect);
	}
	
	/**
	 * Vérifie un polygon intersecte l'item découpée en polygon
	 * @param _polygon le polygon à vérifier
	 * @return vrai si une collision a lieu
	 */
	public boolean intersect(Polygon _polygon) {
        Point p;
        for(int i=0; i<_polygon.npoints;i++) {
            p=new Point(_polygon.xpoints[i], _polygon.ypoints[i]);
            if(polygon.contains(p))
                return true;
        }
        for(int i=0;i<polygon.npoints;i++) {
            p=new Point(polygon.xpoints[i], polygon.ypoints[i]);
            if(_polygon.contains(p))
                return true;
        }
        return false;
	}
	
	/**
	 * Setter de la position x
	 * @param _x nouvelle position x
	 */
	public void setX(int _x) {
		this.x=_x;
	}

	/**
	 * Getter position x
	 * @return position x actuelle
	 */
	@XmlAttribute(name="x")
	public int getX() {
		return x;
	}
	
	/**
	 * Setter de la position y
	 * @param _y nouvelle position y
	 */
	public void setY(int _y) {
		this.y=_y;
	}

	/**
	 * Getter de la position y
	 * @return la position y
	 */
	@XmlAttribute(name="y")
	public int getY() {
		return y;
	}

	/**
	 * Getter de la largeur de l'item
	 * @return largeur de l'item
	 */
	@XmlTransient
	public int getWidth() {
		return width;
	}

	/**
	 * Getter de la hauteur de l'item
	 * @return hauteur de l'item
	 */
	@XmlTransient
	public int getHeight() {
		return height;
	}

	/**
	 * Getter de la densité de l'item
	 * @return la densité de l'item
	 */
	@XmlTransient
	public int getDensity() {
		return density;
	}
	
	/**
	 * Getter de l'image de l'item
	 * @return l'image de l'item
	 */
	@XmlTransient
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * La classe sous forme de chaine de caractères
	 */
	@Override
	public String toString() {
		return name;
	}
}
