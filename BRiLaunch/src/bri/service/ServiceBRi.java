
package bri.service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

class ServiceBRi implements Runnable {

	private Socket client;

	ServiceBRi(Socket socket) {
		client = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(client.getInputStream())
			);

			PrintWriter out = new PrintWriter(
					client.getOutputStream(), true
			);

			out.println(ServiceRegistry.toStringue()
					+ "##Tapez le numéro de service désiré :");

			int choix = Integer.parseInt(in.readLine());

			Class<?> classe = ServiceRegistry.getServiceClass(choix);

			if (classe == null) {
				out.println("Choix invalide.");
				return;
			}

			Service service = (Service) classe
					.getConstructor(Socket.class)
					.newInstance(client);

			service.run();

		} catch (IOException | InstantiationException
				 | IllegalAccessException
				 | IllegalArgumentException
				 | InvocationTargetException
				 | NoSuchMethodException
				 | SecurityException e) {

			e.printStackTrace();

		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Lancement du service
	public void start() {
		new Thread(this).start();
	}
}
