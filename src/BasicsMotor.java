import lejos .hardware.motor.UnregulatedMotor;
import lejos .hardware.port .MotorPort;
import lejos . utility .Delay;
public class BasicsMotor {
public static void main(String [] args) {
UnregulatedMotor motorA = new UnregulatedMotor(MotorPort.A);
motorA.setPower(50); // En avant à mi-puissance
Delay.msDelay(1000);
motorA.backward(); // En arrière à la même puissance
Delay.msDelay(1000);
motorA.close() ;
}
}