/* gnu Licence
 */

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

public class LanceurEffetRetro implements TimerListener{

	private static UnregulatedMotor motorA;
	private static int puissance = 0;
	private static int angle = 0;
	private int i = 0;
	
	public void timedOut() {
		
		i++;
		
		motorA.setPower(puissance);
		
		
		if(Button.RIGHT.isDown() && i%10==0){
			puissance += 20;
		}else if(Button.LEFT.isDown() && i%10==0){
			puissance -= 20;
		}
		
		LCD.clear(0, 6, 4);
		LCD.drawInt(puissance, 0, 6);
	}
	
	public static void main (String[] args){
		
		LanceurEffetRetro et = new LanceurEffetRetro();
		int varTs = 10;
		Timer t = new Timer(varTs, et);
		
		motorA = new UnregulatedMotor(MotorPort.A);
		
		t.start();
		while (!Button.ESCAPE.isDown()) {}
		t.stop();
		
		motorA.close();
		
	}
	
	
}
