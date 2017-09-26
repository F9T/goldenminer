package goldminercreator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.xml.bind.JAXBException;

import goldminercommon.Level;
import goldminercommon.LevelInfo;
import goldminercommon.XmlLevelParser;
import goldminercommon.config.SettingsGame;
import goldminercreator.panels.ItemPanel;
import goldminercreator.panels.SettingsPanel;
import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = -5239672539745063627L;

	private JMenuItem openMenuItem, saveMenuItem, saveAsMenuItem, closeMenuItem, createMenuItem, helpMenuItem;
	private ItemPanel itemPanel;
	private SettingsPanel settingsPanel;
	private LevelInfo levelInfo;
	private Level level;
	private boolean isOpened, isModified;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		isOpened=false;
		isModified=false;
		setResizable(false);
		setBackground(SettingsGame.BACKGROUND_COLOR);
		setFont(SettingsGame.DEFAULT_FONT);
		setUIFont(SettingsGame.DEFAULT_FONT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("GoldMiner Level Creator");
		pack();
		initialize();
	}
	
	private void initialize() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu levelMenu = new JMenu("Level");
		menuBar.add(levelMenu);
		
		createMenuItem=createMenuItem("New", "create", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		levelMenu.add(createMenuItem);
		openMenuItem=createMenuItem("Open", "open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		levelMenu.add(openMenuItem);
		saveMenuItem=createMenuItem("Save", "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		levelMenu.add(saveMenuItem);
		saveAsMenuItem=createMenuItem("Save as...", "saveas", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		levelMenu.add(saveAsMenuItem);
		closeMenuItem=createMenuItem("Close", "close", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		levelMenu.add(closeMenuItem);
		
		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);
		
		helpMenuItem=createMenuItem("Help", "help", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		menuHelp.add(helpMenuItem);
		
		getContentPane().setLayout(new MigLayout("insets 0", "[1056px]", "[100px][720px]"));
		itemPanel = new ItemPanel();
		getContentPane().add(itemPanel, "cell 0 1,alignx left,growy");
		
		settingsPanel = new SettingsPanel(this);
		settingsPanel.setEnabled(false);
		getContentPane().add(settingsPanel, "cell 0 0,grow");
		setMenuEnabled(false);

		Insets insetsFrame=this.getInsets();
		setPreferredSize(new Dimension(SettingsGame.WIDTH+insetsFrame.left+insetsFrame.right, 25+SettingsGame.HEIGHT+menuBar.getHeight()+settingsPanel.getHeight()+insetsFrame.top+insetsFrame.bottom));
		setSize(new Dimension(SettingsGame.WIDTH+insetsFrame.left+insetsFrame.right, 25+SettingsGame.HEIGHT+menuBar.getHeight()+settingsPanel.getHeight()+insetsFrame.top+insetsFrame.bottom));
		setLocationRelativeTo(null);
		this.addWindowListener(this);
	}
	
	private JMenuItem createMenuItem(String _menuName, String _actionCommand, KeyStroke _keyStroke) {
		JMenuItem menuItem=new JMenuItem(_menuName);
		menuItem.addActionListener(this);
		menuItem.setActionCommand(_actionCommand);
		menuItem.setAccelerator(_keyStroke);
		return menuItem;
	}
	
	private void setUIFont(FontUIResource f) {   
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while(keys.hasMoreElements()) {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if(value instanceof FontUIResource) UIManager.put(key, f);
	    }
	}
	
	private void setMenuEnabled(boolean _enabled) {
		saveMenuItem.setEnabled(_enabled);
		saveAsMenuItem.setEnabled(_enabled);
		closeMenuItem.setEnabled(_enabled);
	}
	
	private boolean isModified() {
		return isModified || itemPanel.isModiied() || settingsPanel.isModified();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem) {
			JMenuItem menuItem=(JMenuItem)e.getSource();
			String actionName=menuItem.getActionCommand().toLowerCase();
			switch(actionName) {
				case "create":
					create();
				break;
				case "open":
					open();
				break;
				case "save":
					save();
				break;
				case "saveas":
					saveAs();
				break;
				case "close":
					close();
				break;
			}
		}
	}
	
	private void create() {
		if(isOpened && !isModified()) {
			close();
		} else if(isOpened && isModified()) {
			int result=askConfirm("Save", "Save file "+SettingsGame.CURRENT_PATH+" ?");
			if(result==JOptionPane.OK_OPTION) {
				save();
			} else if(result==JOptionPane.CANCEL_OPTION) {
				return;
			}
			isOpened=false;
			close();
		}
		levelInfo=new LevelInfo();
		settingsPanel.create();
		setMenuEnabled(true);
		isOpened=true;
		isModified=true;
	}
	
	private void open() {
		if(isOpened) {
			close();
		}
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setCurrentDirectory(new File(SettingsGame.USER_DIR));
		FileNameExtensionFilter filter=new FileNameExtensionFilter("XML Files", "xml");
		fileChooser.setFileFilter(filter);
		int returnVal=fileChooser.showOpenDialog(this);
		if(returnVal==JFileChooser.APPROVE_OPTION) {
			File file=fileChooser.getSelectedFile();
			try {
				levelInfo=XmlLevelParser.deserialize(file);
				levelInfo.initLevels();
				SettingsGame.CURRENT_PATH=file.getAbsolutePath();
				settingsPanel.setListLevels(levelInfo.getArrayLevel());
				itemPanel.open(level);
				settingsPanel.open(level);
				setMenuEnabled(true);
				isOpened=true;
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(this, "Erreur fichier des niveaux", "Erreur", JOptionPane.ERROR_MESSAGE);
				close();
			}
		}
	}
	
	private void save() {
		try {
			if(SettingsGame.CURRENT_PATH.isEmpty()) {
				saveAs();
			}
			XmlLevelParser.serialize(levelInfo, SettingsGame.CURRENT_PATH);
			isModified=false;
			settingsPanel.save();
			itemPanel.save();
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void saveAs() {
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setCurrentDirectory(new File(SettingsGame.USER_DIR));
		FileNameExtensionFilter filter=new FileNameExtensionFilter("XML Files", "xml");
		fileChooser.setFileFilter(filter);
		int returnVal=fileChooser.showOpenDialog(this);
		if(returnVal==JFileChooser.APPROVE_OPTION) {
			File fileSave=fileChooser.getSelectedFile();
			if(fileSave.getParentFile().exists() && fileSave.getParentFile().isDirectory()) {
				SettingsGame.CURRENT_PATH=fileSave.getAbsolutePath();
			}
		}
	}
	
	private void close() {
		if(isOpened && isModified()) {
			int result=askConfirm("Save", "Save file "+SettingsGame.CURRENT_PATH+" ?");
			if(result==JOptionPane.OK_OPTION) {
				save();
			} else if(result==JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		isModified=false;
		isOpened=false;
		setMenuEnabled(false);
		level=null;
		levelInfo=null;
		itemPanel.setLevel(null);
		settingsPanel.setLevel(null);
		itemPanel.close();
		settingsPanel.close();
	}
	
	private int askConfirm(String _title, String _message) {
		return JOptionPane.showConfirmDialog(this, _message, _title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	
	public ActionListener actionListener=new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JToggleButton) {
				JToggleButton button=(JToggleButton)e.getSource();
				boolean editMode=button.isSelected();
				settingsPanel.setEditMode(!editMode);
				itemPanel.setEditMode(editMode);
			} else if(e.getSource() instanceof JButton) {
				JButton button=(JButton)e.getSource();
				if(button.getActionCommand().equalsIgnoreCase("addlevel")) {
					if(levelInfo!=null) {
						level=new Level(levelInfo.getLevelCount()+1, 2000);
						levelInfo.addLevel(level);
						itemPanel.addLevel(level);
						settingsPanel.addLevel(level);
						setMenuEnabled(true);
						isOpened=true;
					}
				} else if(button.getActionCommand().equalsIgnoreCase("removelevel")) {
					levelInfo.removeLevel(level);
					settingsPanel.setLevel(null);
					itemPanel.setLevel(null);
					settingsPanel.removeSelectedLevel();
				}
			}
		}
	};
	
	public ListSelectionListener listListener=new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(e.getSource() instanceof JList) {
				@SuppressWarnings("unchecked")
				JList<Level> list=(JList<Level>) e.getSource();
				if(!list.getValueIsAdjusting()) {
					level=list.getSelectedValue();
					itemPanel.setLevel(level);
					settingsPanel.setLevel(level);
				}
			}
		}
	};

	public ItemListener comboboxListener=new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				itemPanel.setItem(e.getItem().toString());
			}
		}
	};

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(isOpened && isModified()) {
			int result=askConfirm("Save", "Save file "+SettingsGame.CURRENT_PATH+" ?");
			if(result==JOptionPane.OK_OPTION) {
				save();
			} else if(result==JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		this.dispose();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
