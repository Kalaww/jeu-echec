package echecs.graphisme;

import javax.swing.JPanel;

/**
 * Affiche les pieces prises (commun au mode de jeu classique et mode replay)
 */
public class PiecesPrises extends JPanel{
	
	protected boolean priseBlanc;
	
	/**
	 * Constructeur de la classe PiecesPrises qui permet d'afficher les pieces prises
	 */
	public PiecesPrises(boolean priseBlanc){
		this.priseBlanc = priseBlanc;
	}
}
