import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class HelloWorld {
	
	public static void main(String[] args) {
		LCD.drawString("En avant ?", 0, 4);
		Button.waitForAnyPress();
	}
}