package echecs.jeu.IA;

import java.util.ArrayList;

import echecs.Echecs;
import echecs.jeu.Jeu;
import echecs.jeu.Joueur;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Roi;
import echecs.utils.Coordonnee;

/**
 * IA qui joue de facon aleatoire
 */
public class IAaleatoire extends Joueur implements IA{
	
	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Coordonnee d'arrivee de la piece a jouer
	 */
	private Coordonnee coordonneeAJouer;

	/**
	 * Constructeur de l'IA
	 * @param c la couleur de l'IA
	 * @param jeu reference du jeu
	 */
	public IAaleatoire(String couleur, Jeu jeu) {
		super(couleur);
		this.jeu = jeu;
		this.estHumain = false;
	}

	@Override
	public void jouer(){
		this.coordonneeAJouer = null;
		
		//Recupere toutes les pieces de la couleurs de l'ia
		ArrayList<Piece> pieces = (this.getCouleur().equals("BLANC"))? jeu.getPlateau().getPiecesBlanches() : jeu.getPlateau().getPiecesNoires();
		
		boolean echec = true;
		int x = -1;
		int y = -1;
		Piece pieceABouger = null;
		while(echec){
			int index;
			pieceABouger = null;
			do{
				index = (int) (Math.random()*pieces.size());
				pieceABouger = pieces.get(index);
			}while(pieceABouger.casesPossibles().isEmpty());
			
			int j = (int) (Math.random()*pieceABouger.casesPossibles().size());
			x = pieceABouger.casesPossibles().get(j).x;
			y = pieceABouger.casesPossibles().get(j).y;
			
			Coordonnee coordPieceABouger = new Coordonnee(pieceABouger.getX(), pieceABouger.getY());
			if(pieceABouger.deplacer(x, y)){
				if(!jeu.getPlateau().getRoi(this.couleur).estEchec()){
					echec = false;
				}
			}else{
				Echecs.addLog("Erreur dans la recherche de coup pour l'ia aleatoire", Echecs.TypeLog.ERREUR);
			}
			jeu.getPlateau().annulerDernierCoup(false);
			jeu.setPieceSelectionee(coordPieceABouger.x, coordPieceABouger.y);
		}
		this.coordonneeAJouer = new Coordonnee(x,y);
	}

	@Override
	public Coordonnee getCoordonneeAJouer() {
		return coordonneeAJouer;
	}

}
