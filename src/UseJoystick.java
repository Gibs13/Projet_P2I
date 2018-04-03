import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
//import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class UseJoystick implements TimerListener{
	
	static final int VAR_TS = 100;
	
	static Joystick joy1;
	
	static UnregulatedMotor motorRoueA;
	static UnregulatedMotor motorRoueB;
	static UnregulatedMotor motorRot;
	static EV3MediumRegulatedMotor motorAdmis;
	
	//static int consigne;

	float[] sample = new float [3];
	int power = 0;
	
	public void timedOut() {
		sample = joy1.getSample();
		
		if (sample[0]>900){
			motorRot.setPower(40);
		} else if(sample[0]<100){
			motorRot.setPower(-40);;
		}else {
			motorRot.setPower(0);
		}
		
		if (sample[1]>800 && power<100) {
			power ++;
		}else if (sample[1]<200 && power>0){
			power --;
			
		}
		
		if(0 == sample[2]) {
			motorAdmis.rotate(120);
		}
		
		motorRoueA.setPower(power);
		motorRoueB.setPower(power);
		
		LCD.clear();
		LCD.drawString(""+power+" "+motorRot.getTachoCount(), 0, 0);
		LCD.drawString(""+sample[0]+"  "+sample[1]+"  "+sample[2], 1, 1);
	}
	
	
	public static void main(String[] args) {
		
		joy1 = new Joystick (SensorPort.S1);
		
		motorRoueA = new UnregulatedMotor(MotorPort.A);
		motorRoueB = new UnregulatedMotor(MotorPort.B);
		motorRot = new UnregulatedMotor (MotorPort.C);
		motorAdmis = new EV3MediumRegulatedMotor (MotorPort.D);
		
		motorRoueA.resetTachoCount();
		motorRoueB.resetTachoCount();
		motorRot.resetTachoCount();
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

}
