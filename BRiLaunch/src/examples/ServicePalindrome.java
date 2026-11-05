
package examples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

import bri.service.Service;

public class ServicePalindrome implements Service {

    private final Socket client;

    public ServicePalindrome(Socket client) {
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

            out.println("Tapez un mot ou une phrase :");

            String texte = in.readLine();

            if (texte == null) {
                return;
            }

            String nettoye = texte
                    .replaceAll("[^\\p{L}\\p{N}]", "")
                    .toLowerCase();

            String inverse = new StringBuilder(nettoye)
                    .reverse()
                    .toString();

            if (nettoye.equals(inverse)) {
                out.println("C'est un palindrome !");
            } else {
                out.println("Ce n'est pas un palindrome.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toStringue() {
        return "Vérification de palindrome";
    }
}
