
package examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

import bri.service.Service;

public class ServiceMinuscules implements Service {

    private final Socket client;

    public ServiceMinuscules(Socket client) {
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

            out.println("Tapez le texte à convertir en minuscules :");

            String texte = in.readLine();

            if (texte == null) {
                return;
            }

            out.println(texte.toLowerCase());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toStringue() {
        return "Conversion en minuscules";
    }
}
