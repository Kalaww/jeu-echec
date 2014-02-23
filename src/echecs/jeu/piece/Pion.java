package echecs.jeu.piece;

import echecs.jeu.Plateau;
import echecs.utils.Coordonnee;

import java.util.ArrayList;

/**
 * Defini le comportement d'un pion
 */
public class Pion extends Piece {
    /**
     * Verifie si la piece a deja joue
     */
    private boolean premierCoup = true;
    
    /**
     * Constructeur de Pion
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param couleur La couleur de la piece
     */
    public Pion(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "PION", couleur, plateau);
    }
    
    /**
     * Verifie que le coup est autorise pour la piece
     * @param x d'arrive
     * @param y d'arrive
     * @return Si le coup est autorisé pour cette piece
     */
    public boolean coupPossible(int x, int y){
    	if(x < 0 || x > 7 || y < 0 || y > 7) return false;
  
    	if(this.plateau.getCase(x, y) != null){
    		if(this.couleur.equals("BLANC")){
    			if(x == this.x+1 && y == this.y +1){
        			return true;
        		}
    			if(x == this.x-1 && y == this.y +1){
        			return true;
        		}
    		}
    		else{
    			if(x == this.x-1 && y == this.y -1){
        			return true;
        		}
    			if(x == this.x+1 && y == this.y-1){
        			return true;
        		}
    		}

    	}
    	if(this.plateau.getCase(x, y) == null){
	    	if(this.couleur.equals("BLANC")){
	            if(x==this.x && y == this.y+2 && premierCoup==true){
	                return true;
	
	            }
	    		if(x==this.x && y ==this.y+1){
	    			return true;
	    		}
	    	}
	    	else{
	            if(x==this.x && y == this.y-2 && premierCoup==true){
	                return true;
	            }
	    		if(x==this.x && y==this.y-1){
	    			return true;
	    		}
	    	}
	    	return false;
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
        return true;
    }
    /**
     * Recupere les coordonnees de toutes les cases ou peut aller la piece
     * @return ArrayList<Coordonnee> de tous les coups possibles
     */
    public ArrayList<Coordonnee> casesPossibles(){
    	ArrayList<Coordonnee> coords = new ArrayList<Coordonnee>();	
    			if(this.couleur.equals("BLANC")){
    				if(((x+1) >= 0 && (y+1) >= 0 && (x+1) < 8 && (y+1) < 8) && (this.plateau.getCase(x+1,y+1) != null) && this.plateau.getCase(x+1,y+1).couleur.equals("NOIR")){
    					coords.add(new Coordonnee(x+1,y+1));
    				}
    				if(((x-1) >= 0 && (y+1) >= 0 && (x-1) < 8 && (y+1) < 8) && (this.plateau.getCase(x-1, y+1) != null) && this.plateau.getCase(x-1,y+1).couleur.equals("NOIR")){
    					coords.add(new Coordonnee(x-1,y+1));
    				}
    				if(y <= 5 && premierCoup && plateau.getCase(x, y+1) == null && plateau.getCase(x, y+2) == null){
    					coords.add(new Coordonnee(x,y+1));
    					coords.add(new Coordonnee(x,y+2));
    				}
    				if(y < 7 && plateau.getCase(x, y+1) == null){
    					coords.add(new Coordonnee(x,y+1));
    				}
    			}
    			else{
    				if(((x+1) >= 0 && (y-1) >= 0 && (x+1) < 8 && (y-1) < 8) && (this.plateau.getCase(x+1,y-1) != null) && this.plateau.getCase(x+1,y-1).couleur.equals("BLANC")){
    					coords.add(new Coordonnee(x+1,y-1));
    				}
    				if(((x-1) >= 0 && (y-1) >= 0 && (x-1) < 8 && (y-1) < 8) && (this.plateau.getCase(x-1,y-1) != null) && this.plateau.getCase(x-1, y-1).couleur.equals("BLANC")){
    					coords.add(new Coordonnee(x-1,y-1));
    				}
    				if(y >= 2 && premierCoup && plateau.getCase(x, y-1) == null && plateau.getCase(x, y-2) == null){
    					coords.add(new Coordonnee(x,y-1));
    					coords.add(new Coordonnee(x,y-2));
    				}
    				if(y > 0 && plateau.getCase(x, y-1) == null){
    					coords.add(new Coordonnee(x,y-1));
    				}
    			}
    	return coords;
    }
    /**
     * Detecte si le pion peut etre promu
     * @return
     */
    public boolean isPromotion(){
    	if(this.couleur.equals("BLANC") && this.y == 7){
    		return true;
    	}
    	if(this.couleur.equals("NOIR") && this.y == 0){
    		return true;
    	}
    	else return false;
    }
    
    
    /**
     * Si le Pion n'as pas encore bouge
     * @return le boolean
     */
    public boolean isPremierCoup(){
    	return this.premierCoup;
    }
    
    /**
     * Setter du premierCoup
     */
    public void setPremierCoup(boolean b){
    	this.premierCoup = b;
    }
}
    
