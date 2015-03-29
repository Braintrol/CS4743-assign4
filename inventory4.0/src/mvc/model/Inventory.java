package mvc.model;


import java.sql.SQLException;

import jdbc.*;

public class Inventory {

	public PartList parts;
	public ItemList items;
	private partsTDG parts_tdg;
	private itemsTDG items_tdg;
	
	
	public Inventory(){
		
		/* get parts stored in database*/
		parts_tdg = new partsTDG();
		parts = parts_tdg.getPartList();

		
		/* get items stored in database*/
		items_tdg = new itemsTDG();
		items = items_tdg.getItemList(parts);

	}
	

///PARTS METHODS: ADD, DELETE, EDIT, REGISTER_OBSERVER, VALIDATION
	
	//ADD PART:
	public Part addPart(String part_external_number, String part_number,
	                    String part_name, String part_vendor, String part_unit) throws IllegalArgumentException{
		
		/* throws illegalArgumentEsception*/
		validateInput(null, part_external_number, part_number, part_name, part_vendor, part_unit);
		
		/* create part */
		Part p = new Part(part_external_number, part_number, part_name, part_vendor, part_unit);
		
		/* try to add part to database and inventory*/
		Long part_id = parts_tdg.insertNewtPart(p);
		if(part_id != null){
			p.setPart_id(part_id);
			parts.addPart(p);
		}

		return p;
	}


	//EDIT PART:
	public void editPart(Part p, String part_external_number, String part_number, 
	String part_name, String part_vendor, String part_unit) throws IllegalArgumentException{
		
		/* throws illegalArgumentEsception*/
		validateInput(p, part_external_number, part_number, part_name, part_vendor, part_unit);
		
		/* add edit existing part */
		parts.editPart(p, part_external_number, part_number, part_name, part_vendor, part_unit);
		parts_tdg.updatePart(p.getID(), p.getExtrnPartNum(), p.getPartNumber(), p.getPartName() , p.getVendor(), p.getPartUnit());
		items.updateObservers();
	}
	
	
	//delete part from parts list
	public void deletePart(Part p) {
		if(items.contains(p))
			throw new IllegalArgumentException("Can't be deleted because part is associated with invenotry item");
		else{
			parts_tdg.deletePart(p.getID());
			parts.deletePart(p);
			items.updateObservers();
		}
	}
	
	//register PartList observer
	public void registerPartListObserver(PartList.Observer o){
		parts.registerObserver(o);
	} 
	
	//get part by index
	public Part getPartAtIndex(int index){
		return parts.getPartByIndex(index);
	}
	
	
	/* VALIDATION */
	private void validateInput(Part p, String part_external_number, String part_number, 
	String part_name, String part_vendor, String part_unit) throws IllegalArgumentException{
	
		if(p != null && parts.isPart_number(part_number) && !part_number.equals(p.getPartNumber()) )
			throw new IllegalArgumentException("part # already exists!");
		if(p == null && parts.isPart_number(part_number))
			throw new IllegalArgumentException("Part # already exists!");
		if(part_number == null || part_number.length() < 1)
			throw new IllegalArgumentException("Part # cannot be blank");
		if(part_name == null || part_name.length() < 1)
			throw new IllegalArgumentException("Part Name cannot be blank");
		if(part_unit.equalsIgnoreCase("Unknown"))
			throw new IllegalArgumentException("Part Unit cannot be unknown");
	}

	
	
	
	
	
	
	
///ITEM METHODS: ADD, DELETE, EDIT, REGISTER
	
	
	//register ItemList observers
		public void registerItemListObserver(ItemList.Observer o){
			items.registerObserver(o);
		} 
		
	//ADD NEW ITEM
	public Item addItem(String ItemLocation, int ItemQuantity, Part p){
		
		/* INPUT VALIDATION */
		if(ItemQuantity < 1)
			throw new IllegalArgumentException("Item quantity cannot be zero or less");
		if(ItemLocation.equalsIgnoreCase("Unknown"))
			throw new IllegalArgumentException("Item location cannot be Unknown");
		if(p==null)
			throw new IllegalArgumentException("part number doesnt exist!");
		for(Item it:items.items){
			if(it.getItemPartNumber().equals(p.getPartNumber()) 
			&& it.getItemLocation().equals(ItemLocation))
				throw new IllegalArgumentException("Item part number and location already exists!");
		}
	
		/* ADD AND RETURN */
		Long item_id = items_tdg.insertNewtItem(ItemLocation, ItemQuantity, p.getID());
		if(item_id != null){
			Item it = new Item(item_id,ItemLocation, ItemQuantity, p);	
			items.addItem(it);	
			return it;
		}
		return null;
	}
	
	
	//ADD NEW ITEM
	public Item addItem(String ItemLocation, int ItemQuantity, String partNumber){
		
		/* INPUT VALIDATION */
		if(ItemQuantity < 1)
			throw new IllegalArgumentException("Item quantity cannot be zero or less");
		if(ItemLocation.equalsIgnoreCase("Unknown"))
			throw new IllegalArgumentException("Item location cannot be Unknown");
		Part p = parts.getPartByPart_number(partNumber);
		if(p==null)
			throw new IllegalArgumentException("part number doesnt exist!");
		for(Item it:items.items){
			if(it.getItemPartNumber().equals(partNumber) 
			&& it.getItemLocation().equals(ItemLocation))
				throw new IllegalArgumentException("Item part number and location already exists!");
		}
		
		/* ADD AND RETURN */
		Long item_id = items_tdg.insertNewtItem(ItemLocation, ItemQuantity, p.getID());
		if(item_id != null){
			Item it = new Item(item_id,ItemLocation, ItemQuantity, p);	
			items.addItem(it);	
			return it;
		}
		return null;
	}

	
	
	//EDIT EXISTING ITEM 
	public void editItem(Item existingItem,String newItemLocation, int newItemQuantity, String newPartNumber){
		
		//VALIDATION
		/* can't edit item to an existing part_number and item_location combination*/
		for(Item it:items.items){
			if(it.getItemPartNumber().equals(newPartNumber) 
			&& it.getItemLocation().equals(newItemLocation)
			&& it.getID() != existingItem.getID()){
				throw new IllegalArgumentException("Item's part number and location already exist");
			}
		}
		
		/* item location cannot be unknown*/
		if(newItemLocation.equalsIgnoreCase("Unknown"))
			throw new IllegalArgumentException("Item location cannot be Unknown");
		
		/* new quantity cannot be less than 0*/
		if(newItemQuantity < 0){
			throw new IllegalArgumentException("Editing Item Error:\nQuantity must be greater or equal to zero");
		}
		
		//EDITING ITEM
		Part p = parts.getPartByPart_number(newPartNumber);
		items_tdg.updateItem(existingItem.getID(), newItemLocation, newItemQuantity, p.getID());
		items.editItem(existingItem, newItemLocation, newItemQuantity, p);
	
	}
	
	//DELETE ITEM
	public void deleteItem(Item i){
		
		//VALIDATION
		/* item quantity must be larger than zero to be deleted */
		if(i.getItemQuantity() > 0)
			throw new IllegalArgumentException("Can't delete item unless quantity is 0");
		
		//DELTING ITEM
		items_tdg.deleteItem(i.getID());
		items.deleteItem(i);
	}
	
	//get item by index
	public Item getItemAtIndex(int index){
		return items.getItemAtIndex(index);
	}
	
	
}




















