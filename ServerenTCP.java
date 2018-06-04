package p2iEV3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;

public class ServerenTCP {

	public static void main(String[] args) throws IOException {
		try {
			ServerSocket serverSocket = null;
			int portNumber = 5001;
			serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = null;
			String inputLine;

			while (true) {
				System.out.println("Attente d'une connexion..... ServerenTCP");
				clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				System.out.println(" Connexion reussie");
				System.out.println(" Attente d'une entrée .....");

				while (clientSocket.isConnected()) {
					if (in.readLine() != null) {
						inputLine = in.readLine();

						System.out.println("  Message from Client " + clientSocket.getRemoteSocketAddress().toString()
								+ ": " + inputLine);
						if (inputLine.contains("Ping !")) {
							System.out.println("Jai reçu ping!");
						}
						if (inputLine.contains("PowerRot :")) {
							System.out.println(Integer.parseInt(inputLine.substring(("PowerRot :".length()))));
						}
					}
//					while (Button.ENTER.isUp()) {
//						in.close();
//						out.close();
//						clientSocket.close();
//					}
				}

				 clientSocket.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
