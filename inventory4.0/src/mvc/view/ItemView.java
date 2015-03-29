package mvc.view;
import mvc.model.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mvc.controller.InventoryController;
import mvc.model.Item;
import mvc.model.Part;
import mvc.model.PartList;

public class ItemView extends JFrame implements Item.Observer, PartList.Observer{

	private JPanel BottomPanel, TopPanel;
    private JTextField Quantity;
	private JLabel ItemID;
	private JComboBox itemLocationComboBox, partIDListComboBox;
	private String[] locations = {"Unknown", "Facility 1 Warehouse 1", "Facility 1 Warehouse 2", "Facility 2"};
	private JButton saveButton;
	private PartList partList;
	private Item item;
	private InventoryController contr;
	//public ItemView(Item item) {
		
		
	public ItemView(InventoryController contr) { 
	
		this.contr = contr;
		
		//BOTTOM PANEL (buttons):
		BottomPanel = new JPanel();
		BottomPanel.setLayout(new GridLayout());
		saveButton = new JButton("Save");
		BottomPanel.add(saveButton);
		this.add(BottomPanel, BorderLayout.SOUTH);
		
		//TOP PANEL (test fields, ):
		JPanel TopPanel = new JPanel();
		TopPanel.setLayout(new GridLayout(6,2));
		
		TopPanel.add(new JLabel("ItemID"));
		ItemID = new JLabel();
		TopPanel.add(ItemID);
		
		TopPanel.add(new JLabel("PartID"));
		partIDListComboBox = new JComboBox();
		TopPanel.add(partIDListComboBox);
		
        
		TopPanel.add(new JLabel("Quantity"));
		Quantity = new JTextField();
		TopPanel.add(Quantity);
		
		TopPanel.add(new JLabel("Location"));
		itemLocationComboBox = new JComboBox();
		for(String s:locations)
			itemLocationComboBox.addItem(s);
		TopPanel.add(itemLocationComboBox);
		
		//FRAME:
		this.setTitle("Adding New Item");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(400, 200);
		this.setLocation(500, 100);
		this.add(BottomPanel, BorderLayout.SOUTH);
		this.add(TopPanel, BorderLayout.CENTER);
		this.setVisible(true);
		this.setTitle("Add Item");
	
		
		saveButton.addActionListener(new  ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(item == null){ /* if this view is NOT related with and item, try adding item */
					try{
						ItemView.this.contr.addNewItemToItemList(ItemView.this);
						
					}catch(IllegalArgumentException ex){
						showError(ex.getMessage());
					}
				}else if (item != null){ /* if this view IS related with and item, try editing item */
					try{
						ItemView.this.contr.editExistingItem(item, ItemView.this);
					}catch(IllegalArgumentException ex){
						showError(ex.getMessage());
					}
				}
			}		
		});
		
	}
	
	
	
	//ERROR MESSAGE:
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
	}
	

	//GET VALUES:
	public String getId(){
		return ItemID.getText();
	}
	public Part getPart(){
		int index = partIDListComboBox.getSelectedIndex();
		return this.partList.getPartByIndex(index);
	}
	public int getQuantity(){
		if(Quantity.getText() != null && Quantity.getText() != "")
			return Integer.parseInt(Quantity.getText());
		else return 0;
	}
	public String getItemLocation(){
		return  itemLocationComboBox.getSelectedItem().toString();
	}

	//OBSERVER METHODS
	@Override
	public void ItemDeleted() {
		this.setVisible(false);
		
	}

	@Override
	public void UpdateObserver(Item item) {
		
		this.item = item;
		this.setTitle("Edit Item");
		if(item!=null){
			int partListIndex = 0; 
			for(Part p: partList.parts){
				
				if(p.getID() == item.getItemPart().getID()){
					break;
				}
				partListIndex++;	
			}
			this.partIDListComboBox.setSelectedIndex(partListIndex);
			this.Quantity.setText(Integer.toString(item.getItemQuantity()));
			this.ItemID.setText(Long.toString(item.getID()));
			this.itemLocationComboBox.setSelectedItem(item.getItemLocation());
			this.setTitle("Edit Item");
			
			
		}
	}

	@Override
	public void updateObserver(PartList partList) {
		this.partList = partList;
		if(partList!=null){
			this.partList = partList;
			partIDListComboBox.removeAllItems();
			for(Part p: partList.parts){
				partIDListComboBox.addItem("ID: "+Long.toString(p.getID())+"    PartName: "+p.getPartName());
			}
		}
		
	}
	
	
	
}