package leap;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class PalmPanel extends JPanel {
    private LeapListener leapListener = null;
    private Controller leapController = null;
	private Timer palmTracker = null;	
	private PalmFrame owner = null;
	
	/**
	 * Create the panel.
	 */
	public PalmPanel(PalmFrame o) {
		owner = o;
		
		setBackground(Color.BLACK);
		setSize(new Dimension(300, 300));
		
		
        // Create a sample listener and controller
        leapListener = new LeapListener();
        leapController = new Controller();
        
        // Have the sample listener receive events from the controller
        leapController.addListener(leapListener);

		palmTracker = new Timer(5, 
				// -- ActionListener for the timer event
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						repaint();
					}
					
				}
					
			);


	}
	
	public void track (boolean s)
	{
		if (s) {
			palmTracker.start();
		}
		else {
			palmTracker.stop();
		}
	}
	public void closing ()
	{
        // Remove the sample listener when done
        leapController.removeListener(leapListener);

	}
	
	
	public void paint (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		float leftPalmHeight = height - ((leapListener.getPalmHeight(false) - 25.0F) / 600.0F) * 300.0F;
		g2d.setColor(Color.yellow);
		g2d.drawLine(0,  (int)leftPalmHeight,  width,  (int)leftPalmHeight);
	    double leftPalmNormal = leapListener.getPalmNormal(false);    
	    g2d.drawString(Integer.toString((int)leftPalmNormal), 5, 20);


		float rightPalmHeight = height - ((leapListener.getPalmHeight(true) - 25.0F) / 600.0F) * 300.0F;
		g2d.setColor(Color.green);
		g2d.drawLine(0,  (int)rightPalmHeight,  width,  (int)rightPalmHeight);
	    double rightPalmNormal = leapListener.getPalmNormal(true);    
	    g2d.drawString(Integer.toString((int)rightPalmNormal), 5, height - 20);
	    
	    System.out.println((int)leftPalmHeight + ", " + (int)rightPalmHeight);
	    
	    
	    if (owner.getOwnerPanel() instanceof RXTXGui) {
	    	SonarPanel pcp = owner.getOwnerPanel().getStampPanel();
	    	if (rightPalmHeight > height / 2) {
//				String s = ((leftPalmNormal < 90) ? "CJOG P, -" : "CJOG P, ") + 300;
//				System.out.println(leftPalmNormal + "\t" + s);
//	    		pcp.writeString(s);
	    		pcp.commconnection.receiver = 1;
	    		pcp.writeString("!A");
	    	}
	    	else {
//	    		pcp.writeString("HALT");
	    	}
	    	if (leftPalmHeight > height / 2) {
//				String s = ((leftPalmNormal < 90) ? "CJOG P, -" : "CJOG P, ") + 300;
//				System.out.println(leftPalmNormal + "\t" + s);
//	    		pcp.writeString(s);
	    		pcp.writeString("!B");
	    	}
	    	else {
//	    		pcp.writeString("HALT");
	    	}
	    }
		
	}

}
