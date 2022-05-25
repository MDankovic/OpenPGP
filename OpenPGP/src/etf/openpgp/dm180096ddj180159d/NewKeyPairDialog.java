package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class NewKeyPairDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField pfPassword;
	private JTextField tfEmail;
	private JTextField tfName;

	public NewKeyPairDialog() {
		this.setTitle("Generate Key-Pair");
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

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(rdbtn1024DSA);
		radioGroup.add(rdbtn2048DSA);
		radioGroup.add(rdbtn1024ElGamal);
		radioGroup.add(rdbtn2048ElGamal);
		radioGroup.add(rdbtn4096ElGamal);

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

	public static void main(String[] args) {
		try {
			NewKeyPairDialog dialog = new NewKeyPairDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
