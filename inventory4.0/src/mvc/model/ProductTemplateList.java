package mvc.model;

import java.util.ArrayList;
import java.util.Iterator;

import mvc.model.Item.Observer;

public class ProductTemplateList implements Iterable<ProductTemplate> {
	
	//fields:
	private ArrayList<Observer> observers;
	private ArrayList<ProductTemplate> list;

	
	//setters:
	public boolean add(ProductTemplate t){
		boolean b = list.add(t);
		updateObservers();
		return b;
	}
	
	public boolean remove(ProductTemplate t){
		boolean b = list.remove(t);
		updateObservers();
		return b;
	}
	

	//observer interface and update methods
		public interface Observer {
			void UpdateObserver(ProductTemplateList list);
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
		
		@Override
		public Iterator<ProductTemplate> iterator() {
			// TODO Auto-generated method stub
			return list.iterator();
		}
		
}
