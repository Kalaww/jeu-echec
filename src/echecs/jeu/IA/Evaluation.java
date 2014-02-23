package echecs.jeu.IA;

import java.util.ArrayList;

import echecs.jeu.Jeu;
import echecs.jeu.piece.Piece;

/**
 * Evalue un plateau de jeu ou un coup pour l'algorithme minimax
 */
public class Evaluation{
	
	/**
	 * Enum pour les differents event d'une partie d'echec
	 */
	public enum Event{
		ECHEC_MAT_SUR_SOI, ECHEC_SUR_SOI, ERREUR, RIEN, ECHEC, PAT, ECHEC_MAT
	};
	
	/**
	 * Valeurs des variables d'evaluations
	 */
	public ValeursEvaluation valeurs;
	
	/**
	 * Event de l'evaluation
	 */
	public Event event;
	
	/**
	 * Profondeur auquel a ete faite l'evaluation
	 */
	public int profondeur;
	
	/**
	 * valeur du plateau pour les attaques, defenses, dangers
	 */
	public int valeurAttaqueDefense;
	
	/**
	 * valeur d'evaluation du plateau
	 */
	public int valeurPlateau;
	
	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Representation en string de l'historique joue pour cette evaluation
	 */
	public String historique;
	
	/**
	 * Constructeur
	 * @param jeu reference du jeu
	 * @param valeurs reference des variables d'evaluations
	 */
	public Evaluation(Jeu jeu, ValeursEvaluation valeurs){
		this.jeu = jeu;
		this.event = Event.RIEN;
		this.valeurAttaqueDefense = 0;
		this.valeurPlateau = 0;
		this.profondeur = 0;
		this.valeurs = valeurs;
	}
	
	/**
	 * Compare l'objet a une autre evaluation et retourne la meilleur
	 * @param autre l'autre evaluation a comparer
	 * @return la meilleur evaluation des deux
	 */
	public Evaluation compare(Evaluation autre){
		if(this.event.compareTo(autre.event) > 0){
			return this;
		}else if(this.event.compareTo(autre.event) < 0){
			return autre;
		}
		
		if(event.equals(Event.ECHEC_MAT_SUR_SOI) || event.equals(Event.ECHEC_SUR_SOI)){
			if(this.profondeur > autre.profondeur){
				return this;
			}else if(this.profondeur < autre.profondeur){
				return autre;
			}else{
				if(this.valeurAttaqueDefense + this.valeurPlateau < autre.valeurAttaqueDefense + autre.valeurPlateau){
					return autre;
				}else{
					return this;
				}
			}
		}else if(event.equals(Event.ECHEC) || event.equals(Event.ECHEC_MAT) || event.equals(Event.PAT)){
			if(this.profondeur < autre.profondeur){
				return this;
			}else if(this.profondeur > autre.profondeur){
				return autre;
			}else{
				if(this.valeurAttaqueDefense + this.valeurPlateau < autre.valeurAttaqueDefense + autre.valeurPlateau){
					return autre;
				}else{
					return this;
				}
			}
		}else if(event.equals(Event.ERREUR)){
			return autre;
		}else if(autre.equals(Event.ERREUR)){
			return this;
		}else{
			if(this.valeurAttaqueDefense + this.valeurPlateau < autre.valeurAttaqueDefense + autre.valeurPlateau){
				return autre;
			}else{
				return this;
			}
		}
	}
	
