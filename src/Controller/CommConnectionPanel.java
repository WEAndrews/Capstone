package Controller;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class CommConnectionPanel extends JPanel {
	
	private JButton getports;
	private RXTXGui owner;
	private JButton connect;
	private JButton disconnect;
	private JList<String> portList;
	private JScrollBar scrollPortList;
	private JLabel lblCommPorts;
	
	/**
	 * Create the panel.
	 */
	public CommConnectionPanel(RXTXGui _o) {
		owner = _o;
		setLayout(null);
		
		getports = new JButton("Get Ports");
		getports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<String> ports = owner.getrxtx().searchForPorts();
				portList.setListData(ports);
			}
		});
		getports.setBounds(23, 26, 100, 23);
		add(getports);
		
		connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = (String)portList.getSelectedValue();
				owner.getrxtx().connect(port);
				if (owner.getrxtx().getconnected()) {
		            owner.getrxtx().initIOStream();
		            owner.getrxtx().initListener();
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
				owner.getrxtx().disconnect();
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
		
		
	}
}
