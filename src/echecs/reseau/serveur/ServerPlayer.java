package echecs.reseau.serveur;

/**
 * Classe ServerPlayer 
 * Represente un joueur
 *
 */
public class ServerPlayer{
	private Client client;
	private String couleur;
	/**
	 * Constructeur de ServerPlayer
	 * @param client Le client
	 * @param couleur La couleur
	 */
	public ServerPlayer(Client client, String couleur){
		this.couleur = couleur;
		this.client = client;
	}
	/**
	 * Retoune le client
	 * @return Le client
	 */
	public Client getClient(){
		return client;
	}
	/**
	 * Retourne la couleur
	 * @return la couleur
	 */
	public String getCouleur(){
		return couleur;
	}
	/**
	 * Permet de d'attribuer la couleur
	 * @param couleur La couleur a attribuer
	 */
	public void setCouleur(String couleur){
		this.couleur = couleur;
	}
	/**
	 * Envoi un message a ce joueur
	 * @param s le message
	 */
	public void send(String s){
		this.client.send(s);
	}
}