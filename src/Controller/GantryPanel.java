package Controller;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JCheckBox;


public class GantryPanel extends JPanel {
	private JTextField X;
	private JTextField Y;
	private JTextField Z;
	private JTextField Velocity;
	JCheckBox chckbxIncremental;
	private JButton btnRemote; 
	private JButton btnServoOn;
	private JButton btnServoOff; 
	private JButton btnHome;
	private JButton btnVelocity;
	private JButton btnMove;
	private RXTXGui owner;
	private JLabel lblGantry;
	
	/**
	 * Create the panel.
	 */
	public GantryPanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);
		
		btnRemote = new JButton("Remote");
		btnRemote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte remote[] = {(byte)0x8F, 0x00};
				owner.getrxtx().writeStringGnoCR(remote);
			}
		});
		btnRemote.setBounds(31, 26, 89, 23);
		add(btnRemote);
		
		btnServoOn = new JButton("Servo On");
		btnServoOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeStringG("SVON");
			}
		});
		btnServoOn.setBounds(31, 60, 89, 23);
		add(btnServoOn);
		
		btnServoOff = new JButton("Servo Off");
		btnServoOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeStringG("SVOF");
			}
		});
		btnServoOff.setBounds(31, 94, 89, 23);
		add(btnServoOff);
		
		btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeStringG("HOM");
			}
		});
		btnHome.setBounds(31, 128, 89, 23);
		add(btnHome);
		
		btnVelocity = new JButton("Velocity");
		btnVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int v = Integer.parseInt(Velocity.getText());
				String s = "VEL " + v;
				owner.getrxtx().writeStringG(s);
			}
		});
		btnVelocity.setBounds(31, 162, 89, 23);
		add(btnVelocity);
		
		btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double x = Double.parseDouble(X.getText());
				double y = Double.parseDouble(Y.getText());
				double z = Double.parseDouble(Z.getText());
				String s;
				if (chckbxIncremental.isSelected()) {
					s = "MOV " + "X" + x + " Y" + y + " Z" + z + " I";
				}
				else {
					s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				}
				owner.getrxtx().writeStringG(s);
			}
		});
		btnMove.setBounds(31, 227, 89, 23);
		add(btnMove);
		
		X = new JTextField();
		X.setToolTipText("X position");
		X.setText("0");
		X.setBounds(31, 258, 65, 20);
		add(X);
		X.setColumns(10);
		
		Y = new JTextField();
		Y.setToolTipText("Y position");
		Y.setText("0");
		Y.setBounds(31, 285, 65, 20);
		add(Y);
		Y.setColumns(10);
		
		Z = new JTextField();
		Z.setToolTipText("Z position");
		Z.setText("0");
		Z.setBounds(31, 316, 65, 20);
		add(Z);
		Z.setColumns(10);
		
		Velocity = new JTextField();
		Velocity.setToolTipText("velocity (0..100)");
		Velocity.setText("10");
		Velocity.setBounds(34, 196, 62, 20);
		add(Velocity);
		Velocity.setColumns(10);
		
		lblGantry = new JLabel("Gantry");
		lblGantry.setBounds(50, 11, 46, 14);
		add(lblGantry);
		
		chckbxIncremental = new JCheckBox("Incremental");
		chckbxIncremental.setBounds(31, 343, 97, 23);
		add(chckbxIncremental);

	}
}
