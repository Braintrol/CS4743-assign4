package mvc.controller;

import java.util.ArrayList;

import mvc.model.Inventory;
import mvc.model.Part;

public class InventoryMain {
	
	
	public static void main(String args[]){
		
		
		Inventory inv = new Inventory();
		//inv.addPart("r45", "4EF6", "bolt", "Hills Bolts", "pieces");
		//Part p = inv.addPart("r45", "945H", "drawer", "Hills Drawers", "pieces");
		//inv.addItem("Facility 1 Warehouse 1", 8, p);
		InventoryController cont = new InventoryController(inv);
		
		
		
				
	}
}
