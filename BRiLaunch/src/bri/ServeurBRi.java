
package bri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurBRi implements Runnable {

    private final ServerSocket listenSocket;

    public ServeurBRi(int port) {
        try {
            listenSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible d'ouvrir le port " + port, e);
        }
    }

    @Override
    public void run() {
        try {
            while (!listenSocket.isClosed()) {
                Socket client = listenSocket.accept();
                new ServiceBRi(client).start();
            }
        } catch (IOException e) {
            if (!listenSocket.isClosed()) {
                System.err.println(
                        "Erreur du serveur : " + e.getMessage());
            }
        }
    }

    public void lancer() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void fermer() {
        try {
            listenSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
