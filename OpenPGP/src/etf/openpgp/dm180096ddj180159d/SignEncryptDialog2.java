package etf.openpgp.dm180096ddj180159d;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class SignEncryptDialog2 extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SignEncryptDialog2 dialog = new SignEncryptDialog2();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SignEncryptDialog2() {
		setBounds(100, 100, 784, 473);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblTitle = new JLabel("Send a message");
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
			contentPanel.add(lblTitle, BorderLayout.NORTH);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(2, 1, 0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(2, 3, 0, 0));
				{
					JLabel lblFile = new JLabel("File:");
					lblFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel_1.add(lblFile);
				}
				{
					JButton btnNewButton = new JButton("Choose...");
					btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
					panel_1.add(btnNewButton);
				}
				{
					JLabel lblFileName = new JLabel("");
					panel_1.add(lblFileName);
				}
				{
					JLabel lblOptions = new JLabel("Options:");
					lblOptions.setFont(new Font("Tahoma", Font.PLAIN, 16));
					panel_1.add(lblOptions);
				}
				{
					JLabel lblFileName = new JLabel("");
					panel_1.add(lblFileName);
				}
				{
					JLabel lblFileName = new JLabel("");
					panel_1.add(lblFileName);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(1, 4, 0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					panel_2.setLayout(new GridLayout(0, 1, 0, 0));
					{
						JCheckBox chckbxNewCheckBox = new JCheckBox("authentication");
						chckbxNewCheckBox.setAlignmentY(Component.TOP_ALIGNMENT);
						chckbxNewCheckBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
						chckbxNewCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
						panel_2.add(chckbxNewCheckBox);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					panel_2.setLayout(new GridLayout(0, 1, 0, 0));
					{
						JCheckBox chckbxNewCheckBox_1 = new JCheckBox("encryption");
						chckbxNewCheckBox_1.setAlignmentY(Component.TOP_ALIGNMENT);
						chckbxNewCheckBox_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
						panel_2.add(chckbxNewCheckBox_1);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					panel_2.setLayout(new GridLayout(0, 1, 0, 0));
					{
						JCheckBox chckbxNewCheckBox_2 = new JCheckBox("compression");
						chckbxNewCheckBox_2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
						panel_2.add(chckbxNewCheckBox_2);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
					panel_2.setLayout(new GridLayout(0, 1, 0, 0));
					{
						JCheckBox chckbxNewCheckBox_3 = new JCheckBox("conversion");
						chckbxNewCheckBox_3.setAlignmentY(Component.TOP_ALIGNMENT);
						chckbxNewCheckBox_3.setAlignmentX(Component.RIGHT_ALIGNMENT);
						panel_2.add(chckbxNewCheckBox_3);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
