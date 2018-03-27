package leap;
import gnu.io.CommPortIdentifier;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class CommConnectionPanel extends JPanel {
	
	private JButton getports;
	private RXTXGui owner;
	private JButton connect;
	private JButton disconnect;
	public static JList<String> portList;
	private JScrollBar scrollPortList;
	private JLabel lblCommPorts;
    private JTextField receivedData;
    private boolean textReset = false;
    private boolean getPos = false;
    private String posString = "";
    private JTextField posData;

    private CommConnection commconnection;
    
	// -- communications items
    //for containing the ports that will be found
    private Enumeration ports = null;
    
    //map the port names to CommPortIdentifiers
    public static HashMap portMap = new HashMap();


    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    
	
	/**
	 * Create the panel.
	 */
	public CommConnectionPanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);
		commconnection = new CommConnection(this);
		
		getports = new JButton("Get Ports");
		getports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<String> ports = searchForPorts();
				portList.setListData(ports);
			}
		});
		getports.setBounds(23, 26, 100, 23);
		add(getports);
		
		connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = (String)portList.getSelectedValue();
				commconnection.connect(port,  (CommPortIdentifier)portMap.get(port));
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
		connect.setBounds(23, 160, 100, 23);
		add(connect);
		
		disconnect = new JButton("Disconnect");
		disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commconnection.disconnect();
			}
		});
		disconnect.setBounds(23, 194, 100, 23);
		add(disconnect);

		portList = new JList<String>();
		portList.setBounds(23, 53, 100, 96);
		add(portList);
		
		lblCommPorts = new JLabel("Comm Ports");
		lblCommPorts.setBounds(44, 11, 74, 14);
		add(lblCommPorts);
		
		receivedData = new JTextField();
		receivedData.setBounds(23, 228, 100, 20);
		add(receivedData);
		receivedData.setColumns(10);
		
		posData = new JTextField();
		posData.setBounds(23, 259, 100, 20);
		add(posData);
		posData.setColumns(10);
		
		
	}
	
	
	// -- Serial port communications message handlers

 	
	//search for all the serial ports
    //pre:  none
    //post: adds all the found ports to a combo box on the GUI
    public Vector<String> searchForPorts()
    {
    	Vector<String> vector = new Vector<String>();
    	
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
 //               System.out.println(curPort.getName());
                vector.add(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
        return vector;
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


}
