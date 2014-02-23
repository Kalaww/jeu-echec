package echecs.reseau.serveur;

import java.util.ArrayList;
import java.net.Socket;
import echecs.jeu.JeuServeur;
/**
 * Classe ServerGameManager, permet de faire l'interface entre la couche resau et la couche logique
 * @author Thomas
 *
 */
public class ServerGameManager{
	
	private ArrayList<Client> clients;
	private ServerPlayer jBlanc;
	private ServerPlayer jNoir;
	private JeuServeur jeu;
	/**
	 * Constructeur de la classe
	 */
	public ServerGameManager(){
		clients = new ArrayList<Client>();
	}
	/**
	 * Constructeur de la classe
	 * @param clients La liste des clients en jeu
	 */
	public ServerGameManager(ArrayList<Client> clients){
		this.clients = clients;
	}
	/**
	 * Ajoute un nouveau client au jeu
	 * @param client Le client à ajouter
	 */
	public void ajouterJoueur(Client client){
		this.clients.add(client);
		System.out.println("[SERVER][INFO] Nouveau Joueur");
	}
	/**
	 * Lance la partie
	 */
	public void lancer(){
		if(valable()){
			this.jBlanc =  new ServerPlayer(this.clients.get(0), "Blanc");
			this.jNoir = new ServerPlayer(this.clients.get(1), "Noir");
			jeu = new JeuServeur(this);
			jeu.demarrerPartie();
			
		}else{
			System.out.println("[ERREUR] Impossible de crÃ©er une partie: nombre de clients incompatible");
		}
	}
	/**
	 * Verifie si les conditions pour lancer une partie sont présentes.
	 * @return
	 */
	public boolean valable(){
		if(this.clients.size() == 2){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Envoi un message a tout le monde
	 * @param s Le message a envoyer
	 */
	public void sendAll(String s){	
		for(int i =0;i<clients.size();i++){
			clients.get(i).send(s);
		}
	}
	/**
	 * Envoi un message au Blanc
	 * @param s Le message
	 */
	public void sendB(String s){
		jBlanc.send(s);
	}
	/**
	 * Envoi un message au Noir
	 * @param s Le message a envoyer
	 */
	public void sendN(String s){
		jNoir.send(s);
	}
	/**
	 * Analyse les messages recu
	 * @param s Le message recu
	 * @param socket
	 */
	public void recu(String s, Socket socket){
		if(jeu != null){
			if(socket == this.jBlanc.getClient().getSocket()){
				if(s.startsWith("/say ")){
					s = "B"+s;	
					sendAll(s);
				}
				else{
					s = "B"+s;
				}
			}
			else{
				if(s.startsWith("/say ")){
					s = "N"+s;	
					sendAll(s);
				}
				else{
					s = "N"+s;
				}
			}
			System.out.println("Message recu :"+s);
			jeu.lectureMessage(s);
		}
	}
	/**
	 * Enleve le Client de la liste
	 * @param s Le client a enlever
	 */
	public void enleverClient(Socket s){
		for(int i=0; i<clients.size();i++){
			if(clients.get(i).getSocket() == s){
				clients.remove(i);
			}
		}
	}
}