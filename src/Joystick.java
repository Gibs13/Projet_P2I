
import lejos .hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.I2CSensor;

public class Joystick {

	static int I2CSlaveAddress = 0x08;
    static byte[] buf = new byte[8];

    public static void main(String[] args) {
        System.out.println("Arduino Connection Test");
        I2CSensor arduino = new I2CSensor(SensorPort.S1, I2CSlaveAddress);

        System.out.println(arduino.getAddress());
        
        while (Button.ESCAPE.isUp()) {
            int id = Button.waitForAnyPress();
            if (id == Button.ID_ENTER) {
                //arduino.sendData(I2CSlaveAddress,buf,0,1);
             arduino.getData('A', buf, buf.length);
             arduino.setRetryCount(10);
             System.out.println(new String(buf));
            }
        } 
        Button.waitForAnyPress();
        arduino.close();
    }
	
}
