import lejos .hardware.Button;

	import lejos .hardware.lcd .LCD;
	import lejos .hardware.port .SensorPort;
	import lejos .hardware.sensor .EV3UltrasonicSensor;
	import lejos . utility .Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
	import lejos .hardware.motor.UnregulatedMotor;
	import lejos .hardware.port .MotorPort;
	
public class RobotUltrasonic_v4 implements TimerListener{
	
	private int indice = 0;
	private long t0;
	public static float [][] tabD; 
	private static float[] sampleSound;
	private static UnregulatedMotor motorB;
	private static UnregulatedMotor motorC;
	private static EV3UltrasonicSensor sSound;
	private static double difference;
	private static int puissance;
	private static int accumulateur;
	
	public void timedOut() {
		
		sSound .fetchSample(sampleSound, 0);
		LCD.clear();
		LCD.drawInt((int)(100*sampleSound[0]), 0, 4);
		if(sampleSound[0]>=0.205){
			
			difference = 0.2 - sampleSound[0];
			puissance = -(int)(difference*500);
			if(puissance>100){puissance = 100;}
			motorB.setPower(puissance);
			motorC.setPower(puissance);
			
		}else if(sampleSound[0]<=0.195){
						
			motorB.setPower(-80);
			motorC.setPower(-80);
			
		} else {
			motorB.setPower(0);
			motorC.setPower(0)  ;
		}
		
		
		
	}
	
	public static void main(String[] args){
		
		motorB = new UnregulatedMotor(MotorPort.B);
		motorC = new UnregulatedMotor(MotorPort.C);
		sSound = new EV3UltrasonicSensor(SensorPort.S4);
		
		sSound.setCurrentMode("Distance"); // distance
		sampleSound = new float[sSound.sampleSize()];
		
		motorB.setPower(50);
		motorC.setPower(50);
			
		sSound .fetchSample(sampleSound, 0);
		
		RobotUltrasonic_v2 et = new RobotUltrasonic_v2();
		int varTs = 100;
		Timer t = new Timer(varTs, et);
		t.start();
		Button.waitForAnyPress();
		t.stop();
		motorB.close();
		motorC.close();
		sSound.close();
		
		

		
	}
}