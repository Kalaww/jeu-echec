package echecs.jeu.IA;

public class ValeursEvaluation {
	
	/**
	 * Valeur du pion
	 */
	public int PION = 5;
	
	/**
	 * Valeur de la tour
	 */
	public int TOUR = 10;
	
	/**
	 * Valeur du cavalier
	 */
	public int CAVALIER = 10;
	
	/**
	 * Valeur du fou
	 */
	public int FOU = 10;
	
	/**
	 * Valeur de la reine
	 */
	public int REINE = 50;
	
	/**
	 * Valeur du bonus de defense
	 */
	public int DEFENSE = 2;
	
	/**
	 * Valeur du malus de danger d'une piece. Dois etre negatif
	 */
	public int DANGER = -1;
	
	/**
	 * Valeur du bonus d'attaque
	 */
	public int ATTAQUE = 1;

}
