
package appli;

import java.util.Scanner;

import bri.ServeurBRi;
import bri.ServiceRegistry;

public class BRiLaunch {

    private static final int PORT_SERVICE = 3000;

    public static void main(String[] args) {

        ServeurBRi serveur = new ServeurBRi(PORT_SERVICE);
        serveur.lancer();

        System.out.println("Bienvenue dans BRiLaunch !");
        System.out.println("Serveur démarré sur le port "
                + PORT_SERVICE);
        System.out.println(
                "Saisissez le nom complet d'une classe de service.");
        System.out.println(
                "Exemple : examples.ServiceInversion");
        System.out.println("Tapez quit pour arrêter le programme.");

        try (Scanner clavier = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String nomClasse = clavier.nextLine().trim();

                if (nomClasse.equalsIgnoreCase("quit")) {
                    serveur.fermer();
                    break;
                }

                if (nomClasse.isEmpty()) {
                    continue;
                }

                try {
                    Class<?> classe = Class.forName(nomClasse);

                    ServiceRegistry.addService(classe);

                    System.out.println("Service ajouté : "
                            + classe.getSimpleName());

                } catch (Exception e) {
                    System.out.println(
                            "Impossible d'ajouter le service : "
                            + e.getMessage());
                }
            }
        }

        System.out.println("Serveur arrêté.");
    }
}
