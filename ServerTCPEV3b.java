package p2iEV3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;

public class ServerTCPEV3b {
	static EV3LargeRegulatedMotor motorRoueGauche;
	static EV3LargeRegulatedMotor motorRoueDroite;
	static UnregulatedMotor motorRot;
	static EV3MediumRegulatedMotor motorAdmis;
	static float[] sampleJoystick = new float[3];
	static double phi;
	static int power = 0;
	static int powerAdmis = 0;
	static int powerRot = 0;

	public static void main(String[] args) throws IOException {

		motorRoueGauche = new EV3LargeRegulatedMotor(MotorPort.A);
		motorRoueDroite = new EV3LargeRegulatedMotor(MotorPort.D);
		motorRot = new UnregulatedMotor(MotorPort.C);
		motorAdmis = new EV3MediumRegulatedMotor(MotorPort.B);

		motorRoueGauche.synchronizeWith(new RegulatedMotor[] { motorRoueDroite });

		initialise();

		motorRoueGauche.resetTachoCount();
		motorRoueDroite.resetTachoCount();
		motorAdmis.resetTachoCount();

		try {
			ServerSocket serverSocket = null;
			int portNumber = 5001;
			serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = null;
			String inputLine;

			while (true) {
				System.out.println("Attente d'une connexion.....");
				clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				System.out.println(" Connexion reussie");
				System.out.println(" Attente d'une entr�e .....");
				while (clientSocket.isConnected()) {

					if ((inputLine = in.readLine()) != null) {
						System.out.println("  Message from Client " + clientSocket.getRemoteSocketAddress().toString()
								+ ": " + inputLine);

						if (inputLine.contains("PowerRot :")) {
							phi = phiMoteurToPhi(motorRot.getTachoCount());
							if (35 < phi && phi < 135) {
								motorRot.setPower(Integer.parseInt(inputLine.substring(("PowerRot :".length()))));
							}
						} else if (inputLine.contains("PowerAdmis :")) {
							motorAdmis.setSpeed(Integer.parseInt(inputLine.substring(("PowerAdmis :".length()))));
							motorAdmis.rotate(120);
							motorAdmis.stop();
						} else if (inputLine.contains("Power :")) {
							power = Integer.parseInt(inputLine.substring(("Power :".length())));
						}
						//
						// motorRoueGauche.setSpeed(power);
						// motorRoueDroite.setSpeed(power);
						// motorRoueGauche.forward();
						// motorRoueDroite.forward();
						// clientSocket.close();
						// System.out.println();
					}

					motorRoueGauche.setSpeed(power);
					motorRoueDroite.setSpeed(power);
					motorRoueGauche.forward();
					motorRoueDroite.forward();
					// clientSocket.close();
				}
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	static private int phiMoteurToPhi(double phiMoteur) {
		return ((int) (-phiMoteur / 7.0 - 5.714));
	}

	static private void initialise() {

		LCD.clear();
		LCD.drawString("Initialisation", 4, 4);

		EV3TouchSensor butee = new EV3TouchSensor(SensorPort.S2);
		butee.setCurrentMode(0);
		float[] sampleButee = new float[butee.sampleSize()];
		int resultButee;
		motorRot.setPower(50);
		do {
			butee.fetchSample(sampleButee, 0);
			resultButee = (int) (sampleButee[0]);
		} while (1 != resultButee);
		motorRot.setPower(0);
		motorRot.resetTachoCount();
		motorRot.setPower(-50);
		do {
			phi = phiMoteurToPhi(motorRot.getTachoCount());
			LCD.drawString(" rot: " + phi, 0, 0);
		} while (90 > phi);
		motorRot.setPower(0);
		butee.close();
		LCD.clear();

	}

	public static void update() {
		if (sampleJoystick[0] > 900 && 35 < phi) {
			// motorRot.setPower(40);
			motorRot.setPower(30);
		} else if (sampleJoystick[0] < 100 && 145 > phi) {
			// motorRot.setPower(-40);
			motorRot.setPower(-30);
		} else {
			motorRot.setPower(0);
		}

		if (sampleJoystick[1] > 700 && power > 0) {
			power -= 7;
		} else if (sampleJoystick[1] < 200 && power < 800) {
			power += 7;

		}

		if (sampleJoystick[2] == 0) {
			motorAdmis.setSpeed(1000);
			motorAdmis.rotate(120);
			motorAdmis.stop();
		}
		motorRoueGauche.setSpeed(power);
		motorRoueDroite.setSpeed(power);
		motorRoueGauche.forward();
		motorRoueDroite.forward();
		phi = phiMoteurToPhi(motorRot.getTachoCount());

	}
}
