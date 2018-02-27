import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos .hardware.motor.UnregulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos .hardware.port .MotorPort;
import lejos . utility .Delay;

public class Motor1 {
	
	public static void main(String[] args) {
		LCD.drawString("En avant ?", 0, 4);
		Button.waitForAnyPress();
		
		UnregulatedMotor motor1 = new UnregulatedMotor(MotorPort.A);
		/*
		motor1.setPower(50);
		Delay.msDelay(2000);
		motor1.setPower(-50);
		Delay.msDelay(2000);
		motor1.setPower(0);
		
		for (int i=1; i<=8;i++){
			motor1.setPower(i*10);
			Delay.msDelay(1000);
		}
		*/
		motor1.resetTachoCount();
		motor1.setPower(30);
		int degre = 0;
		while (degre<=3*360){
			degre=motor1.getTachoCount();
			LCD.drawInt(degre, 0, 5);
			
		}
		motor1.setPower(0);
		Delay.msDelay(5000);
		EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S1);
		touch.setCurrentMode(0);
		float [ ] sampleTouch = new float[touch.sampleSize() ];
		int resultTouch = 0;
		while(resultTouch==0){
			touch.fetchSample(sampleTouch, 0);
			resultTouch = (int) (sampleTouch[0]);

		}
		
		motor1.resetTachoCount();
		motor1.setPower(30);
		degre = 0;
		while (degre<=360){
			degre=motor1.getTachoCount();
			LCD.drawInt(degre, 0, 6);
			
		}
		motor1.setPower(0);
		Delay.msDelay(5000);

		touch.close();
		motor1.close();
	}
}