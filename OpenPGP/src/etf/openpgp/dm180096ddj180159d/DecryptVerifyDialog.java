package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.openpgp.PGPException;

@SuppressWarnings("serial")
public class DecryptVerifyDialog extends JDialog {
	private DecryptVerifyDialog me;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private JTable tableSecretKeys;
	private JTable tablePublicKeys;
	private File file;

	public DecryptVerifyDialog(JTable tablePublicKeys, JTable tableSecretKeys) {
		this.setTitle("Decrypt/Verify File");
		this.me = this;
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

		this.tablePublicKeys = tablePublicKeys;
		this.tableSecretKeys = tableSecretKeys;
		
		setBounds(100, 100, 340, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblDeleteKey = new JLabel("Password:");
		lblDeleteKey.setBounds(10, 40, 90, 30);
		lblDeleteKey.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDeleteKey.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lblDeleteKey);

		passwordField = new JPasswordField();
		passwordField.setBounds(105, 40, 200, 30);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setColumns(1);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(passwordField);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					ReceiveOperation decVer = new ReceiveOperation((SecretKeyRingTableModel)(tableSecretKeys.getModel()),
							(PublicKeyRingTableModel)(tablePublicKeys.getModel()));
					
					decVer.receiveMsg(file.getAbsolutePath(), passwordField.getPassword());
					
					dispose();
				} catch (PGPException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(me, "Error while decrypting file.",
							"Decrypt/Verify File", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(me, "Error while opening file.",
							"Decrypt/Verify File", JOptionPane.ERROR_MESSAGE);
				}
				
				dispose();
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

	public JTable getTableSecretKeys() {
		return tableSecretKeys;
	}

	public void setTableSecretKeys(JTable tableSecretKeys) {
		this.tableSecretKeys = tableSecretKeys;
	}
	
	public JTable getTablePublicKeys() {
		return tablePublicKeys;
	}

	public void setTablePublicKeys(JTable tablePublicKeys) {
		this.tablePublicKeys = tablePublicKeys;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
