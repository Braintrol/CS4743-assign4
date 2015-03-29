package mvc.view;
import mvc.model.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import mvc.controller.InventoryController;
import mvc.model.Item;
import mvc.model.ItemList;

public class ItemListView extends JFrame implements ItemList.Observer{
	public JList<String> list;
	private DefaultListModel<String> listModel;
	private JButton addButton;
	private JButton deleteButton;
	private JPanel bottomPanel;
	private ItemList itemList;
	private InventoryController contr;
	
	
	public ItemListView(InventoryController contr) {
		
		this.contr = contr;
		
		//BOTTOM PANEL:
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		addButton = new JButton("Add Item");
		bottomPanel.add(addButton);
		deleteButton = new JButton("Delete Item");
		bottomPanel.add(deleteButton);
	
		//JFRAME:
		this.setLayout(new BorderLayout());
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setFixedCellWidth(100);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(new JScrollPane(list),BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(400, 400);
		this.setLocation(500, 100);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.setTitle("Item List");
		this.setVisible(true);
		
		
		
		//add listeners
		list.addMouseListener( new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList)e.getSource();
				if (e.getClickCount() >= 2) {	
					int index = list.locationToIndex(e.getPoint());
					Item i = itemList.getItemAtIndex(index);
					ItemListView.this.contr.openItemView_forEditing(i);
				}
			}	
		});
		
		
		addButton.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					ItemListView.this.contr.openItemtView_toAddNewItem();
				}catch(IllegalArgumentException ex){
					showError(ex.getMessage());
				}
			}
		});
		
		
		
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if( !list.isSelectionEmpty()){
					int index = list.getSelectedIndex();
					Item i = itemList.getItemAtIndex(index);
					try{
						ItemListView.this.contr.deleteItemFromItemList(i);
					}catch(IllegalArgumentException ex){
						showError(ex.getMessage());
					}
				}
			}	
		});
	}
	
	
	
	//display error
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
	}

	//METHODS TO GET NOTIFICATIONS FROM THE MODEL CHANGES:
	@Override
	public void updateObserver(ItemList e) {
		itemList = e;
		listModel.clear();
		if(itemList != null){
			for(Item i : itemList.items){
				String formated = String.format("ID%03d  we have %d %s(s) #%s, at %20s",i.getID(),i.getItemQuantity(),
												i.getItemPart().getPartName(),i.getItemPartNumber(),i.getItemLocation());
				listModel.addElement(formated);	
			}
			this.setTitle("Item List");
		}
	}
	
}
