package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class DeleteKeyDialog extends JDialog {
	private DeleteKeyDialog me;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordField;
	private JTable tableSecretKeys;
	private int selectedRow;

	public DeleteKeyDialog() {
		this.setTitle("Delete Key");
		this.me = this;
		this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);

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
				SecretKeyRingTableModel model = (SecretKeyRingTableModel) tableSecretKeys.getModel();
				try {
					model.removeKeyRing(selectedRow, passwordField.getPassword());
					JOptionPane.showMessageDialog(me, "Key successfully deleted.", "Delete Key",
							JOptionPane.INFORMATION_MESSAGE);

					dispose();
				} catch (IllegalValueException e1) {
					JOptionPane.showMessageDialog(me, e1.getMessage(), "Delete Key", JOptionPane.ERROR_MESSAGE);
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

	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public JTable getTableSecretKeys() {
		return tableSecretKeys;
	}

	public void setTableSecretKeys(JTable tableSecretKeys) {
		this.tableSecretKeys = tableSecretKeys;
	}

	public static void main(String[] args) {
		try {
			DeleteKeyDialog dialog = new DeleteKeyDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
