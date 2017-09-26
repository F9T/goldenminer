package goldminercreator.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import goldminercommon.Level;
import goldminercommon.config.SettingsGame;
import goldminercommon.enums.OreWeightEnum;
import goldminercommon.items.Bomb;
import goldminercommon.items.Diamond;
import goldminercommon.items.DropItem;
import goldminercommon.items.Gold;
import goldminercommon.items.Stone;
import goldminercommon.items.SurpriseBag;

public class ItemPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -1173852572111612157L;
	
	private BufferedImage background;
	private DropItem selectedItem, saveLastSelectedItem, draggedItem, deleteItem;
	private Level level;
	private JPopupMenu contextMenu;
	private boolean mouseEntered, editMode, dragging, isModified;
	private int draggedX, draggedY;
	
	public ItemPanel() {
		this.initializeContextMenu();
		this.editMode=false;
		this.isModified=false;
		this.dragging=false;
		this.mouseEntered=false;
		try {
			this.background=ImageIO.read(getClass().getResource("/res/images/background_game.png").openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPreferredSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
		this.setMinimumSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
		this.setMaximumSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
		this.setSize(new Dimension(SettingsGame.WIDTH, SettingsGame.HEIGHT));
	}
	
	private void initializeContextMenu() {
		this.contextMenu=new JPopupMenu();
		JMenuItem deleteMenuItem=new JMenuItem("Delete");
		deleteMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(deleteItem!=null) {
					level.removeItem(deleteItem);
					isModified=true;
				}
				repaint();
			}
		});
		contextMenu.add(deleteMenuItem);
	}
	
	public int getAddHeight() {
		return SettingsGame.HEIGHT-background.getHeight();
	}
	
	public void setItem(String _name) {
		switch(_name.toLowerCase()) {
			case "little gold":
				selectedItem=new Gold(OreWeightEnum.LITTLE);
			break;
			case "medium gold":
				selectedItem=new Gold(OreWeightEnum.MEDIUM);
			break;
			case "big gold":
				selectedItem=new Gold(OreWeightEnum.BIG);
			break;
			case "little stone":
				selectedItem=new Stone(OreWeightEnum.LITTLE);
			break;
			case "big stone":
				selectedItem=new Stone(OreWeightEnum.BIG);
			break;
			case "diamond":
				selectedItem=new Diamond();
			break;
			case "surprise bag":
				selectedItem=new SurpriseBag();
			break;
			case "bomb":
				selectedItem=new Bomb();
			break;
			default:
				selectedItem=null;
				saveLastSelectedItem=null;
			break;
		}
		if(mouseEntered && selectedItem!=null) {
			selectedItem.setX((int)this.getMousePosition().getX()-(selectedItem.getWidth()/2));
			selectedItem.setY((int)this.getMousePosition().getY()-(selectedItem.getHeight()/2));
			repaint();
		}
	}
	
	public boolean isModiied() {
		return isModified;
	}
	
	public void setEditMode(boolean _enabled) {
		editMode=_enabled;
		selectedItem=null;
		saveLastSelectedItem=null;
	}
	
	public void setGoal(int _goal) {
		level.setGoal(_goal);
	}
	
	public void setNumber(int _number) {
		level.setNo(_number);
	}
	
	public void setLevel(Level _level) {
		this.level=_level;
		repaint();
	}
	
	public void open(Level _level) {
		setLevel(_level);
		isModified=false;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setEditMode(false);
	}
	
	public void save() {
		isModified=false;
	}
	
	public void addLevel(Level _level) {
		setLevel(_level);
		isModified=true;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setEditMode(false);
	}
	
	public void close() {
		setLevel(null);
		this.removeMouseListener(this);
		this.removeMouseMotionListener(this);
		this.setEditMode(false);
		selectedItem=null;
		saveLastSelectedItem=null;
		isModified=false;
	}
	
	private boolean checkPosition(DropItem _item) {
		final int x=_item.getX();
		final int y=_item.getY();
		final int width=_item.getWidth();
		final int height=_item.getHeight();
		if(y<getAddHeight() || (y+height)>this.getHeight() || x<0 || (x+width)>=this.getWidth()) {
			JOptionPane.showMessageDialog(this, "Item is out of area!");
			return true;
		}
		for(DropItem item : level.getItems()) {
			if(item.intersect(new Rectangle(_item.getX(), _item.getY(), _item.getWidth(), _item.getHeight())) && item!=_item) {
				JOptionPane.showMessageDialog(this, "Another item is already there!");
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(background, 0, getAddHeight(), background.getWidth(), background.getHeight(), this);
		if(level!=null) level.paint(g);
		if(selectedItem!=null) selectedItem.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseEntered=true;
		if(selectedItem==null && saveLastSelectedItem!=null) {
			selectedItem=saveLastSelectedItem;
			saveLastSelectedItem=null;
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseEntered=false;
		saveLastSelectedItem=selectedItem;
		selectedItem=null;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1) {
			if(selectedItem!=null) {
				if(checkPosition(selectedItem)) {
					return;
				}
				level.addItem(selectedItem);
				isModified=true;
				setItem(selectedItem.toString());
				saveLastSelectedItem=null;
				repaint();
			}
		} else if(e.getButton()==MouseEvent.BUTTON3) {
			for(DropItem item : level.getItems()) {
				if(item.intersect(new Rectangle(e.getX(), e.getY(), 1, 1))) {
					deleteItem=item;
					contextMenu.show(this, e.getX(), e.getY());
					break;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(editMode && dragging) {
			if(checkPosition(draggedItem)) {
				draggedItem.setX(draggedX);
				draggedItem.setY(draggedY);
			} else {
				isModified=true;
			}
			draggedX=0;
			draggedY=0;
			dragging=false;
			draggedItem=null;
			repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		final int x=e.getX();
		final int y=e.getY();
		if(editMode) {
			if(!dragging) {
				for(DropItem item : level.getItems()) {
					if(item.intersect(new Rectangle(x, y, 1, 1))) {
						draggedX=item.getX();
						draggedY=item.getY();
						dragging=true;
						draggedItem=item;
						break;
					}
				}
			} else {
				draggedItem.setX(e.getX());
				draggedItem.setY(e.getY());
				repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(selectedItem!=null) {
			selectedItem.setX(e.getX()-(selectedItem.getWidth()/2));
			selectedItem.setY(e.getY()-(selectedItem.getHeight()/2));
			repaint();
		}
	}
}
