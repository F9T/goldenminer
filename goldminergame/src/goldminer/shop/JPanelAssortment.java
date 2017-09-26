
package goldminer.shop;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import goldminer.items.EnumBonusItem;
import goldminer.items.GameInfo;
import goldminercommon.config.SettingsGame;

public class JPanelAssortment extends JPanel implements ActionListener {

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	
	private static final long serialVersionUID = 8870855860362009246L;
	private GameInfo gameInfo;
	private JPanelShop jpanelParent;
	private ButtonBonus buttonTnt,  buttonSpeedHook, buttonMoreTime, buttonMultiplicator, buttonMultiplicatorPlus;
	private JLabel labelTnt, labelSpeedHook, labelMoreTime, labelMultiplicator, labelMultiplicatorPlus;
	private String messageInfo;

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	
	public JPanelAssortment(JPanelShop j, GameInfo _gameInfo) {
		this.jpanelParent = j;
		this.gameInfo=_gameInfo;
		this.geometry();
		this.control();
		this.appearance();
	}
	
	public void updateGameInfo(GameInfo _gameInfo) {
		this.gameInfo=_gameInfo;
	}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transformInitial = g2d.getTransform();
		try {
			draw(g2d);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g2d.setTransform(transformInitial);
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getMessage() {
		return messageInfo;
	}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void draw(Graphics2D g2d) throws IOException {
		Font fontDrawString = new Font("Monospaced", Font.BOLD, 18);
		g2d.setFont(fontDrawString);
		g2d.setColor(new Color(124, 252, 0));
		// avant 70 --> 490;
		g2d.drawString("" + buttonTnt.getPrice() + "$", 140, 120);
		g2d.drawString("" + buttonSpeedHook.getPrice()  + "$", 340, 120);
		g2d.drawString("" + buttonMoreTime.getPrice()  + "$", 550, 120);
		g2d.drawString("" + buttonMultiplicator.getPrice()  + "$", 775, 120);
		g2d.drawString("" + buttonMultiplicatorPlus.getPrice()  + "$", 985, 120);

	}

	private void geometry() {

		// création boutons
		buttonTnt = createButton("stick_tnt_transparent.png", "stick_tnt_out.png", "stick_tnt_close.png", EnumBonusItem.TNT);
		buttonSpeedHook =createButton("bonus_speed.png", "bonus_speed_out.png", "bonus_speed_close.png", EnumBonusItem.SPEED);
		buttonMoreTime = createButton("bonus_moretime.png", "bonus_moretime_out.png", "bonus_moretime_close.png", EnumBonusItem.MORETIME);
		buttonMultiplicator =createButton("bonus_multiplicateur.png", "bonus_multiplicateur_out.png", "bonus_multiplicateur_close.png", EnumBonusItem.MULTX1);
		buttonMultiplicatorPlus =createButton("bonus_multiplicateur_plus.png", "bonus_multiplicateur_plus_out.png", "bonus_multiplicateur_plus_close.png", EnumBonusItem.MULTX2);

		labelTnt = new JLabel("<html><div style='text-align: center;'>Permet de détruire un objet pendant le remontage</div></html>", SwingConstants.CENTER);
		labelSpeedHook = new JLabel("<html><div style='text-align: center;'>Augmente la vitesse de remontée du grappin</div></html>", SwingConstants.CENTER);
		labelMoreTime = new JLabel("<html><div style='text-align: center;'>Ajoute 15 secondes<br>au temps imparti</div></html>", SwingConstants.CENTER);
		labelMultiplicator = new JLabel("<html><div style='text-align: center;'>Multiplie la valeur de chaque prise par 1.10 pendant tout le niveau</div></html>", SwingConstants.CENTER);
		labelMultiplicatorPlus = new JLabel("<html><div style='text-align: center;'>Multiplie la valeur de chaque prise par 1.25 pendant tout le niveau</div></html>", SwingConstants.CENTER);

		// Layout : Specification
		GridLayout layout = new GridLayout(2, 5);
		setLayout(layout);

		layout.setHgap(25);

		// JComponent : add
		add(buttonTnt);
		add(buttonSpeedHook);
		add(buttonMoreTime);
		add(buttonMultiplicator);
		add(buttonMultiplicatorPlus);
		add(labelTnt);
		add(labelSpeedHook);
		add(labelMoreTime);
		add(labelMultiplicator);
		add(labelMultiplicatorPlus);
	}
	
	private ButtonBonus createButton(String _srcNormal, String _srcOut, String _srcClose, EnumBonusItem _bonusType) {
		final ImageIcon imageNormal = createImageIcon(SettingsGame.PATH_SHOP+_srcNormal);
		final ImageIcon imageOut = createImageIcon(SettingsGame.PATH_SHOP+_srcOut);
		final ImageIcon imageClose = createImageIcon(SettingsGame.PATH_SHOP+_srcClose);
		return new ButtonBonus(imageNormal, imageOut, imageClose, _bonusType);
	}
	
	private ImageIcon createImageIcon(String _src) {
		return new ImageIcon(this.getClass().getResource(_src));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof ButtonBonus) {
			final int price=((ButtonBonus)e.getSource()).getPrice();
			EnumBonusItem bonusType=((ButtonBonus)e.getSource()).getBonusType();
			if(gameInfo.hasEnoughMoney(price)) {
				if(((ButtonBonus)e.getSource()).use()) {
					switch(bonusType) {
						case TNT:
							messageInfo = "TNT acquis";
						break;
						case MORETIME:
							messageInfo="More Time acquis";
						break;
						case MULTX1:
							messageInfo="Multiplicateur acquis";
						break;
						case MULTX2:
							messageInfo="MultiplicateurPlus acquis";
						break;
						case SPEED:
							messageInfo="Vitesse grappin acquis";
						break;
					}
					gameInfo.decreaseMoney(price);
					gameInfo.addBonus(bonusType, 1);
					gameInfo.increaseSpentMoney(price);
					checkAllButton();
				}
			} else {
				messageInfo="Argent insuffisant";
			}
			jpanelParent.repaint();
		}
	}
	
