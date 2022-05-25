package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class SignEncryptDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblSelectedFile;
	private JPasswordField pfPassword;
	private File file;

	public SignEncryptDialog() {
		this.setTitle("Sign/Encrypt Message");
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

		setBounds(100, 100, 360, 340);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 170, 90, 30);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPanel.add(lblPassword);

		pfPassword = new JPasswordField();
		pfPassword.setBounds(105, 170, 220, 30);
		pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pfPassword.setColumns(1);
		pfPassword.setHorizontalAlignment(SwingConstants.LEFT);
		contentPanel.add(pfPassword);

		JLabel lblEncrypt = new JLabel("Encrypt for:");
		lblEncrypt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEncrypt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEncrypt.setBounds(10, 120, 90, 30);
		contentPanel.add(lblEncrypt);

		JLabel lblSignAs = new JLabel("Sign as:");
		lblSignAs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSignAs.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSignAs.setBounds(10, 70, 90, 30);
		contentPanel.add(lblSignAs);

		JList listEncryptFor = new JList();
		listEncryptFor.setBounds(105, 120, 220, 30);
		contentPanel.add(listEncryptFor);

		JList listSignAs = new JList();
		listSignAs.setBounds(105, 70, 220, 30);
		contentPanel.add(listSignAs);

		JLabel lblFileName = new JLabel("File:");
		lblFileName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setBounds(10, 20, 90, 30);
		contentPanel.add(lblFileName);

		lblSelectedFile = new JLabel();
		lblSelectedFile.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSelectedFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectedFile.setBounds(105, 20, 220, 30);
		contentPanel.add(lblSelectedFile);

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new GridLayout(0, 2, 0, 0));

		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

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

	public static void main(String[] args) {
		try {
			SignEncryptDialog dialog = new SignEncryptDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
