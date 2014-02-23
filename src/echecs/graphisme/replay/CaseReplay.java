package echecs.graphisme.replay;

import java.awt.Color;

import echecs.graphisme.Case;

/**
 * Une case de la grille pour l'interface graphique du mode replay
 */
public class CaseReplay extends Case{
	
	/**
	 * Reference de la fenetre
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * Constructeur d'une case vide
	 * @param x coordonnee en abscisse
	 * @param y coordonnee en ordonnee
	 * @param backgroundColor couleur du fond
	 */
	public CaseReplay(int x, int y, Color backgroundColor, ReplayFenetre fenetre){
		super(x, y, backgroundColor);
		this.fenetre = fenetre;
	}
}
