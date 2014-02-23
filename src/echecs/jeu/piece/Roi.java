package echecs.jeu.piece;

import java.util.ArrayList;

import echecs.jeu.Plateau;
import echecs.sauvegarde.CoupPGN;
import echecs.utils.Coordonnee;

/**
 * Defini le comportement du roi
 */
public class Roi extends Piece{
	
	/**
	 * Vrai si c'est le premier coup 
	 */
	private boolean premierCoup;
	
    /**
     * Constructeur de Roi
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param couleur La couleur de la piece
     */
    public Roi(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "ROI", couleur, plateau);
    	this.premierCoup = true;
    }
    
    /**
     * Verifie que le coup est autorise pour la piece
     * @param x Le x d'arrive
     * @param y Le y d'arrive
     * @return Si le coup est autorise pour cette piece
     */
    public boolean coupPossible(int x, int y){
    	//deplacement horizontal
    	if(x==this.x+1 && y==this.y || x==this.x-1 && y==this.y){
    		return true;
    	//deplacement vertical
    	}else if(x==this.x && y==this.y+1 || x==this.x && y==this.y-1){
    		return true;
    	//deplacement dans les 4 diagonales
    	}else if(x==this.x+1 && y==this.y+1 || x==this.x+1 && y==this.y-1 || x==this.x-1 && y==this.y-1 || x==this.x-1 && y==this.y+1){
    		return true;
    	//petit roque
    	}else if(y==this.y && x==(this.x+2) && premierCoup){
    		Piece p = (this.couleur.equals("BLANC"))? plateau.getCase(7, 0) : plateau.getCase(7, 7);
    		if(p != null && p.getFamille().equals("TOUR")){
    			Tour t = (Tour) p;
    			if(t.getPremierCoup()){
    				return true;
    			}
    		}
    	//grand roque
    	}else if(y==this.y && x==(this.x-2) && premierCoup){
    		Piece p = (this.couleur.equals("BLANC"))? plateau.getCase(0, 0) : plateau.getCase(0, 7);
    		if(p != null && p.getFamille().equals("TOUR")){
    			Tour t = (Tour) p;
    			if(t.getPremierCoup()){
    				return true;
    			}
    		}
    	}
    	return false;    	
    }
    
    /**
     * Verifie si il n'y a pas d'obstacle au deplacement
     * @param x
     * @param y
     * @return vrai ou faux
     */
        public boolean mouvementPossible(int x, int y){
        	if(x==(this.x+2) && premierCoup){
        		Piece p1 = (this.couleur.equals("BLANC"))? plateau.getCase(5,0) : plateau.getCase(5,7);
        		Piece p2 = (this.couleur.equals("BLANC"))? plateau.getCase(6,0) : plateau.getCase(6,7);
        		if(p1 != null || p2 != null){
        			return false;
        		}
        	}
        	else if(x==(this.x-2) && premierCoup){
        		Piece p1 = (this.couleur.equals("BLANC"))? plateau.getCase(1,0) : plateau.getCase(1,7);
        		Piece p2 = (this.couleur.equals("BLANC"))? plateau.getCase(2,0) : plateau.getCase(2,7);
        		Piece p3 = (this.couleur.equals("BLANC"))? plateau.getCase(3,0) : plateau.getCase(3,7);
        		if(p1 != null || p2 != null || p3 != null){
        			return false;
        		}
        	}
         return true;
        }
        
        /**
         * Test si le roi est en echec
         * @return resultat du test
         */
        public boolean estEchec(){
        	ArrayList<Piece> atester = plateau.getPiece();
        	for(Piece pi : atester){
        		if(pi.coupPossible(this.x, this.y) && pi.mouvementPossible(this.x,  this.y)){
        			if(this.getCouleur().equals("BLANC") && pi.getCouleur().equals("NOIR")){
        				return true;
        			}
        			if(this.getCouleur().equals("NOIR") && pi.getCouleur().equals("BLANC")){
        				return true;
        			}
        		}

        	}
			return false;

        }
        
        /**
         * Test si le roi est en echec et mat
         * @return resultat du test
         */
        public boolean estEchecEtMat(){
        	//Recupere toutes les pieces de la couleur du roi
        	ArrayList<Piece> pieces = (this.couleur.equals("BLANC"))? plateau.getPiecesBlanches() : plateau.getPiecesNoires();
        	       	
        	//On prend chaques pieces une a une
        	for(int i = 0; i < pieces.size(); i++){
        		//recupere toutes les cases ou peut aller la piece
        		ArrayList<Coordonnee> casesPossibles = pieces.get(i).casesPossibles();
        		
        		//on prend chaque coordonnee une a une
        		for(int j = 0; j < casesPossibles.size(); j++){
        			int valeurPrerequis = plateau.getJeu().recherchePrerequis(pieces.get(i), casesPossibles.get(j).x, casesPossibles.get(j).y);
        			
        			//deplace la piece en la coordonnee
        			if(plateau.getCase(pieces.get(i).getX(), pieces.get(i).getY()).deplacer(casesPossibles.get(j).x, casesPossibles.get(j).y)){
        				CoupPGN coup = plateau.getJeu().getHistorique().getDernierCoup();
        				coup.setPrerequis(valeurPrerequis);
        				//s'il n'y a plus d'echec au roi, alors il y a pas echec et mat
        				if(!this.estEchec()){
        					coup.isEchec = true;
        					//on annule le coup que l'on vient de jouer
        					plateau.annulerDernierCoup(false);
        					return false;
        				}
        				//on annule le coup que l'on vient de jouer
        				plateau.annulerDernierCoup(false);
        			}
        		}
        	}
        	
        	//Si aucun deplacement n'a casse l'echec, alors il y a echec et mat.
        	return true;
        }
        
        /**
         * Test de l'egalite
         * @return
         */
        public boolean estPat(){
        	ArrayList<Piece> pieces = (this.couleur.equals("BLANC"))? plateau.getPiecesBlanches() : plateau.getPiecesNoires();
        	for(int i = 0; i < pieces.size(); i++){ // parcours l'ensemble des pieces de la couleur
        		ArrayList<Coordonnee> coupPossiblesPieces = pieces.get(i).casesPossibles();
        		Piece piece = pieces.get(i);
        		for(int j = 0; j < coupPossiblesPieces.size(); j++){ // parcours l'ensemble des cases possibles de chaque pièce
        			Coordonnee coord = coupPossiblesPieces.get(j);
        			int valeurPrerequis = plateau.getJeu().recherchePrerequis(piece, coord.x, coord.y);
        			plateau.getJeu().switchJoueur();
        			piece.deplacer(coord.x, coord.y);
        			plateau.getJeu().switchJoueur();
        			CoupPGN coup = plateau.getJeu().getHistorique().getDernierCoup();
        			coup.setPrerequis(valeurPrerequis);
        			if(!estEchec()){
        				plateau.annulerDernierCoup(false);
        				return false;
        			}
        			plateau.annulerDernierCoup(false);
        		}
        	}
        	return true;
        }
        
        /**
         * Recupere les coordonnees de toutes les cases ou peut aller la piece
         * @return ArrayList<Coordonnee> de tous les coups possibles
         */
        public ArrayList<Coordonnee> casesPossibles(){
        	ArrayList<Coordonnee> possible = new ArrayList<Coordonnee>();
        	if(((x) >= 0 && (y+1) >= 0 && (x) < 8 && (y+1) < 8) && (this.plateau.getCase(x,y+1) == null || !this.plateau.getCase(x,y+1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x,y+1));
        	}
        	if(((x) >= 0 && (y-1) >= 0 && (x) < 8 && (y-1) < 8) && (this.plateau.getCase(x,y-1) == null || !this.plateau.getCase(x,y-1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x,y-1));
        	}
        	if(((x+1) >= 0 && (y) >= 0 && (x+1) < 8 && (y) < 8) && (this.plateau.getCase(x+1,y) == null || !this.plateau.getCase(x+1,y).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x+1,y));
        	}
        	if(((x-1) >= 0 && (y) >= 0 && (x-1) < 8 && (y) < 8) && (this.plateau.getCase(x-1,y) == null || !this.plateau.getCase(x-1,y).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x-1,y));
        	}
        	if(((x+1) >= 0 && (y+1) >= 0 && (x+1) < 8 && (y+1) < 8) && (this.plateau.getCase(x+1,y+1) == null || !this.plateau.getCase(x+1,y+1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x+1,y+1));
        	}
        	if(((x-1) >= 0 && (y+1) >= 0 && (x-1) < 8 && (y+1) < 8) && (this.plateau.getCase(x-1,y+1) == null || !this.plateau.getCase(x-1,y+1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x-1,y+1));
        	}
        	if(((x+1) >= 0 && (y-1) >= 0 && (x+1) < 8 && (y-1) < 8) && (this.plateau.getCase(x+1,y-1) == null || !this.plateau.getCase(x+1,y-1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x+1,y-1));
        	}
        	if(((x-1) >= 0 && (y-1) >= 0 && (x-1) < 8 && (y-1) < 8) && (this.plateau.getCase(x-1,y-1) == null || !this.plateau.getCase(x-1,y-1).couleur.equals(this.couleur))){
        		possible.add(new Coordonnee(x-1,y-1));
        	}
        	if(premierCoup && this.plateau.getCase(x+1, y) == null && this.plateau.getCase(x+2, y) == null && this.plateau.getCase(x+3, y) != null && this.plateau.getCase(x+3, y).getClass().equals(Tour.class)){
        		Tour t = (Tour)plateau.getCase(x+3, y);
        		if(t.getPremierCoup()){
        			possible.add(new Coordonnee(x+2,y));
        		}
        	}
        	if(premierCoup && this.plateau.getCase(x-1, y) == null && this.plateau.getCase(x-2,y) == null && this.plateau.getCase(x-3,y) == null && plateau.getCase(x-4, y) != null && plateau.getCase(x-4, y).getClass().equals(Tour.class)){
        		Tour t = (Tour)plateau.getCase(x-4, y);
        		if(t.getPremierCoup()){
        			possible.add(new Coordonnee(x-2,y));
        		}
        	}
        	return possible;
        }
        
        /**
         * Getter premier coup
         * @return
         */
        public boolean getPremierCoup(){
        	return premierCoup;
        }
        
        /**
         * Setter premier coup
         * @param b
         */
        public void setPremierCoup(boolean b){
        	this.premierCoup = b;
        }
    }

