package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.List;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partagée en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = null;
	}
	private static List<Class<?>> servicesClasses;

// ajoute une classe de service aprés contrôle de la norme BLTi
	public static void addService(Class<?> classe) throws ClassInvalideException {
		 if (classe == null) {
	            throw new ClassInvalideException("Classe inexistante.");
	        }
	        int mod = classe.getModifiers();

	        if (!Modifier.isPublic(mod)) {
	            throw new ClassInvalideException("La classe doit être publique.");
	        }
	        if (Modifier.isAbstract(mod) || classe.isInterface()) {
	            throw new ClassInvalideException("La classe ne doit pas être abstraite.");
	        }
	        if (!Service.class.isAssignableFrom(classe)) {
	            throw new ClassInvalideException("La classe doit implémenter bri.Service.");
	        }
	        Constructor<?> constructeur;
			try {
				constructeur = classe.getConstructor(Socket.class);
				
				if (!Modifier.isPublic(constructeur.getModifiers())) {
		            throw new ClassInvalideException("Le constructeur doit être public.");
		        }

		        Field socket = classe.getDeclaredField("client");

		        if (!Modifier.isPrivate(socket.getModifiers())
		                || !Modifier.isFinal(socket.getModifiers())
		                || socket.getType() != Socket.class) {
		            throw new ClassInvalideException("Il faut un attribut private final Socket client.");
		        }

		        Method methode = classe.getMethod("toStringue");

		        if (!Modifier.isPublic(methode.getModifiers())
		                || !Modifier.isStatic(methode.getModifiers())
		                || methode.getReturnType() != String.class
		                || methode.getParameterCount() != 0) {
		            throw new ClassInvalideException("Il faut public static String toStringue().");
		        }

		        synchronized (servicesClasses) {
		            if (servicesClasses.contains(classe)) {
		                throw new ClassInvalideException("Service déjà enregistré.");
		            }

		            servicesClasses.add(classe);
		        }
			} catch (NoSuchMethodException | SecurityException  | NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
// renvoie la classe de service (numService -1)	
	public static Class<? extends Service> getServiceClass(int numService) {
		return Service.class;
	}
	
// liste les activités présentes
	public static String toStringue() {
		if(servicesClasses == null) {return "il n'y a pas de service disponible";}
		StringBuilder resultat =
                new StringBuilder("Activités présentes :##");
		
		for (int i = 0; i < servicesClasses.size(); i++) {
            Class<?> classe = servicesClasses.get(i);

            try {
                Method methode = classe.getMethod("toStringue");
                String nom = (String) methode.invoke(null);

                resultat.append(i + 1)
                        .append(" - ")
                        .append(nom)
                        .append("##");
            }catch(Exception e) {}
        }       
		return resultat.toString();
	}

}
