package mvc.model;

import java.util.ArrayList;

import mvc.model.ProductTemplate.Observer;



public class ProductTemplatePartDetail {
	
	//FIELDS
	private ArrayList<Observer> observers;
	private long template_id;
	private long part_id; 
	private int quantity;
	
	//SETTER METHODS
	public void set_template_id(long template_id){
		this.template_id = template_id;
	}
	public void set_part_id(long part_id){
		this.part_id = part_id;
	}
	public void set_quantity(int quantity){
		this.quantity = quantity;
	}
	
	//GETTER METHODS
	public long get_template_id(){
		return this.template_id;
	}
	public long get_part_id(){
		return this.part_id;
	}
	public int get_quantity(){
		return this.quantity;
	}
	
	//NOTIFICARION METHODS:
		public interface Observer {
			void ItemDeleted();
			void UpdateObserver(ProductTemplatePartDetail productTemplatePartDetail);
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
