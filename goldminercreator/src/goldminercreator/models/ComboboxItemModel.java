package goldminercreator.models;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ComboboxItemModel implements ComboBoxModel<String> {

	String[] items= {"", "Little gold", "Medium gold", "Big gold", "Little stone", "Big stone", "Diamond", "Surprise bag", "Bomb"};
	String selectedItem=null;
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
	}

	@Override
	public String getElementAt(int _index) {
		return items[_index];
	}

	@Override
	public int getSize() {
		return items.length;
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public void setSelectedItem(Object _item) {
		selectedItem=(String)_item;
	}
}
