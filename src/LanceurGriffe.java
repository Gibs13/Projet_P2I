/* gnu Licence
 */

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class LanceurGriffe implements TimerListener{

	private static UnregulatedMotor motorA;
	private static int puissance = 20;
	private static int angle = 0;
	private int i = 0;
	
	public void timedOut() {
		
		i++;
		
		if(Button.UP.isDown()){
			motorA.setPower(puissance);
		}
		
		if ((motorA.getTachoCount()-angle) >= 360) {
			motorA.setPower(0);
			angle += 360;
		}
		
		if(Button.RIGHT.isDown() && i%20==0 && puissance<100){
			puissance += 20;
		}else if(Button.LEFT.isDown() && i%20==0 && puissance>0){
			puissance -= 20;
		}
		
		LCD.drawInt(puissance, 0, 6);
	}
	
	public static void main (String[] args){
		
		LanceurGriffe et = new LanceurGriffe();
		int varTs = 10;
		Timer t = new Timer(varTs, et);
		
		motorA = new UnregulatedMotor(MotorPort.A);
		
		motorA.resetTachoCount();
		
		t.start();
		while (!Button.ESCAPE.isDown()) {}
		t.stop();
		
		motorA.close();
		
	}
	
	
}
