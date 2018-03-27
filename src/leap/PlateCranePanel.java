package leap;
import gnu.io.CommPortIdentifier;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;


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
	private JButton btnLeapCtrl;
    private CommConnection commconnection;
    private JButton btnConnect;
    private JButton btnDisconnect;
	private PalmFrame frame;
	
    public void writeString(String data)
    {
    	byte[] bdata = data.getBytes();
    	for (int i = 0; i < bdata.length; ++i) {
    		commconnection.writeData(bdata[i]);
    	}
    	commconnection.writeData(0x0D);
    	commconnection.writeData(0x0A);
    	
    }

    /**
	 * Create the panel.
	 */
	public PlateCranePanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);
		commconnection = new CommConnection(this);
		
		btnConfig = new JButton("Config");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//owner.getCommPanel().writeString("SETCONFIG 1");
				writeString("SETCONFIG 1");
			}
		});
		btnConfig.setBounds(26, 85, 89, 23);
		add(btnConfig);
		
		btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				owner.getCommPanel().writeString("HOME");
				writeString("HOME");
			}
		});
		btnHome.setBounds(26, 109, 89, 23);
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
//				owner.getCommPanel().writeString(s);
				writeString(s);
			}
		});
		btnJogR.setBounds(26, 134, 89, 23);
		add(btnJogR);
		
		btnJogP = new JButton("Jog P");
		btnJogP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isp = Integer.parseInt(Velocity.getText());
				if (isp <= 500) isp = 500;
				if (isp >= 5000) isp = 5000;
				boolean dir = chckbxClockwise.isSelected();
				String s = ((dir) ? "CJOG P, -" : "CJOG P, ") + isp;
//				owner.getCommPanel().writeString(s);
				writeString(s);
			}
		});
		btnJogP.setBounds(26, 158, 89, 23);
		add(btnJogP);
		
		btnJogZ = new JButton("Jog Z");
		btnJogZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isp = Integer.parseInt(Velocity.getText());
				if (isp <= 500) isp = 500;
				if (isp >= 5000) isp = 5000;
				boolean dir = chckbxClockwise.isSelected();
				String s = ((dir) ? "CJOG Z, -" : "CJOG Z, ") + isp;
//				owner.getCommPanel().writeString(s);
				writeString(s);
			}
		});
		btnJogZ.setBounds(26, 182, 89, 23);
		add(btnJogZ);
		
		btnGrip = new JButton("Grip");
		btnGrip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean dir = chckbxClockwise.isSelected();
				String s = (dir) ? "OPEN" : "CLOSE";
//				owner.getCommPanel().writeString(s);
				writeString(s);
			}
		});
		btnGrip.setBounds(26, 206, 89, 23);
		add(btnGrip);
		
		JButton btnHalt = new JButton("Halt");
		btnHalt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				owner.getCommPanel().writeString("HALT");
				writeString("HALT");
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
		
		frame = new PalmFrame(owner);
		btnLeapCtrl = new JButton("Leap Ctrl");
		btnLeapCtrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!frame.isVisible()) {
					frame.setVisible(true);
				}
				else {
					frame.setVisible(false);
				}
			}
		});
		btnLeapCtrl.setBounds(26, 324, 89, 23);
		add(btnLeapCtrl);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = (String)CommConnectionPanel.portList.getSelectedValue();
				commconnection.connect(port,  (CommPortIdentifier)CommConnectionPanel.portMap.get(port));
				if (commconnection.getconnected()) {
					commconnection.initIOStream();
					commconnection.initListener();
		            System.out.println("comm port connected");
				}
				else {
					System.out.println("comm port not connected");
				}
			}
		});
		btnConnect.setBounds(26, 28, 89, 23);
		add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commconnection.disconnect();
			}
		});
		btnDisconnect.setBounds(26, 51, 89, 23);
		add(btnDisconnect);

	}
}
