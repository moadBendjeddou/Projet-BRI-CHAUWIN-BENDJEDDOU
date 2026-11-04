
package bri;

import java.util.HashMap;
import java.util.Map;

public class ProgrammerRegistry {

    private static final Map<String, Programmer> programmeurs =
            new HashMap<>();

    // Représente un compte programmeur.
    private static class Programmer {
        private final String motDePasse;
        private String adresseFTP;

        public Programmer(String motDePasse, String adresseFTP) {
            this.motDePasse = motDePasse;
            this.adresseFTP = adresseFTP;
        }
    }

    // Création d'un compte programmeur.
    public static synchronized boolean ajouterProgrammer(
            String login,
            String motDePasse,
            String adresseFTP) {

        if (login == null || login.isBlank()
                || motDePasse == null || motDePasse.isBlank()
                || adresseFTP == null || adresseFTP.isBlank()) {
            return false;
        }

        if (programmeurs.containsKey(login)) {
            return false;
        }

        programmeurs.put(
                login,
                new Programmer(motDePasse, adresseFTP)
        );

        return true;
    }

    // Vérification des identifiants.
    public static synchronized boolean authentifier(
            String login,
            String motDePasse) {

        Programmer programmer = programmeurs.get(login);

        return programmer != null
                && programmer.motDePasse.equals(motDePasse);
    }

    // Modification de l'adresse FTP.
    public static synchronized boolean modifierAdresseFTP(
            String login,
            String nouvelleAdresse) {

        Programmer programmer = programmeurs.get(login);

        if (programmer == null || nouvelleAdresse == null
                || nouvelleAdresse.isBlank()) {
            return false;
        }

        programmer.adresseFTP = nouvelleAdresse;
        return true;
    }

    // Récupération de l'adresse FTP.
    public static synchronized String getAdresseFTP(String login) {

        Programmer programmer = programmeurs.get(login);

        if (programmer == null) {
            return null;
        }

        return programmer.adresseFTP;
    }
}
