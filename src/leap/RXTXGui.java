package leap;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import GantryClaw.ClawGui;


public class RXTXGui extends JFrame {

	private JPanel contentPane;
	private CommConnectionPanel controlpanel;
	private GantryPanel gantrypanel;
	private PlateCranePanel platecranepanel;
	private ImagePanel imagePanel;
	private CameraPanel cameraPanel;
	private JButton btnClawInterface;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RXTXGui frame = new RXTXGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public RXTXGui getGui(){
		return this;
	}
	public ImagePanel getImagePanel()
	{
		return imagePanel;
	}
	
	/**
	 * Create the frame.
	 */
	public RXTXGui() {
		setTitle("RXTX GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 507, 855);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		controlpanel = new CommConnectionPanel(this);
		controlpanel.setBounds(330, 11, 150, 250);
		contentPane.add(controlpanel);
				
		gantrypanel = new GantryPanel(this);
		gantrypanel.setBounds(10, 11, 150, 399);
		contentPane.add(gantrypanel);
		
		platecranepanel = new PlateCranePanel(this);
		platecranepanel.setBounds(170, 11, 150, 399);
		contentPane.add(platecranepanel);
		
		imagePanel = new ImagePanel(this);
		imagePanel.setBounds(10, 421, 470, 302);
		contentPane.add(imagePanel);
		
		cameraPanel = new CameraPanel(null, this);
		cameraPanel.setBounds(330, 272, 150, 138);
		contentPane.add(cameraPanel);
		
		btnClawInterface = new JButton("Claw Interface");
		btnClawInterface.setBounds(279, 726, 201, 37);
		contentPane.add(btnClawInterface);
		
		

		btnClawInterface.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getGui().setVisible(false);
				ClawGui CG = new ClawGui(getGui(),gantrypanel.getComm());
				CG.setVisible(true);
				
			}
		});
	}
}
