package leap;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class PalmControlPanel extends JPanel {

	private PalmFrame owner = null;
	private boolean tracking = false;
	private JButton btnPushButton;
	
	/**
	 * Create the panel.
	 */
	public PalmControlPanel(PalmFrame o) {
		owner = o;
		
		setSize(new Dimension(175, 300));
		
		btnPushButton = new JButton("Start");
		btnPushButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!tracking) {
					owner.palmpanel.track(true);
					btnPushButton.setText("Stop");
					tracking = true;
				}
				else {
					owner.palmpanel.track(false);
					btnPushButton.setText("Start");
					tracking = false;
				}
					
			}
		});
		btnPushButton.setRolloverEnabled(true);
		add(btnPushButton);

	}

}
