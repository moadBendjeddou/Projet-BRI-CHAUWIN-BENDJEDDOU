
package bri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ProgrammerHandler implements Runnable {

    private final Socket client;

    public ProgrammerHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream())
                );

                PrintWriter out = new PrintWriter(
                        client.getOutputStream(), true
                )
        ) {

            out.println("=== Espace Programmeur ===");
            out.println("1 - Créer un compte");
            out.println("2 - Se connecter");
            out.println("Votre choix :");

            String choix = in.readLine();

            if ("1".equals(choix)) {
                creerCompte(in, out);

            } else if ("2".equals(choix)) {
                seConnecter(in, out);

            } else {
                out.println("Choix invalide.");
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void creerCompte(
            BufferedReader in,
            PrintWriter out
    ) throws IOException {

        out.println("Choisissez un login :");
        String login = in.readLine();

        out.println("Choisissez un mot de passe :");
        String motDePasse = in.readLine();

        out.println("Entrez votre adresse FTP :");
        String adresseFTP = in.readLine();

        boolean resultat = ProgrammerRegistry.ajouterProgrammer(
                login,
                motDePasse,
                adresseFTP
        );

        if (resultat) {
            out.println("Compte créé avec succès !");
        } else {
            out.println("Création impossible. Vérifiez vos informations.");
        }
    }

    private void seConnecter(
            BufferedReader in,
            PrintWriter out
    ) throws IOException {

        out.println("Login :");
        String login = in.readLine();

        out.println("Mot de passe :");
        String motDePasse = in.readLine();

        if (ProgrammerRegistry.authentifier(login, motDePasse)) {

            out.println("Connexion réussie !");
            out.println(
                    "Adresse FTP : "
                            + ProgrammerRegistry.getAdresseFTP(login)
            );

        } else {
            out.println("Identifiants incorrects.");
        }
    }
}
