package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private MainFrame me;
	private JPanel contentPane;
	private JTable tablePublicKeys;
	private JTable tableSecretKeys;
	private File file;

	private String data1[][] = { { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" },
			{ "PeraP", "Vinod", "MCA", "Computer", "Test" }, { "PeraP", "Vinod", "MCA", "Computer", "Test" } };

	private String data2[][] = { { "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" },
			{ "ZivojinZ", "Aleksa", "WTP", "Television", "Zika" } };

	public MainFrame() {
		super("OpenPGP");

		this.me = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 10));

		JPanel panelButtons = new JPanel();
		contentPane.add(panelButtons, BorderLayout.NORTH);
		panelButtons.setLayout(new GridLayout(1, 6, 30, 0));
		
		createButtons(panelButtons);

		JPanel panelMain = new JPanel();
		contentPane.add(panelMain, BorderLayout.CENTER);
		panelMain.setLayout(new CardLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelMain.add(tabbedPane, "tPaneKeyRings");
		
		createKeyRingTables();

		JScrollPane sPaneSecretKeys = new JScrollPane();
		sPaneSecretKeys.setViewportView(tableSecretKeys);
		tabbedPane.addTab("Secret Keys", null, sPaneSecretKeys, null);

		JScrollPane sPanePublicKeys = new JScrollPane();
		sPanePublicKeys.setViewportView(tablePublicKeys);
		tabbedPane.addTab("Public Keys", null, sPanePublicKeys, null);
	}
	
	private void createKeyRingTables() {
		// DUMMY VALUES
		DefaultTableModel secretTableModel = new KeyRingTableModel();
		for (Object[] row : data1) {
			secretTableModel.addRow(row);
		}

		tablePublicKeys = new JTable(secretTableModel);
		tablePublicKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePublicKeys.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tablePublicKeys.setFont(new Font("Tahoma", Font.BOLD, 14));
		tablePublicKeys.setRowHeight(25);
		tablePublicKeys.setShowHorizontalLines(false);
		tablePublicKeys.setShowVerticalLines(false);
		tablePublicKeys.getTableHeader().setOpaque(false);
		tablePublicKeys.getTableHeader().setBackground(Color.LIGHT_GRAY);
		tablePublicKeys.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));

		DefaultTableModel publicTableModel = new KeyRingTableModel();
		for (Object[] row : data2) {
			publicTableModel.addRow(row);
		}

		tableSecretKeys = new JTable(publicTableModel);
		tableSecretKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSecretKeys.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableSecretKeys.setFont(new Font("Tahoma", Font.BOLD, 14));
		tableSecretKeys.setRowHeight(25);
		tableSecretKeys.setShowHorizontalLines(false);
		tableSecretKeys.setShowVerticalLines(false);
		tableSecretKeys.getTableHeader().setOpaque(false);
		tableSecretKeys.getTableHeader().setBackground(Color.LIGHT_GRAY);
		tableSecretKeys.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
	}

	private void createButtons(JPanel panelButtons) {
		JButton btnSignEncrypt = new JButton("Sign/Encrypt");
		btnSignEncrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenPGP encrypted files", "gpg");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SignEncryptDialog signEncryptDialog = new SignEncryptDialog();
					signEncryptDialog.setFile(chooser.getSelectedFile());
					signEncryptDialog
							.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
					signEncryptDialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected", "File Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnSignEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSignEncrypt.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnSignEncrypt);

		JButton btnDecryptVerify = new JButton("Decrypt/Verify");
		btnDecryptVerify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenPGP encrypted files", "gpg");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("FILE processed");
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected", "File Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnDecryptVerify.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDecryptVerify.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnDecryptVerify);

		JButton btnKeyPair = new JButton("New Key-Pair");
		btnKeyPair.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog newKeyPairDialog = new NewKeyPairDialog();
				newKeyPairDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				newKeyPairDialog.setVisible(true);
				DefaultTableModel model = (DefaultTableModel) tableSecretKeys.getModel();
				model.addRow(new Object[] { "Petar", "IMG", "Zikica123", "Mare", "Jovic" });
			}
		});
		btnKeyPair.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnKeyPair.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnKeyPair);

		JButton btnDeleteKey = new JButton("Delete Key");
		btnDeleteKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tableSecretKeys.getSelectedRow();
				if (selectedRow != -1) {
					JDialog deleteKeyDialog = new DeleteKeyDialog();
					deleteKeyDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
					deleteKeyDialog.setVisible(true);
					DefaultTableModel model = (DefaultTableModel) tableSecretKeys.getModel();
					model.removeRow(selectedRow);
				} else {
					JOptionPane.showMessageDialog(me, "Please select the key you want to delete", "No Key Selected",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnDeleteKey.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDeleteKey.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnDeleteKey);

		JButton btnImportKey = new JButton("Import Key");
		btnImportKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenPGP exported kry", "asc");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("KEY IMPORTED");
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected", "File Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnImportKey.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnImportKey.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnImportKey);

		JButton btnExportKey = new JButton("Export Key");
		btnExportKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = tableSecretKeys.getSelectedRow();
				if (selectedRow != -1) {
					System.out.println("KEY EXPORTED");
				} else {
					JOptionPane.showMessageDialog(me, "Please select the key you want to export", "No Key Selected",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnExportKey.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnExportKey.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnExportKey);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
