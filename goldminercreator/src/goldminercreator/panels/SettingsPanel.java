package goldminercreator.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import goldminercommon.Level;
import goldminercommon.config.SettingsGame;
import goldminercreator.MainFrame;
import goldminercreator.models.ComboboxItemModel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = -882751134000131850L;
	
	private DefaultListModel<Level> levelModel;
	private JComboBox<String> comboboxItems;
	private JToggleButton editModeButton;
	private JTextField numberTextfield, goalTextfield;
	private JList<Level> levelsList;
	private Level level;
	private JButton addLevelButton, removeButton;
	private boolean isModified;
	
	public SettingsPanel(MainFrame _mainFrame) {
		isModified=false;
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(SettingsGame.WIDTH, 100));
		setSize(new Dimension(SettingsGame.WIDTH, 100));
		setMinimumSize(new Dimension(SettingsGame.WIDTH, 100));
		setMaximumSize(new Dimension(SettingsGame.WIDTH, 100));
		comboboxItems=new JComboBox<String>();
		comboboxItems.setMinimumSize(new Dimension(120, 20));
		comboboxItems.setSize(new Dimension(120, 20));
		comboboxItems.setPreferredSize(new Dimension(120, 20));
		comboboxItems.addItemListener(_mainFrame.comboboxListener);
		setLayout(new MigLayout("", "[266.00px][100.00px][133.00][][134.00][][]", "[50.00px][][][]"));
		comboboxItems.setModel(new ComboboxItemModel());
		comboboxItems.setSelectedIndex(0);
		add(comboboxItems, "cell 0 0,aligny top");
		
		add(new JLabel("Number level :"), "flowx,cell 1 0,alignx left");
		add(new JLabel("Select edit level :"), "cell 3 0");
		
		editModeButton = new JToggleButton("Edit mode");
		editModeButton.addActionListener(_mainFrame.actionListener);
		add(editModeButton, "flowx,cell 0 1");
		add(new JLabel("Goal level :"), "cell 1 1,alignx left");
		add(new JLabel("Right click on the item to delete"), "cell 0 3");
		
		numberTextfield = new JTextField();
		numberTextfield.setMaximumSize(new Dimension(80, 20));
		numberTextfield.setMinimumSize(new Dimension(80, 20));
		numberTextfield.setPreferredSize(new Dimension(80, 20));
		numberTextfield.getDocument().addDocumentListener(documentListener);
		((AbstractDocument) numberTextfield.getDocument()).setDocumentFilter(integerFilter);
		numberTextfield.setHorizontalAlignment(SwingConstants.RIGHT);
		add(numberTextfield, "cell 2 0,alignx left");
		numberTextfield.setColumns(10);
		
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 4 0 1 4,grow");
		
		levelModel = new DefaultListModel<Level>();
		levelsList = new JList<Level>(levelModel);
		scrollPane.setViewportView(levelsList);
		levelsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		levelsList.addListSelectionListener(_mainFrame.listListener);
		
		addLevelButton = new JButton("Add new level");
		addLevelButton.addActionListener(_mainFrame.actionListener);
		addLevelButton.setActionCommand("addlevel");
		add(addLevelButton, "cell 6 0");
		
		
		goalTextfield = new JTextField();
		goalTextfield.setMinimumSize(new Dimension(80, 20));
		goalTextfield.setMaximumSize(new Dimension(80, 20));
		goalTextfield.setPreferredSize(new Dimension(80, 20));
		goalTextfield.getDocument().addDocumentListener(documentListener);
		goalTextfield.setHorizontalAlignment(SwingConstants.RIGHT);
		((AbstractDocument) goalTextfield.getDocument()).setDocumentFilter(integerFilter);
		add(goalTextfield, "cell 2 1,alignx left");
		goalTextfield.setColumns(10);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(_mainFrame.actionListener);
		removeButton.setActionCommand("removelevel");
		add(removeButton, "cell 6 1,growx");
		JLabel label = new JLabel("(Drag and drop for move item)");
		add(label, "cell 0 1");
	}
	
	public void removeSelectedLevel() {
		levelModel.removeElementAt(levelsList.getSelectedIndex());
		addLevelButton.setEnabled(true);
		isModified=true;
	}
	
	public void setListLevels(Level[] _levels) {
		for(Level level : _levels) {
			levelModel.addElement(level);
		}
	}
	
	public boolean isModified() {
		return isModified;
	}
	
	public void setLevel(Level _level) {
		this.level=_level;
		if(_level!=null) {
			numberTextfield.setText(String.valueOf(level.getNo()));
			goalTextfield.setText(String.valueOf(level.getGoal()));
			setEnabled(true);
			isModified=false;
		} else {
			numberTextfield.setText("0");
			goalTextfield.setText("0");
			setEnabled(false);
			isModified=false;
		}
		repaint();
	}
	
	public void create() {
		addLevelButton.setEnabled(true);
	}
	
	public void addLevel(Level _level) {
		setLevel(_level);
		setEnabled(true);
		levelModel.addElement(_level);
		levelsList.setSelectedIndex(levelModel.size()-1);
	}
	
	public void open(Level _level) {
		setLevel(_level);
		addLevelButton.setEnabled(true);
		isModified=false;
	}
	
	public void save() {
		isModified=false;
	}
	
	public void close() {
		setLevel(null);
		levelsList.clearSelection();
		levelModel.removeAllElements();
		editModeButton.setSelected(false);
		isModified=false;
	}
	
	@Override
	public void setEnabled(boolean _enabled) {
		super.setEnabled(_enabled);
		for(Component component : getComponents()) {
			component.setEnabled(_enabled);
		}
	}
	
	public void setEditMode(boolean _enabled) {
		comboboxItems.setEnabled(_enabled);
		if(!_enabled) {
			comboboxItems.setSelectedIndex(-1);
		}
	}
	
	private DocumentListener documentListener=new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			if(e.getDocument().getLength()>0 && level!=null) {
				if(e.getDocument()==numberTextfield.getDocument()) {
					level.setNo(Integer.parseInt(numberTextfield.getText()));
				} else if(e.getDocument()==goalTextfield.getDocument()) {
					level.setGoal(Integer.parseInt(goalTextfield.getText()));
				}
				isModified=true;
			}
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			if(e.getDocument().getLength()>0 && level!=null) {
				if(e.getDocument()==numberTextfield.getDocument()) {
					level.setNo(Integer.parseInt(numberTextfield.getText()));
				} else if(e.getDocument()==goalTextfield.getDocument()) {
					level.setGoal(Integer.parseInt(goalTextfield.getText()));
				}
				isModified=true;
			}
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}
	};
	
	private DocumentFilter integerFilter=new DocumentFilter() {
        Pattern regEx = Pattern.compile("\\d+");

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Matcher matcher = regEx.matcher(text);
            if (!matcher.matches()) {
                return;
            }
            super.replace(fb, offset, length, text, attrs);
        }
	};
}
