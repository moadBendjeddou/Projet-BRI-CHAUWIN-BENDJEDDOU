
package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceRegistry {

    private static final List<Class<?>> servicesClasses =
            Collections.synchronizedList(new ArrayList<>());

    public static void addService(Class<?> classe) throws Exception {

        if (classe == null) {
            throw new Exception("Classe inexistante.");
        }

        int mod = classe.getModifiers();

        if (!Modifier.isPublic(mod)) {
            throw new Exception("La classe doit être publique.");
        }

        if (Modifier.isAbstract(mod) || classe.isInterface()) {
            throw new Exception("La classe ne doit pas être abstraite.");
        }

        if (!Service.class.isAssignableFrom(classe)) {
            throw new Exception("La classe doit implémenter bri.Service.");
        }

        Constructor<?> constructeur =
                classe.getConstructor(Socket.class);

        if (!Modifier.isPublic(constructeur.getModifiers())) {
            throw new Exception("Le constructeur doit être public.");
        }

        Field socket = classe.getDeclaredField("client");

        if (!Modifier.isPrivate(socket.getModifiers())
                || !Modifier.isFinal(socket.getModifiers())
                || socket.getType() != Socket.class) {
            throw new Exception(
                    "Il faut un attribut private final Socket client.");
        }

        Method methode = classe.getMethod("toStringue");

        if (!Modifier.isPublic(methode.getModifiers())
                || !Modifier.isStatic(methode.getModifiers())
                || methode.getReturnType() != String.class
                || methode.getParameterCount() != 0) {
            throw new Exception(
                    "Il faut public static String toStringue().");
        }

        synchronized (servicesClasses) {
            if (servicesClasses.contains(classe)) {
                throw new Exception("Service déjà enregistré.");
            }

            servicesClasses.add(classe);
        }
    }

    public static Class<?> getServiceClass(int numero) {
        synchronized (servicesClasses) {
            if (numero < 1 || numero > servicesClasses.size()) {
                return null;
            }

            return servicesClasses.get(numero - 1);
        }
    }

    public static String toStringue() {
        StringBuilder resultat =
                new StringBuilder("Activités présentes :##");

        synchronized (servicesClasses) {
            for (int i = 0; i < servicesClasses.size(); i++) {
                Class<?> classe = servicesClasses.get(i);

                try {
                    Method methode = classe.getMethod("toStringue");
                    String nom = (String) methode.invoke(null);

                    resultat.append(i + 1)
                            .append(" - ")
                            .append(nom)
                            .append("##");
                } catch (Exception e) {
                    resultat.append(i + 1)
                            .append(" - ")
                            .append(classe.getSimpleName())
                            .append("##");
                }
            }
        }

        return resultat.toString();
    }
}
