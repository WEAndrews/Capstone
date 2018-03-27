package GantryClaw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

//Created by Wilbert Andrews
//Jan-Apr 2018
//
//This class was created to display the image to the user

public class ClawImagePanel extends JPanel {
	//test
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private boolean HueBarActive = false;
	private ClawGui owner;
	private Timer animationTimer = null;
	private Mat frame;
	private VideoCapture cap = null;
	private boolean haveImage = false;
	private BufferedImage bufferedImage;
	private int timePerUpdate = 10;
	private BufferedImage image;
	private int hueMin = 0;
	private int hueMax = 20;
	private boolean findColors = false;
	private int x = 0;
	private int y = 0;
	private int count = 0;
	private CameraThreading cam;

	boolean savesequence = false;
	String basename = "c:/users/weand/desktop/CameraPNG/";
	String extension = ".png";
	int framecounter = 0;
	
	public void setHueBar(boolean _h){
		findColors = true;
		HueBarActive = _h;
	}

	public ClawGui getOwner() {
		return owner;
	}

	public ClawImagePanel getCIP() {
		return this;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setSave(boolean s) {
		savesequence = s;
	}

	public BufferedImage getImage() {
		return image;
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

	public void findBlue() {
		findColors = true;
		HueBarActive = false;
		hueMin = 105;
		hueMax = 125;
	}

	public void findGreen() {
		findColors = true;
		HueBarActive = false;
		hueMin = 65;
		hueMax = 85;
	}

	public void findRed() {
		findColors = true;
		HueBarActive = false;
		hueMin = 150;
		hueMax = 179;
	}

	public void stopFind() {
		findColors = false;
		HueBarActive = false;
		cam.setStop(true);
		count = 0;
	}

	/**
	 * Create the panel.
	 */
	public ClawImagePanel(ClawGui _o) {
		owner = _o;
		setLayout(null);

		animationTimer = new Timer(timePerUpdate,
		// -- ActionListener for the timer event
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//reads in the frame to the videoCapture
						cap.read(frame);
						//if the users wants to use the Hue Slider Bar then this will be true
						if(HueBarActive){
							//gets the value from the slider bar as the min
							hueMin = owner.getPanel().getSliderMinValue();
							hueMax = owner.getPanel().getSliderMaxValue();
							if(hueMin > hueMax){
								hueMax = hueMin;
								owner.getPanel().setSliderMaxValue(hueMin);
							}
							//if the user just wants to access the camera and display it.
							if (!findColors) {
								//converts the frame to a buffered image
								bufferedImage = matToBufferedImage(frame);
								haveImage = true;
								//repaints the GUI
								repaint();
							} else {
								//if the user wants to find a color based on the hue slider bar
								
								
								//if no thread is created, this instantiate the thread
								if (count == 0) {
									//the thread passes the values of the clawImagePanel, We cant
									//just use this because we are in an actionlistener and the 
									//this keyword will call the actionlistener.
									//the frame, huemin, and huemax are also passed in
									//and are used for finding objects of different colors
									cam = new CameraThreading(getCIP(), frame,
											hueMin, hueMax);
									cam.start();
									count++;
								} else {
									//if the thread was already created then it
									//will just update the hueMin and hueMax and make sure
									//stop is set to false (if setStop is true it will stop
									//the thread)
									cam.setHueMin(hueMin);
									cam.setHueMax(hueMax);
									cam.setStop(false);
								}
								
								Mat hsv_image = new Mat();
								Mat threshold = new Mat();
								Mat threshold2 = new Mat();
								Mat distance = new Mat(frame.height(), frame
										.width(), CvType.CV_8UC1);
								
								//sets the hue min and max as a scalar 
								Scalar hsv_min = new Scalar(hueMin, 100, 100, 0);
								Scalar hsv_max = new Scalar(hueMax, 255, 255, 0);

								//converts the matrix to an hsv image
								Imgproc.cvtColor(frame, hsv_image,
										Imgproc.COLOR_BGR2HSV);
								//checks the hsv image for blobs in the range of hue min/max
								Core.inRange(hsv_image, hsv_min, hsv_max, threshold);
								// Filters out blobs less than size 20 by 20
								Imgproc.erode(threshold, threshold, Imgproc
										.getStructuringElement(Imgproc.MORPH_RECT,
												new Size(20, 20)));
								Imgproc.dilate(threshold, threshold, Imgproc
										.getStructuringElement(Imgproc.MORPH_RECT,
												new Size(20, 20)));
								Core.inRange(distance, new Scalar(0.0), new Scalar(
										200.0), threshold2);
								Core.bitwise_and(threshold, threshold2, threshold);
								Imgproc.GaussianBlur(threshold, threshold,
										new Size(9, 9), 0, 0);

								// stores each contour of each blob in a list
								List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
								// stores each contour in the list
								Imgproc.findContours(threshold, contours,
										threshold, Imgproc.RETR_LIST,
										Imgproc.CHAIN_APPROX_SIMPLE);
								List<Moments> mu = new ArrayList<Moments>(contours
										.size());
								// Draws Circles in the middle of the contours
								for (int i = 0; i < contours.size(); i++) {
									mu.add(i, Imgproc.moments(contours.get(i)));
									Moments p = mu.get(i);
									int ax;
									int ay;
									ax = (int) (p.get_m10() / p.get_m00());
									ay = (int) (p.get_m01() / p.get_m00());
									// Draws Circles in the middle of the contours
									Core.circle(frame, new Point(ax, ay), 4,
											new Scalar(255, 300, 300, 255));

								}

								// Draw the contours of the item
								Imgproc.drawContours(frame, contours, -1,
										new Scalar(255, 255, 255), 1);
								// Convert matrix to buffered image for display

								bufferedImage = matToBufferedImage(frame);
								haveImage = true;
								repaint();
							}
							
						}else{
							//if the user wants to find a color based on the B/G/R buttons
							if (!findColors) {
								//if the user stops the find after pressing B/G/R button to find a color
								bufferedImage = matToBufferedImage(frame);
								haveImage = true;
								repaint();
							} else {
								//checks the number of threads that are alive
								if (count == 0) {
									//if no threads are alive, create a thread, start the thread
									//increment count.
									cam = new CameraThreading(getCIP(), frame,
											hueMin, hueMax);
									cam.start();
									count++;
								} else {
									//if a thread exists, update the hue min/max
									//sets stop to false.
									cam.setHueMin(hueMin);
									cam.setHueMax(hueMax);
									cam.setStop(false);
								}
								Mat hsv_image = new Mat();
								Mat threshold = new Mat();
								Mat threshold2 = new Mat();
								Mat distance = new Mat(frame.height(), frame
										.width(), CvType.CV_8UC1);
								//sets the hue min and max as a scalar 
								Scalar hsv_min = new Scalar(hueMin, 100, 100, 0);
								Scalar hsv_max = new Scalar(hueMax, 255, 255, 0);
								
								//converts the matrix to an hsv image
								Imgproc.cvtColor(frame, hsv_image,
										Imgproc.COLOR_BGR2HSV);
								//checks if any blobs exist in the image in the range of hue min-max
								Core.inRange(hsv_image, hsv_min, hsv_max, threshold);
								// Filters out blobs less than size 20 by 20
								Imgproc.erode(threshold, threshold, Imgproc
										.getStructuringElement(Imgproc.MORPH_RECT,
												new Size(20, 20)));
								Imgproc.dilate(threshold, threshold, Imgproc
										.getStructuringElement(Imgproc.MORPH_RECT,
												new Size(20, 20)));
								Core.inRange(distance, new Scalar(0.0), new Scalar(
										200.0), threshold2);
								Core.bitwise_and(threshold, threshold2, threshold);
								Imgproc.GaussianBlur(threshold, threshold,
										new Size(9, 9), 0, 0);

								// stores each contour of each blob in a list
								List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
								// stores each contour in the list
								Imgproc.findContours(threshold, contours,
										threshold, Imgproc.RETR_LIST,
										Imgproc.CHAIN_APPROX_SIMPLE);
								List<Moments> mu = new ArrayList<Moments>(contours
										.size());
								// Draws Circles in the middle of the contours
								for (int i = 0; i < contours.size(); i++) {
									mu.add(i, Imgproc.moments(contours.get(i)));
									Moments p = mu.get(i);
									int ax;
									int ay;
									ax = (int) (p.get_m10() / p.get_m00());
									ay = (int) (p.get_m01() / p.get_m00());
									// Draws Circles in the middle of the contours
									Core.circle(frame, new Point(ax, ay), 4,
											new Scalar(255, 300, 300, 255));

								}

								// Draw the contours of the item
								Imgproc.drawContours(frame, contours, -1,
										new Scalar(255, 255, 255), 1);
								
								// Convert matrix to buffered image for display
								bufferedImage = matToBufferedImage(frame);
								haveImage = true;
								repaint();
							}
						}
					}
				});
	}

	public void setimagewithMat(Mat newimage) {
		image = this.matToBufferedImage(newimage);
		return;
	}

	public BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
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
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage temp = getImage();
		if (temp != null)
			g.drawImage(temp, 10, 10, temp.getWidth(), temp.getHeight(), this);
	}

	public void paint(Graphics g) {
		// -- the base class paintComponent(g) method ensures
		// the drawing area will be cleared properly. Do not
		// modify any attributes of g prior to sending it to
		// the base class

		// -- for legacy reasons the parameter comes in as type Graphics
		// but it is really a Graphics2D object. Cast it up since the
		// Graphics2D class is more capable
		Graphics2D g2d = (Graphics2D) g;

		// -- get the height and width of the JPanel drawing area
		int width = getWidth();
		int height = getHeight();

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