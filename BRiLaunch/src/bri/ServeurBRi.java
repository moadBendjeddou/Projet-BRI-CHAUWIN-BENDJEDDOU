
package bri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import bri.service.ServiceBRi;

public class ServeurBRi implements Runnable {

	private final int port;
	private ServerSocket listen_socket;

	public ServeurBRi(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			listen_socket = new ServerSocket(port);

			System.out.println(
					"Serveur BRi en écoute sur le port " + port
			);

			while (!listen_socket.isClosed()) {
				Socket client = listen_socket.accept();

				System.out.println(
						"Nouvelle connexion sur le port " + port
				);

				new ServiceBRi(client).start();
			}

		} catch (IOException e) {
			if (listen_socket == null || !listen_socket.isClosed()) {
				e.printStackTrace();
			}
		}
	}

	// Démarre le serveur dans un thread indépendant.
	public void lancer() {
		Thread thread = new Thread(this);
		thread.start();
	}

	// Arrête proprement le serveur.
	public void fermer() {
		try {
			if (listen_socket != null && !listen_socket.isClosed()) {
				listen_socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
