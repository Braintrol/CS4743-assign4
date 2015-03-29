package mvc.controller;
import mvc.*;
import mvc.model.Inventory;
import mvc.model.Item;
import mvc.model.Part;
import mvc.view.ItemListView;
import mvc.view.ItemView;
import mvc.view.PartListView;
import mvc.view.PartView;

public class InventoryController {
	Inventory inv;
	
	InventoryController(Inventory inv){
		this.inv = inv;
		PartListView pLstView = new PartListView(this);
		inv.parts.registerObserver(pLstView);
		ItemListView iLstView = new ItemListView(this);
		inv.items.registerObserver(iLstView);
	}
	
	
	
	//METHODS CALLED BY PARTLISTVIEW:
	public void openPartView_toAddNewPart(){
		PartView pView = new PartView(this);
	} 
	
	public void deletePartFromPartList(Part p) throws IllegalArgumentException{	
		inv.deletePart(p);
	}
	
	public void openParView_forEditing(Part p){
		PartView pv = new PartView(this); /* create a part view for part p */
		p.registerObserver(pv);           /* and register the view as an observer */
	}
	

	//METHODS CLLED BY PARTVIEW:
	public void addNewPartToPartList(PartView pView) throws IllegalArgumentException{
		Part p = inv.addPart(pView.getExternPartNum(), 
							 pView.getPartNum(),
							 pView.getPartName(),
							 pView.getVendor(), 
							 pView.getUnit());
		p.registerObserver(pView);	
	}
	
	public void editExistingPart(Part p, PartView pView) throws IllegalArgumentException{
		inv.editPart(p,
					pView.getExternPartNum(), 
					pView.getPartNum(),
					pView.getPartName(), 
					pView.getVendor(), 
					pView.getUnit());
	};
	
	
	
	//METHODS CALLED BY ITEMLISTVIEW:
		public void openItemtView_toAddNewItem(){
			ItemView iView = new ItemView(this);
			inv.parts.registerObserver(iView);
			
		} 
		
		public void deleteItemFromItemList(Item i) throws IllegalArgumentException{	
			inv.deleteItem(i);
		}
		
		public void openItemView_forEditing(Item i){
			ItemView iView = new ItemView(this); /* create a part view for part p */
			inv.parts.registerObserver(iView);
			i.registerObserver(iView);           /* and register the view as an observer */
			
		}
		
		
	//MEHTODS CALLED BY ITEMVIEW:
		public void addNewItemToItemList(ItemView iView) throws IllegalArgumentException{
			Item i = inv.addItem(iView.getItemLocation(), iView.getQuantity(), iView.getPart().getPartNumber());
			i.registerObserver(iView);
			
		}
	
		public void editExistingItem(Item i, ItemView iView) throws IllegalArgumentException{
			inv.editItem(i, iView.getItemLocation(), iView.getQuantity(), iView.getPart().getPartNumber());
			
		}
}
















































