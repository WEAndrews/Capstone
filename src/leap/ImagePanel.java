package leap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

public class ImagePanel extends JPanel {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	private JFrame owner;
	private Timer animationTimer = null;
	private boolean running = false;
	private Mat frame = null;
	private VideoCapture cap = null;
	private boolean haveImage = false;
	private BufferedImage bufferedImage;
	private int timePerUpdate = 100;
	private BufferedImage image;

	boolean savesequence = false;
	String basename = "c:/users/weand/desktop/CameraPNG/";
	String extension = ".png";
	int framecounter = 0;

	void setSave(boolean s) {
		savesequence = s;
	}

	public void setimage(BufferedImage newimage) {
		image = newimage;
		return;
	}

	public Timer getAnimationTimer() {
		return animationTimer;
	}

	public Mat getFrame() {
		return frame;
	}

	public void setFrame() {
		frame = new Mat();
	}

	public VideoCapture getCap() {
		return cap;
	}

	public void setCap() {
		cap = new VideoCapture(0);
	}

	/**
	 * Create the panel.
	 */
	public ImagePanel(JFrame _o) {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println(e.getX() + ", " + e.getY());
			}
		});
		owner = _o;

		setLayout(null);


		animationTimer = new Timer(timePerUpdate,
		// -- ActionListener for the timer event
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						// Code in this section will modify what is being shown
						// on the camera image
						// -- grab frame
						cap.read(frame);
						// Mat blurredImage = new Mat();
						// Mat mask = new Mat();
						// Mat morphOutput = new Mat();
						// Mat hsvImage = new Mat();
						// Imgproc.blur(frame, blurredImage, new Size(7, 7));
						// Imgproc.cvtColor(blurredImage, hsvImage,
						// Imgproc.COLOR_BGR2GRAY);
						//
						// Scalar minValues = new Scalar(150, 10, 10);
						// Scalar maxValues = new Scalar(255, 100, 100);
						//
						// String valuesToPrint = "Hue range: " +
						// minValues.val[0]
						// + "-" + maxValues.val[0]
						// + "\tSaturation range: " + minValues.val[1]
						// + "-" + maxValues.val[1] + "\tValue range: "
						// + minValues.val[2] + "-" + maxValues.val[2];
						// this.onFXThread(this.hsvValuesProp, valuesToPrint);
						//
						// // threshold HSV image to select tennis balls
						// Core.inRange(hsvImage, minValues, maxValues, mask);
						// // show the partial output
						// this.onFXThread(maskProp, this.mat2Image(mask));
						// -- detect corner features
						MatOfKeyPoint keypoints = new MatOfKeyPoint();
						FeatureDetector harris = FeatureDetector
								.create(FeatureDetector.HARRIS);
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
							Graphics2D g2d = (Graphics2D) bufferedImage
									.getGraphics();
							g2d.setColor(Color.RED);
							for (int i = 0; i < kp.length; ++i) {
								int x = (int) (kp[i].pt.x);
								int y = (int) (kp[i].pt.y);
								System.out.println("i = " + i );
								System.out.println("kp.x = " + x );
								System.out.println("kp.y = " + y );
								g2d.fillOval(x - 2, y - 2, 5, 5);
//								bufferedImage.setRGB(x, y, 255);
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
	public void setimagewithMat(Mat newimage) {
		image = this.matToBufferedImage(newimage);
		return;
	}

	public BufferedImage matToBufferedImage(Mat matrix) {  
		int cols = matrix.cols();  
		int rows = matrix.rows();  
		int elemSize = (int)matrix.elemSize();  
		byte[] data = new byte[cols * rows * elemSize];  
		int type;  
		matrix.get(0, 0, data);  
		switch (matrix.channels()) {  
		case 1:  
			type = BufferedImage.TYPE_BYTE_GRAY;  
			break;  
		case 3:  
			type = BufferedImage.TYPE_3BYTE_BGR;  
			// bgr to rgb  
			byte b;  
			for(int i=0; i<data.length; i=i+3) {  
				b = data[i];  
				data[i] = data[i+2];  
				data[i+2] = b;  
			}  
			break;  
		default:  
			return null;  
		}  
		BufferedImage image2 = new BufferedImage(cols, rows, type);  
		image2.getRaster().setDataElements(0, 0, cols, rows, data);  
		return image2;  
	}  
	
	public void paint(Graphics g) {
		// -- the base class paintComponent(g) method ensures
		// the drawing area will be cleared properly. Do not
		// modify any attributes of g prior to sending it to
		// the base class
		super.paintComponent(g);

		// -- for legacy reasons the parameter comes in as type Graphics
		// but it is really a Graphics2D object. Cast it up since the
		// Graphics2D class is more capable
		Graphics2D g2d = (Graphics2D) g;

		// -- get the height and width of the JPanel drawing area
		int width = this.getWidth();
		int height = this.getHeight();

		if (haveImage) {
			// -- destination coords, source image coordinates
			g2d.drawImage(bufferedImage, 0, 0, width, height, 0, 0,
					bufferedImage.getWidth(), bufferedImage.getHeight(), null);
			if (savesequence) {
				String seqno = "";
				if (framecounter < 10) {
					seqno = "000" + framecounter;
				} else if (framecounter < 100) {
					seqno = "00" + framecounter;
				} else if (framecounter < 1000) {
					seqno = "0" + framecounter;
				} else {
					seqno = "" + framecounter;
				}
				String fname = basename + seqno + extension;
				File outputfile = new File(fname);
				try {
					ImageIO.write(bufferedImage, "PNG", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				++framecounter;
			}

		} else {
			Random rn = new Random();
			rn.setSeed(432);
			BufferedImage bi = new BufferedImage(512, 256,
					BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < 256; ++i) {
				for (int j = 0; j < 512; ++j) {
					int pixel = (rn.nextInt() % 256 << 16)
							| (rn.nextInt() % 256 << 8) | (rn.nextInt() % 256);
					bi.setRGB(j, i, pixel);

				}
			}
			g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), 0, 0,
					bi.getWidth(), bi.getHeight(), null);
		}

	}

}
