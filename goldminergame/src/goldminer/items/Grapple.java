package goldminer.items;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import goldminer.panels.GamePanel;
import goldminercommon.config.SettingsGame;
import goldminercommon.items.DropItem;

public class Grapple {

	private enum StateGrapple { ROTATE, FORWARD, BACK }
	
	private StateGrapple stateGrapple;
	private double theta, ropeLength, ropeX, ropeY;
	private int rotation, angularSpeed, speed, speedBack; //if rotation=-1 -> grapple direction left else rotation=1 -> grapple direction right
	private DropItem catchItem;
	private boolean explodeTnt;

	
	public Grapple() {
		this.reset();
	}
	
	public void reset() {
		this.stateGrapple=StateGrapple.ROTATE;
		this.ropeX=SettingsGame.WIDTH/2;
		this.ropeY=90;
		this.angularSpeed=35; //L'angle que le grappin bouge par tick du timer (influence sur la vitesse)
		this.speed=15;
		this.speedBack=0;
		this.catchItem=null;
		this.explodeTnt=false;
		this.ropeLength=0; //La longueur de la corde du grappin
		this.theta=0; //L'angle actuel du grappin
	}
	
	public void update(GamePanel _panel) {
		 switch (stateGrapple) {
	         case ROTATE:
	        	 //Rotation du grappin
	         	theta += rotation*Math.PI/angularSpeed;
	         	
	         	//Change la direction du grappin
	         	if (theta>=Math.PI*(angularSpeed-1)/angularSpeed) {
	         		rotation = -1;
	         	} else if(theta<=Math.PI/angularSpeed) {
	         		rotation = 1;
	         	}
	         break;
	         case FORWARD:
	        	 try {
	        	 ropeLength+=speed;				//Condition si une tnt explose
				if(explodeTnt) {
					if(catchItem!=null) {
						_panel.destroyItemTnt(catchItem);
						catchItem=null;
					}
					explodeTnt=false;
				}
 
				if(getCurrentX()<0 || getCurrentX()>SettingsGame.WIDTH || getCurrentY()>SettingsGame.HEIGHT) {
					stateGrapple=StateGrapple.BACK;
					break;
				}
				
				//Check la collision avec un item
				Polygon pol=getPolygonCollision(getCurrentX(), getCurrentY(), theta);
				for(DropItem item : _panel.getCurrentItems()) {
					//Si on touche un item on l'attrape et on remonte le grappin
					if(item.intersect(pol)) {
						stateGrapple=StateGrapple.BACK;
						catchItem=item;
					}
				}
	        	 }catch(Exception e) {
	        		 JOptionPane.showMessageDialog(null, e.getStackTrace()[0]);
	        	 }
	    	 break;
	         case BACK:
	        	 ropeLength-=getWeight() + speedBack;
	        	 
	        	 if(explodeTnt) {
	        		 //Si on a un item on le détruit
	        		 if(catchItem!=null) {
	        			 _panel.destroyItemTnt(catchItem);
	        			 catchItem=null;
	        		 }
        			 explodeTnt=false;
	        	 }
	        	 
	        	 //Si on arrive tout en haut, on remet le grappin en rotation
	        	 if(ropeLength<=0) {
	        		 stateGrapple=StateGrapple.ROTATE;
	        		 ropeLength=0;
	        		 //Si on a un item on recupère les gains
	        		 if(catchItem!=null) {
	        			 _panel.catchItem(catchItem);
	        		 	catchItem=null;
	        		 }
	        		 break;
	        	 }
	        	 
	        	 //Permet de faire bouger l'objet en meme temps que le grappin
	        	 if(catchItem!=null) {
	        		 catchItem.setX((int)getCurrentX()-(catchItem.getWidth()/2));
	        		 catchItem.setY((int)getCurrentY());
	        	 }
	    	 break;
	 	}
	}
	
	/**
	 * Getter du poids total à remonter, plus le poids est lourd plus le grappin mettra du temps
	 * @return le poids à remonter
	 */
	private int getWeight() {
		if(catchItem==null) {
			return speed;
		} else {
			//Calcul du poids par rapport à la densité, la largeur et la hauteur de l'item
			//Les 90000 ont été défini après quelques tests
			int calcSpeed=90000/(catchItem.getDensity()*catchItem.getWidth()*catchItem.getHeight());
			if(calcSpeed>speed) {
				return speed;
			}
			return calcSpeed;
		}
	}
	
	/**
	 * Lance le grappin s'il et en rotation uniquement
	 */
	public void launch() {
		if (stateGrapple == StateGrapple.ROTATE)
			stateGrapple = StateGrapple.FORWARD;
	}
	
	/**
	 * L'utilisateur a envoyé une TNT
	 */
	public void explodeTnt() {
		explodeTnt=true;
	}
	
	/**
	 * Recupère le polygon du grappin par rapport à l'angle actuel
	 * @param _currentX position actuel par rapport à l'angle en x
	 * @param _currentY position actuel par rapport à l'angle en y
	 * @param _theta l'angle actuel
	 * @return
	 */
	private Polygon getPolygonCollision(double _currentX, double _currentY, double _theta) {
		//Les valeurs 23, 20 ont été testés pour trouver la bonne largeur et hauteur du polygon
		final int rightX1=(int)(23*Math.sin(_theta)+_currentX);
		final int rightY1=(int)(-23*Math.cos(_theta)+_currentY);
		final int rightX2=(int)(20*Math.cos(_theta)+rightX1);
		final int rightY2=(int)(20*Math.sin(_theta)+rightY1);
		final int leftX1=(int)(-23*Math.sin(_theta)+_currentX);
		final int leftY1=(int)(23*Math.cos(_theta)+_currentY);
		final int leftX2=(int)(20*Math.cos(_theta)+leftX1);
		final int leftY2=(int)(20*Math.sin(_theta)+leftY1);
		final int centerX=(int)_currentX;
		final int centerY=(int)_currentY;
		
		final int xPoints[]= {leftX1, rightX1, rightX2, centerX, leftX2};
		final int yPoints[]= {leftY1, rightY1, rightY2, centerY, leftY2};
		return new Polygon(xPoints, yPoints, xPoints.length);
	}
	
	/**
	 * Affiche le grappin avec sa rotation par rapport à l'angle actuel
	 * @param g
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		BufferedImage grappleImage;
		try {
			String url="";
			if(catchItem!=null) {
				url="/resources/images/grappleclose.png";
			} else {
				url="/resources/images/grappleopen.png";
			}
			grappleImage = ImageIO.read(getClass().getResource(url).openStream());
			
			final int width=grappleImage.getWidth();
			final int height=grappleImage.getHeight();

			g.setColor(SettingsGame.COLOR_ROPE);
			g2.setStroke(new BasicStroke(4.0f));
			//Dessine la corde du grappin
			g2.drawLine((int)ropeX, (int)ropeY, (int)getCurrentX(), (int)getCurrentY());
			AffineTransform tx=AffineTransform.getRotateInstance(theta, width/2, height/2);
			AffineTransformOp op=new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g2.drawImage(op.filter(grappleImage, null), (int)(getCurrentX()-(width/2)), (int)(getCurrentY()-(height/2)), null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter du bonus de vitesse de remonter
	 * @param s
	 */
	public void setSpeedBack(int s) {
		this.speedBack = s;
	}

	public double getCurrentX() {
		return ropeX+ropeLength*Math.cos(theta);
	}

	public double getCurrentY() {
		return ropeY+ropeLength*Math.sin(theta);
	}
}
