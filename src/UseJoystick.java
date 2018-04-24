import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class UseJoystick implements TimerListener{
	public static final int DEMULTIPLICATEUR = 7;
	public static final int VAR_TS = 100;
	
	static Joystick joy1;
	static Convertisseur conv;
	
	static UnregulatedMotor motorRoueA;
	static UnregulatedMotor motorRoueB;
	static UnregulatedMotor motorRot;
	static EV3MediumRegulatedMotor motorAdmis;
	
	//static int consigne;
	
	float[] sampleJoystick = new float [3];
	int power = 0;
	double phi;

	
	public void timedOut() {
		sampleJoystick = joy1.getSample();
		
		if (900<sampleJoystick[0] && 180>phi){
			motorRot.setPower(40);
		} else if(100>sampleJoystick[0] && 0<phi){
			motorRot.setPower(-40);;
		}else {
			motorRot.setPower(0);
		}
		
		if (sampleJoystick[1]>800 && power<100) {
			power ++;
		}else if (sampleJoystick[1]<200 && power>0){
			power --;
			
		}
		
		if(0 == sampleJoystick[2]) {
			motorAdmis.rotate(120);
		}
		
		motorRoueA.setPower(power);
		motorRoueB.setPower(power);
		
		phi = phiMoteurToPhi(motorRot.getTachoCount());

		LCD.clear();
		LCD.drawString("pow: "+power+" rot: "+phi, 0, 0);
		LCD.drawString(""+sampleJoystick[0]+"  "+sampleJoystick[1]+"  "+sampleJoystick[2], 1, 1);
	}
	
	
	public static void main(String[] args) {
		
		joy1 = new Joystick (SensorPort.S1);
		
		motorRoueA = new UnregulatedMotor(MotorPort.A);
		motorRoueB = new UnregulatedMotor(MotorPort.B);
		motorRot = new UnregulatedMotor (MotorPort.C);
		motorAdmis = new EV3MediumRegulatedMotor (MotorPort.D)


		initialise();
		
		motorRoueA.resetTachoCount();
		motorRoueB.resetTachoCount();
		motorAdmis.resetTachoCount();
		
		UseJoystick obj = new UseJoystick ();
		
		Timer t = new Timer(VAR_TS, obj);
		t.start();
		Button.waitForAnyPress();
		t.stop();
		
		motorRoueA.close();
		motorRoueB.close();
		motorRot.close();
		motorAdmis.close();
		
	}

	private double phiMoteurToPhi (double phiMoteur){
		return phiMoteur/DEMULTIPLICATEUR;
	}

	private void initialise {

		EV3TouchSensor butee = new EV3TouchSensor (SensorPort.S2);
		butee.setCurrentMode(0);

		float sampleButee = new float[butee.sampleSize()];
		int resultButee;

		motorRot.setPower(-40);
		do {
			butee.fetchSample(sampleTouch, 0);
			resultButee = (int) (sampleTouch[0]);
		}while (1!=resultButee);
		motorRot.setPower(0);
		motorRot.resetTachoCount();

		motorRot.setPower(30);
		do{
			phi = phiMoteurToPhi(motorRot.getTachoCount());
		}while(90<phi);
		motorRot.setPower(0);

	}

}


