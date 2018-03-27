package leap;
import gnu.io.CommPortIdentifier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JCheckBox;



import java.awt.Font;


public class GantryPanel extends JPanel {
	private JTextField X;
	private JTextField Y;
	private JTextField Z;
	private JTextField Velocity;
	private JCheckBox chckbxIncremental;
	private JButton btnRemote; 
	private JButton btnServoOn;
	private JButton btnServoOff; 
	private JButton btnHome;
	private JButton btnVelocity;
	private JButton btnMove;
	private JLabel lblGantry;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JButton btnLeapCtrl;
	private CommConnection CC;
	private String port;
	
    private CommConnection commconnection;
    private boolean getPos = false;
    private JTextField xPos;
    private JTextField yPos;
    private JTextField zPos;
    private JTextField statusField;
    private PalmFrame frame;
    
    private boolean busy;

	/**
	 * Create the panel.
	 */
	public GantryPanel(final RXTXGui _o) {
		setLayout(null);
		commconnection = new CommConnection(this);
		
		btnRemote = new JButton("Remote");
		btnRemote.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnRemote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte remote[] = {(byte)0x8F, 0x00};
				writeStringGnoCR(remote);
				setStatus(2);
				busy = false;
			}
		});
		btnRemote.setBounds(31, 26, 89, 20);
		add(btnRemote);
		
		btnServoOn = new JButton("Servo On");
		btnServoOn.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnServoOn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeStringG("SVON");
			}
		});
		btnServoOn.setBounds(31, 51, 89, 20);
		add(btnServoOn);
		
		btnServoOff = new JButton("Servo Off");
		btnServoOff.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnServoOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeStringG("SVOF");
			}
		});
		btnServoOff.setBounds(31, 75, 89, 20);
		add(btnServoOff);
		
		btnHome = new JButton("Home");
		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeStringG("HOM");
			}
		});
		btnHome.setBounds(31, 100, 89, 20);
		add(btnHome);
		
		btnVelocity = new JButton("Velocity");
		btnVelocity.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int v = Integer.parseInt(Velocity.getText());
				String s = "VEL " + v;
				writeStringG(s);
			}
		});
		btnVelocity.setBounds(31, 123, 89, 20);
		add(btnVelocity);
		
		btnMove = new JButton("Move");
		btnMove.setFont(new Font("Tahoma", Font.PLAIN, 9));
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
				writeStringG(s);
			}
		});
		btnMove.setBounds(31, 173, 89, 20);
		add(btnMove);
		
		X = new JTextField();
		X.setToolTipText("X position");
		X.setText("0");
		X.setBounds(31, 197, 33, 20);
		add(X);
		X.setColumns(10);
		
		Y = new JTextField();
		Y.setToolTipText("Y position");
		Y.setText("0");
		Y.setBounds(31, 224, 33, 20);
		add(Y);
		Y.setColumns(10);
		
		Z = new JTextField();
		Z.setToolTipText("Z position");
		Z.setText("0");
		Z.setBounds(31, 255, 33, 20);
		add(Z);
		Z.setColumns(10);
		
		Velocity = new JTextField();
		Velocity.setToolTipText("velocity (0..100)");
		Velocity.setText("10");
		Velocity.setBounds(31, 148, 62, 20);
		add(Velocity);
		Velocity.setColumns(10);
		
		lblGantry = new JLabel("Gantry");
		lblGantry.setBounds(50, 11, 46, 14);
		add(lblGantry);
		
		chckbxIncremental = new JCheckBox("Incremental");
		chckbxIncremental.setFont(new Font("Tahoma", Font.PLAIN, 9));
		chckbxIncremental.setBounds(31, 279, 97, 23);
		add(chckbxIncremental);
		
		JButton btnGetPosition = new JButton("Get Position");
		btnGetPosition.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnGetPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = "POS";
				writeStringG(s);
			}
		});
		btnGetPosition.setBounds(31, 309, 89, 20);
		add(btnGetPosition);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				port = (String)CommConnectionPanel.portList.getSelectedValue();
				commconnection.connect(port,  (CommPortIdentifier)CommConnectionPanel.portMap.get(port));
				if (commconnection.getconnected()) {
					commconnection.initIOStream();
					commconnection.initListener();
		            System.out.println("comm port connected");
		            setStatus(1);
				}
				else {
					System.out.println("comm port not connected");
				}
			}
		});
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnConnect.setBounds(31, 333, 89, 20);
		add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commconnection.disconnect();
	            setStatus(0);
			}
		});
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnDisconnect.setBounds(31, 358, 89, 20);
		add(btnDisconnect);
		
		xPos = new JTextField();
		xPos.setEditable(false);
		xPos.setBounds(74, 197, 46, 20);
		add(xPos);
		xPos.setColumns(10);
		
		yPos = new JTextField();
		yPos.setEditable(false);
		yPos.setBounds(74, 224, 46, 20);
		add(yPos);
		yPos.setColumns(10);
		
		zPos = new JTextField();
		zPos.setEditable(false);
		zPos.setBounds(74, 255, 46, 20);
		add(zPos);
		zPos.setColumns(10);
		
		statusField = new JTextField();
		statusField.setEditable(false);
		statusField.setBounds(31, 385, 89, 20);
		add(statusField);
		statusField.setColumns(10);
		
		frame = new PalmFrame(_o);
		btnLeapCtrl = new JButton("Leap Ctrl");
		btnLeapCtrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!frame.isVisible()) {
					frame.setVisible(true);
				}
				else {
					frame.setVisible(false);;
				}
			}
		});
		btnLeapCtrl.setBounds(31, 411, 89, 23);
		add(btnLeapCtrl);

		setStatus(0);
	}
	
	public void setStatus (int value)
	{
		String s = "";
		switch (value) {
			case 0: s = "Disconnected";
					busy = false;
			break;
			case 1: s = "Connected";
			        busy = false;
			break;
			case 2: s = "Remote";
			        busy = false;
			break;
			case 3: s = "Busy";
      			    busy = true;
			break;
		}
		
		statusField.setText(s);
	}
	
	public boolean getBusy ()
	{
		return busy;
	}
	
    public void writeString(String data)
    {
    	byte[] bdata = data.getBytes();
    	for (int i = 0; i < bdata.length; ++i) {
    		commconnection.writeData(bdata[i]);
    	}
    	commconnection.writeData(0x0D);
    	commconnection.writeData(0x0A);
    	
    }
    
    public void writeStringG(String data)
    {
    	if (data.equals("POS")) getPos = true;
    	else getPos = false;
    	
    	byte[] bdata = data.getBytes();
    	for (int i = 0; i < bdata.length; ++i) {
    		commconnection.writeData(bdata[i]);
    	}
    	commconnection.writeData(0x0D);
    	
    }

    public void writeStringGnoCR(byte[] bdata)
    {
    	for (int i = 0; i < bdata.length; ++i) {
    		commconnection.writeData(bdata[i]);
    	}
    }
    
    public CommConnection getComm(){
    	return commconnection;
    }
    
	public void setPositionFields(double x, double y, double z)
	{
		xPos.setText(Double.toString(x));
		yPos.setText(Double.toString(y));
		zPos.setText(Double.toString(z));
	}
}
