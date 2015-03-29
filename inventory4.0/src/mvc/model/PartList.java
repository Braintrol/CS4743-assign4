package mvc.model;

import java.util.ArrayList;



public class PartList {
	
	public ArrayList<Part> parts;
	private ArrayList<Observer> observers;
	
	public PartList(){
		parts = new ArrayList<Part>();
		observers = new ArrayList<Observer>();
	}
	
	PartList(ArrayList<Part> partList){
		parts = partList;
		observers = new ArrayList<Observer>();
	}
	
	
	public void addPart(Part p){
		parts.add(p);
		updateObservers();
	}
	
	/* to be deleted
	public Part addPart(String extrnPartNum,String partNumber,
	String partName, String partVendor,String partUnit) throws IllegalArgumentException{
		Part p;
		p = new Part(extrnPartNum, partNumber, partName, partVendor, partUnit);

		parts.add(p);
		p.genPart_id();
		updateObservers();
		return p;
	}
	*/
	
	

	/* edit part, no input checking*/
	public void editPart(Part p, String extrnPartNum, String partNumber, String partName,
			             String partVendor, String partUnit) {
		if(p!=null){
			p.setFields(extrnPartNum, partNumber, partName, partVendor, partUnit);
			updateObservers();
		}
	}

	public void deletePart(Part p){
		if(p!=null){
			if(parts.contains(p))
				parts.remove(p);
			updateObservers();
			p.updateDeleted();
		}
	}
	
	public Part getPartByPart_number(String partNumber){
		for(Part p : parts) {
			if(p.getPartNumber().equals(partNumber))
				return p;
		}
		return null;
	}
	
	public boolean isPart_number(String part_number) {
		for(Part p : parts) {
			if(p.getPartNumber().equals(part_number))
				return true;
		}
		return false;
	}
	
	public Part getPartByIndex(int index) {
		if(index < parts.size())
			return parts.get(index);
		else
			return null;
	}
	
	public boolean contains(Part p){
		return parts.contains(p);
	}
	
	
	//OBSERVER METHODS:
	
	public interface Observer {
		abstract public void updateObserver(PartList parts);
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
