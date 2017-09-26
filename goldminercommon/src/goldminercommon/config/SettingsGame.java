package goldminercommon.config;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.FontUIResource;

public class SettingsGame {
	
	public static final int WIDTH=1056;
	public static final int HEIGHT=720;
	public static final int DEFAULT_TIME_GAME=60;
	
	public static String USER_DIR=System.getProperty("user.home");
	public static String NAME_DEFAULT_DIR="GolderMinerLevels";
	public static String LEVEL_PATH="/resources/levels.xml";
	public static String LEVEL_FILE_NAME="levels.xml";
	public static String CURRENT_PATH="";
	public static String PATH_SHOP="/resources/images/shop/";
	
	public static final FontUIResource DEFAULT_FONT=new FontUIResource("Arial", Font.PLAIN, 12);
	public static final FontUIResource DEFAULT_FONT_GAME=new FontUIResource("Comic Sans MS", Font.BOLD, 25);
	public static final FontUIResource FONT_LEVEL_FINISH=new FontUIResource("Comic Sans MS", Font.BOLD, 35);
	public static final FontUIResource FONT_GAMEOVER_FINISH=new FontUIResource("Comic Sans MS", Font.BOLD, 50);
	public static final FontUIResource FONT_BONUS=new FontUIResource("Comic Sans MS", Font.BOLD, 16);

	public static final Color BACKGROUND_COLOR=Color.WHITE;
	public static final Color COLOR_ROPE=Color.BLACK;
	public static final Color TEXT_COLOR=new Color(223, 109, 20);
	public static final Color WIN_COLOR=new Color(52, 201, 36);
	public static final Color BORDER_MENU=new Color(231, 187, 78);
	public static final Color LOSE_COLOR=Color.RED;
}
