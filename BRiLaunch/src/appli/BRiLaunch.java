	package appli;
	
	import java.net.MalformedURLException;
	import java.net.URL;
	import java.net.URLClassLoader;
	import java.io.InputStream;


	import java.util.Scanner;
	
	import bri.ServeurBRi;
	import bri.ServiceRegistry;
	
	public class BRiLaunch {
		private final static int PORT_SERVICE = 3000;
		
		public static void main(String[] args) {
			@SuppressWarnings("resource")
			Scanner clavier = new Scanner(System.in);
			
			String fileNameURL = "ftp://localhost:2121/mesClasse/";
			URLClassLoader urlcl = null;
			try {
				urlcl = new URLClassLoader(new URL[] {new URL(fileNameURL)});
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
			System.out.println("Pour ajouter une activité, celle-ci doit étre présente sur votre serveur ftp");
			System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
			System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
			
			new Thread(new ServeurBRi(PORT_SERVICE)).start();
			
			while (true){
					try {
						String classeName = clavier.next();
						// charger la classe et la déclarer au ServiceRegistry
						 Class<?> classe = urlcl.loadClass(classeName);

						ServiceRegistry.addService(classe);
						
						System.out.println("service loaded");
					} catch (Exception e) {
						System.out.println(e);
					}
				}		
		}
	}
