package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistry {

	private static final List<Class<?>> servicesClasses =
			new ArrayList<>();

	/**
	 * Ajoute un service au registre après avoir vérifié
	 * qu'il respecte la norme BRi.
	 */
	public static synchronized void addService(Class<?> classe)
			throws Exception {

		// Vérifie que la classe est publique.
		if (!Modifier.isPublic(classe.getModifiers())) {
			throw new Exception("La classe doit être publique.");
		}

		// Vérifie que la classe n'est ni abstraite ni une interface.
		if (Modifier.isAbstract(classe.getModifiers())
				|| classe.isInterface()) {
			throw new Exception("Le service doit être une classe concrète.");
		}

		// Vérifie que la classe implémente Service.
		if (!Service.class.isAssignableFrom(classe)) {
			throw new Exception("La classe doit implémenter Service.");
		}

		// Vérifie la présence d'un constructeur public avec Socket.
		Constructor<?> constructeur =
				classe.getConstructor(Socket.class);

		if (!Modifier.isPublic(constructeur.getModifiers())) {
			throw new Exception("Le constructeur doit être public.");
		}

		// Vérifie la présence d'un attribut client privé et final.
		Field client = classe.getDeclaredField("client");

		int modificateurs = client.getModifiers();

		if (!Modifier.isPrivate(modificateurs)
				|| !Modifier.isFinal(modificateurs)
				|| client.getType() != Socket.class) {
			throw new Exception(
					"L'attribut client doit être private final Socket."
			);
		}

		// Vérifie la méthode publique, statique et sans paramètre toStringue().
		Method methode = classe.getMethod("toStringue");

		if (!Modifier.isPublic(methode.getModifiers())
				|| !Modifier.isStatic(methode.getModifiers())
				|| methode.getParameterCount() != 0
				|| methode.getReturnType() != String.class) {
			throw new Exception(
					"La méthode toStringue() doit être public static "
							+ "et retourner un String."
			);
		}

		// Évite d'ajouter deux fois le même service.
		if (servicesClasses.contains(classe)) {
			throw new Exception("Ce service est déjà enregistré.");
		}

		servicesClasses.add(classe);

		System.out.println("Service ajouté : " + classe.getName());
	}

	/**
	 * Retourne la classe du service correspondant au numéro choisi.
	 * Les numéros affichés dans le menu commencent à 1.
	 */
	public static synchronized Class<?> getServiceClass(int numero) {

		if (numero < 1 || numero > servicesClasses.size()) {
			return null;
		}

		return servicesClasses.get(numero - 1);
	}

	/**
	 * Construit le menu des services disponibles.
	 */
	public static synchronized String toStringue() {

		if (servicesClasses.isEmpty()) {
			return "Aucun service disponible.";
		}

		StringBuilder menu = new StringBuilder();

		for (int i = 0; i < servicesClasses.size(); i++) {
			Class<?> classe = servicesClasses.get(i);

			try {
				Method methode = classe.getMethod("toStringue");
				String description = (String) methode.invoke(null);

				menu.append(i + 1)
						.append(" - ")
						.append(description)
						.append("\n");

			} catch (Exception e) {
				menu.append(i + 1)
						.append(" - ")
						.append(classe.getSimpleName())
						.append("\n");
			}
		}

		return menu.toString();
	}
}