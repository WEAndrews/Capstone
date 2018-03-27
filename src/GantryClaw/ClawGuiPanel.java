package GantryClaw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import leap.CommConnection;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Created by Wilbert Andrews
//Jan-Apr 2018
//
//This class was created to house all the buttons and
//functionallity for the GUI panel.

public class ClawGuiPanel extends JPanel {

	private JTextField tfX;
	private JTextField tfY;
	private JTextField tfZ;
	private JTextField tfVel;
	private CommConnection commconnection;
	private JPanel ControlPanel;
	private int Increment = 5;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ClawGui clawGui;
	private int sliderMinValue;
	private int sliderMaxValue;
	private JSlider MaxSlider;

	public ClawGuiPanel(final ClawGui _o, CommConnection _c) {
		/******************* setting dimensions for the JPanel and its Components *************************/
		clawGui = _o;
		commconnection = _c;
		setBounds(0, 0, 395, 954);
		setLayout(null);
		ControlPanel = new JPanel();
		ControlPanel.setBounds(0, 10, 395, 954);
		ControlPanel.setLayout(null);

		JButton btnMoveNorth = new JButton("Inc. Move North");
		btnMoveNorth.setBounds(22, 24, 181, 37);

		JButton btnMoveSouth = new JButton("Inc. Move South");
		btnMoveSouth.setBounds(22, 71, 181, 37);

		JButton btnMoveWest = new JButton("Inc. Move West");
		btnMoveWest.setBounds(22, 118, 181, 37);

		JButton btnMoveEast = new JButton("Inc. Move East");
		btnMoveEast.setBounds(22, 166, 181, 37);

		JButton btnMoveUp = new JButton("Move Head Up");
		btnMoveUp.setBounds(22, 301, 233, 37);

		JButton btnMoveDown = new JButton("Move Head Down");
		btnMoveDown.setBounds(22, 344, 233, 37);

		tfX = new JTextField();
		tfX.setText("0.0");
		tfX.setBounds(94, 399, 109, 35);
		tfX.setColumns(10);

		tfY = new JTextField();
		tfY.setText("0.0");
		tfY.setColumns(10);
		tfY.setBounds(94, 458, 109, 35);

		tfZ = new JTextField();
		tfZ.setText("0.0");
		tfZ.setColumns(10);
		tfZ.setBounds(94, 522, 109, 35);

		JLabel lblX = new JLabel("x");
		lblX.setBounds(32, 402, 40, 29);

		JLabel lblY = new JLabel(" y");
		lblY.setBounds(22, 461, 50, 29);

		JLabel lblZ = new JLabel(" z");
		lblZ.setBounds(22, 525, 50, 29);

		JButton btnMoveToCoordinate = new JButton("Move To Coordinate");
		btnMoveToCoordinate.setBounds(22, 581, 278, 37);

		JButton btnFindRed = new JButton("Find Red");
		btnFindRed.setBounds(22, 645, 181, 37);

		JButton btnFindGreen = new JButton("Find Green");
		btnFindGreen.setBounds(22, 699, 181, 37);

		JButton btnFindBlue = new JButton("Find Blue");
		btnFindBlue.setBounds(22, 749, 181, 37);

		JButton btnCloseClawInterface = new JButton("Close Claw Interface");
		btnCloseClawInterface.setBounds(22, 863, 181, 37);

		final JLabel lblXloc = new JLabel(tfX.getText());
		lblXloc.setBounds(240, 402, 104, 29);

		final JLabel lblYloc = new JLabel(tfY.getText());
		lblYloc.setBounds(240, 461, 104, 29);

		final JLabel lblZloc = new JLabel(tfZ.getText());
		lblZloc.setBounds(240, 525, 104, 29);

		JPanel ClawCameraPanel = new JPanel();
		ClawCameraPanel.setBounds(397, 10, 962, 954);
		ClawCameraPanel.setLayout(null);

		tfVel = new JTextField();
		tfVel.setText("10");
		tfVel.setBounds(225, 86, 154, 35);
		tfVel.setColumns(10);

		JButton btnVelocity = new JButton("Velocity");
		btnVelocity.setBounds(225, 24, 155, 37);

		JButton btnHome = new JButton("Home");
		btnHome.setBounds(22, 234, 155, 37);

		JButton btnClawClose = new JButton("Claw Close");
		btnClawClose.setBounds(224, 699, 155, 37);

		JButton btnClawOpen = new JButton("Claw Open");
		btnClawOpen.setBounds(225, 645, 155, 37);

		JButton btnStopFind = new JButton("Stop Find");
		btnStopFind.setBounds(22, 807, 181, 37);

		JRadioButton rdbtnFive = new JRadioButton("5");
		rdbtnFive.setSelected(true);
		buttonGroup.add(rdbtnFive);
		rdbtnFive.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnFive.setBounds(225, 167, 75, 35);

		JLabel lblIncrements = new JLabel("Increments");
		lblIncrements.setBounds(225, 129, 149, 26);

		JRadioButton rdbtnTwenty = new JRadioButton("20");
		buttonGroup.add(rdbtnTwenty);
		rdbtnTwenty.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnTwenty.setBounds(225, 199, 75, 35);

		JRadioButton rdbtnHundred = new JRadioButton("100");
		buttonGroup.add(rdbtnHundred);
		rdbtnHundred.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnHundred.setBounds(225, 236, 75, 35);

		final JSlider MinSlider = new JSlider();
		MinSlider.setToolTipText("Adjusts the color the program searches for");
		MinSlider.setValue(0);
		MinSlider.setMaximum(159);
		MinSlider.setBounds(225, 749, 154, 26);

		final JLabel lblMinHue = new JLabel("Min Hue= 0");
		sliderMinValue = MinSlider.getValue();
		lblMinHue.setBounds(240, 773, 134, 26);

		JButton btnFindHueBar = new JButton("Find HueBar");
		btnFindHueBar.setBounds(225, 864, 154, 35);

		MaxSlider = new JSlider();
		MaxSlider.setValue(0);
		MaxSlider.setToolTipText("Adjusts the color the program searches for");
		MaxSlider.setMaximum(159);
		MaxSlider.setBounds(224, 807, 154, 26);

		final JLabel lblMaxHue = new JLabel("Max Hue = 0");
		lblMaxHue.setBounds(240, 834, 134, 26);

		/******************* setting functionality to JPanel's Components *************************/
		btnMoveNorth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move North");
				String s;
				double x = Double.parseDouble(lblXloc.getText());
				double y = Double.parseDouble(lblYloc.getText())
						- getIncrement();
				double z = Double.parseDouble(lblZloc.getText());

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnMoveSouth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move South");
				String s;
				double x = Double.parseDouble(lblXloc.getText());
				double y = Double.parseDouble(lblYloc.getText())
						+ getIncrement();
				double z = Double.parseDouble(lblZloc.getText());

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnMoveEast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move East");
				String s;
				double x = Double.parseDouble(lblXloc.getText())
						- getIncrement();
				double y = Double.parseDouble(lblYloc.getText());
				double z = Double.parseDouble(lblZloc.getText());

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnMoveWest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move West");
				String s;
				double x = Double.parseDouble(lblXloc.getText())
						+ getIncrement();
				double y = Double.parseDouble(lblYloc.getText());
				double z = Double.parseDouble(lblZloc.getText());

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move Home");
				lblXloc.setText("0.0");
				lblYloc.setText("0.0");
				lblZloc.setText("0.0");
				writeStringG("HOM");
			}
		});

		btnVelocity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Set Velocity");
				int v = Integer.parseInt(tfVel.getText());
				String s = "VEL " + v;
				writeStringG(s);
			}
		});

		btnMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move Head Up");
				String s;
				double x = Double.parseDouble(lblXloc.getText());
				double y = Double.parseDouble(lblYloc.getText());
				double z = Double.parseDouble(lblZloc.getText())
						- getIncrement();

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move Head Down");
				String s;
				double x = Double.parseDouble(lblXloc.getText());
				double y = Double.parseDouble(lblYloc.getText());
				double z = Double.parseDouble(lblZloc.getText())
						+ getIncrement();

				lblZloc.setText(String.valueOf(z));
				lblXloc.setText(String.valueOf(x));
				lblYloc.setText(String.valueOf(y));

				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnMoveToCoordinate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move To Coordinate");
				lblXloc.setText(tfX.getText());
				lblYloc.setText(tfY.getText());
				lblZloc.setText(tfZ.getText());
				double x = Double.parseDouble(tfX.getText());
				double y = Double.parseDouble(tfY.getText());
				double z = Double.parseDouble(tfZ.getText());
				String s;
				s = "MOV " + "X" + x + " Y" + y + " Z" + z;
				System.out.println(s);
				writeStringG(s);
			}
		});

		btnFindRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move To Red");
				clawGui.getImagePanel().findRed();
			}

		});

		btnFindBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move To Blue");
				clawGui.getImagePanel().findBlue();
			}
		});

		btnFindGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Move To Green");
				clawGui.getImagePanel().findGreen();
			}
		});

		btnCloseClawInterface.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_o.getCallingJFrame().setVisible(true);
				_o.dispose();
			}
		});

		btnStopFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Stop Find");
				clawGui.getImagePanel().stopFind();
			}
		});

		rdbtnHundred.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setIncrement(100);
				System.out.println("set 100");
			}
		});

		rdbtnTwenty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setIncrement(20);
				System.out.println("set 20");
			}
		});

		rdbtnFive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setIncrement(5);
				System.out.println("set 5");
			}
		});

		MinSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblMinHue.setText("Hue Bar " + MinSlider.getValue());
				sliderMinValue = MinSlider.getValue();
			}
		});

		MaxSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblMaxHue.setText("Hue Bar " + MaxSlider.getValue());
				sliderMaxValue = MaxSlider.getValue();
			}
		});

		btnFindHueBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_o.getImagePanel().setHueBar(true);
			}
		});

		/******************* Adding Components to JPanel *************************/
		ControlPanel.add(tfVel);
		ControlPanel.add(lblZloc);
		ControlPanel.add(lblYloc);
		ControlPanel.add(lblXloc);
		ControlPanel.add(btnCloseClawInterface);
		ControlPanel.add(btnFindBlue);
		ControlPanel.add(btnFindGreen);
		ControlPanel.add(btnFindRed);
		ControlPanel.add(btnMoveToCoordinate);
		ControlPanel.add(lblZ);
		ControlPanel.add(lblY);
		ControlPanel.add(lblX);
		ControlPanel.add(tfZ);
		ControlPanel.add(tfX);
		ControlPanel.add(btnMoveDown);
		ControlPanel.add(btnMoveUp);
		ControlPanel.add(btnMoveEast);
		ControlPanel.add(btnMoveWest);
		ControlPanel.add(btnMoveNorth);
		ControlPanel.add(btnMoveSouth);
		ControlPanel.add(btnVelocity);
		ControlPanel.add(btnHome);
		ControlPanel.add(btnClawOpen);
		ControlPanel.add(btnClawClose);
		ControlPanel.add(btnStopFind);
		ControlPanel.add(rdbtnFive);
		ControlPanel.add(lblIncrements);
		ControlPanel.add(rdbtnTwenty);
		ControlPanel.add(rdbtnHundred);
		ControlPanel.add(tfY);
		ControlPanel.add(MinSlider);
		ControlPanel.add(lblMinHue);
		ControlPanel.add(btnFindHueBar);
		ControlPanel.add(MaxSlider);
		ControlPanel.add(lblMaxHue);
		this.add(ControlPanel);
	}

	/******************* Functions *************************/
	public void setIncrement(int x) {
		this.Increment = x;
	}

	public int getIncrement() {
		return this.Increment;
	}

	public void writeStringG(String data) {
		byte[] bdata = data.getBytes();
		for (int i = 0; i < bdata.length; ++i) {
			commconnection.writeData(bdata[i]);
		}
		commconnection.writeData(0x0D);
	}

	public int getSliderMinValue() {
		return sliderMinValue;
	}

	public int getSliderMaxValue() {
		return sliderMaxValue;
	}

	public void setSliderMaxValue(int _h) {
		MaxSlider.setValue(_h);
	}
}