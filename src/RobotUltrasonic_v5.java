import lejos .hardware.Button;

	import lejos .hardware.lcd .LCD;
	import lejos .hardware.port .SensorPort;
	import lejos .hardware.sensor .EV3UltrasonicSensor;
	import lejos . utility .Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;
	import lejos .hardware.motor.UnregulatedMotor;
	import lejos .hardware.port .MotorPort;
	
public class RobotUltrasonic_v5 implements TimerListener{
	
	private int indice = 0;
	private long t0;
	public static float [][] tabD; 
	private static float[] sampleSound;
	private static UnregulatedMotor motorB;
	private static UnregulatedMotor motorC;
	private static EV3UltrasonicSensor sSound;
	private static double erreur;
	private static int puissance;
	private static double consigne = 1.2;
	private static int i;
	
	public void timedOut() {
		
		i++;
		if(i==5){consigne=1;}
		if(i==30){consigne=0.2;}
		if(i==55){consigne=1;}
		
		sSound .fetchSample(sampleSound, 0);
		LCD.clear();
		LCD.drawInt((int)(100*sampleSound[0]), 0, 4);
		if(sampleSound[0]>=consigne+0.05){
			
			erreur = consigne - sampleSound[0];
			puissance = -(int)(erreur*500);
			if(puissance>100){puissance = 100;}
			motorB.setPower(puissance);
			motorC.setPower(puissance);
			
		}else if(sampleSound[0]<=consigne-0.05){
			
			puissance = -80;
			motorB.setPower(puissance);
			motorC.setPower(puissance);
			erreur = 0;
			
		} else {
			
			puissance = 0;
			motorB.setPower(puissance);
			motorC.setPower(puissance)  ;
			erreur = 0;
		}
		
		ecrireInfo((float)(puissance), (float)(sampleSound[0]));
		
		
		
	}

	
		
	public static void main(String[] args){
			
		motorB = new UnregulatedMotor(MotorPort.B);
		motorC = new UnregulatedMotor(MotorPort.C);
		sSound = new EV3UltrasonicSensor(SensorPort.S4);
		
		tabD = new float[1000][4];
		
		sSound.setCurrentMode("Distance"); // distance
		sampleSound = new float[sSound.sampleSize()];
		
		Button.waitForAnyPress();
	
		sSound .fetchSample(sampleSound, 0);
		
		RobotUltrasonic_v5 et = new RobotUltrasonic_v5();
		int varTs = 200;
		Timer t = new Timer(varTs, et);
		t.start();
		Button.waitForAnyPress();
		t.stop();
		motorB.close();
		motorC.close();
		sSound.close();
		Enregistreur.ecrireFich("Sauvegarde3.txt",tabD);
		
		

		
	}
	
	private void ecrireInfo ( float info1 , float info2 ) {
		if ( indice <tabD.length){
			if ( indice == 0) {
			t0 = System.currentTimeMillis() ; // memorisation de l’instant initial
			tabD[indice ][0] = 0;
			} else
			tabD[indice ][0] = System.currentTimeMillis()-t0;
			tabD[indice ][1] = info1;
			tabD[indice ][2] = info2;
			indice++; // pour passer à la ligne suivante
		}
	}
}

	
