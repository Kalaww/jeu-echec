package echecs.reseau.serveur;

import java.net.*;
import java.io.IOException;
/**
 * Classe principale du serveur
 *
 */
public class Server extends Thread{
	 private ServerGameManager gm;
	 /**
	  * Cree le serveur
	  */
	 public Server(){
		 start();
	 }
	 
	 public void run(){
		gm = new ServerGameManager();
		ServerSocket ssocket;
		try {
			ssocket = new ServerSocket(1337);
			System.out.println("[SERVER][INFO] Lancement du serveur sur le port 1337");
			do{
			    System.out.println("[SERVER][INFO] En attente");
			    Socket s = ssocket.accept();
			    gm.ajouterJoueur(new Client(s, gm));    
			}while(!gm.valable());
			System.out.println("[SERVER] Lancement d'une nouvelle partie");
			gm.lancer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
