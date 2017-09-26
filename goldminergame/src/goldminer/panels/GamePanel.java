package goldminer.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.xml.bind.JAXBException;

import goldminer.SoundEffect;
import goldminer.ExplodeEffect;
import goldminer.GameFrame;
import goldminer.GameManager;
import goldminer.Music;
import goldminer.PanelSlider;
import goldminer.items.EnumBonusItem;
import goldminer.items.GameInfo;
import goldminer.items.Grapple;
import goldminer.menu.ImageMenu;
import goldminer.menu.Menu;
import goldminercommon.Level;
import goldminercommon.LevelInfo;
import goldminercommon.XmlLevelParser;
import goldminercommon.config.SettingsGame;
import goldminercommon.items.Bomb;
import goldminercommon.items.DropItem;
import goldminercommon.items.OreItem;
import goldminercommon.items.SurpriseBag;

public class GamePanel extends AbstractPanel implements KeyListener {

	private static final long serialVersionUID = -3196263058761093342L;
	
	public enum StateGame { PLAY, PAUSE, START_MENU, GAME_OVER, FINISH }

	public StateGame stateGame;
	private Grapple grapple;
	private Timer rotationTimer, timeTimer;
	private Menu startMenu, pauseMenu, actualMenu;
	private ImageMenu selectedMenuImage;
	private LevelInfo levelInfo;
	private Level currentLevel;
	private int elapsedTime, catchGain;
	private long startGainTime;
	private boolean catchedItem, tntAnimation, multx1, multx2;
	private Image tntImage;
	private ExplodeEffect bombExplode, tntExplode;
	private GameFrame gameFrame;
	private SoundEffect soundEffect;
	private Music music;

	public GamePanel(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name, GameFrame _gameFrame) {
		super(_panelSlider, _gameInfo, _name);
		soundEffect = new SoundEffect();
		this.gameFrame=_gameFrame;
		this.loadLevels();
		this.grapple=new Grapple();
		this.rotationTimer=new Timer(75, rotationTick);
		this.timeTimer=new Timer(1000, timeTick);
		this.startMenu=new Menu("/resources/images/menu/menu_background.png", 300, 89, 15);
		this.pauseMenu=new Menu("/resources/images/menu/menu_background.png", 300, 89, 15);
		this.currentLevel=null;
		this.initialize();
	}
	
	private void initialize() {
		this.music = new Music();
		this.stateGame=StateGame.START_MENU;
		this.catchedItem=false;
		this.tntAnimation=false;
		this.multx1=false;
		this.multx2=false;
		this.tntImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/stick_tnt_transparent.png"));

		this.bombExplode=new ExplodeEffect((int)(SettingsGame.WIDTH/2)-80, 10, "/resources/images/explosions/bomb", "explode", 19);
		this.tntExplode=new ExplodeEffect("/resources/images/explosions/tnt", "explode", 6);

		this.startMenu.addMenuImage("/resources/images/menu/reloadMenu.png", "reload");
		this.startMenu.setEnabled(GameManager.checkSave(), "reload");
		this.startMenu.addMenuImage("/resources/images/menu/startMenu.png", "start");
		this.startMenu.addMenuImage("/resources/images/menu/helpMenu.png", "help");
		this.startMenu.addMenuImage("/resources/images/menu/quitMenu.png", "exit");

		this.pauseMenu.addMenuImage("/resources/images/menu/resumeMenu.png", "resume");
		this.pauseMenu.addMenuImage("/resources/images/menu/helpMenu.png", "help");
		this.pauseMenu.addMenuImage("/resources/images/menu/quitMenu.png", "exit");
		
		this.actualMenu=startMenu;

		this.addKeyListener(this);
		this.music.play();
	}
	
	public void setLevelInfo(LevelInfo _levelInfo) {
		this.levelInfo=_levelInfo;
	}
	
	public void useBonus(EnumBonusItem _bonusItem) {
		if(gameInfo.getBonus(_bonusItem)>0) {
			switch(_bonusItem) {
				case TNT:
					useTnt();
				break;
				case MORETIME:
					useMoreTime();
				break;
				case MULTX1:
					this.multx1=true;
				break;
				case MULTX2:
					this.multx2=true;
				break;
				case SPEED:
					useSpeed();
				break;
			}
			gameInfo.useBonus(_bonusItem);
		}
	}
	
	private void useSpeed() {
		grapple.setSpeedBack(15);
	}

	private void useMoreTime() {
		elapsedTime += 20;
	}

