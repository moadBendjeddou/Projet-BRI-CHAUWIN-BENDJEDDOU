
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ClientBRi {


	private static final String HOST = "localhost";

	public static void lancer(int port) {

		try (
				Socket socket = new Socket(HOST, port);

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
			while(true) {
				//affichage du texte serveur
				String txtServ = in.readLine();
				if(txtServ==null) { System.out.println("le serveur a racroché\n");break;}
				System.out.println(sautDeLigne(txtServ));
	
				//envoie du texte client client
				System.out.print("> ");
				String txtCli = clavier.readLine();
				if (txtCli.isBlank()) { System.out.println("On raccroche avec le serveur\n");socket.close();break;}
				out.println(txtCli);
			}
			

		} catch (Exception e) {
			System.err.println("Erreur de connexion au serveur.");
			e.printStackTrace();
		}
	}
	
	public static String sautDeLigne(String texte) {
	    if (texte == null) {
	        return null;
	    }
	    return texte.replace("##", "\n");
	}
}
