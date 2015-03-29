package mvc.model;

import java.util.ArrayList;

public class ItemList {

	//FIELDS:
	public ArrayList<Item> items;
	ArrayList<Observer> observers;
	
	//CONTRUCTOR
	public ItemList(){
		items = new ArrayList<Item>();
		observers = new ArrayList<Observer>();
	}
	
	//METHODS: ADD, EDIT, DELETE
    public void addItem(Item it){
    	items.add(it);
    	updateObservers();
    }
    
	public Item addItem(String itemLocation, int itemQuantity, Part part){
		Item i = new Item(itemLocation, itemQuantity, part);
		items.add(i);
		updateObservers();
		return i;
	}
	
	public void deleteItem(Item i){
		items.remove(i);
		i.updateDeleted();
		updateObservers();
	}
	
	public void editItem(Item itemToEdit,String newItemLocation, int newItemQuantity, Part newPart){
		if(itemToEdit!=null){
			itemToEdit.setFields(newItemLocation, newItemQuantity, newPart);
			updateObservers();
		}
	}
	
	////HELPER METHODS
	
	public Item getItemByID(long id){
		int index = 0;
		for(Item it: items){
			if(it.getID() == id){
				return items.get(index);
			}
			index++;	
		}
		return null;
	}
	
	//getItem
	public Item getItemAtIndex(int index){
		return items.get(index);
	}

	//check whether an part exists within any item in Items
	public boolean contains(Part p){
		for(Item i:items){
			if(i.getItemPart().getID() == p.getID())
				return true;
		}
		return false;
	}
	
	//check whether a particular item exists in Items
	public boolean contains(Item i){
		for(Item it: items){
			if(it.getID() == i.getID()){
				return true;
			}
		}
		return false;
	}
		

	
	//OBSERVER METHODS:
	public interface Observer {
		abstract public void updateObserver(ItemList inv);
	}

	public void registerObserver(Observer o) {
		observers.add(o);
		updateObservers();
	}
		public void updateObservers() {
			for(Observer o : observers) {
				try {
					o.updateObserver(this);
				} catch(Exception e) {
					//ignore for now
				}
			}
		}
	
}