	private void useTnt() {
		if(!tntExplode.isExploded()) {
			tntAnimation=true;
			soundEffect.playBomb();
			tntExplode.setX((int)(grapple.getCurrentX()-(tntExplode.getWidth()/2)));
			tntExplode.setY((int)(grapple.getCurrentY()-(tntExplode.getHeight()/2)));
			tntExplode.explode();
			grapple.explodeTnt();
		}
	}
	
	public void destroyItemTnt(DropItem _item) {
		if(currentLevel!=null) {
			currentLevel.removeItem(_item);
		}
	}
	
	private void loadLevels() {
		File fileLevel=new File(SettingsGame.LEVEL_FILE_NAME);
		if(fileLevel.exists()) {
			try {
				this.levelInfo=XmlLevelParser.deserialize(fileLevel);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(this, "Erreur fichier des niveaux", "Erreur", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		} else {
			InputStream is=this.getClass().getResourceAsStream(SettingsGame.LEVEL_PATH);
			try {
				this.levelInfo=XmlLevelParser.deserialize(is);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(this, "Erreur fichier des niveaux", "Erreur", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}
	
	public void start() {
		nextLevel();
		startLevel();
		repaint();
	}
	
	private void startLevel() {
		if(currentLevel!=null) 
		{
			grapple.reset();
			gameInfo.resetBonus();
			this.multx1=false;
			this.multx2=false;
			stateGame=StateGame.PLAY;
			rotationTimer.start();
			timeTimer.start();
			elapsedTime=SettingsGame.DEFAULT_TIME_GAME;
			actualMenu=pauseMenu;
			this.removeMouseListener(this);
			this.removeMouseMotionListener(this);
			int nbTimer = gameInfo.getBonus(EnumBonusItem.MORETIME);
			if(nbTimer >= 1) {
				for(int i=1; i<=nbTimer; i++)
					useBonus(EnumBonusItem.MORETIME);
			}
			if(gameInfo.getBonus(EnumBonusItem.SPEED) > 0)
				useBonus(EnumBonusItem.SPEED);
			if(gameInfo.getBonus(EnumBonusItem.MULTX1) > 0) {
				useBonus(EnumBonusItem.MULTX1);
			}
			if(gameInfo.getBonus(EnumBonusItem.MULTX2) > 0) {
				useBonus(EnumBonusItem.MULTX2);
			}
		}
		repaint();
	}
	
	private void startAtLevel(int _levelNumber) {
		currentLevel=levelInfo.getLevelAtNumber(_levelNumber);
		if(currentLevel!=null) {
			currentLevel.initItems();
			elapsedTime=SettingsGame.DEFAULT_TIME_GAME;
			gameInfo.setGoalMoney(currentLevel.getGoal());
			startLevel();
		} else {
			start();
		}
	}
	
	public void restart() {
		gameInfo.reset();
		this.loadLevels();
		start();
		this.music.play();
	}
	
	public void resume() {
		stateGame=StateGame.PLAY;
		if(elapsedTime>0) {
			rotationTimer.start();
			timeTimer.start();
		}
		this.removeMouseListener(this);
		this.removeMouseMotionListener(this);
	}
	
	public void pause() {
		stateGame=StateGame.PAUSE;
		rotationTimer.stop();
		timeTimer.stop();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		repaint();
	}
	
	private void nextLevel() {
		currentLevel=levelInfo.nextLevel();
		if(currentLevel!=null) {
			currentLevel.initItems();
			elapsedTime=SettingsGame.DEFAULT_TIME_GAME;
			gameInfo.setGoalMoney(currentLevel.getGoal());
		} else {
			JOptionPane.showMessageDialog(this, "Vous avez gagné!!");
		}
	}
	
	public void gameOver() {
		stateGame=StateGame.GAME_OVER;
		this.music.stop();
		GameManager.removeSave();
		panelSlider.slideBottom("gameover");
	}
	
	private void checkWin() {
		if(gameInfo.checkWin()) {
			GameManager.saveGame(gameInfo);
			stateGame=StateGame.FINISH;
			timeTimer.start();
			panelSlider.slideTop("finish");
		} 
		else {
			gameOver();
		}
	}

	private void help() {
		panelSlider.slideBottom("help");
	}
	
	private void update() {
		grapple.update(this);
		repaint();
	}
	
	public ArrayList<DropItem> getCurrentItems() {
		if(currentLevel!=null) {
			return currentLevel.getItems();
		}
		return null;
	}
	
	/**
	 * Attrape un item arrivé au sommet
	 * @param _item l'item attrapé
	 */
	public void catchItem(DropItem _item) {
		if(currentLevel!=null) {
			final int currentGain=gameInfo.getAmountMoney();
			currentLevel.removeItem(_item);
			if(_item instanceof OreItem) {
				soundEffect.playGold();
				gameInfo.increaseMoney(multBonus(((OreItem)_item).getGain()));
			} else if(_item instanceof Bomb) {
				soundEffect.playBomb();
				gameInfo.decreasePourcent(((Bomb)_item).getPercentDecrease());
				bombExplode.explode();
			} else if(_item instanceof SurpriseBag) {
				SurpriseBag surpriseBag=(SurpriseBag) _item;
				int gain=surpriseBag.randomSurprise();
				if(gain==-1) {
					gameInfo.decreasePourcent(50);
				} else {
					soundEffect.playGold();
					gameInfo.increaseMoney(multBonus(gain));
				}
			}
			catchGain=gameInfo.getAmountMoney()-currentGain;
			catchedItem=true;
			startGainTime=0;
		}
	}
	
	private int multBonus(float _gain) {
		if(multx1) {
			_gain*=1.25;
		}
		if(multx2) {
			_gain*=1.5;
		}
		return Math.round(_gain);
	}

	public void launchGrapple() {
		if(stateGame==StateGame.PLAY) {
			grapple.launch();
		}
	}
	
	private ActionListener rotationTick=new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			update();
		}
	};
	
	private ActionListener timeTick=new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(stateGame==StateGame.PLAY) {
				elapsedTime-=1;
				if(elapsedTime==0) {
					timeTimer.stop();
					rotationTimer.stop();
					checkWin();
				}
			}
			repaint();
		}
	};
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		switch(stateGame) {
			case PAUSE:
				currentLevel.paint(g);
				gameInfo.paint(g);
				g.setFont(SettingsGame.DEFAULT_FONT_GAME);
				g.setColor(Color.BLACK);
				g.drawString(elapsedTime+" s", 970, 70);
				grapple.paint(g);
				pauseMenu.paint(g);
			break;
			case PLAY:
				currentLevel.paint(g);
				gameInfo.paint(g);
				g.setFont(SettingsGame.DEFAULT_FONT_GAME);
				//On affiche pendant 2 secondes le gain perdu ou gagné de l'objet attrapé
				if(catchedItem) {
					String str="";
					if(catchGain>0) {
						g.setColor(SettingsGame.WIN_COLOR);
						str+="+";
					}
					else {
						if(catchGain==0) str+="-";
						g.setColor(SettingsGame.LOSE_COLOR);
					}
					g.drawString(str+catchGain+" $", (this.getWidth()/2)+10, 35);
					if(startGainTime==0)
						startGainTime=System.currentTimeMillis();
					final long elapsedTime=System.currentTimeMillis()-startGainTime;
					if(elapsedTime>2000) {
						startGainTime=0;
						catchedItem=false;
					}
				}
				g.setColor(Color.BLACK);
				g.drawString(elapsedTime+" s", 970, 70);
				grapple.paint(g);
				bombExplode.paint(g);
				if(!tntAnimation) {
					tntExplode.paint(g);
				}
				if(tntAnimation) {
					g.drawImage(tntImage, (int)grapple.getCurrentX(), (int)grapple.getCurrentY(), 70, 47, this);
					tntAnimation=false;
				}
			break;
			case START_MENU:
				startMenu.paint(g);
			break;
			case FINISH:
			break;
			case GAME_OVER:
			break;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		ImageMenu imageMenu=actualMenu.overlap(e.getX(), e.getY());
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
		ImageMenu imageMenu=actualMenu.overlap(e.getX(), e.getY());
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
	public void mouseClicked(MouseEvent e) {
		if(selectedMenuImage!=null && selectedMenuImage.isEnabled()) {
			String name=selectedMenuImage.getName();
			switch(name) {
				case "reload":
					gameInfo=GameManager.loadGame();
					this.gameFrame.updateGameInfo(gameInfo);
					this.startAtLevel(gameInfo.getCountLevel());
				break;
				case "start":
					this.start();
					GameManager.removeSave();
				break;
				case "resume":
					this.resume();
				break;
				case "help":
					this.help();
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
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				launchGrapple();
			break;
			case KeyEvent.VK_ENTER:
				useBonus(EnumBonusItem.TNT);
			break;
			case KeyEvent.VK_ESCAPE:
				if(stateGame==StateGame.PLAY) {
					pause();
				} else if(stateGame==StateGame.PAUSE) {
					resume();
				}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(stateGame==StateGame.FINISH) {
			GameManager.saveGame(gameInfo);
			start();
		}
		else if(stateGame==StateGame.GAME_OVER)
			restart();
	}

	@Override
	public void focusLost(FocusEvent e) {
		
	}
}
