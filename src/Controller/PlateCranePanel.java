package Controller;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;


public class PlateCranePanel extends JPanel {
	private RXTXGui owner;
	private JTextField Velocity;
	private JCheckBox chckbxClockwise;
	private JButton btnConfig; 
	private JButton btnHome; 
	private JButton btnJogR; 
	private JButton btnJogP;
	private JButton btnJogZ;
	private JButton btnGrip;
	private JButton btnHalt;
	
	/**
	 * Create the panel.
	 */
	public PlateCranePanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);
		
		btnConfig = new JButton("Config");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeString("SETCONFIG 1");
			}
		});
		btnConfig.setBounds(26, 29, 89, 23);
		add(btnConfig);
		
		btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeString("HOME");
			}
		});
		btnHome.setBounds(26, 63, 89, 23);
		add(btnHome);
		
		btnJogR = new JButton("Jog R");
		btnJogR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isp = Integer.parseInt(Velocity.getText());
				if (isp <= 500) isp = 500;
				if (isp >= 5000) isp = 5000;
				boolean dir = chckbxClockwise.isSelected();
				String s = ((dir) ? "CJOG R, " : "CJOG R, -") + isp;
				System.out.println("rotating R: " + s);
				owner.getrxtx().writeString(s);
			}
		});
		btnJogR.setBounds(26, 97, 89, 23);
		add(btnJogR);
		
		btnJogP = new JButton("Jog P");
		btnJogP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isp = Integer.parseInt(Velocity.getText());
				if (isp <= 500) isp = 500;
				if (isp >= 5000) isp = 5000;
				boolean dir = chckbxClockwise.isSelected();
				String s = ((dir) ? "CJOG P, -" : "CJOG P, ") + isp;
				owner.getrxtx().writeString(s);
			}
		});
		btnJogP.setBounds(26, 131, 89, 23);
		add(btnJogP);
		
		btnJogZ = new JButton("Jog Z");
		btnJogZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isp = Integer.parseInt(Velocity.getText());
				if (isp <= 500) isp = 500;
				if (isp >= 5000) isp = 5000;
				boolean dir = chckbxClockwise.isSelected();
				String s = ((dir) ? "CJOG Z, -" : "CJOG Z, ") + isp;
				owner.getrxtx().writeString(s);
			}
		});
		btnJogZ.setBounds(26, 165, 89, 23);
		add(btnJogZ);
		
		btnGrip = new JButton("Grip");
		btnGrip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean dir = chckbxClockwise.isSelected();
				String s = (dir) ? "OPEN" : "CLOSE";
				owner.getrxtx().writeString(s);
			}
		});
		btnGrip.setBounds(26, 199, 89, 23);
		add(btnGrip);
		
		JButton btnHalt = new JButton("Halt");
		btnHalt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getrxtx().writeString("HALT");
			}
		});
		btnHalt.setBounds(26, 233, 89, 23);
		add(btnHalt);
		
		chckbxClockwise = new JCheckBox("Clockwise");
		chckbxClockwise.setBounds(26, 263, 97, 23);
		add(chckbxClockwise);
		
		Velocity = new JTextField();
		Velocity.setToolTipText("speed");
		Velocity.setText("500");
		Velocity.setBounds(29, 293, 86, 20);
		add(Velocity);
		Velocity.setColumns(10);
		
		JLabel lblPlateCrane = new JLabel("Plate Crane");
		lblPlateCrane.setBounds(44, 11, 61, 14);
		add(lblPlateCrane);
		
		JButton btnA = new JButton("A");
		btnA.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte data[] = {'!', 'A', 'A'};
				owner.getrxtx().writeStringGnoCR(data);
			}
		});
		btnA.setBounds(26, 320, 39, 23);
		add(btnA);
		
		JButton btnB = new JButton("B");
		btnB.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte data[] = {'!', 'B', 'B'};
				owner.getrxtx().writeStringGnoCR(data);
			}
		});
		btnB.setBounds(75, 320, 40, 23);
		add(btnB);
		
		JButton btnC = new JButton("C");
		btnC.setFont(new Font("Tahoma", Font.PLAIN, 8));
		btnC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte data[] = {'!', 'C', 'C'};
				owner.getrxtx().writeStringGnoCR(data);
			}
		});
		btnC.setBounds(26, 347, 39, 23);
		add(btnC);

	}
}
