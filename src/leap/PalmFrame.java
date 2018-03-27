package leap;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class PalmFrame extends JFrame {

	private JPanel contentPane;

	public PalmPanel palmpanel;
	public PalmControlPanel controlpanel;
	private RXTXGui owner;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PalmFrame frame = new PalmFrame(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public RXTXGui getOwnerPanel()
	{
		return owner;
	}
	
	/**
	 * Create the frame.
	 */
	public PalmFrame(RXTXGui o) {
		owner = o;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				palmpanel.closing();
				System.exit(0);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		palmpanel = new PalmPanel(this);
		contentPane.add(palmpanel, BorderLayout.CENTER);
		
		controlpanel = new PalmControlPanel(this);
		contentPane.add(controlpanel, BorderLayout.EAST);
	}

}
