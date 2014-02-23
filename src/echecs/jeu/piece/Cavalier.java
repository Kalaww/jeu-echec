package echecs.jeu.piece;

import echecs.jeu.Plateau;
import echecs.utils.Coordonnee;

import java.util.ArrayList;

/**
 * Defini le comportement du Cavalier
 */
public class Cavalier extends Piece {
    /**
     * Constructeur du Cavalier
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param couleur La couleur de la piece (NOIR/BLANC)
     */
    public Cavalier(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "CAVALIER", couleur, plateau);

    }
    
    /**
     * Verifie si le deplacement est possible
     * @param x L'abscisse d'arrive
     * @param y l'ordonnee d'arrive
     * @return resultat du test
     */
    public boolean coupPossible(int x, int y){
    	if(x==this.x-2 && y==this.y-1){
    		return true;
    	}
    	if(x==this.x-1 && y==this.y-2){
    		return true;
    	}
    	//
    	if(x==this.x+1 && y==this.y-2){
    		return true;
    	}
    	if(x==this.x+2 && y==this.y-1){
    		return true;
    	} 
    	//
    	if(x==this.x+2 && y==this.y+1){
    		return true;
    	}
    	if(x==this.x+1 && y==this.y+2){
    		return true;
    	} 
    	//
    	if(x==this.x-1 && y==this.y+2){
    		return true;
    	}
    	if(x==this.x-2 && y==this.y+1){
    		return true;
    	} 
    	//
    	return false;
    	
    }

    /**
     * Verifie si il n'y a pas d'obstacle au deplacement
     * @param x
     * @param y
     * @return vrai ou faux
     */
    public boolean mouvementPossible(int x, int y){
        return true;
    }
    
    /**
     * Recupere les coordonnees de toutes les cases ou peut aller la piece
     * @return ArrayList<Coordonnee> de tous les coups possibles
     */
    public ArrayList<Coordonnee> casesPossibles(){
    ArrayList<Coordonnee> coords = new ArrayList<Coordonnee>();
    	if(this.couleur.equals("BLANC")){
    		if(((x-2) >= 0 && (y-1) >= 0 && (x-2) < 8 && (y-1) < 8) && ((this.plateau.getCase(x-2,y-1) == null) || this.plateau.getCase(x-2,y-1).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x-2,y-1));
    		}
    		if(((x-1) >= 0 && (y-2) >= 0 && (x-1) < 8 && (y-2) < 8) && ((this.plateau.getCase(x-1,y-2) == null) || this.plateau.getCase(x-1,y-2).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x-1,y-2));
    		}
    		if(((x+1) >= 0 && (y-2) >= 0 && (x+1) < 8 && (y-2) < 8) && ((this.plateau.getCase(x+1,y-2) == null) || this.plateau.getCase(x+1, y-2).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x+1,y-2));
    		}
    		if(((x+2) >= 0 && (y-1) >= 0 && (x+2) < 8 && (y-1) < 8) && ((this.plateau.getCase(x+2,y-1) == null) || this.plateau.getCase(x+2,y-1).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x+2,y-1));
    		}
    		if(((x+2) >= 0 && (y+1) >= 0 && (x+2) < 8 && (y+1) < 8) && ((this.plateau.getCase(x+2,y+1) == null) || this.plateau.getCase(x+2,y+1).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x+2,y+1));
    		}
    		if(((x+1) >= 0 && (y+2) >= 0 && (x+1) < 8 && (y+2) < 8) && ((this.plateau.getCase(x+1,y+2) == null) || this.plateau.getCase(x+1, y+2).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x+1,y+2));
    		}
    		if(((x-1) >= 0 && (y+2) >= 0 && (x-1) < 8 && (y+2) < 8) && ((this.plateau.getCase(x-1,y+2) == null) || this.plateau.getCase(x-1, y+2).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x-1,y+2));
    		}
    		if(((x-2) >= 0 && (y+1) >= 0 && (x-2) < 8 && (y+1) < 8) && ((this.plateau.getCase(x-2,y+1) == null) || this.plateau.getCase(x-2,y+1).couleur.equals("NOIR"))){
    			coords.add(new Coordonnee(x-2,y+1));
    		}
    	}
    	else{
    		if(((x-2) >= 0 && (y-1) >= 0 && (x-2) < 8 && (y-1) < 8) && ((this.plateau.getCase(x-2,y-1) == null) || this.plateau.getCase(x-2,y-1).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x-2,y-1));
    		}
    		if(((x-1) >= 0 && (y-2) >= 0 && (x-1) < 8 && (y-2) < 8) && ((this.plateau.getCase(x-1,y-2) == null) || this.plateau.getCase(x-1,y-2).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x-1,y-2));
    		}
    		if(((x+1) >= 0 && (y-2) >= 0 && (x+1) < 8 && (y-2) < 8) && ((this.plateau.getCase(x+1,y-2) == null) || this.plateau.getCase(x+1, y-2).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x+1,y-2));
    		}
    		if(((x+2) >= 0 && (y-1) >= 0 && (x+2) < 8 && (y-1) < 8) && ((this.plateau.getCase(x+2,y-1) == null) || this.plateau.getCase(x+2,y-1).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x+2,y-1));
    		}
    		if(((x+2) >= 0 && (y+1) >= 0 && (x+2) < 8 && (y+1) < 8) && ((this.plateau.getCase(x+2,y+1) == null) || this.plateau.getCase(x+2,y+1).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x+2,y+1));
    		}
    		if(((x+1) >= 0 && (y+2) >= 0 && (x+1) < 8 && (y+2) < 8) && ((this.plateau.getCase(x+1,y+2) == null) || this.plateau.getCase(x+1, y+2).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x+1,y+2));
    		}
    		if(((x-1) >= 0 && (y+2) >= 0 && (x-1) < 8 && (y+2) < 8) && ((this.plateau.getCase(x-1,y+2) == null) || this.plateau.getCase(x-1, y+2).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x-1,y+2));
    		}
    		if(((x-2) >= 0 && (y+1) >= 0 && (x-2) < 8 && (y+1) < 8) && ((this.plateau.getCase(x-2,y+1) == null) || this.plateau.getCase(x-2,y+1).couleur.equals("BLANC"))){
    			coords.add(new Coordonnee(x-2,y+1));
    		}
    	}
    return coords;
    }
}
