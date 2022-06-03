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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private MainFrame me;
	private JPanel contentPane;
	private JTable tablePublicKeys;
	private JTable tableSecretKeys;
	private JTabbedPane tabbedPane;

	public MainFrame() {
		super("OpenPGP");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SecretKeyRingTableModel modelSecret = (SecretKeyRingTableModel) tableSecretKeys.getModel();
				PublicKeyRingTableModel modelPublic = (PublicKeyRingTableModel) tablePublicKeys.getModel();

				try (ArmoredOutputStream outSecret = new ArmoredOutputStream(new FileOutputStream("keys-secret.asc"));
						ArmoredOutputStream outPublic = new ArmoredOutputStream(
								new FileOutputStream("keys-public.asc"));) {
					modelSecret.getKeyRingCollection().encode(outSecret);
					modelPublic.getKeyRingCollection().encode(outPublic);
				} catch (IOException | PGPException e1) {
					e1.printStackTrace();
				}
			}
		});

		this.me = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 1200, 700);
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

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelMain.add(tabbedPane, "tPaneKeyRings");

		createKeyRingTables();

		try (ArmoredInputStream inSecret = new ArmoredInputStream(new FileInputStream("keys-secret.asc"));
				ArmoredInputStream inPublic = new ArmoredInputStream(new FileInputStream("keys-public.asc"));) {
			PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(inSecret,
					new BcKeyFingerprintCalculator());
			PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(inPublic,
					new BcKeyFingerprintCalculator());

			SecretKeyRingTableModel modelSecret = (SecretKeyRingTableModel) tableSecretKeys.getModel();
			modelSecret.setKeyRingList(secretKeyRingCollection);

			PublicKeyRingTableModel modelPublic = (PublicKeyRingTableModel) tablePublicKeys.getModel();
			modelPublic.setKeyRingList(publicKeyRingCollection);

		} catch (IOException | PGPException e1) {
			e1.printStackTrace();
		}

		JScrollPane sPaneSecretKeys = new JScrollPane();
		sPaneSecretKeys.setViewportView(tableSecretKeys);
		tabbedPane.addTab("Secret Keys", null, sPaneSecretKeys, null);

		JScrollPane sPanePublicKeys = new JScrollPane();
		sPanePublicKeys.setViewportView(tablePublicKeys);
		tabbedPane.addTab("Public Keys", null, sPanePublicKeys, null);
	}

	private void createKeyRingTables() {
		SecretKeyRingTableModel secretTableModel = new SecretKeyRingTableModel();

		tableSecretKeys = new JTable(secretTableModel);
		tableSecretKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSecretKeys.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableSecretKeys.setFont(new Font("Tahoma", Font.BOLD, 14));
		tableSecretKeys.setRowHeight(25);
		tableSecretKeys.setShowHorizontalLines(false);
		tableSecretKeys.setShowVerticalLines(false);
		tableSecretKeys.getTableHeader().setOpaque(false);
		tableSecretKeys.getTableHeader().setBackground(Color.LIGHT_GRAY);
		tableSecretKeys.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));

		PublicKeyRingTableModel publicTableModel = new PublicKeyRingTableModel();

		tablePublicKeys = new JTable(publicTableModel);
		tablePublicKeys.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePublicKeys.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tablePublicKeys.setFont(new Font("Tahoma", Font.BOLD, 14));
		tablePublicKeys.setRowHeight(25);
		tablePublicKeys.setShowHorizontalLines(false);
		tablePublicKeys.setShowVerticalLines(false);
		tablePublicKeys.getTableHeader().setOpaque(false);
		tablePublicKeys.getTableHeader().setBackground(Color.LIGHT_GRAY);
		tablePublicKeys.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
	}

	private void createButtons(JPanel panelButtons) {
		JButton btnSignEncrypt = new JButton("Sign/Encrypt File");
		btnSignEncrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SignEncryptDialog signEncryptDialog = new SignEncryptDialog(tablePublicKeys, tableSecretKeys);
					signEncryptDialog.setFile(chooser.getSelectedFile());
					signEncryptDialog
							.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
					signEncryptDialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected.", "File Error",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnSignEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSignEncrypt.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnSignEncrypt);

		JButton btnDecryptVerify = new JButton("Decrypt/Verify File");
		btnDecryptVerify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenPGP encrypted files", "gpg");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					DecryptVerifyDialog decryptVerifyDialog = new DecryptVerifyDialog(tablePublicKeys, tableSecretKeys);
					decryptVerifyDialog.setFile(chooser.getSelectedFile());
					decryptVerifyDialog
							.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
					decryptVerifyDialog.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected.", "File Error",
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
				NewKeyPairDialog newKeyPairDialog = new NewKeyPairDialog();
				newKeyPairDialog.setTableSecretKeys(tableSecretKeys);
				newKeyPairDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				newKeyPairDialog.setVisible(true);
			}
		});
		btnKeyPair.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnKeyPair.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnKeyPair);

		JButton btnDeleteKey = new JButton("Delete Key");
		btnDeleteKey.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedTab = tabbedPane.getSelectedIndex();
				int selectedRow = -1;
				if (selectedTab == 0) {
					selectedRow = tableSecretKeys.getSelectedRow();
					if (selectedRow != -1) {
						DeleteKeyDialog deleteKeyDialog = new DeleteKeyDialog();
						deleteKeyDialog.setTableSecretKeys(tableSecretKeys);
						deleteKeyDialog.setSelectedRow(selectedRow);
						deleteKeyDialog
								.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) e.getSource()));
						deleteKeyDialog.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(me, "Please select the key you want to delete.", "Delete Key",
								JOptionPane.WARNING_MESSAGE);
					}
				} else if (selectedTab == 1) {
					selectedRow = tablePublicKeys.getSelectedRow();
					if (selectedRow != -1) {
						PublicKeyRingTableModel model = (PublicKeyRingTableModel) tablePublicKeys.getModel();
						model.removeKeyRing(selectedRow);
						JOptionPane.showMessageDialog(me, "Key successfully deleted.", "Delete Key",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(me, "Please select the key you want to delete.", "Delete Key",
								JOptionPane.WARNING_MESSAGE);
					}
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
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OpenPGP exported key", "asc");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(SwingUtilities.getWindowAncestor((Component) e.getSource()));
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					try (ArmoredInputStream inPublic = new ArmoredInputStream(
							new FileInputStream(chooser.getSelectedFile().getAbsolutePath()));) {
						
						// Try to import PUBLIC key
						PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(inPublic,
								new BcKeyFingerprintCalculator());
						PublicKeyRingTableModel modelPublic = (PublicKeyRingTableModel) tablePublicKeys.getModel();
						
						if (!publicKeyRingCollection.getKeyRings().hasNext()) throw new PGPException("Empty list");
						modelPublic.addKeyRingList(publicKeyRingCollection);
						
//					} catch (IOException | PGPException e1) {
//						e1.printStackTrace();
//						try (ArmoredInputStream inSecret = new ArmoredInputStream(
//								new FileInputStream(chooser.getSelectedFile().getAbsolutePath()));) {
//							
//							// Try to import SECRET key
//							PGPSecretKeyRingCollection secretKeyRingCollection = new PGPSecretKeyRingCollection(
//									inSecret, new BcKeyFingerprintCalculator());
//							SecretKeyRingTableModel modelSecret = (SecretKeyRingTableModel) tableSecretKeys.getModel();
//							modelSecret.addKeyRingList(secretKeyRingCollection);
//
						} catch (IOException | PGPException e2) {
//							e2.printStackTrace();
							JOptionPane.showMessageDialog(me, "File is invalid, no keys detected.", "Import Key",
									JOptionPane.ERROR_MESSAGE);
						}
//					}
				} else {
					JOptionPane.showMessageDialog(me, "No file has been selected.", "Import Key",
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
					SecretKeyRingTableModel model = (SecretKeyRingTableModel) tableSecretKeys.getModel();
					model.exportPublicKey(selectedRow);
					JOptionPane.showMessageDialog(me, "Key successfully exported.", "Export Key",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(me, "Please select the key you want to export.", "Export Key",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnExportKey.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnExportKey.setPreferredSize(new Dimension(40, 40));
		panelButtons.add(btnExportKey);
	}

	public JTable getTablePublicKeys() {
		return tablePublicKeys;
	}

	public void setTablePublicKeys(JTable tablePublicKeys) {
		this.tablePublicKeys = tablePublicKeys;
	}

	public JTable getTableSecretKeys() {
		return tableSecretKeys;
	}

	public void setTableSecretKeys(JTable tableSecretKeys) {
		this.tableSecretKeys = tableSecretKeys;
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
