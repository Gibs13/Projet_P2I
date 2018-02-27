import lejos .hardware.Button;
import lejos .hardware.lcd .LCD;
import lejos .hardware.port .SensorPort;
import lejos .hardware.sensor .EV3ColorSensor;
import lejos . utility .Delay;
import lejos .hardware.motor.UnregulatedMotor;
import lejos .hardware.port .MotorPort;

public class Motor2 {
	
	public static void main (String [] args) {
		
		UnregulatedMotor motor1 = new UnregulatedMotor(MotorPort.A);
		
		EV3ColorSensor sColor = new EV3ColorSensor(SensorPort.S1);
		sColor.setCurrentMode("Red"); // luminosite avec LED rouge
		
		float[] sampleColor = new float[sColor.sampleSize()];
		
		motor1.setPower(50);
		
		Delay.msDelay(50);
		while(Button.ESCAPE.isUp()){
			sColor .fetchSample(sampleColor, 0);
			
			if (sampleColor[0] < 0.05){
				
				motor1.setPower(0);
				
			}
			
			Delay.msDelay(50);
			}
			sColor.close () ;
		
			motor1.close();

	}
}
