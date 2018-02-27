import lejos .hardware.Button;
import lejos .hardware.lcd .LCD;
import lejos .hardware.port .SensorPort;
import lejos .hardware.sensor .EV3UltrasonicSensor;
import lejos . utility .Delay;
import lejos .hardware.motor.UnregulatedMotor;
import lejos .hardware.port .MotorPort;

public class Asservissement10cm {
	
	public static void main(String[] args){
		
		UnregulatedMotor motor1 = new UnregulatedMotor(MotorPort.A);
		EV3UltrasonicSensor sSound = new EV3UltrasonicSensor(SensorPort.S1);
		sSound.setCurrentMode("Distance"); // distance
		float[] sampleSound = new float[sSound.sampleSize()];
		
		motor1.setPower(50);
		sSound .fetchSample(sampleSound, 0);

		while(sampleSound[0]>0.1){
			sSound .fetchSample(sampleSound, 0);
			LCD.drawString("valeur :" +sampleSound[0],0,6) ;

		}
		
		motor1.setPower(-50);
		
		while(sampleSound[0]<0.15){
			sSound .fetchSample(sampleSound, 0);
			LCD.drawString("valeur :" +sampleSound[0],0,6) ;

		}
		
		motor1.setPower(50);
		Delay.msDelay(2000);
		



	}

}
