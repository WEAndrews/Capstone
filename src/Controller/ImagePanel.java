package Controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


public class ImagePanel extends JPanel {
	
	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}

	private RXTXGui owner;
	private Timer animationTimer = null;	
	private boolean running = false;
	private Mat frame = null;
	private VideoCapture cap = null;
	private boolean haveImage = false;
	private BufferedImage bufferedImage;
	
	public Timer getAnimationTimer()
	{
		return animationTimer;
	}

	public Mat getFrame()
	{
		return frame;
	}
	
	public void setFrame()
	{
		frame = new Mat();
	}
	
	public VideoCapture getCap()
	{
		return cap;
	}
	
	public void setCap()
	{
		cap = new VideoCapture(0);
	}
	
	/**
	 * Create the panel.
	 */
	public ImagePanel(RXTXGui _o) {
		owner = _o;

		setLayout(null);


		animationTimer = new Timer(10, 
				// -- ActionListener for the timer event
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						// -- grab frame
						cap.retrieve(frame);

						// -- detect corner features
				        MatOfKeyPoint keypoints = new MatOfKeyPoint();
						FeatureDetector harris = FeatureDetector.create(FeatureDetector.HARRIS);						
						harris.detect(frame, keypoints);
						
						try {						
							// -- Convert OpenCV Mat image to Java BufferedImage
							MatOfByte matOfByte = new MatOfByte();
							Highgui.imencode(".png", frame, matOfByte); 
							byte[] byteArray = matOfByte.toArray();
							InputStream in = new ByteArrayInputStream(byteArray);
							bufferedImage = ImageIO.read(in);

							// -- paint corners onto image for display
							KeyPoint kp[] = keypoints.toArray();
							Graphics2D g2d = (Graphics2D)bufferedImage.getGraphics();
							g2d.setColor(Color.RED);
							for (int i = 0; i < Math.min(kp.length, 100); ++i) {
								int x = (int)(kp[i].pt.x);
								int y = (int)(kp[i].pt.y);
								g2d.fillOval(x-2,  y-2, 5, 5);
								// bufferedImage.setRGB(x,  y,  255<<16);	
							}
							
							haveImage = true;
							repaint();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
					
			);
		
	
	}
	
	public void paint(Graphics g)
	{
		// -- the base class paintComponent(g) method ensures 
		//    the drawing area will be cleared properly. Do not
		//    modify any attributes of g prior to sending it to
		//    the base class
		super.paintComponent(g);
		
		// -- for legacy reasons the parameter comes in as type Graphics
		//    but it is really a Graphics2D object. Cast it up since the
		//    Graphics2D class is more capable
		Graphics2D g2d = (Graphics2D)g;

		// -- get the height and width of the JPanel drawing area
		int width = this.getWidth();
		int height = this.getHeight();
		
		if (haveImage) {
			// --                 destination coords, source image coordinates
	    	g2d.drawImage(bufferedImage, 0, 0, width, height, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);			

		}
		else {
			Random rn = new Random();
			rn.setSeed(432);
			BufferedImage bi = new BufferedImage(512, 256, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < 256; ++i) {
				for (int j = 0; j < 512; ++j) {
	    			int pixel =	(rn.nextInt() % 256 << 16) | (rn.nextInt() % 256 << 8) | (rn.nextInt() % 256);
	    			bi.setRGB(j, i, pixel);
					
				}
			}
	    	g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), 0, 0, bi.getWidth(), bi.getHeight(), null);			
		}
		
	}


}
