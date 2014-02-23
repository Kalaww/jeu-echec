package echecs.jeu.piece;

import java.util.ArrayList;

import echecs.jeu.Plateau;
import echecs.utils.Coordonnee;

/**
 * Defini le comportement de la reine
 */
public class Reine extends Piece {
    /**
     * Constructeur de la reine
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param couleur La couleur de la piece
     */
    public Reine(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "REINE", couleur, plateau);
    }
    
    /**
     * Retourne une copie de l'objet
     * @param plateau copie sur le plateau
     * @return la copie
     */
    public Reine copy(Plateau plateau){
    	return new Reine(this.x, this.y, this.couleur, plateau);
    }
    
    
    /**
     * Verifie que le coup est autorise pour la piece
     * @param x Le x d'arrive
     * @param y Le y d'arrive
     * @return Si le coup est autorisé pour cette pièce
     */
    public boolean coupPossible(int x, int y){
    	for(int i = 0;i <=8;i++){
    		if(i==x && y==this.y){
    			return true;
    		}
    		if(i==y && x==this.x){
    			return true;
    		}
    	}
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
     * Verifie si il n'y a pas d'obstacle au deplacement
     * @param x
     * @param y
     * @return vrai ou faux
     */
    public boolean mouvementPossible(int x, int y){
        if(this.x != x && this.y != y){       //Si il s'agit d'un mouvement diagonal
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
        }
        if(this.x != x && this.y ==y){
        	if(this.x > x){ // droite vers gauche
        		for(int i=this.x-1;i>x;i--){

        			if(!plateau.estVide(i, y)){
        				return false;
        			}
        		}
        		return true;
        	}
        	if(this.x < x){ // droite vers gauche
        		for(int i=this.x+1;i<x;i++){

        			if(!plateau.estVide(i, y)){
        				return false;
        			}
        		}
        		return true;
        	}
        }
        if(this.y != y && this.x == x){
        	if(this.y > y){ // bas vers haut
        		for(int i=this.y-1;i>y;i--){

        			if(!plateau.estVide(x, i)){
        				return false;
        			}
        		}
        		return true;
        	}
        	if(this.y < y){ // haut vers bas
        		for(int i=this.y+1;i<y;i++){

        			if(!plateau.estVide(x, i)){
        				return false;
        			}
        		}
        	return true;
        }
        }
        return true;
    }
    
    /**
     * Recupere les coordonnees de toutes les cases ou peut aller la piece
     * @return ArrayList<Coordonnee> de tous les coups possibles
     */
    public ArrayList<Coordonnee> casesPossibles(){
    	int x = this.x+1;
    	int y = this.y;
    	ArrayList<Coordonnee> possible = new ArrayList<Coordonnee>();
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y) && (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x++;
    	}
    	x = this.x-1;
    	
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		x--;
    	}
    	x = this.x;
    	y = this.y+1;
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		y++;
    	}
    	x = this.x;
    	y = this.y-1;
    	while(x<8 && x>=0 && y>=0&& y<8 && mouvementPossible(x,y)&& (plateau.getCase(x, y) == null || plateau.getCase(x, y).getCouleur() != this.getCouleur())){
    		possible.add(new Coordonnee(x,y));
    		y--;
    	}
    	x = this.x+1;
    	y = this.y+1;
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
