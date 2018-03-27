package Controller;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TooManyListenersException;
import java.util.Vector;

public class RXTXTest implements SerialPortEventListener {



	
    //for containing the ports that will be found
    private Enumeration ports = null;
    
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
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

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    
    public boolean getconnected()
    {
    	return bConnected;
    }
    
//    public RXTXTest()
//   {
//    	System.loadLibrary("rxtxSerial");
//    }
    
    //search for all the serial ports
    //pre style="font-size: 11px;": none
    //post: adds all the found ports to a combo box on the GUI
    public Vector<String> searchForPorts()
    {
    	Vector<String> vector = new Vector<String>();
    	
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
 //               System.out.println(curPort.getName());
                vector.add(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
        return vector;
    }
    
    //connect to the selected port in the combo box
    //pre style="font-size: 11px;": ports are already found by using the searchForPorts
    //method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect(String selectedPort)
    {
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

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
        }
        catch (UnsupportedCommOperationException e)
        {
        	System.out.println("unsupported operation");
        }
        catch (PortInUseException e)
        {
            System.out.println(selectedPort + " is in use. (" + e.toString() + ")");

        }
        catch (Exception e)
        {
            System.out.println("Failed to open " + selectedPort + "(" + e.toString() + ")");
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
    
public String msg = "";
public int recvdCount = 0;

	
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
                recvdCount++;
 //               System.out.println("received: " + msg);
                if (recvdCount == 10)
                {	
                	int distance = (((int)(msg.charAt(6)) << 8) & 0xFF) + (int)(msg.charAt(7) & 0xFF);
                	System.out.println(msg.substring(0, 5) + ": " + distance + "cm");
                	msg = "";
                	recvdCount = 0;
                    logText = new String(new byte[] {singleData});
//                    System.out.println("received: " + logText);
                }
                else
                {
//                    System.out.println("\n");
                }
            }
            catch (Exception e)
            {
                logText = "Failed to read data. (" + e.toString() + ")";
               System.out.println(logText + "\n");
            }
        }
    }
    
    public void writeString(String data)
    {
    	byte[] bdata = data.getBytes();
    	for (int i = 0; i < bdata.length; ++i) {
    		writeData(bdata[i]);
    	}
    	writeData(0x0D);
    	writeData(0x0A);
    	
    }
    
    public void writeStringG(String data)
    {
    	byte[] bdata = data.getBytes();
    	for (int i = 0; i < bdata.length; ++i) {
    		writeData(bdata[i]);
    	}
    	writeData(0x0D);
    	
    }

    public void writeStringGnoCR(byte[] bdata)
    {
    	for (int i = 0; i < bdata.length; ++i) {
    		writeData(bdata[i]);
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
	
	public static void main(String[] args) {
		
		RXTXTest rxtx = new RXTXTest();
		rxtx.searchForPorts();

		Scanner kb = new Scanner(System.in);
		
		System.out.println("port: ");
		String port = kb.next();
		rxtx.connect(port);
		if (rxtx.bConnected) {
			System.out.println("connected");
            rxtx.initIOStream();

			rxtx.initListener();
			
			int data;
			System.out.println("Data: ");
			data = kb.nextInt();
			while (data != 'X') {
				rxtx.writeData(data);
				data = kb.nextInt();
			}
			
			rxtx.disconnect();
		}
		else {
			System.out.println("not connected");
		}
	}



}
