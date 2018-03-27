package leap;
import gnu.io.CommPortIdentifier;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;


public class SonarPanel extends JPanel {

	private RXTXGui owner;
	private JTextField sonarField;
    public CommConnection commconnection;
 
    private int marker = 0;
    
    public String me = "I am the Stamp Panel";
    
	/**
	 * Create the panel.
	 */
	public SonarPanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);

		commconnection = new CommConnection(this);

		sonarField = new JTextField();
		sonarField.setEditable(false);
		sonarField.setBounds(30, 24, 86, 20);
		add(sonarField);
		sonarField.setColumns(10);
		
		JLabel lblSonar = new JLabel("SONAR");
		lblSonar.setHorizontalAlignment(SwingConstants.CENTER);
		lblSonar.setBounds(54, 11, 46, 14);
		add(lblSonar);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 9));
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
		btnConnect.setBounds(30, 46, 89, 20);
		add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commconnection.disconnect();
			}
		});
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnDisconnect.setBounds(30, 69, 89, 20);
		add(btnDisconnect);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (marker) {
				case 0:
					writeString("!A");
					break;
				case 1:
					writeString("!B");
					break;
				case 2:
					writeString("!C");
					break;
				}
				marker = (marker + 1) % 3;
			}
		});
		btnSend.setBounds(30, 95, 89, 23);
		add(btnSend);

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

	public void setSonarField(int v)
	{
		sonarField.setText(Integer.toString(v) + " cm");
	}
}
