
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Application {

    private static final String HOST = "localhost";
    private static final int PORT_SERVICE = 3000;

    public static void main(String[] args) {

        try (
            Socket socket = new Socket(HOST, PORT_SERVICE);

            BufferedReader serveur = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter sortie = new PrintWriter(
                    socket.getOutputStream(), true);

            BufferedReader clavier = new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
            System.out.println("Connecté au serveur BRi.");

            String menu = serveur.readLine();

            if (menu == null) {
                System.out.println("Le serveur a fermé la connexion.");
                return;
            }

            System.out.println(menu.replace("##", "\n"));

            sortie.println(clavier.readLine());

            String question = serveur.readLine();

            if (question == null) {
                System.out.println("Aucune réponse du serveur.");
                return;
            }

            System.out.println(question);

            if (question.contains("invalide")
                    || question.contains("invalide.")) {
                return;
            }

            String texte = clavier.readLine();
            sortie.println(texte);

            String resultat = serveur.readLine();

            if (resultat != null) {
                System.out.println("Résultat : " + resultat);
            }

        } catch (IOException e) {
            System.err.println(
                    "Connexion impossible : " + e.getMessage());
        }
    }
}
