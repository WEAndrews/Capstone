package GantryClaw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.opencv.core.Core;
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
//This class was created because there was to much image lag
//without multithreading

class CameraThreading extends Thread {
	
	private Thread t;
	private Mat frame;
	private int x;
	private int y;
	private int prevX;
	private int prevY;
	private int cCounter;
	private VideoCapture cap;
	private int hueMin;
	private int hueMax;
	private boolean stop;
	private ClawImagePanel CIP;
	private int sendDelay;
	private int fHeight;
	private int fWidth;
	private int timeCounter;

	public void setHueMax(int _hmax) {
		hueMax = _hmax;
	}

	public void setHueMin(int _hmin) {
		hueMin = _hmin;
	}

	public void setStop(boolean _tf) {
		stop = _tf;
	}

	public int mapTo(long x, long in_min, long in_max, long out_min, long out_max)
	{
	  return (int)((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
	}
	
	CameraThreading(ClawImagePanel _c, Mat _f, int _hmin, int _hmax) {
		CIP = _c;
		frame = _f;
		hueMin = _hmin;
		hueMax = _hmax;
		cap = new VideoCapture(0);
		stop = false;
		
		//Delay is in seconds
		sendDelay = 1;
		
		//increments when a color is in the same position
		cCounter = 0;
		
		//checks the total number of times a color has been at a position
		//set this very high to disable the check
		timeCounter = 4000;
	}

	public void run() {
		while (!stop && cCounter < timeCounter) {
			//reads in the frame
			cap.read(frame);
			
			Mat hsv_image = new Mat();
			Mat threshold = new Mat();

			//gets the frame size then puts the height/width into variables
			Size fSize = frame.size();
			fHeight = (int) fSize.height;
			fWidth = (int) fSize.width;
			//sets (x, y) to be at (0, 0) when no blobs are found on camera
			x = fWidth/2;
			y = fHeight/2;
			
			//sets the min and max hue value to search for.
			Scalar hsv_min = new Scalar(hueMin, 100, 100, 0);
			Scalar hsv_max = new Scalar(hueMax, 255, 255, 0);
			
			//converts the frame to an hsv image
			Imgproc.cvtColor(frame, hsv_image, Imgproc.COLOR_BGR2HSV);
			
			//checks if there is an blob in the image in the hue range
			Core.inRange(hsv_image, hsv_min, hsv_max, threshold);
			
			// Filters out blobs less than size 20 by 20
			Imgproc.erode(threshold, threshold, Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(20, 20)));
			Imgproc.dilate(threshold, threshold, Imgproc.getStructuringElement(
					Imgproc.MORPH_RECT, new Size(20, 20)));
			
			// stores each contour of each blob in a list
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(threshold, contours, threshold,
					Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

			// Draws Circles in the middle of the contours
			List<Moments> mu = new ArrayList<Moments>(contours.size());
			for (int i = 0; i < contours.size(); i++) {
				mu.add(i, Imgproc.moments(contours.get(i)));
				Moments p = mu.get(i);
				x = (int) (p.get_m10() / p.get_m00());
				y = (int) (p.get_m01() / p.get_m00());
				// Draws Circles in the middle of the contours
				Core.circle(frame, new Point(x, y), 4, new Scalar(255, 300,
						300, 255));
			}
			
			//x is the value that will be mapped//
			//0 to 650 is the range on screen for x
			//-500 to 500 is the range the machine takes to move
			//from one side of the screen to the other.
			x = mapTo(x, 0, fWidth, -500, 500);
			y = mapTo(y, 0, fHeight, -500, 500);
			
			//checks if the object has been at the same point -+5
			if(x - prevX >-5 && x-prevX < 5 && y - prevY >-5 && y-prevY < 5 ){
				cCounter++;
			}else{
				cCounter = 0;
			}
			
			//sets the prevX/prevY location to x/y
			prevX = x;
			prevY = y;
			
			//loads the positions into a string and attempts to move the machine
			String s;
			s = "MOV " + "X" + x + " Y" + y + " Z" + 0;
			System.out.println(s);
			CIP.getOwner().getPanel().writeStringG(s);
			System.out.println("X = " + x + " Y = " + y);
			
			//delays for 1 second then checks to enter the loop again.
			try {
				TimeUnit.SECONDS.sleep(sendDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		CIP.stopFind();
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}