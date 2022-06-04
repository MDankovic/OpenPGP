package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPException;

@SuppressWarnings("serial")
public class NewKeyPairDialog extends JDialog {

	private NewKeyPairDialog me;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField pfPassword;
	private JTextField tfEmail;
	private JTextField tfName;
	private JTable tableSecretKeys;
	private MainFrame mainFrame;

	public NewKeyPairDialog() {
		this.setTitle("New Key-Pair");
		this.me = this;
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

		setBounds(100, 100, 360, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 120, 90, 30);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lblPassword);

		pfPassword = new JPasswordField();
		pfPassword.setBounds(105, 120, 220, 30);
		pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pfPassword.setColumns(1);
		pfPassword.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(pfPassword);

		JLabel lblEmail = new JLabel("E-Mail:");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBounds(10, 70, 90, 30);
		contentPanel.add(lblEmail);

		tfEmail = new JTextField();
		tfEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tfEmail.setBounds(105, 70, 220, 30);
		contentPanel.add(tfEmail);
		tfEmail.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName.setBounds(10, 20, 90, 30);
		contentPanel.add(lblName);

		tfName = new JTextField();
		tfName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tfName.setColumns(10);
		tfName.setBounds(105, 20, 220, 30);
		contentPanel.add(tfName);

		JLabel lblDSA = new JLabel("DSA:");
		lblDSA.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDSA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDSA.setBounds(10, 172, 90, 30);
		contentPanel.add(lblDSA);

		JLabel lblElGamal = new JLabel("ElGamal:");
		lblElGamal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblElGamal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblElGamal.setBounds(10, 250, 90, 30);
		contentPanel.add(lblElGamal);

		JRadioButton rdbtn1024DSA = new JRadioButton("1024b");
		rdbtn1024DSA.setSelected(true);
		rdbtn1024DSA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtn1024DSA.setBounds(120, 170, 100, 30);
		contentPanel.add(rdbtn1024DSA);

		JRadioButton rdbtn2048DSA = new JRadioButton("2048b");
		rdbtn2048DSA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtn2048DSA.setBounds(120, 210, 100, 30);
		contentPanel.add(rdbtn2048DSA);

		JRadioButton rdbtn1024ElGamal = new JRadioButton("1024b");
		rdbtn1024ElGamal.setSelected(true);
		rdbtn1024ElGamal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtn1024ElGamal.setBounds(120, 250, 100, 30);
		contentPanel.add(rdbtn1024ElGamal);

		JRadioButton rdbtn2048ElGamal = new JRadioButton("2048b");
		rdbtn2048ElGamal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtn2048ElGamal.setBounds(120, 290, 100, 30);
		contentPanel.add(rdbtn2048ElGamal);

		JRadioButton rdbtn4096ElGamal = new JRadioButton("4096b");
		rdbtn4096ElGamal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtn4096ElGamal.setBounds(120, 330, 100, 30);
		contentPanel.add(rdbtn4096ElGamal);

		ButtonGroup radioGroupDSA = new ButtonGroup();
		radioGroupDSA.add(rdbtn1024DSA);
		radioGroupDSA.add(rdbtn2048DSA);

		ButtonGroup radioGroupElGamal = new ButtonGroup();
		radioGroupElGamal.add(rdbtn1024ElGamal);
		radioGroupElGamal.add(rdbtn2048ElGamal);
		radioGroupElGamal.add(rdbtn4096ElGamal);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SecretKeyRingTableModel model = (SecretKeyRingTableModel) tableSecretKeys.getModel();
				try {
					int dsaKeySize = 1024;
					int egKeySize = 1024;

					if (rdbtn2048DSA.isSelected())
						dsaKeySize = 2048;

					if (rdbtn2048ElGamal.isSelected())
						egKeySize = 2048;
					else if (rdbtn4096ElGamal.isSelected())
						egKeySize = 4096;

					String error = "";
					String user = tfName.getText();
					String email = tfEmail.getText();
					char[] passPhrase = pfPassword.getPassword();

					if ("".equals(user))
						error += "Name field must not be empty.\n";
					if ("".equals(email))
						error += "Email field must not be empty.\n";
					if (passPhrase.length == 0)
						error += "Password field must not be empty.";

					if (error != "")
						throw new IllegalValueException(error);

					String userId = user + " <" + email + ">";
					model.addKeyRing(DsaEgGenerator.generateKeyRing(userId, passPhrase, false, dsaKeySize, egKeySize));

					mainFrame.getTabbedPane().setSelectedIndex(0);
					int index = tableSecretKeys.getRowCount() - 1;
					tableSecretKeys.setRowSelectionInterval(index, index);

					JOptionPane.showMessageDialog(me, "New key-pair successfully generated.", "New Key-Pair",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();

				} catch (IllegalValueException e1) {
					JOptionPane.showMessageDialog(me, e1.getMessage(), "New Key-Pair", JOptionPane.ERROR_MESSAGE);
				} catch (NoSuchAlgorithmException | NoSuchProviderException | PGPException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnOk.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnOk.setActionCommand("OK");
		btnOk.setPreferredSize(new Dimension(40, 40));
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.setActionCommand("Cancel");
		btnCancel.setPreferredSize(new Dimension(40, 40));
		buttonPane.add(btnCancel);
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public JTable getTableSecretKeys() {
		return tableSecretKeys;
	}

	public void setTableSecretKeys(JTable tableSecretKeys) {
		this.tableSecretKeys = tableSecretKeys;
	}

}
