
package goldminer.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;

import goldminer.GameFrame;
import goldminer.PanelSlider;
import goldminer.items.GameInfo;
import goldminer.panels.AbstractPanel;

public class JPanelShop extends AbstractPanel {


	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	private static final long serialVersionUID = -1605669827462689042L;
	private Box boxv;
	private BufferedImage backgroundBoutique;
	private JPanelAssortment jpanelassortiement;
	private JPanelHeaderShop jpaneltitreboutique;
	private JButton buttonNextLvl;

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	
	public JPanelShop(PanelSlider<GameFrame> _panelSlider, GameInfo _gameInfo, String _name) {
		super(_panelSlider, _gameInfo, _name);
		this.geometry();
		this.control();
		this.addFocusListener(this);
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
			e.printStackTrace();
		}
		g2d.setTransform(transformInitial);
	}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/
	private void draw(Graphics2D g2d) throws IOException {
		int px = 0;
		int py = 0;
		backgroundBoutique = ImageIO.read(this.getClass().getResource("/resources/images/backgrounds/boutique_background.png").openStream());
		g2d.drawImage(backgroundBoutique, px, py, getWidth(), getHeight(), this);

		Font fontDrawString2 = new Font("Monospaced", Font.BOLD, 22);
		g2d.setFont(fontDrawString2);
		g2d.setColor(new Color(124, 252, 0));
		g2d.drawString("Porte-monaie : " + gameInfo.getAmountMoney() + " $", 750, 300);

		Font fontLabelInfo = new Font("Monospaced", Font.BOLD, 18);
		g2d.setFont(fontLabelInfo);
		g2d.setColor(new Color(255, 0, 0));
		g2d.drawString(jpanelassortiement.getMessage(), 50, 340);

	}

	private void geometry() {

		boxv = Box.createVerticalBox();
		jpanelassortiement = new JPanelAssortment(this, gameInfo);
		jpaneltitreboutique = new JPanelHeaderShop();

		buttonNextLvl = new JButton("Next level");
		buttonNextLvl.setForeground(new Color(127, 62, 6));
		Font fontLabel = new Font("Comic Sans MS", Font.BOLD, 22);
		buttonNextLvl.setFont(fontLabel);
		buttonNextLvl.setBackground(new Color(218, 165, 32));
		buttonNextLvl.setBounds(50, 245, 145, 50);

		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		add(buttonNextLvl);
		boxv.add(Box.createVerticalGlue());
		boxv.add(jpaneltitreboutique);
		boxv.add(Box.createVerticalGlue());
		boxv.add(Box.createVerticalGlue());
		boxv.add(Box.createVerticalGlue());
		boxv.add(jpanelassortiement);

		add(boxv, BorderLayout.CENTER);
	}

	private void control() {
		buttonNextLvl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelSlider.slideLeft("game");
			}
		});
	}
	
	@Override
	public void updateGameInfo(GameInfo _gameInfo) {
		super.updateGameInfo(_gameInfo);
		this.jpanelassortiement.updateGameInfo(_gameInfo);
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		jpanelassortiement.initShop(gameInfo.getCountLevel());
	}

	@Override
	public void focusLost(FocusEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
