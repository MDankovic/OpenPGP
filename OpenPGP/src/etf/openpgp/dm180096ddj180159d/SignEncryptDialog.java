package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;

@SuppressWarnings("serial")
public class SignEncryptDialog extends JDialog {

	private SignEncryptDialog me;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblSelectedFile;
	private JPasswordField pfPassword;
	private File file;
	private JTable tableSecretKeys;
	private JTable tablePublicKeys;

	public SignEncryptDialog(JTable tablePublicKeys, JTable tableSecretKeys) {
		this.setTitle("Sign/Encrypt Message");
		this.me = this;
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

		this.tablePublicKeys = tablePublicKeys;
		this.tableSecretKeys = tableSecretKeys;

		setBounds(100, 100, 580, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(60, 140, 90, 30);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lblPassword);

		pfPassword = new JPasswordField();
		pfPassword.setBounds(200, 140, 310, 30);
		pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pfPassword.setColumns(1);
		pfPassword.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(pfPassword);

		JLabel lblEncrypt = new JLabel("Encrypt for:");
		lblEncrypt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEncrypt.setBounds(60, 235, 90, 30);
		contentPanel.add(lblEncrypt);

		JLabel lblSignAs = new JLabel("Sign as:");
		lblSignAs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSignAs.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignAs.setBounds(60, 100, 90, 30);
		contentPanel.add(lblSignAs);

		DefaultListModel<String> encryptListModel = new DefaultListModel<>();
		PublicKeyRingTableModel publicKeyRingTableModel = (PublicKeyRingTableModel) tablePublicKeys.getModel();

		int pubRowCnt = publicKeyRingTableModel.getRowCount();
		for (int i = 0; i < pubRowCnt; i++) {
			encryptListModel.add(i, publicKeyRingTableModel.getPublicKeyString(i));
		}

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(200, 220, 310, 60);
		contentPanel.add(scrollPane_1);
		JList<String> listEncryptFor = new JList<String>(encryptListModel);
		scrollPane_1.setViewportView(listEncryptFor);
		listEncryptFor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		listEncryptFor.setVisibleRowCount(4);

		SecretKeyRingTableModel secretKeyRingTableModel = (SecretKeyRingTableModel) tableSecretKeys.getModel();

		int secRowCnt = secretKeyRingTableModel.getRowCount();
		String[] signListArr = new String[secRowCnt];

		for (int i = 0; i < secRowCnt; i++) {
			signListArr[i] = secretKeyRingTableModel.getSecretKeyString(i);
		}

		JComboBox<String> comboBox = new JComboBox<>(signListArr);
		comboBox.setBackground(Color.WHITE);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(200, 100, 310, 30);
		contentPanel.add(comboBox);

		JLabel lblFileName = new JLabel("File:");
		lblFileName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setBounds(60, 20, 90, 30);
		contentPanel.add(lblFileName);

		lblSelectedFile = new JLabel();
		lblSelectedFile.setText("./filepath");
		lblSelectedFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectedFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectedFile.setBounds(200, 20, 310, 30);
		contentPanel.add(lblSelectedFile);

		JCheckBox chckSign = new JCheckBox("Sign");
		chckSign.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckSign.setBounds(20, 60, 90, 30);
		contentPanel.add(chckSign);

		JCheckBox chckEncrypt = new JCheckBox("Encrypt");
		chckEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckEncrypt.setBounds(20, 180, 90, 30);
		contentPanel.add(chckEncrypt);

		JCheckBox chckCompress = new JCheckBox("Compress");
		chckCompress.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckCompress.setBounds(20, 330, 120, 30);
		contentPanel.add(chckCompress);

		JCheckBox chckConvert = new JCheckBox("Convert");
		chckConvert.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckConvert.setBounds(20, 370, 120, 30);
		contentPanel.add(chckConvert);

		JLabel lblCompressAlg = new JLabel();
		lblCompressAlg.setText("ZIP");
		lblCompressAlg.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompressAlg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCompressAlg.setBounds(200, 330, 310, 30);
		contentPanel.add(lblCompressAlg);

		JLabel lblConvertAlg = new JLabel();
		lblConvertAlg.setText("Base64");
		lblConvertAlg.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvertAlg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConvertAlg.setBounds(200, 370, 310, 30);
		contentPanel.add(lblConvertAlg);

		JLabel lblEncryptionAlgorithm = new JLabel("Algorithm:");
		lblEncryptionAlgorithm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncryptionAlgorithm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEncryptionAlgorithm.setBounds(60, 290, 90, 30);
		contentPanel.add(lblEncryptionAlgorithm);

		JRadioButton rdbtnNewRadioButton = new JRadioButton("3DES, EDE, 3K");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNewRadioButton.setBounds(200, 290, 145, 30);
		contentPanel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("CAST5-128");
		rdbtnNewRadioButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNewRadioButton_1.setBounds(365, 290, 145, 30);
		contentPanel.add(rdbtnNewRadioButton_1);

		ButtonGroup radioGroupEncAlg = new ButtonGroup();
		radioGroupEncAlg.add(rdbtnNewRadioButton);
		radioGroupEncAlg.add(rdbtnNewRadioButton_1);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnOk = new JButton("OK");
//		this.getRootPane().setDefaultButton(btnOk);
//		btnOk.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				System.out.println("|TETSTSTSGSV");
//				if (e.getKeyCode() == KeyEvent.VK_ENTER)
//					sendOperationCallback(publicKeyRingTableModel, listEncryptFor, secretKeyRingTableModel, comboBox,
//							chckSign, chckEncrypt, chckCompress, chckConvert, rdbtnNewRadioButton);
//			}
//		});
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendOperationCallback(publicKeyRingTableModel, listEncryptFor, secretKeyRingTableModel, comboBox,
						chckSign, chckEncrypt, chckCompress, chckConvert, rdbtnNewRadioButton);
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		this.lblSelectedFile.setText(file.getName());
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

	public static void main(String[] args) {
		try {
			SignEncryptDialog dialog = new SignEncryptDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendOperationCallback(PublicKeyRingTableModel publicKeyRingTableModel, JList<String> listEncryptFor,
			SecretKeyRingTableModel secretKeyRingTableModel, JComboBox<String> comboBox, JCheckBox chckSign,
			JCheckBox chckEncrypt, JCheckBox chckCompress, JCheckBox chckConvert, JRadioButton rdBtn) {
		SignEncryptOperation sOpr = new SignEncryptOperation(secretKeyRingTableModel, publicKeyRingTableModel);
		try {
			int encAlg = rdBtn.isSelected() ? SymmetricKeyAlgorithmTags.TRIPLE_DES : SymmetricKeyAlgorithmTags.CAST5;
			if (chckEncrypt.isSelected()) {

				int[] indices = listEncryptFor.getSelectedIndices();

				if (indices.length == 0) {
					throw new RecepientsNotSelectedException("Please select recepients, since encryption is selected.");
				} else
					for (int ind : indices) {
						sOpr.signEncryptMsg(file.getAbsolutePath(), comboBox.getSelectedIndex(), ind, encAlg,
								pfPassword.getPassword(), chckSign.isSelected(), true, chckCompress.isSelected(),
								chckConvert.isSelected());
					}
			} else {
				sOpr.signEncryptMsg(file.getAbsolutePath(), comboBox.getSelectedIndex(), -1, encAlg,
						pfPassword.getPassword(), chckSign.isSelected(), false, chckCompress.isSelected(),
						chckConvert.isSelected());
			}
			
			JOptionPane.showMessageDialog(me, "File successfully signed/encrypted.", "Sign/Encrypt File",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} catch (IncorrectPasswordException e1) {
			JOptionPane.showMessageDialog(me, e1.getMessage(), "Sign/Encrypt File", JOptionPane.ERROR_MESSAGE);
		} catch (RecepientsNotSelectedException e1) {
			JOptionPane.showMessageDialog(me, e1.getMessage(), "Sign/Encrypt File", JOptionPane.ERROR_MESSAGE);
		} catch (PGPException | IOException e1) {
			System.out.println("GRIJESKA PRILIKOM ENKRIPCIJE");
			e1.printStackTrace();
		}
	}
}
