package echecs.graphisme.replay;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComponent;

import echecs.graphisme.Case;
import echecs.graphisme.Case.Etat;

/**
 * Une grille pour l'interface graphique du mode replay
 */
public class GrilleReplay extends JComponent{
	
	/**
	 * Reference de la fenetre qui contient la grille
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre qui contient la grille
	 */
	public GrilleReplay(ReplayFenetre fenetre){
		super();
		this.fenetre = fenetre;
		initGrille();
		updateGrille();
	}
	
	/**
	 * Initialise les cases de la grille
	 */
	private void initGrille(){
		this.setLayout(new GridLayout(8,8));
		Color backgroundColor = Color.WHITE;
		for(int i = 7; i >= 0; i--){
			for(int j = 0; j < 8; j++){
				CaseReplay c = new CaseReplay(j, i, backgroundColor, fenetre);
				if(j != 7){
					backgroundColor = (backgroundColor.equals(Color.GRAY))? Color.WHITE : Color.GRAY;
				}
				this.add(c);
			}
		}
	}
	
	/**
	 * Met a jour toutes les cases de la grille selon le plateau de jeu
	 */
	public void updateGrille(){
		Component[] contenu = this.getComponents();
		for(int i = 0; i < contenu.length; i++){
			//Si le composant est une Case
			if(contenu[i].getClass().equals(CaseReplay.class)){
				//Mise a jour de la case selon sa m�me position dans le plateau
				Case c = (Case)contenu[i];
				c.updateCase(fenetre.getReplay().getPlateau().getCase(c.getXTableau(), c.getYTableau()));
			}
		}
	}
	
	/**
	 * Passe l'etat de la case en (x,y) en DERNIER_COUP
	 * @param x coordonnee de la case en x
	 * @param y coordonnee de la case en y
	 */
	public void setCaseDernierCoup(int x, int y){
		Component[] contenu = this.getComponents();
		for(int i = 0; i < contenu.length; i++){
			if(contenu[i].getClass().equals(CaseReplay.class)){
				CaseReplay c = (CaseReplay)contenu[i];
				if(c.getXTableau() == x && c.getYTableau() == y){
					c.setEtat(Case.Etat.DERNIER_COUP);
				}
			}
		}
	}
	
	/**
	 * Remet tous les etats des cases a RIEN
	 */
	public void resetEtatCases(){
		Component[] contenu = this.getComponents();
		for(int i = 0; i < contenu.length; i++){
			if(contenu[i].getClass().equals(CaseReplay.class)){
				CaseReplay c = (CaseReplay)contenu[i];
				if(c.getEtat() != Etat.RIEN){
					c.setEtat(Case.Etat.RIEN);
				}
			}
		}
	}

}
