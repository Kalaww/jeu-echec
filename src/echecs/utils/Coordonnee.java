package echecs.utils;

/**
 * Permet de stocker des coordonnees
 */
public class Coordonnee {

	/**
	 * Coordonnee sur abscisse
	 */
	public int x;
	
	/**
	 * Coordonnee sur ordonnee
	 */
	public int y;
	
	/**
	 * Constructeur pour x et y
	 * @param x valeur en abscisse
	 * @param y caleur en ordonnee
	 */
	public Coordonnee(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructeur : initialise x et y en 0
	 */
	public Coordonnee(){
		this(0,0);
	}
	
	/**
	 * Constructeur : initialise x et y sur la meme valeur
	 * @param xy valeur pour x et pour y
	 */
	public Coordonnee(int xy){
		this(xy, xy);
	}
	
	/**
	 * Setter pour x et y
	 * @param x nouvelle valeur de x
	 * @param y nouvelle valeur de y
	 */
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Converti l'object en String
	 */
	public String toString(){
		return "["+x+", "+y+"]";
	}
}
