
import lejos .hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.I2CSensor;
import lejos.utility.Delay;

public class Joystick_main {

	static int I2CSlaveAddress = 0x08;
    static byte[] buf = new byte[5];
    
    static int xValue = 0; // variable to store x value
    static int yValue = 0; // variable to store y value

    public static void main(String[] args) {
        System.out.println("Arduino Connection Test");
        
        
        I2CSensor arduino = new I2CSensor(SensorPort.S1, I2CSlaveAddress);
        
        
        
        System.out.println(arduino.getAddress());
        
        
        UnregulatedMotor motor1 = new UnregulatedMotor(MotorPort.A);
        UnregulatedMotor motor2 = new UnregulatedMotor(MotorPort.B);
        motor1.resetTachoCount();
        motor2.resetTachoCount();
        
        while (Button.ESCAPE.isUp()) {
        	
        	
             arduino.getData('B', buf, buf.length);
             
             arduino.setRetryCount(10);
             Delay.msDelay(50);
             xValue = ((buf[1] << 8 | (buf[0] & 0xFF))-508);
             yValue = ((buf[3] << 8 | (buf[2] & 0xFF))-496);
             System.out.println(""+xValue+"\n"+yValue+"\n"+buf[4]);
             if(buf[4]==0){
            	 motor1.setPower(0);
            	 motor2.setPower(0);
             } else {
            	 motor1.setPower(xValue/5);
            	 motor2.setPower(yValue/5);
             }
        }
        
        arduino.close();
    }
	
}