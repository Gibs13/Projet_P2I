import lejos .hardware.Button;
	import lejos .hardware.lcd .LCD;
	import lejos .hardware.port .SensorPort;
	import lejos .hardware.sensor .EV3UltrasonicSensor;
	import lejos . utility .Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
	import lejos .hardware.motor.UnregulatedMotor;
	import lejos .hardware.port .MotorPort;
	
public class RobotUltrasonic implements TimerListener{
	
	private int indice = 0;
	private long t0;
	public static float [][] tabD; 
	private static float[] sampleSound;
	private static UnregulatedMotor motorB;
	private static UnregulatedMotor motorC;
	private static EV3UltrasonicSensor sSound;
	private static double difference;
	private static int puissance;
	
	public void timedOut() {
		
		sSound .fetchSample(sampleSound, 0);

		if(sampleSound[0]<0.09){
			
			puissance = -50;
			motorB.setPower(puissance);
			motorC.setPower(puissance);
			
		}else if(sampleSound[0]>0.11){
			
			puissance = 50;
			motorB.setPower(puissance);
			motorC.setPower(puissance);
			
		} else {
			puissance = 0;
			motorB.setPower(puissance);
			motorC.setPower(puissance);
		}
		
		ecrireInfo(sampleSound[0], motorB.getTachoCount(),puissance);
		
	}

	
		
	public static void main(String[] args){
			
		motorB = new UnregulatedMotor(MotorPort.B);
		motorC = new UnregulatedMotor(MotorPort.C);
		sSound = new EV3UltrasonicSensor(SensorPort.S4);
		
		tabD = new float[1000][4];
		
		sSound.setCurrentMode("Distance"); // distance
		sampleSound = new float[sSound.sampleSize()];
		
		motorB.setPower(50);
		motorC.setPower(50);
			
		sSound .fetchSample(sampleSound, 0);
		
		RobotUltrasonic et = new RobotUltrasonic();
		int varTs = 100;
		Timer t = new Timer(varTs, et);
		t.start();
		Button.waitForAnyPress();
		t.stop();
		motorB.close();
		motorC.close();
		sSound.close();
		Enregistreur.ecrireFich("Sauvegarde.txt",tabD);

		
		

		
	}
	
	private void ecrireInfo ( float info1 , float info2, float info3 ) {
		if ( indice <tabD.length){
			if ( indice == 0) {
			t0 = System.currentTimeMillis() ; // memorisation de l’instant initial
			tabD[indice ][0] = 0;
			} else
			tabD[indice ][0] = System.currentTimeMillis()-t0;
			tabD[indice ][1] = info1;
			tabD[indice ][2] = info2;
			tabD[indice ][3] = info3;
			indice++; // pour passer à la ligne suivante
		}
	}
}

	
