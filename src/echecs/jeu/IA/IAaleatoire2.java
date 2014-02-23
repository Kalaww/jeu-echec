package echecs.jeu.IA;

import java.util.ArrayList;

import echecs.jeu.Jeu;
import echecs.jeu.Joueur;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Roi;
import echecs.utils.Coordonnee;

/**
 * IA qui joue aleatoirement, mais en evitant de se faire prendre ses pieces
 */
public class IAaleatoire2 extends Joueur implements IA{

	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Coordonnee d'arrivee de la piece a jouer
	 */
	private Coordonnee coordonneeAJouer;
	
	/**
	 * Reference d'une ia qui joue aleatoirement
	 */
	private IAaleatoire iaAleatoire;

	/**
	 * Constructeur de l'IA
	 * @param couleur la couleur de l'IA
	 * @param jeu reference du jeu
	 */
	public IAaleatoire2(String couleur, Jeu jeu) {
		super(couleur);
		this.jeu = jeu;
		this.iaAleatoire = new IAaleatoire(couleur, jeu);
		this.estHumain = false;
	}
	
	@Override
	public void jouer(){
		this.coordonneeAJouer = null;
		ArrayList<Piece> p;
		ArrayList<Piece> p_ennemie;
		if(this.getCouleur().equals("BLANC")){
			p = jeu.getPlateau().getPiecesBlanches();
			p_ennemie = jeu.getPlateau().getPiecesNoires();
		}else{
			p = jeu.getPlateau().getPiecesNoires();
			p_ennemie = jeu.getPlateau().getPiecesBlanches();
		}
		
		for(Piece c: p){
			if(c.getFamille().equals("ROI")){
				Roi b = (Roi) c;
				if(b.estEchec()){
					if(!b.estEchecEtMat()){
						this.iaAleatoire.jouer();
						this.coordonneeAJouer = this.iaAleatoire.getCoordonneeAJouer();
						return;
					}
				}
			}
		}
		
		for(Piece i: p){
			for(Piece pp: p_ennemie){
				ArrayList<Coordonnee> cases = pp.casesPossibles();
				for(Coordonnee c: cases){
					if(c.x == i.getX() && c.y == i.getY()){	
						if(!i.casesPossibles().isEmpty()){
							int index = (int) (Math.random()*i.casesPossibles().size());
							Coordonnee coordAJouer = new Coordonnee(i.casesPossibles().get(index).x,i.casesPossibles().get(index).y);
							Coordonnee coordPieceAJouer = new Coordonnee(i.getX(), i.getY());
							boolean echec = true;
							if(i.deplacer(coordAJouer.x, coordAJouer.y)){
								if(!jeu.getPlateau().getRoi(this.couleur).estEchec()){
									echec = false;
								}
							}
							jeu.getPlateau().annulerDernierCoup(false);
							if(!echec){
								jeu.setPieceSelectionee(coordPieceAJouer.x, coordPieceAJouer.y);
								this.coordonneeAJouer = coordAJouer;
								return;
							}
						}
					}
				}
			}
		}
		this.iaAleatoire.jouer();
		this.coordonneeAJouer = this.iaAleatoire.getCoordonneeAJouer();
	}
	
	@Override
	public Coordonnee getCoordonneeAJouer() {
		return coordonneeAJouer;
	}

}
