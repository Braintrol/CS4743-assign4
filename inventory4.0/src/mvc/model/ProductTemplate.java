package mvc.model;

import java.util.ArrayList;

import mvc.model.Item.Observer;

public class ProductTemplate {
	
	private ArrayList<Observer> observers;
	private long product_id;
	private String product_number;
	private String product_description;
	
	public ProductTemplate(){
		this.observers = new ArrayList<Observer>();
	}
	
	//GETTERS
	public long getProduct_id() {
		return product_id;
	}
	public String getProduct_number() {
		return product_number;
	}
	public String getProduct_description() {
		return product_description;
	}
	//SETTERS
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public void setProduct_number(String product_number) {
		this.product_number = product_number;
	}
	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}
	
	//NOTIFICARION METHODS:
	public interface Observer {
		void ItemDeleted();
		void UpdateObserver(ProductTemplate productTemplate);
	}

	
	public void registerObserver(Observer o) {
		observers.add(o);
		updateObservers();
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
