package Controller;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;


public class CameraPanel extends JPanel {
	private JButton btnGetCamera;
	private JButton btnStart;
	private JLabel lblCamera;
	private RXTXGui owner;
	private boolean running = false;
	
	/**
	 * Create the panel.
	 */
	public CameraPanel(RXTXGui _o) {
		owner = _o;
		
		setLayout(null);
		
		btnGetCamera = new JButton("Get");
		btnGetCamera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
				} 
				else {
					System.out.println("found webcam: " + owner.getImagePanel().getCap().toString());
				}
			}
		});
		btnGetCamera.setBounds(25, 26, 89, 23);
		add(btnGetCamera);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (running == false) {
					btnStart.setText("Stop");;
					owner.getImagePanel().getAnimationTimer().start();
					running = true;
				}
				else {
					btnStart.setText("Start");
					owner.getImagePanel().getAnimationTimer().stop();
					running = false;
				}
			}
		});
		btnStart.setBounds(25, 53, 89, 23);
		add(btnStart);
		

		
		lblCamera = new JLabel("Camera");
		lblCamera.setBounds(50, 11, 46, 14);
		add(lblCamera);

	}

}
