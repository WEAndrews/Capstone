package leap;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.swing.JPanel;

public class CommConnection implements SerialPortEventListener {
	
    //this is the object that contains the opened port
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;

    private JPanel owner;
    
    public int receiver = 0;
    
    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    
    public void setOwner(JPanel owner)
    {
    	this.owner = owner;
    }
    
	// -- Serial port communications message handlers

    public boolean getconnected()
    {
    	return bConnected;
    }
    public CommConnection(JPanel owner)
    {
    	this.owner = owner;
    }
    
    //connect to the selected port in the combo box
    //pre style="font-size: 11px;": ports are already found by using the searchForPorts
    //method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public boolean connect(String selectedPort, CommPortIdentifier selectedPortIdentifier)
    {
        CommPort commPort = null;

        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("BASIC Stamp", TIMEOUT);
            
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
             
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            
            						 // <<< ADDED THIS LINE TO GET CHARACTER READ BACK EVENTS
            serialPort.setRTS(true); // <<< ADDED THIS LINE TO GET CHARACTER READ BACK EVENTS
            						 // <<< ADDED THIS LINE TO GET CHARACTER READ BACK EVENTS
            
            this.bConnected = true;
            return true;
        }
        catch (UnsupportedCommOperationException e)
        {
        	System.out.println("unsupported operation");
        	return false;
        }
        catch (PortInUseException e)
        {
            System.out.println(selectedPort + " is in use. (" + e.toString() + ")");
        	return false;
        }
        catch (Exception e)
        {
            System.out.println("Failed to open " + selectedPort + "(" + e.toString() + ")");
        	return false;
        }
        
    }   
    
    //disconnect the serial port
    //pre style="font-size: 11px;": an open serial port
    //post: closed serial port
    public void disconnect()
    {
        //close the serial port
        try
        {

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();

            System.out.println("Disconnected.");
        }
        catch (Exception e)
        {
            System.out.println("Failed to close " + serialPort.getName()
                              + "(" + e.toString() + ")");
        }
    }    
    //open the input and output streams
    //pre style="font-size: 11px;": an open port
    //post: initialized input and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whether opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();


            successful = true;
            return successful;
        }
        catch (IOException e) {
            System.out.println("I/O Streams failed to open. (" + e.toString() + ")");
            return successful;
        }
    }
    
    //starts the event listener that knows whenever data is available to be read
    //pre style="font-size: 11px;": an open serial port
    //post: an event listener for the serial port that knows when data is received
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            System.out.println("Too many listeners. (" + e.toString() + ")");
        }
    }   
    

    private String msg = "";
    private int recvdCount = 0;

    	
        //what happens when data is received
        //pre style="font-size: 11px;": serial event is triggered
        //post: processing on the data it reads
        public void serialEvent(SerialPortEvent evt) {
            if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
            {
                try
                {
                    byte singleData = (byte)input.read();
                    msg = msg + (char)singleData;
                    
                    if (receiver == 1) { // -- 1: Leap sensor
                    	System.out.println("received:>" + msg);
                    }
                    else if (receiver == 0) { // -- 0: gantry
	                    if (owner instanceof GantryPanel) {
	                		GantryPanel gp = (GantryPanel)owner;
	                		if (msg.charAt(0) == '#') {
	                			gp.setStatus(3);
	                		}
	                		else if (msg.charAt(0) == '*') {
	                			gp.setStatus(2);
	                		}
	                    	//System.out.println("from gantry: " + msg + "(" + msg.length() + ")");
	                    }
	                    recvdCount++;
	                    // -- complete message received from gantry
	                	if (msg.charAt(msg.length() - 1) == 0x0D) {
	                        // -- distance data from SONAR Basic Stamp
		                    if (msg.charAt(0) == 'U')
		                    {	
		                    	int distance = (((int)(msg.charAt(6)) << 8) & 0xFF) + (int)(msg.charAt(7) & 0xFF);
		                    	// System.out.println(msg.substring(0, 5) + ": " + distance + "cm");
		
		                    	if (owner instanceof SonarPanel) {
		                    		SonarPanel sp = (SonarPanel)owner;
		                    		sp.setSonarField(distance);
		                    	}
		                        logText = new String(new byte[] {singleData});
		                    }
		                    // -- position data from gantry
		                    else if (msg.charAt(0) == 'X') {
		                    	// -- X0000.00 Y0000.00 Z0000.00
		                    	// System.out.println("position: " + msg);
		                    	double x = Double.parseDouble(msg.substring(1, 7));
		                    	double y = Double.parseDouble(msg.substring(10, 17));
		                    	double z = Double.parseDouble(msg.substring(20, 27));
		                    	if (owner instanceof GantryPanel) {
		                    		GantryPanel gp = (GantryPanel)owner;
		                    		gp.setPositionFields(x, y, z);
		                    	}
		                    }
		                    else {
		                    	// System.out.println("other: " + msg);
		                    }
		                	msg = "";
		                	recvdCount = 0;
	                	}
                    }
                }
                catch (Exception e)
                {
                    logText = "Failed to read data. (" + e.toString() + ")";
                   System.out.println(logText + "\n");
                }
            }
        }
   
    
    //method that can be called to send data
    //pre style="font-size: 11px;": open serial port
    //post: data sent to the other device
    public void writeData(int data)
    {
        try
        {
//        	System.out.println("RED");
//          output.write('!');
//            output.write(data);
//            output.write('\r');
//            output.flush();
        	
        	output.write(data);
        	output.flush();

         }
        catch (Exception e)
        {
            logText = "Failed to write data. (" + e.toString() + ")";
            System.out.println(logText + "\n");
        }
    }

}
