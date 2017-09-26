
package goldminer.shop;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JPanelHeaderShop extends JPanel {

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private JLabel labelShopTitle;
	private static final long serialVersionUID = -8757621637871015402L;

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	
	public JPanelHeaderShop() {
		this.geometry();
		this.appearance();
	}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry() {
		// JComponent : Instanciation
		labelShopTitle = new JLabel("<html><div style='text-align: center;'>BOUTIQUE</div></html>", SwingConstants.CENTER);

		FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
		setLayout(flowlayout);

		// JComponent : add
		add(labelShopTitle);
	}

	private void appearance() {
		setOpaque(false);
		labelShopTitle.setForeground(new Color(218, 165, 32));
		Font fontLabel = new Font("Monospaced", Font.BOLD, 90);
		labelShopTitle.setFont(fontLabel);
	}
}
