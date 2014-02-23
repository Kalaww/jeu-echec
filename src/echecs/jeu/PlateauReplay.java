package echecs.jeu;

import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Pion;
import echecs.jeu.piece.Roi;
import echecs.jeu.piece.Tour;
import echecs.sauvegarde.CoupPGN;

/**
 * Plateau de jeu specialement utilise pour le mode replay
 */
public class PlateauReplay extends Plateau{
	
	/**
	 * Reference du replay
	 */
	private Replay replay;
	
	/**
	 * Constructeur
	 * @param replay reference du replay
	 */
	public PlateauReplay(Replay replay){
		plateau = new Piece[8][8];
		this.replay = replay;
		this.setRois();
	}
	
	/**
	 * Annule le dernier coup jouer
	 * @param changerDeJoueur vrai si on doit changer egalement de joueur courant
	 */
	public void annulerDernierCoup(boolean changerDeJoueur){
    	if(replay.getCoupCourant() < 0) return;
    	
    	CoupPGN coup = replay.getCoup(replay.getCoupCourant());
    	Piece pieceDuCoup = plateau[coup.arrivee.x][coup.arrivee.y];
    	if(coup.nomPiece == ' ' && ((coup.departMemoire.y == 1 && pieceDuCoup.getCouleur().equals("BLANC")) || (coup.departMemoire.y == 6 && pieceDuCoup.getCouleur().equals("NOIR")))){
    		Pion pionDuCoup = (Pion)pieceDuCoup;
    		pionDuCoup.setPremierCoup(true);
    	}else if(coup.isPetitRoque){
    		int rangee = (replay.getJoueurCourant().getCouleur().equals("BLANC"))? 7 : 0;
    		Roi roi = (Roi) plateau[6][rangee];
    		Tour tour = (Tour) plateau[5][rangee];
    		roi.setPremierCoup(true);
    		tour.setPremierCoup(true);
    		roi.setX(4);
    		tour.setX(7);
    		plateau[6][rangee] = null;
    		plateau[5][rangee] = null;
    		plateau[4][rangee] = roi;
    		plateau[7][rangee] = tour;
    		if(changerDeJoueur){
        		replay.switchJoueur();
        	}
    		replay.setCoupCourant(replay.getCoupCourant()-1);
        	replay.getFenetre().repaint();
        	return;
    	}else if(coup.isGrandRoque){
    		int rangee = (replay.getJoueurCourant().getCouleur().equals("BLANC"))? 7 : 0;
    		Roi roi = (Roi) plateau[2][rangee];
    		Tour tour = (Tour) plateau[3][rangee];
    		roi.setPremierCoup(true);
    		tour.setPremierCoup(true);
    		roi.setX(4);
    		tour.setX(0);
    		plateau[2][rangee] = null;
    		plateau[3][rangee] = null;
    		plateau[4][rangee] = roi;
    		plateau[0][rangee] = tour;
    		if(changerDeJoueur){
        		replay.switchJoueur();
        	}
    		replay.setCoupCourant(replay.getCoupCourant()-1);
        	replay.getFenetre().repaint();
        	return;
    	}else if(coup.isTransformation){
    		Pion p = new Pion(coup.arrivee.x, coup.arrivee.y, pieceDuCoup.getCouleur(), this);
    		p.setPremierCoup(false);
    		pieceDuCoup = p;
    	}
    	
    	plateau[coup.departMemoire.x][coup.departMemoire.y] = pieceDuCoup;
    	if(coup.isPrise){
    		plateau[coup.arrivee.x][coup.arrivee.y] = replay.getPrises().get(replay.getPrises().size()-1);
    		replay.getPrises().remove(replay.getPrises().size()-1);
    	}else{
    		plateau[coup.arrivee.x][coup.arrivee.y] = null;
    	}
    	pieceDuCoup.setX(coup.departMemoire.x);
    	pieceDuCoup.setY(coup.departMemoire.y);
    	
    	if(changerDeJoueur){
    		replay.switchJoueur();
    	}
    	replay.setCoupCourant(replay.getCoupCourant()-1);
    	replay.testSuivantPrecedent();
    	replay.getFenetre().repaint();
    }

}

