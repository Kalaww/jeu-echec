package echecs.jeu.IA;

import echecs.Echecs;
import echecs.jeu.Jeu;
import echecs.jeu.Joueur;
import echecs.utils.Coordonnee;

/**
 * IA complete : utilise deux IA, IA ouverture puis IA minimax
 */
public class IAcomplet extends Joueur implements IA{
	
	/**
	 * Coordonnee d'arrivee de la piece a jouer
	 */
	private Coordonnee coordonneeAJouer;
	
	/**
	 * Reference de l'ia minimax
	 */
	private IAminimax iaMinimax;
	
	/**
	 * Reference de l'ia ouverture
	 */
	private IAouverture iaOuverture;
	
	/**
	 * Boolean si vrai, c'est au tour de l'ia minimax
	 */
	private boolean tourMinimax;
	
	/**
	 * Constructeur
	 * @param couleur couleur de l'ia
	 * @param jeu reference du jeu
	 * @param iaThread reference du thread dans lequel est l'ia
	 * @param valeurs reference des variables d'evaluations
	 */
	public IAcomplet(String couleur, Jeu jeu, IAThread iaThread, ValeursEvaluation valeurs){
		super(couleur);
		this.estHumain = false;
		this.tourMinimax = false;
		
		iaMinimax = new IAminimax(couleur, jeu, iaThread, valeurs);
		iaOuverture = new IAouverture(couleur, jeu);
	}
	
	@Override
	public void jouer(){
		if(tourMinimax){
			if(Echecs.DEBUG) Echecs.addLog("< Joue facon Minimax >", Echecs.TypeLog.INFO);
			iaMinimax.jouer();
			this.coordonneeAJouer = iaMinimax.getCoordonneeAJouer();
		}else{
			iaOuverture.jouer();
			this.coordonneeAJouer = iaOuverture.getCoordonneeAJouer();
			if(this.coordonneeAJouer == null){
				tourMinimax = true;
				if(Echecs.DEBUG) Echecs.addLog("-- Changement d'IA --", Echecs.TypeLog.INFO);
				iaMinimax.jouer();
				this.coordonneeAJouer = iaMinimax.getCoordonneeAJouer();
			}else{
				if(Echecs.DEBUG) Echecs.addLog("< Joue facon ouverture >", Echecs.TypeLog.INFO);
			}
		}
	}

	@Override
	public Coordonnee getCoordonneeAJouer() {
		return this.coordonneeAJouer;
	}
}
