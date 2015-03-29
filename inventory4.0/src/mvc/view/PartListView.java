package mvc.view;
import mvc.model.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mvc.controller.InventoryController;
import mvc.model.Part;
import mvc.model.PartList;
import mvc.model.PartList.Observer;






public class PartListView extends JFrame implements PartList.Observer{
	
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private JButton addButton;
	private JButton deleteButton;
	private JPanel bottomPanel;
    private PartList pList;
	private InventoryController contr;
	
	
	//public PartListView(PartList partList) {
	public PartListView(InventoryController contr) {
				
		//register controller
		this.contr = contr;
		
		//BOTTOM PANEL:
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		addButton = new JButton("Add Part");
		bottomPanel.add(addButton);
		deleteButton = new JButton("Delete Part");
		bottomPanel.add(deleteButton);
	
		//JFRAME:
		this.setLayout(new BorderLayout());
		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setFixedCellWidth(100);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(400, 400);
		this.setLocation(500, 100);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.setTitle("Part List");
		this.add(new JScrollPane(list),BorderLayout.CENTER);
		this.setVisible(true);
		
		
		addButton.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				PartListView.this.contr.openPartView_toAddNewPart();
			}
		});
		
		deleteButton.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!list.isSelectionEmpty()){
					int index = list.getSelectedIndex();
					Part p = pList.getPartByIndex(index);
					try{
						PartListView.this.contr.deletePartFromPartList(p);
					}catch(IllegalArgumentException ex){
						showError(ex.getMessage());
					}
				}
			}	
		});
		

		
		list.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList)e.getSource();
				if (e.getClickCount() >= 2) {
					int index = list.locationToIndex(e.getPoint());
					Part p = pList.getPartByIndex(index);	
					PartListView.this.contr.openParView_forEditing(p);   
				}
			}
		});
	}
	
	
	
	//DISPLAY ERROR MESSAGE:
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
	}

	//OBSERVERS:
	@Override
	public void updateObserver(PartList e) {
		
		pList = e;
		listModel.clear();
		if(pList!=null){
			for(Part p : e.parts){
				String formated = String.format("ID%03d %5s, sold in %s, by %s",p.getID(),p.getPartName(),p.getPartUnit(), p.getVendor());
				listModel.addElement(formated);	
			}
		}
	}

	
	

	
}