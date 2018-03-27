package GantryClaw;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import leap.CameraPanel;
import leap.CommConnection;

//Created by Wilbert Andrews
//Jan-Apr 2018
//
//This class was created to house all the panels in one JFrame

public class ClawGui extends JFrame {

	private JPanel contentPane;
	private CommConnection commconnection;
	private CameraPanel cameraPanel;
	private ClawImagePanel imagePanel;
	private int Increment = 5;
	private JFrame _o;
	private JPanel ClawPanel;
	private ClawGuiPanel CGP;

	public ClawGui(final JFrame _o, CommConnection _c) {
		/******************* setting dimensions for the JFrame and JPanels *************************/
		this._o = _o;
		commconnection = _c;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1387, 1030);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel CameraPanel = new JPanel();
		CameraPanel.setBounds(397, 10, 962, 954);
		CameraPanel.setLayout(null);

		ClawPanel = new JPanel();
		ClawPanel.setBounds(0, 10, 395, 954);
		ClawPanel.setLayout(null);

		imagePanel = new ClawImagePanel(this);
		imagePanel.setBounds(0, 0, 936, 527);

		cameraPanel = new CameraPanel(this, null);
		cameraPanel.setBounds(0, 530, 497, 199);
		CGP = new ClawGuiPanel(this, commconnection);
		CGP.setBounds(0, 0, 395, 954);

		/******************* Adding JPanels to the JFrame *************************/

		CameraPanel.add(cameraPanel);
		CameraPanel.add(imagePanel);
		contentPane.add(CameraPanel);
		contentPane.add(ClawPanel);
		ClawPanel.add(CGP);
	}

	/******************* Function Calls *************************/
	public JFrame getCallingJFrame() {
		return _o;
	}

	public void setIncrement(int x) {
		this.Increment = x;
	}

	public int getIncrement() {
		return this.Increment;
	}

	public ClawGuiPanel getPanel() {
		return CGP;
	}

	public ClawImagePanel getImagePanel() {
		return imagePanel;
	}
}
