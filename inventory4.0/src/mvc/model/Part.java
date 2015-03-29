package mvc.model;

import java.util.UUID;
import java.util.ArrayList;

public class Part {
	
	
	
	//FIELDS:
	private String extrnPartNum;
	private String partNumber;
	private String partName;
	private String partVendor;
	private String partUnit;
	private Long part_id ;
	private ArrayList<Observer> observers = new ArrayList<Observer>();	
	

    //CONSTRUCTOR:
	public Part(Long part_id,String extrnPartNum,String partNumber, String partName, String partVendor,String partUnit){
		this.part_id = part_id;
		setFields(extrnPartNum,partNumber, partName, partVendor, partUnit);
	}
	public Part(String extrnPartNum,String partNumber, String partName, String partVendor,String partUnit) {
		setFields(extrnPartNum,partNumber, partName, partVendor, partUnit);
	}
    
	
	//GETTER METHODS:
	public String getPartNumber(){
		return this.partNumber;
	}
	public String getPartName(){
		return this.partName;
	}
	public String getVendor(){
		return this.partVendor;
	}
    public String getPartUnit(){
    	return this.partUnit;
    }
    public String getExtrnPartNum(){
    	return this.extrnPartNum;
    }
	public long getID(){
		
		return this.part_id;
		
	}
	
	//SETTER METHODS:
	public void setFields(String extrnPartNum, String partNumber, String partName, String partVendor, String partUnit){
		
		this.extrnPartNum = extrnPartNum;
		this.partNumber = partNumber;
		this.partName = partName;
		this.partVendor = partVendor;
		this.partUnit = partUnit;
		
		updateObservers();
	}
	
	public void setPart_id(long id){
		this.part_id = id;
		updateObservers();
	}
	
	//HELPER METHODS:
	public String toString(){
		
		return null;
	
	}
	
	//NOTIFICARION METHODS:
	public void registerObserver(Observer o) {
		observers.add(o);
		updateObservers();
	}
	
	public void updateDeleted() {
		for(Observer o : observers) {
			try {
				o.PartDeleted();
			} catch(Exception e) {
				//ignore
			}
		}
	}
	
	private void updateObservers() {
	
		for(Observer o : observers) {
			try {
				o.updateObserver(this);
			} catch(Exception e) {
				//ignore
			}
		}
	}
	
	public interface Observer {
		abstract public void updateObserver(Part part);
		abstract public void PartDeleted();
	}

}
	
	

