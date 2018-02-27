import lejos . utility .Timer;
import lejos . utility . TimerListener ;
import lejos .hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos .hardware.motor.UnregulatedMotor;
import lejos .hardware.port .MotorPort;


public class TestTimer implements TimerListener{

	private static int puissance = 0;
	private static UnregulatedMotor motor1 = new UnregulatedMotor(MotorPort.A);
	private int i = 0;
	private int indice = 0;
	private long t0;
	public static float [][] tabD; 
	
	public void timedOut() {
		i++;
		if(puissance<80 && i%20==0){
			puissance+=20;
			motor1.setPower(puissance);
		}
		ecrireInfo (puissance, motor1.getTachoCount());
	}
	
	public static void main(String[] args){
		
		tabD = new float [120][3];
		
		motor1.resetTachoCount();
		TestTimer et = new TestTimer();
		int varTs = 100;
		Timer t = new Timer(varTs, et);
		t.start();
		Button.waitForAnyPress();
		t.stop();
		motor1.close();		
		
		Enregistreur.ecrireFich("Sauvegarde.txt",tabD);
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
