package etf.openpgp.dm180096ddj180159d;

import javax.swing.JTable;

public class KeyRingTable {
	private JTable table;
	
	private String data[][] = { 
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" },
			{ "Vinod", "MCA", "Computer", "Test" }
		};
	
	
	private String[] columns = {"E-Mail", "Valid From", "Valid Until", "Key-ID"};
	
	public KeyRingTable() {
		super();
		
		this.table = new JTable(this.data, this.columns);
	}
	
	public KeyRingTable(String[][] data, String[] columns) {
		this.data = data;
		this.columns = columns;
		
		this.table = new JTable(data, columns);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}
	
}