	/**
	 * Evalue le jeu selon le nombres d'attaques, defense et mise en danger de chaque piece
	 */
	public void evaluerAttaqueDefense(){
		ArrayList<Piece>[] allPieces = new ArrayList[2];
		allPieces[0] = jeu.getPlateau().getPiecesBlanches();
		allPieces[1] = jeu.getPlateau().getPiecesNoires();
		
		int[] valeurPieces = new int[2];
		
		for(int k = 0; k < allPieces.length; k++){
			ArrayList<Piece> pieces = allPieces[k];
			for(int i = 0; i < pieces.size(); i++){
				Piece piece = pieces.get(i);
				//Defense de chaque piece
				for(int j = 0; j < pieces.size(); j++){
					Piece pieceDefense = pieces.get(j);
					if(pieceDefense != piece && pieceDefense.coupPossible(piece.getX(), piece.getY()) && pieceDefense.mouvementPossible(piece.getX(), piece.getY())){
						if(piece.getFamille().equals("valeurs.PION")){
							valeurPieces[k] += valeurs.DEFENSE * valeurs.PION;
						}else if(piece.getFamille().equals("valeurs.TOUR")){
							valeurPieces[k] += valeurs.DEFENSE * valeurs.TOUR;
						}else if(piece.getFamille().equals("valeurs.CAVALIER")){
							valeurPieces[k] += valeurs.DEFENSE * valeurs.CAVALIER;
						}else if(piece.getFamille().equals("valeurs.FOU")){
							valeurPieces[k] += valeurs.DEFENSE * valeurs.FOU;
						}else if(piece.getFamille().equals("valeurs.REINE")){
							valeurPieces[k] += valeurs.DEFENSE * valeurs.REINE;
						}
					}
				}
				
				//Attaque et danger de la piece
				int k1 = (k == 0)? 1 : 0;
				ArrayList<Piece> piecesAdverses = allPieces[k1];
				for(int l = 0; l < piecesAdverses.size(); l++){
					Piece pieceAdverse = piecesAdverses.get(l);
					//Piece en danger
					if(pieceAdverse.coupPossible(piece.getX(), piece.getY()) && pieceAdverse.mouvementPossible(piece.getX(), piece.getY())){
						if(piece.getFamille().equals("valeurs.PION")){
							valeurPieces[k] += valeurs.DANGER * valeurs.PION;
						}else if(piece.getFamille().equals("valeurs.TOUR")){
							valeurPieces[k] += valeurs.DANGER * valeurs.TOUR;
						}else if(piece.getFamille().equals("valeurs.CAVALIER")){
							valeurPieces[k] += valeurs.DANGER * valeurs.CAVALIER;
						}else if(piece.getFamille().equals("valeurs.FOU")){
							valeurPieces[k] += valeurs.DANGER * valeurs.FOU;
						}else if(piece.getFamille().equals("valeurs.REINE")){
							valeurPieces[k] += valeurs.DANGER * valeurs.REINE;
						}
					}
					
					//Piece peut attaquer
					if(piece.coupPossible(pieceAdverse.getX(), pieceAdverse.getY()) && piece.mouvementPossible(pieceAdverse.getX(), pieceAdverse.getY())){
						if(pieceAdverse.getFamille().equals("valeurs.PION")){
							valeurPieces[k] += valeurs.ATTAQUE * valeurs.PION;
						}else if(pieceAdverse.getFamille().equals("valeurs.TOUR")){
							valeurPieces[k] += valeurs.ATTAQUE * valeurs.TOUR;
						}else if(pieceAdverse.getFamille().equals("valeurs.CAVALIER")){
							valeurPieces[k] += valeurs.ATTAQUE * valeurs.CAVALIER;
						}else if(pieceAdverse.getFamille().equals("valeurs.FOU")){
							valeurPieces[k] += valeurs.ATTAQUE * valeurs.FOU;
						}else if(pieceAdverse.getFamille().equals("valeurs.REINE")){
							valeurPieces[k] += valeurs.ATTAQUE * valeurs.REINE;
						}
					}
				}
			}
		}
		
		if(jeu.getJoueurCourant().getCouleur().equals("BLANC")){
			this.valeurAttaqueDefense = valeurPieces[1] - valeurPieces[0];
		}else{
			this.valeurAttaqueDefense = valeurPieces[0] - valeurPieces[0];
		}
	}
	
	/**
	 * Evalue le plateau de jeu selon plusieurs criteres
	 */
	public void evaluerPlateau(){
		//Evaluation du nombre de piece
		ArrayList<Piece>[] allPieces = new ArrayList[2];
		allPieces[0] = jeu.getPlateau().getPiecesBlanches();
		allPieces[1] = jeu.getPlateau().getPiecesNoires();
		
		int[] forceNombre = new int[2];
		
		for(int i = 0; i < allPieces.length; i++){
			ArrayList<Piece> pieces = allPieces[i];
			for(int j = 0; j < pieces.size(); j++){
				Piece piece = pieces.get(j);
				if(piece.getFamille().equals("PION")){
					forceNombre[i] += valeurs.PION;
				}else if(piece.getFamille().equals("TOUR")){
					forceNombre[i] += valeurs.TOUR;
				}else if(piece.getFamille().equals("CAVALIER")){
					forceNombre[i] += valeurs.CAVALIER;
				}else if(piece.getFamille().equals("FOU")){
					forceNombre[i] += valeurs.FOU;
				}else if(piece.getFamille().equals("REINE")){
					forceNombre[i] += valeurs.REINE;
				}
			}
		}
		
		
		if(jeu.getJoueurCourant().getCouleur().equals("BLANC")){
			this.valeurPlateau += forceNombre[1] - forceNombre[0];
		}else{
			this.valeurPlateau += forceNombre[0] - forceNombre[1];
		}
		
		this.historique = jeu.getHistorique().toStringSavePGN();
	}
	
	/**
	 * Ajoute une evaluation a l'objet
	 * @param add l'evaluation a ajouter
	 */
	public void add(Evaluation add){
		this.profondeur = add.profondeur;
		this.event = add.event;
		this.valeurAttaqueDefense += add.valeurAttaqueDefense;
		this.valeurPlateau += add.valeurPlateau;
		this.historique = add.historique;
	}
	
	/**
	 * Representation en String
	 */
	public String toString(){
		return "Evaluation : ["+event.toString()+"]("+this.valeurAttaqueDefense+")("+this.valeurPlateau+")";
	}
}
