import lejos .hardware.Button;
import lejos .hardware.motor.UnregulatedMotor;
import lejos .hardware.port .MotorPort;
	
public class RobotUltrasonic_v3{
	
	private static UnregulatedMotor motorB;
	private static UnregulatedMotor motorC;
	
	public static void main(String[] args){
		
		Button.waitForAnyPress();
		
		motorB = new UnregulatedMotor(MotorPort.B);
		motorC = new UnregulatedMotor(MotorPort.C);
		motorB.resetTachoCount();
		
		motorB.setPower(100);
		motorC.setPower(100);
		
		while (motorB.getTachoCount()<1984){}
		
		motorB.setPower(0);
		motorC.setPower(0);
		
		motorB.close();
		motorC.close();
		
	}
	
}
