package goldminer.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;
import goldminer.menu.ImageMenu;
import goldminer.menu.Menu;
import goldminercommon.config.SettingsGame;

public class PanelGameOver extends AbstractDisplayPanel {

	private static final long serialVersionUID = 1588591243758453600L;
	private final String textGameOver="GAME OVER";
	private final int widthText;
	private final Menu gameOverMenu;
	private ImageMenu selectedMenuImage;
	
	public PanelGameOver(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		super(_panelSlider, _gameInfo, _name);
		this.widthText=this.getFontMetrics(SettingsGame.FONT_GAMEOVER_FINISH).stringWidth(textGameOver);
		this.gameOverMenu=new Menu(300, 89, 15);
		this.gameOverMenu.addMenuImage("/resources/images/menu/restartMenu.png", "restart");
		this.gameOverMenu.addMenuImage("/resources/images/menu/quitMenu.png", "exit");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(SettingsGame.FONT_GAMEOVER_FINISH);
		g.setColor(Color.WHITE);
		final int posX=this.getWidth()/2-widthText/2;
		final int posY=this.getHeight()/2-300;
		g.drawString(textGameOver, posX, posY);
		g.setFont(SettingsGame.DEFAULT_FONT_GAME);
		g.drawString("Niveau atteint : "+gameInfo.getCountLevel(), posX, posY+60);
		g.drawString("Argent total récolté : "+gameInfo.getTotalAmountMoney()+" $", posX, posY+110);
		g.drawString("Argent total dépensé : "+gameInfo.getSpentMoney()+" $", posX, posY+160);
		gameOverMenu.paint(g);
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(selectedMenuImage!=null) {
			String name=selectedMenuImage.getName();
			switch(name) {
				case "restart":
					panelSlider.slideTop("game");
				break;
				case "exit":
					System.exit(0);
				break;
			}
			selectedMenuImage.released();
			selectedMenuImage.exited();
			selectedMenuImage=null;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(selectedMenuImage!=null) {
			selectedMenuImage.pressed();
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(selectedMenuImage!=null) {
			selectedMenuImage.released();
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		ImageMenu imageMenu=gameOverMenu.overlap(e.getX(), e.getY());
		if(imageMenu!=null && selectedMenuImage==null) {
			imageMenu.entered();
			selectedMenuImage=imageMenu;
			repaint();
		} else if(imageMenu!=null && selectedMenuImage!=imageMenu) {
			selectedMenuImage.released();
			selectedMenuImage.exited();
			selectedMenuImage=null;
			imageMenu.entered();
			selectedMenuImage=imageMenu;
			repaint();
		} else if(imageMenu==null && selectedMenuImage!=null) {
			selectedMenuImage.exited();
			selectedMenuImage.released();
			selectedMenuImage=null;
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ImageMenu imageMenu=gameOverMenu.overlap(e.getX(), e.getY());
		if(imageMenu!=null && selectedMenuImage==null) {
			imageMenu.entered();
			selectedMenuImage=imageMenu;
			repaint();
		} else if(imageMenu!=null && selectedMenuImage!=imageMenu) {
			selectedMenuImage.released();
			selectedMenuImage.exited();
			selectedMenuImage=null;
			imageMenu.entered();
			selectedMenuImage=imageMenu;
			repaint();
		} else if(imageMenu==null && selectedMenuImage!=null) {
			selectedMenuImage.exited();
			selectedMenuImage.released();
			selectedMenuImage=null;
			repaint();
		}
	}
}
