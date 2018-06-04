package p2iEV3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Timer;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import lejos.utility.TimerListener;

public class ClientTCPEV3 {
	public static final int DEMULTIPLICATEUR = 7;
	public static final int VAR_TS = 100;

	static Joystick joy1;

	static EV3LargeRegulatedMotor motorRoueGauche;
	static EV3LargeRegulatedMotor motorRoueDroite;
	static UnregulatedMotor motorRot;
	static EV3MediumRegulatedMotor motorAdmis;

	static int powerRot;
	static int powerAdmis;

	float[] sampleJoystick = new float[3];
	static int power = 0;
	static double phi;
	static double phi1;

	static String serverHostname = new String("192.168.137.74"); // L'adresse du
																	// serveur
	static int portNumber = 5001; // Le port du serveur

	static Socket socket = null;
	static BufferedReader in = null;
	static PrintWriter out = null;

	public void initialize() throws IOException {
		try {

			socket = new Socket(serverHostname, portNumber);
			System.out.println("Connexion reussie");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			System.err.println("Hote inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) throws IOException {

		joy1 = new Joystick(SensorPort.S1);
		ClientTCPEV3 obj = new ClientTCPEV3();
		obj.initialize();

		try {
			obj.update();

			while (true) {
				System.out.println("jecoute le joystick");
				obj.initialize();
				obj.update();
				System.out.println("jenvoie pow");
				//out.println("PowerRot :" + powerRot);
				System.out.println("PowerRot :" + powerRot);
				System.out.println("PowerAdmis :" + powerAdmis);
				System.out.println("Power :" + power);
				out.close();
				in.close();
			}
		} catch (UnknownHostException e) {
			System.err.println("Hote inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void update() throws IOException {
		try {
			sampleJoystick = joy1.getSample();
			System.out.println("1");

			String test;
			if ((test = in.readLine()) != null) {
				phi = Double.parseDouble(test);
			}

			System.out.println("test" + phi);

			if (900 < sampleJoystick[1] && 35 < phi) {
				powerRot = 40;
				out.println("PowerRot :" + powerRot);
			} else if (100 > sampleJoystick[1] && 145 > phi) {
				powerRot = -40;
				out.println("PowerRot :" + powerRot);
			} else {
				// motorRot.setPower(0);
				powerRot = 0;
				out.println("PowerRot :" + powerRot);
			}

			if (sampleJoystick[0] <200 && power > 0) {
				power -= 15;
			} else if (sampleJoystick[0] >800 && power < 800) {
				power += 15;

			}

			if (0 == sampleJoystick[2]) {

				powerAdmis = 1000;
				out.println("PowerAdmis :" + powerAdmis);
			}

			out.println("Power :" + power);

			out.close();
			in.close();
			socket.close();

		} catch (UnknownHostException e) {
			System.err.println("Hote inconnu: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
