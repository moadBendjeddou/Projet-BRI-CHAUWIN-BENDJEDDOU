
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Application {

	private static final int PORT_SERVICE = 3000;
	private static final String HOST = "localhost";

	public static void main(String[] args) {

		try (
				Socket socket = new Socket(HOST, PORT_SERVICE);

				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream())
				);

				PrintWriter out = new PrintWriter(
						socket.getOutputStream(), true
				);

				BufferedReader clavier = new BufferedReader(
						new InputStreamReader(System.in)
				)
		) {

			// Affichage du menu envoyé par le serveur.
			String menu = in.readLine();
			System.out.println(menu);

			// Saisie du numéro de service.
			System.out.print("Votre choix : ");
			String choix = clavier.readLine();

			out.println(choix);

			// Affichage de la demande du service.
			String demande = in.readLine();
			System.out.println(demande);

			// Saisie du texte à traiter.
			System.out.print("> ");
			String texte = clavier.readLine();

			out.println(texte);

			// Affichage du résultat.
			String resultat = in.readLine();
			System.out.println("Résultat : " + resultat);

		} catch (Exception e) {
			System.err.println("Erreur de connexion au serveur.");
			e.printStackTrace();
		}
	}
}
