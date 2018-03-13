
import lejos.hardware.port.Port;
import lejos.hardware.sensor.I2CSensor;

public class Joystick {

	private int I2CSlaveAddress = 0x08;
	I2CSensor arduino;
	private byte[] buf = new byte[5];
    
	private int xValue = 0; // variable to store x value
	private int yValue = 0; // variable to store y value
	private int pushed;
	
	Joystick(Port S){
		this.arduino = new I2CSensor(S, I2CSlaveAddress);
		
	}
	
	void actualisation(){
		arduino.getData('B', buf, buf.length);
        arduino.setRetryCount(10);
        
        xValue = buf[1] << 8 | (buf[0] & 0xFF);
        yValue = buf[3] << 8 | (buf[4] & 0xFF);
		pushed = buf[5];
		
	}
	
    int getVertical(){
    	actualisation();
    	return xValue;
    };
    
    int getHorizontal(){
    	actualisation();
    	return yValue;
    };
    
    boolean isPushed(){
    	actualisation();
    	return pushed==0;
    }
    
    public void close() {
        arduino.close();
    }
	
}