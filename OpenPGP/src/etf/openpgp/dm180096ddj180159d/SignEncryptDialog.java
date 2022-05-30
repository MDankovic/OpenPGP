package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;

@SuppressWarnings("serial")
public class SignEncryptDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblSelectedFile;
	private JPasswordField pfPassword;
	private File file;
	private JTable tableSecretKeys;
	private JTable tablePublicKeys;

	public SignEncryptDialog(JTable tablePublicKeys, JTable tableSecretKeys) {
		this.setTitle("Sign/Encrypt Message");
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

		this.tablePublicKeys = tablePublicKeys;
		this.tableSecretKeys = tableSecretKeys;

		setBounds(100, 100, 530, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(40, 140, 90, 30);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lblPassword);

		pfPassword = new JPasswordField();
		pfPassword.setBounds(170, 140, 290, 30);
		pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pfPassword.setColumns(1);
		pfPassword.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(pfPassword);

		JLabel lblEncrypt = new JLabel("Encrypt for:");
		lblEncrypt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEncrypt.setBounds(40, 220, 90, 30);
		contentPanel.add(lblEncrypt);

		JLabel lblSignAs = new JLabel("Sign as:");
		lblSignAs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSignAs.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignAs.setBounds(40, 100, 90, 30);
		contentPanel.add(lblSignAs);

		DefaultListModel<String> encryptListModel = new DefaultListModel<>();
		PublicKeyRingTableModel publicKeyRingTableModel = (PublicKeyRingTableModel) tablePublicKeys.getModel();

		for (int i = 0; i < publicKeyRingTableModel.getRowCount(); i++) {
			encryptListModel.add(i, publicKeyRingTableModel.getPublicKeyString(i));
		}

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(170, 220, 290, 30);
		contentPanel.add(scrollPane_1);
		JList<String> listEncryptFor = new JList<String>(encryptListModel);
		scrollPane_1.setViewportView(listEncryptFor);
		listEncryptFor.setFont(new Font("Tahoma", Font.PLAIN, 10));
		listEncryptFor.setVisibleRowCount(4);

		DefaultListModel<String> signListModel = new DefaultListModel<>();
		SecretKeyRingTableModel secretKeyRingTableModel = (SecretKeyRingTableModel) tableSecretKeys.getModel();

		for (int i = 0; i < secretKeyRingTableModel.getRowCount(); i++) {
			signListModel.add(i, secretKeyRingTableModel.getSecretKeyString(i));
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(170, 100, 290, 30);
		contentPanel.add(scrollPane);

		JList<String> listSignAs = new JList<String>(signListModel);
		scrollPane.setColumnHeaderView(listSignAs);
		listSignAs.setFont(new Font("Tahoma", Font.PLAIN, 10));
		listSignAs.setVisibleRowCount(4);

		JLabel lblFileName = new JLabel("File:");
		lblFileName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setBounds(10, 20, 90, 30);
		contentPanel.add(lblFileName);

		lblSelectedFile = new JLabel();
		lblSelectedFile.setText("./filepath");
		lblSelectedFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectedFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectedFile.setBounds(170, 20, 290, 30);
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
		chckCompress.setBounds(20, 300, 120, 30);
		contentPanel.add(chckCompress);

		JCheckBox chckConvert = new JCheckBox("Convert");
		chckConvert.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckConvert.setBounds(20, 340, 120, 30);
		contentPanel.add(chckConvert);

		JLabel lblCompressAlg = new JLabel();
		lblCompressAlg.setText("ZIP");
		lblCompressAlg.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompressAlg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCompressAlg.setBounds(170, 300, 290, 30);
		contentPanel.add(lblCompressAlg);

		JLabel lblConvertAlg = new JLabel();
		lblConvertAlg.setText("Base64");
		lblConvertAlg.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvertAlg.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConvertAlg.setBounds(170, 340, 290, 30);
		contentPanel.add(lblConvertAlg);

		JLabel lblEncryptionAlgorithm = new JLabel("Algorithm:");
		lblEncryptionAlgorithm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncryptionAlgorithm.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEncryptionAlgorithm.setBounds(40, 260, 90, 30);
		contentPanel.add(lblEncryptionAlgorithm);

		JRadioButton rdbtnNewRadioButton = new JRadioButton("3DES, EDE, 3K");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNewRadioButton.setBounds(170, 260, 140, 30);
		contentPanel.add(rdbtnNewRadioButton);

		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("CAST5-128");
		rdbtnNewRadioButton_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNewRadioButton_1.setBounds(330, 260, 140, 30);
		contentPanel.add(rdbtnNewRadioButton_1);

		ButtonGroup radioGroupEncAlg = new ButtonGroup();
		radioGroupEncAlg.add(rdbtnNewRadioButton);
		radioGroupEncAlg.add(rdbtnNewRadioButton_1);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnOk = new JButton("OK");
		this.getRootPane().setDefaultButton(btnOk);
		btnOk.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("|TETSTSTSGSV");
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					sendOperationCallback(publicKeyRingTableModel, listEncryptFor, secretKeyRingTableModel, listSignAs,
							chckSign, chckEncrypt, chckCompress, chckConvert, rdbtnNewRadioButton);
			}
		});
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendOperationCallback(publicKeyRingTableModel, listEncryptFor, secretKeyRingTableModel, listSignAs,
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
			SecretKeyRingTableModel secretKeyRingTableModel, JList<String> listSignAs, JCheckBox chckSign,
			JCheckBox chckEncrypt, JCheckBox chckCompress, JCheckBox chckConvert, JRadioButton rdBtn) {
		SendOperation sOpr = new SendOperation(secretKeyRingTableModel, publicKeyRingTableModel);
		try {
			int encAlg = rdBtn.isSelected() ? SymmetricKeyAlgorithmTags.TRIPLE_DES : SymmetricKeyAlgorithmTags.CAST5;
			if (chckEncrypt.isSelected()) {
				for (int ind : listEncryptFor.getSelectedIndices()) {
					sOpr.encryptMsg(file.getAbsolutePath(), listSignAs.getSelectedIndex(), ind, encAlg,
							pfPassword.getPassword(), chckSign.isSelected(), true, chckCompress.isSelected(),
							chckConvert.isSelected());
				}
			} else {
				sOpr.encryptMsg(file.getAbsolutePath(), listSignAs.getSelectedIndex(), -1, encAlg,
						pfPassword.getPassword(), chckSign.isSelected(), false, chckCompress.isSelected(),
						chckConvert.isSelected());
			}
			dispose();
		} catch (PGPException | IOException e1) {
			System.out.println("GRIJESKA PRILIKOM ENKRIPCIJE");
			e1.printStackTrace();
		}
	}
}
