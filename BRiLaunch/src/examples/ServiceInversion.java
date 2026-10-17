
package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bri.Service;

public class ServiceInversion implements Service {

    private final Socket client;

    public ServiceInversion(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            PrintWriter out = new PrintWriter(
                    client.getOutputStream(), true);

            out.println("Tapez un texte à inverser :");

            String texte = in.readLine();

            if (texte != null) {
                String resultat =
                        new StringBuilder(texte).reverse().toString();

                out.println(resultat);
            }

        } catch (IOException e) {
            System.err.println(
                    "Erreur du service inversion : " + e.getMessage());
        }
    }

    public static String toStringue() {
        return "Inversion de texte";
    }
}
