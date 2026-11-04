
package appli;

import bri.ServeurBRi;
import bri.ServeurProgrammer;
import bri.ServiceRegistry;

import examples.ServiceInversion;
import examples.ServiceMajuscules;
import examples.ServiceMinuscules;
import examples.ServicePalindrome;

public class BRiLaunch {

	public static final int PORT_AMA = 3000;
	public static final int PORT_PROG = 3001;

	public static void main(String[] args) {

		try {
			System.out.println("=== Démarrage de BRiLaunch ===");

			// Enregistrement des services amateurs.
			ServiceRegistry.addService(ServiceInversion.class);
			ServiceRegistry.addService(ServiceMajuscules.class);
			ServiceRegistry.addService(ServiceMinuscules.class);
			ServiceRegistry.addService(ServicePalindrome.class);

			System.out.println("\nServices enregistrés :");
			System.out.println(ServiceRegistry.toStringue());

			// Démarrage du serveur amateur.
			ServeurBRi serveurAma = new ServeurBRi(PORT_AMA);
			serveurAma.lancer();

			// Démarrage du serveur programmeur.
			ServeurProgrammer serveurProg =
					new ServeurProgrammer(PORT_PROG);
			serveurProg.lancer();

			System.out.println("BRiLaunch est prêt.");

		} catch (Exception e) {
			System.err.println("Erreur au démarrage de BRiLaunch :");
			e.printStackTrace();
		}
	}
}
