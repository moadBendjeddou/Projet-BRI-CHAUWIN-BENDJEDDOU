
package examples;

import bri.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class ServiceInversion implements Service {

    private final Socket client;

    // Constructeur obligatoire : il reçoit le socket du client.
    public ServiceInversion(Socket client) {
        this.client = client;
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

            // Demande le texte au client.
            out.println("Tapez le texte à inverser :");

            String texte = in.readLine();

            if (texte == null) {
                return;
            }

            // Inverse le texte.
            String texteInverse = new StringBuilder(texte)
                    .reverse()
                    .toString();

            // Renvoie le résultat au client.
            out.println(texteInverse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Nom affiché dans le menu des services.
    public static String toStringue() {
        return "Inversion de texte";
    }
}
