
package bri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurProgrammer implements Runnable {

    private final int port;
    private ServerSocket serveur;

    public ServeurProgrammer(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            serveur = new ServerSocket(port);

            System.out.println(
                    "Serveur programmeur en écoute sur le port " + port
            );

            while (!serveur.isClosed()) {
                Socket client = serveur.accept();

                System.out.println(
                        "Connexion d'un programmeur."
                );

                new Thread(new ProgrammerHandler(client)).start();
            }

        } catch (IOException e) {
            if (serveur == null || !serveur.isClosed()) {
                e.printStackTrace();
            }
        }
    }

    public void lancer() {
        new Thread(this).start();
    }

    public void fermer() {
        try {
            if (serveur != null && !serveur.isClosed()) {
                serveur.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
