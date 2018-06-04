package p2iEV3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class ServerTCPArd {

	static EV3LargeRegulatedMotor motorRoueGauche;
	static EV3LargeRegulatedMotor motorRoueDroite;
	static UnregulatedMotor motorRot;
	static EV3MediumRegulatedMotor motorAdmis;
	float[] sampleJoystick = new float[3];
	static double phi;
	static int xValeur;
	static int yValeur;
	static int press;
	static int power = 0;

	public static void main(String[] args) throws IOException {

		motorRoueGauche = new EV3LargeRegulatedMotor(MotorPort.A);
		motorRoueDroite = new EV3LargeRegulatedMotor(MotorPort.D);
		motorRot = new UnregulatedMotor(MotorPort.C);
		motorAdmis = new EV3MediumRegulatedMotor(MotorPort.B);

		motorRoueGauche.synchronizeWith(new RegulatedMotor[] { motorRoueDroite });

//		initialise();

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
				System.out.println(" Attente d'une entrée .....");

				while ((inputLine = in.readLine()) != null) {
					System.out.println("  Message from Client " + clientSocket.getRemoteSocketAddress().toString()
							+ ": " + inputLine);

					if (inputLine.contains("PowerRot :")) {
						xValeur = (Integer.parseInt(inputLine.substring(("PowerRot :".length()))));

					} else if (inputLine.contains("PowerAdmis :")) {
						press = (Integer.parseInt(inputLine.substring(("PowerAdmis :".length()))));
					} else {
						yValeur = Integer.parseInt(inputLine.substring(("Power :".length())));
					}
					// Fermeture de la communication
					update();
				}
				// out.close();
				// in.close();
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
		if (900 < xValeur && 35 < phi) {
			// motorRot.setPower(40);
			motorRot.setPower(30);
		} else if (100 > xValeur && 145 > phi) {
			// motorRot.setPower(-40);
			motorRot.setPower(-30);
		} else {
			motorRot.setPower(0);
		}

		if (yValeur > 700 && power > 0) {
			power -= 7;
		} else if (yValeur < 200 && power < 800) {
			power += 7;

		}

		if (0 == press) {
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
