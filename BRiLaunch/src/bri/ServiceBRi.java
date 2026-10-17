package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

public class ServiceBRi implements Runnable {

    private final Socket client;

    public ServiceBRi(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try (Socket socket = client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socket.getOutputStream(), true)) {

            out.println(ServiceRegistry.toStringue()
                    + "##Tapez le numéro du service désiré :");

            String ligne = in.readLine();

            if (ligne == null) {
                return;
            }

            int choix;

            try {
                choix = Integer.parseInt(ligne.trim());
            } catch (NumberFormatException e) {
                out.println("Choix invalide.");
                return;
            }

            Class<?> classe = ServiceRegistry.getServiceClass(choix);

            if (classe == null) {
                out.println("Aucun service correspondant à ce numéro.");
                return;
            }

            Constructor<?> constructeur =
                    classe.getConstructor(Socket.class);

            Object instance = constructeur.newInstance(socket);

            ((Service) instance).run();

        } catch (Exception e) {
            System.err.println("Erreur du service BRi : " + e.getMessage());
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
