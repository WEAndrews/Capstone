package GantryClaw;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import GantryClaw.ClawGui;
import javax.swing.JCheckBox;
import leap.RXTXGui;

//This class is a duplicate of the leap cameraPanel class and thus
//I don't know who created it.
//Jan-Apr 2018
//
//This class was created to display the buttons for the camera
//and also to give functionality to the buttons 

public class ClawCameraPanel extends JPanel {
	private JButton btnGetCamera;
	private JButton btnStart;
	private JLabel lblCamera;
	private RXTXGui ownerRx;
	private ClawGui owner;
	private boolean running = false;
	private JCheckBox saveSequence;

	public ClawCameraPanel(ClawGui _o, RXTXGui _p) {
		owner = _o;
		ownerRx = _p;

		setLayout(null);

		btnGetCamera = new JButton("Get");
		btnGetCamera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (owner != null) {
					owner.getImagePanel().setFrame();
					owner.getImagePanel().setCap();

					try {
						Thread.sleep(500);
					} catch (InterruptedException ie) {
						// TODO Auto-generated catch block
						ie.printStackTrace();
					} // 0.5 sec of a delay. This is not obvious but its
						// necessary
						// as the camera simply needs time to initialize
						// itself
					if (!owner.getImagePanel().getCap().isOpened()) {
						System.out.println("Did not connect to camera");
					} else {
						System.out.println("found webcam: "
								+ owner.getImagePanel().getCap().toString());
					}
				}else{
					ownerRx.getImagePanel().setFrame();
					ownerRx.getImagePanel().setCap();

				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					// TODO Auto-generated catch block
					ie.printStackTrace();
				} // 0.5 sec of a delay. This is not obvious but its
					// necessary
					// as the camera simply needs time to initialize
					// itself
				if (!ownerRx.getImagePanel().getCap().isOpened()) {
					System.out.println("Did not connect to camera");
				} else {
					System.out.println("found webcam: "
							+ ownerRx.getImagePanel().getCap().toString());
				}
			}
			}
		});
		btnGetCamera.setBounds(25, 26, 89, 23);
		add(btnGetCamera);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (running == false) {
					btnStart.setText("Stop");
					if(owner != null){
						owner.getImagePanel().getAnimationTimer().start();
						running = true;
					}else{
						ownerRx.getImagePanel().getAnimationTimer().start();
						running = true;
					}
				} else {
					btnStart.setText("Start");
					if(owner != null){
						owner.getImagePanel().getAnimationTimer().stop();
						running = false;
					}else{
						ownerRx.getImagePanel().getAnimationTimer().stop();
						running = false;
					}
				}
			}
		});
		btnStart.setBounds(25, 53, 89, 23);
		add(btnStart);

		lblCamera = new JLabel("Camera");
		lblCamera.setBounds(50, 11, 46, 14);
		add(lblCamera);

		saveSequence = new JCheckBox("Save seq");
		saveSequence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.getImagePanel().setSave(saveSequence.isSelected());
			}
		});
		saveSequence.setBounds(25, 87, 79, 23);
		add(saveSequence);

		JButton btnNewButton = new JButton("Busy?");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println(owner.getGantry().getBusy());
			}
		});
		btnNewButton.setBounds(25, 117, 89, 23);
		add(btnNewButton);

	}
}
