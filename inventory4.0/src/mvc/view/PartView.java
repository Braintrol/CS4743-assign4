package mvc.view;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import mvc.model.Part;

public class PartView extends JFrame  implements Part.Observer {

	private JPanel BottomPanel, TopPanel;
	private JTextField PartNum, PartName, Vendor, ExternalPartNum;
	private JLabel ID;
	private JComboBox PartUnit;
	private String[] unitTypes = {"Unknown","Pieces","Linear feet"};
	private JButton saveButton;
	private InventoryController contr;
	private Part part = null;
	
	
	public PartView(InventoryController contr) {	
		
		this.contr = contr;
		
		//BOTTOM PANEL:
		BottomPanel = new JPanel();
		BottomPanel.setLayout(new GridLayout());
		saveButton = new JButton("Save");
		BottomPanel.add(saveButton);
		this.add(BottomPanel, BorderLayout.SOUTH);
		
		//TOP PANEL:
		JPanel TopPanel = new JPanel();
		TopPanel.setLayout(new GridLayout(6,2));
		
		TopPanel.add(new JLabel("Part ID"));
		ID = new JLabel();
		TopPanel.add(ID);

		TopPanel.add(new JLabel("Part #"));
		PartNum = new JTextField();
		TopPanel.add(PartNum);
		
		TopPanel.add(new JLabel("Part Name"));
		PartName = new JTextField();
		TopPanel.add(PartName);
		
		TopPanel.add(new JLabel("External Part#"));
		ExternalPartNum = new JTextField();
		TopPanel.add(ExternalPartNum);
        
		TopPanel.add(new JLabel("Vendor"));
		Vendor = new JTextField();
		TopPanel.add(Vendor);
		
		TopPanel.add(new JLabel("Unit"));
		PartUnit = new JComboBox(unitTypes);
		TopPanel.add(PartUnit);
		
		//FRAME:
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(400, 200);
		this.setLocation(500, 100);
		this.add(BottomPanel, BorderLayout.SOUTH);
		this.add(TopPanel, BorderLayout.CENTER);
		this.setVisible(true);
		
	
		//LISTENER:
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(part == null){
					try{
						PartView.this.contr.addNewPartToPartList(PartView.this);
					}catch(IllegalArgumentException ex){ 
						showError(ex.getMessage());
					}
				}
				else if (part != null){
					try{
						PartView.this.contr.editExistingPart(part,PartView.this);
					}catch(IllegalArgumentException ex){
						showError(ex.getMessage());
					}
				}
			}
		});
		
	}
	
	
	
	
	//DISPLAY ERROR MESSAGE:
	private void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
	}
	
	//GET FIELD VALUES:
	public String getId(){
		return ID.getText();
	}
	public String getPartNum(){
		return PartNum.getText();
	}
	public String getPartName(){
		return PartName.getText();
	}
	public String getExternPartNum(){
		return ExternalPartNum.getText();
	}
	public String getVendor(){
		return Vendor.getText();
	}
	public String getUnit(){
		return PartUnit.getSelectedItem().toString();
		
	}

	//OBSERVER METHODS
	@Override
	public void updateObserver(Part part) {
		
		this.part = part;
		if(part != null){
			this.PartNum.setText(part.getPartNumber());
			this.PartName.setText(part.getPartName());
			this.Vendor.setText(part.getVendor());
			this.ExternalPartNum.setText(part.getExtrnPartNum());
			this.ID.setText(Long.toString(part.getID()));
			this.PartUnit.setSelectedItem(part.getPartUnit());
			this.setTitle("Edit Part");
		}
	}

	@Override
	public void PartDeleted() {
		this.setVisible(false);
	}
	
	
	
}