	private void checkAllButton() {
		buttonTnt.checkEnabled(gameInfo.getAmountMoney());
		buttonSpeedHook.checkEnabled(gameInfo.getAmountMoney());
		buttonMoreTime.checkEnabled(gameInfo.getAmountMoney());
		buttonMultiplicator.checkEnabled(gameInfo.getAmountMoney());
		buttonMultiplicatorPlus.checkEnabled(gameInfo.getAmountMoney());
	}
	
	private void control() {

		initShop(gameInfo.getCountLevel());

		messageInfo = "Bienvenue dans la Boutique";

		buttonTnt.addActionListener(this);
		buttonSpeedHook.addActionListener(this);
		buttonMoreTime.addActionListener(this);
		buttonMultiplicator.addActionListener(this);
		buttonMultiplicatorPlus.addActionListener(this);
	}

	private void appearance() {
		setOpaque(false);

		labelTnt.setForeground(new Color(255, 255, 255));
		labelMoreTime.setForeground(new Color(255, 255, 255));
		labelSpeedHook.setForeground(new Color(255, 255, 255));
		labelMultiplicator.setForeground(new Color(255, 255, 255));
		labelMultiplicatorPlus.setForeground(new Color(255, 255, 255));

		Font fontLabel = new Font("Monospaced", Font.BOLD, 17);
		labelTnt.setFont(fontLabel);
		labelSpeedHook.setFont(fontLabel);
		labelMoreTime.setFont(fontLabel);
		labelMultiplicator.setFont(fontLabel);
		labelMultiplicatorPlus.setFont(fontLabel);

	}
	
	public void initShop(int _level) {

		final int amountMoney=gameInfo.getAmountMoney();
		int addPercent=_level;
		if(addPercent>=10) addPercent=10;
		buttonTnt.setPrice(100+(amountMoney*((1+addPercent))/100));
		buttonSpeedHook.setPrice(150+(amountMoney*((3+addPercent))/100));
		buttonMoreTime.setPrice(150+(amountMoney*((5+addPercent))/100));
		buttonMultiplicator.setPrice(250+(amountMoney*((7+addPercent))/100));
		buttonMultiplicatorPlus.setPrice(300+(amountMoney*((10+addPercent))/100));
		if(_level==1) {
			buttonTnt.setAvaible(1);
			buttonSpeedHook.setAvaible(1);
			buttonMoreTime.close();
			buttonMultiplicator.close();
			buttonMultiplicatorPlus.close();
		} else if(_level==2) {
			buttonTnt.setAvaible(2);
			buttonSpeedHook.setAvaible(1);
			buttonMoreTime.setAvaible(1);
			buttonMultiplicator.close();
			buttonMultiplicatorPlus.close();
		} else if(_level==3) {
			buttonTnt.setAvaible(2);
			buttonSpeedHook.setAvaible(2);
			buttonMoreTime.setAvaible(2);
			buttonMultiplicator.setAvaible(1);
			buttonMultiplicatorPlus.close();
		} else if(_level>=4) {
			buttonTnt.setAvaible(3);
			buttonSpeedHook.setAvaible(3);
			buttonMoreTime.setAvaible(3);
			buttonMultiplicator.setAvaible(1);
			buttonMultiplicatorPlus.setAvaible(1);
		}
		buttonTnt.checkEnabled(amountMoney);
		buttonSpeedHook.checkEnabled(amountMoney);
		buttonMoreTime.checkEnabled(amountMoney);
		buttonMultiplicator.checkEnabled(amountMoney);
		buttonMultiplicatorPlus.checkEnabled(amountMoney);
		jpanelParent.repaint();
		repaint();
	}
}
