package echecs.reseau.client;

import echecs.graphisme.jeu.Fenetre;
import echecs.jeu.JeuClient;
/**
 * Classe faisant la transition entre la couche reseau et logique
 *
 */
public class JoueurClient {
	private Client client;
	private String couleur;
	private JeuClient jeu;
	private Fenetre fenetre;
	
	/**
	 * Constructeur
	 */
	public JoueurClient(){
		
	}
	/**
	 * Constructeur
	 * @param cl Client du jeu
	 */
	public JoueurClient(Client cl){
		this.client = cl;
		this.fenetre = new Fenetre(this);
		this.jeu = (JeuClient) fenetre.getJeu();
	}
	/**
	 * Constructeur
	 * @param c La couleur du joueur
	 * @param cl Le Client
	 */
	public JoueurClient(String c, Client cl){
		this.couleur = c;
		this.client = cl;
	}
	/**
	 * Renvoi la couleur du joueur
	 * @return La couleur du joueur
	 */
	public String getCouleur(){
		return this.couleur;
	}
	/**
	 * Setter de la couleur du joueur
	 * @param c La couleur voulu
	 */
	public void setCouleur(String c){
		this.couleur = c;
	}
	/**
	 * Setter de Client
	 * @param client Le Client
	 */
	public void setClient(Client client){
		this.client = client;
	}
	/**
	 * Retourne le Client
	 * @return Le Client
	 */
	public Client getClient(){
		return this.client;
	}
	/**
	 * Permet d'envoyer un message
	 * @param s Le message a envoyer
	 */
	public void send(String s){
		this.client.send(s);
	}
	/**
	 * Gere les messages recu
	 * @param s Le message recu
	 */
	public void recu(String s){
		jeu.lectureMessage(s);
	}
	/**
	 * Permet d'indiquer un TimeOut
	 */
	public void timeout(){
		jeu.timeout();
	}
}
