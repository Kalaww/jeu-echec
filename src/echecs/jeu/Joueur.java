package echecs.jeu;

/**
 * Defini un joueur humain
 */
public class Joueur {

	/**
	 * Defini si le Joueur est humain ou non
	 */

	protected boolean estHumain = true;
	
	/**
	 * Couleur du joueur
	 */
	protected String couleur;
	
	/**
	 * Constructeur
	 */
	public Joueur(){
		
	}
	
	/**
	 * Instancie un joueur selon la couleur c
	 * @param c couleur du joueur
	 */
	public Joueur(String c){
		this.couleur = c;
	}

	/**
	 * Accesseur de la couleur du joueur
	 * @return couleur du joueur
	 */
	public String getCouleur(){
		return couleur;
	}

	/**
	 * Retourne si le joueur est humain
	 * @return True si il l'est sinon false
	 */
	public boolean estHumain(){
		return estHumain;
	}

	/** 
	 * Defini si le Joueur est humain
	 * @param b
	 */
	public void setHumain(boolean b){
		estHumain = b;
	}
}
