package mvc.model;


import java.util.ArrayList;

public class Item {
	
	//FIELDS:
	private String ItemLocation;
	private int item_quantity;
	private Part part;
	private long item_id;
	private static long count;
	
	private ArrayList<Observer> observers;
	
	
	
    //CONSTRUCTOR:
	public Item(long item_id, String ItemLocation, int ItemQuantity, Part Part) {
			observers = new ArrayList<Observer>();
			setFields(ItemLocation,ItemQuantity,Part);	
			this.item_id = item_id;
	}
	
    
	public Item(String ItemLocation, int ItemQuantity, Part Part) {
		observers = new ArrayList<Observer>();	
		setFields(ItemLocation,ItemQuantity,Part);	
	}
    
	
	//GETTER METHODS:
	public Part getItemPart(){
		return part;
	}
	public String getItemLocation(){
		return ItemLocation;
	}
	public int getItemQuantity(){
		return item_quantity;
	}
	public long getID(){
		return item_id;
	}
	public String getItemPartName(){
		return part.getPartName();
	}
	public String getItemPartNumber(){
		return part.getPartNumber();
	}
	
	//SETTER METHODS:
	public void setFields(String newItemLocation, int newItemQuantity, Part newPart){
		this.ItemLocation = newItemLocation;
		this.item_quantity = newItemQuantity;
		this.part = newPart;
		updateObservers();
	}
	
	public void generateID(){
		this.item_id = ++count;
	}
	
	//NOTIFICARION METHODS:
	public interface Observer {
		void ItemDeleted();
		void UpdateObserver(Item i);
	}

	
	public void registerObserver(Observer o) {
		observers.add(o);
		updateObservers();
	}
	
	public void updateDeleted() {
		for(Observer o : observers) {
			try {
				o.ItemDeleted();
			} catch(Exception e) {
				//ignore
			}
		}
	}
	
	private void updateObservers() {
		for(Observer o : observers) {
			try {
				o.UpdateObserver(this);
			} catch(Exception e) {
				//ignore
			}
		}
	}
	
	
}