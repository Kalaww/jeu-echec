package echecs.jeu.piece;

import java.util.ArrayList;

import echecs.utils.Coordonnee;
import echecs.jeu.Plateau;

/**
 * Defini le comportement du Fou
 */
public class Fou extends Piece{
	
    /**
     * Constructeur du fou
     * @param x Position en abscisse
     * @param y Position en ordonnee
     * @param couleur Couleur de la pièce(NOIR/BLANC)
     */
    public Fou(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "FOU", couleur, plateau);
    	
    }
    
    /**
     * Verifie que le coup est autorise pour la piece
     * @param x Le x d'arrive
     * @param y Le y d'arrive
     * @return Si le coup est autorisé pour cette pièce
     */
    public boolean coupPossible(int x, int y){
    	for(int i=0; i<= 8; i++){
    		if(this.x+i==x && this.y+i==y)
    			return true;
    		if(this.x-i==x && this.y-i==y)
    			return true;
    		if(this.x+i==x && this.y-i==y)
    			return true;
    		if(this.x-i==x && this.y+i==y)
    			return true; 		
    	}
    		return false;
    	
    	
    }

    /**
     * Verifie si il le chemin est libre
     * @param x d'arrive
     * @param y d'arrive
     * @return  vrai si il y n'y pas d'obstacle
     */
    public boolean mouvementPossible(int x,int y){
        //Si va vers le haut � gauche
    	
    	if(x>this.x && y>this.y){
    		while(this.x != x-1 && this.y != y-1){
    			
    			if(!plateau.estVide(x-1, y-1)){
    				return false;
    			}
    			x--;
    			y--;
    		}
    		return true;
    	}
    	//Si va vers le haut droit
    	if(x<this.x && y>this.y){
    		while(this.x != x+1 && this.y != y-1){
    			
    			if(!plateau.estVide(x+1, y-1)){
    				return false;
    			}
    			x++;
    			y--;
    		}
    		return true;
    	}
    	//Si va vers le bas � gauche
    	
    	if(x>this.x && y<this.y){
    		while(this.x != x-1 && this.y != y+1){
    			
    			if(!plateau.estVide(x-1, y+1)){
    				return false;
    			}
    			x--;
    			y++;
    		}
    		return true;
    	}
    	//Si va vers le bas droit
    	if(x<this.x && y<this.y){
    		while(this.x != x+1 && this.y != y+1){
    			
    			if(!plateau.estVide(x+1, y+1)){
    				return false;
    			}
    			x++;
    			y++;
    		}
    		return true;
    	}
        return false;
    }
    
    /**
     * Recupere les coordonnees de toutes les cases ou peut aller la piece
     * @return ArrayList<Coordonnee> de tous les coups possibles
     */
    public ArrayList<Coordonnee> casesPossibles(){
    	int x = this.x+1;
    	int y = this.y+1;
    	ArrayList<Coordonnee> possible = new ArrayList<Coordonnee>();
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y) && (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x++;
    		y++;
    	}
    	x = this.x-1;
    	y = this.y-1;
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x--;
    		y--;
    	}
    	
    	x = this.x-1;
    	y = this.y+1;
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x--;
    		y++;
    	}
    	x = this.x+1;
    	y = this.y-1;
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x++;
    		y--;
    	}
    	return possible;
    }
}
