package echecs.jeu.piece;

import java.util.ArrayList;

import echecs.jeu.Plateau;
import echecs.utils.Coordonnee;

/**
 * Defini le comportement de la Tour
 */
public class Tour extends Piece{
	
	/**
	 * Vrai si c'est le premier coup
	 */
	private boolean premierCoup;
	
	/**
	 * Vrai si la tour est sur le cote gauche au depart (colonne H)
	 */
	private boolean startGauche;
	
    /**
     * Constructeur de le tour
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param couleur La couleur de la piece
     */
    public Tour(int x, int y, String couleur, Plateau plateau){
    	super(x, y, "TOUR", couleur, plateau);
    	this.premierCoup = true;
    	if(x == 0){
    		this.startGauche = true;
    	}else{
    		this.startGauche = false;
    	}
    }
    
    /**
     * Verifie si le déplacement est autorise
     * @param x le x d'arrive
     * @param y le y d'arrive
     * @return Si le coup est possible
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
    	return false;
    }

    /**
     * Verifie si il n'y a pas d'obstacle au deplacement
     * @param x
     * @param y
     * @return vrai ou faux
     */
    public boolean mouvementPossible(int x, int y){

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
	    return false;
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
	 * @param premierCoup
	 */
	public void setPremierCoup(boolean premierCoup){
		this.premierCoup = premierCoup;
	}
	
	/**
	 * Vrai si la tour commence a gauche (colonne H)
	 * @return
	 */
	public boolean isStartGauche(){
		return this.startGauche;
	}
}
