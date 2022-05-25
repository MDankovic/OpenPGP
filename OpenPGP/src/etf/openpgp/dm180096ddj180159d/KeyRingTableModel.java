package etf.openpgp.dm180096ddj180159d;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class KeyRingTableModel extends DefaultTableModel {

	private String[] columns = {"Name", "E-Mail", "Valid From", "Valid Until", "Key-ID"};

	public KeyRingTableModel() {
		super();
		
		this.setColumnIdentifiers(columns);
	}
	
}
