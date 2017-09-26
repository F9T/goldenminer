package goldminer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import goldminer.items.GameInfo;
import goldminer.panels.GamePanel;
import goldminer.panels.HelpPanel;
import goldminer.panels.GamePanel.StateGame;
import goldminer.panels.PanelFinishLevel;
import goldminer.panels.PanelGameOver;
import goldminer.shop.JPanelShop;
import goldminercommon.config.SettingsGame;

public class GameFrame extends JFrame implements WindowStateListener, WindowFocusListener {

	private static final long serialVersionUID = 6748091550290746686L;
	private PanelSlider<GameFrame> panelSlider;
	private GamePanel gamePanel;
	private JPanelShop shop;
	private PanelFinishLevel panelFinishLevel;
	private PanelGameOver panelGameOver;
	private HelpPanel helpPanel;
	private GameInfo gameInfo;
	private Image iconWindow;

	public GameFrame() throws Exception {
		this.iconWindow = Toolkit.getDefaultToolkit().getImage((this.getClass().getResource("/resources/images/icon_window.png")));
		this.gameInfo=new GameInfo(0, 0);
		this.panelSlider=new PanelSlider<GameFrame>(this);
		this.helpPanel=new HelpPanel(panelSlider, gameInfo, "help");
		this.gamePanel=new GamePanel(panelSlider, gameInfo, "game", this);
		this.panelFinishLevel=new PanelFinishLevel(panelSlider, gameInfo, "finish");
		this.shop=new JPanelShop(panelSlider, gameInfo, "shop");
		this.panelGameOver=new PanelGameOver(panelSlider, gameInfo, "gameover");
		this.panelSlider.addComponent(gamePanel);
		this.panelSlider.addComponent(panelGameOver);
		this.panelSlider.addComponent(shop);
		this.panelSlider.addComponent(panelFinishLevel);
		this.panelSlider.addComponent(helpPanel);
		this.setTitle("GoldMiner Game");
		this.setIconImage(iconWindow);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		Insets insetsFrame=this.getInsets();
		this.setPreferredSize(new Dimension(SettingsGame.WIDTH+insetsFrame.left+insetsFrame.right, SettingsGame.HEIGHT+insetsFrame.top+insetsFrame.bottom));
		this.setSize(new Dimension(SettingsGame.WIDTH+insetsFrame.left+insetsFrame.right, SettingsGame.HEIGHT+insetsFrame.top+insetsFrame.bottom));
		this.setLocationRelativeTo(null);
		this.addWindowStateListener(this);
		this.addWindowFocusListener(this);
		this.gamePanel.requestFocus();
	}
	
	public void updateGameInfo(GameInfo _gameInfo) {
		this.gameInfo=_gameInfo;
		this.panelFinishLevel.updateGameInfo(_gameInfo);
		this.shop.updateGameInfo(_gameInfo);
		this.panelGameOver.updateGameInfo(_gameInfo);
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if(e.getNewState()==JFrame.ICONIFIED) {
			if(gamePanel.stateGame==StateGame.PLAY) {
				gamePanel.pause();
			}
		} 
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		if(gamePanel.stateGame==StateGame.PLAY) {
			gamePanel.pause();
		}
	}
	
	@Override
	public void windowGainedFocus(WindowEvent e) {}
	
	public static void main(String[] _args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameFrame frame=new GameFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.toString());
					e.printStackTrace();
				}
			}
		});
	}
}
