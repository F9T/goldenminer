package goldminercommon.items;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Bomb extends DropItem {
	
	private int percentDecrease;
	
	public Bomb() {
		this.initialize();
	}
	
	public Bomb(int _x, int _y) {
		super(_x, _y);
		this.initialize();
		this.initializePolygon("/res/coord_bomb.txt");
	}
	
	/**
	 * Initialise les bombes
	 */
	private void initialize() {
		try {
			this.image=ImageIO.read(this.getClass().getResource("/res/images/bomb.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.percentDecrease=20;
		this.density=1;
		this.name="Bomb";
		this.width=image.getWidth();
		this.height=image.getHeight();
	}
	
	/**
	 * Getter du pourcentage de valeur à perdre
	 * @return pourcentage de la valeur à perdre
	 */
	public int getPercentDecrease() {
		return percentDecrease;
	}
}
