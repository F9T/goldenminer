package goldminer.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;

import goldminercommon.config.SettingsGame;

public class GameInfo implements Serializable {

	private static final long serialVersionUID = -1747888836008482879L;

	private final int  widthIcon=16, heightIcon=16;
	private int amountMoney, goalMoney, tnt, bonusSpeed, multx1, multx2, bonusTime, countLevel, totalMoney, spentMoney;
	private boolean useBonusSpeed, useMultx1, useMultx2, useBonusTime;
	private transient Image tntImage, bonusSpeedImage, bonusTimeImage, multx1Image, multx2Image, trueImage, falseImage;
	
	public GameInfo(int _amountMoney, int _goalMoney) {
		this.reset();
		this.amountMoney=_amountMoney;
		this.goalMoney=_goalMoney;
	}
	
	public void paint(Graphics g) {
		this.initializeImage();
		g.setFont(SettingsGame.DEFAULT_FONT_GAME);
		g.setColor(Color.BLACK);
		g.drawImage(tntImage, 260, 20, 70, 47, null);
		g.drawString("x"+tnt, 280, 52);
		g.setColor(SettingsGame.TEXT_COLOR);
		g.drawString("Argent : ", 17, 35);
		g.drawString("Objectif : ", 17, 75);
		g.drawString("Temps restant", 850, 35);
		
		g.setColor(SettingsGame.WIN_COLOR);
		g.drawString(amountMoney + " $", 150, 35);
		g.drawString(goalMoney + " $", 150, 75);

		g.setFont(SettingsGame.FONT_BONUS);
		g.setColor(SettingsGame.TEXT_COLOR);
		paintBonus(g, EnumBonusItem.SPEED);
		paintBonus(g, EnumBonusItem.MORETIME);
		paintBonus(g, EnumBonusItem.MULTX1);
		paintBonus(g, EnumBonusItem.MULTX2);
	}
	
	public void paintResume(Graphics g, int _x, int _y) {
		g.setFont(SettingsGame.DEFAULT_FONT_GAME);
		g.drawString("Argent récolté : ", _x, _y+50);
		g.setColor(SettingsGame.WIN_COLOR);
		g.drawString(amountMoney+" $", _x+230, _y+50);
		
		g.setFont(SettingsGame.FONT_LEVEL_FINISH);
		g.setColor(Color.WHITE);
		g.drawString("Objets restants :", _x, _y+150);

		g.setFont(SettingsGame.DEFAULT_FONT_GAME);
		g.drawString("TNT : "+tnt, _x, _y+200);
		g.drawString("Bonus temps : "+bonusTime, _x,  _y+230);
		g.drawString("Bonus vitesse : "+bonusSpeed, _x,  _y+260);
		g.drawString("Mult. x1 : "+multx1, _x,  _y+290);
		g.drawString("Mutl. x2 : "+multx2, _x,  _y+320);
	}
	
	private void paintBonus(Graphics g, EnumBonusItem _bonusType) {
		int textPosX=650, imagePosX=770, posY=0;
		boolean trueOrFalse=false;
		switch(_bonusType) {
			case TNT:
			break;
			case SPEED:
				posY=20;
				g.drawString("Bonus vitesse :", textPosX, posY);
				trueOrFalse=useBonusSpeed;
			break;
			case MORETIME:
				posY=40;
				g.drawString("Bonus temps : ", textPosX, posY);
				trueOrFalse=useBonusTime;
			break;
			case MULTX1:
				posY=60;
				g.drawString("Mult. x1.25 :", textPosX, posY);
				trueOrFalse=useMultx1;
			break;
			case MULTX2:
				posY=80;
				g.drawString("Mult. x1.50 :", textPosX, posY);
				trueOrFalse=useMultx2;
			break;
		}
		if(trueOrFalse) g.drawImage(trueImage, imagePosX, posY-heightIcon+4, widthIcon, heightIcon, null);
		else g.drawImage(falseImage, imagePosX, posY-heightIcon+4, widthIcon, heightIcon, null);
	}
	
	private void initializeImage() {
		if(tntImage==null)
			this.tntImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/stick_tnt_transparent.png"));
		if(bonusSpeedImage==null)
			this.bonusSpeedImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/bonus_speed.png"));
		if(bonusTimeImage==null)
			this.bonusTimeImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/bonus_moretime.png"));
		if(multx1Image==null)
			this.multx1Image=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/bonus_multiplicateur.png"));
		if(multx2Image==null)
			this.multx2Image=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/shop/bonus_multiplicateur_plus.png"));
		if(trueImage==null)
			this.trueImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/true.png"));
		if(falseImage==null)
			this.falseImage=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/images/false.png"));
	}

	public void reset() {
		this.totalMoney=0;
		this.spentMoney=0;
		this.amountMoney=0;
		this.tnt=0;
		this.bonusSpeed=0;
		this.multx1=0;
		this.multx2=0;
		this.bonusTime=0;
		this.countLevel=0;
		this.resetBonus();
	}
	
	public void resetBonus() {
		this.useBonusSpeed=false;
		this.useMultx1=false;
		this.useMultx2=false;
		this.useBonusTime=false;
	}
	
	public int getCountLevel() {
		return countLevel;
	}
	
	public void decreaseMoney(int _money) {
		this.amountMoney-=_money;
	}
	
	public void increaseMoney(int _money) {
		this.amountMoney+=_money;
		this.totalMoney+=_money;
	}
	
	public void increaseSpentMoney(int _money) {
		this.spentMoney+=_money;
	}
	
	public int getAmountMoney() {
		return amountMoney;
	}
	
	public int getTotalAmountMoney() {
		return totalMoney;
	}
	
	public int getSpentMoney() {
		return spentMoney;
	}
	
	public void decreasePourcent(int _percentage) {
		this.amountMoney-=(this.amountMoney*_percentage)/100;
	}
	
	public void setGoalMoney(int _goalMoney) {
		this.goalMoney=_goalMoney;
		this.countLevel++;
	}
	
	public boolean hasEnoughMoney(int _money) {
		return amountMoney>=_money;
	}
	
	public boolean checkWin() {
		return amountMoney>=goalMoney;
	}
	
	public void addBonus(EnumBonusItem _bonusItem, int _number) {
		switch(_bonusItem) {
			case TNT:
				this.tnt+=_number;
			break;
			case MORETIME:
				this.bonusTime+=_number;
			break;
			case MULTX1:
				this.multx1+=_number;
			break;
			case MULTX2:
				this.multx2+=_number;
			break;
			case SPEED:
				this.bonusSpeed+=_number;
			break;
		}
	}
	
	public void useBonus(EnumBonusItem _bonusItem) {
		switch(_bonusItem) {
			case TNT:
				this.tnt--;
			break;
			case MORETIME:
				this.bonusTime--;
				this.useBonusTime=true;
			break;
			case MULTX1:
				this.multx1--;
				this.useMultx1=true;
			break;
			case MULTX2:
				this.multx2--;
				this.useMultx2=true;
			break;
			case SPEED:
				this.bonusSpeed--;
				this.useBonusSpeed=true;
			break;
		}
	}
	
	public int getBonus(EnumBonusItem _bonusItem) {
		switch(_bonusItem) {
			case TNT:
				return tnt;
			case MORETIME:
				return bonusTime;
			case MULTX1:
				return multx1;
			case MULTX2:
				return multx2;
			case SPEED:
				return bonusSpeed;
			default:
				return -1;
		}
	}
}