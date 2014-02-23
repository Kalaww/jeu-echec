package echecs.jeu.piece;

import java.util.ArrayList;

import echecs.jeu.Plateau;
import echecs.sauvegarde.CoupPGN;
import echecs.utils.Coordonnee;

/**
 * Classe mere des differentes pieces
 */
public class Piece {
    /**
     * La famille de la piece
     */
    private String famille;
    
    /**
     * Reference au plateau
     */
    protected Plateau plateau;
    
    /**
     * La couleur de la piece
     */
    protected String couleur;
    
    /**
     * L'abscisse et l'ordonnee de la piece
     */
    protected int x,y;

    /**
     * Constructeur de Piece
     * @param x La position en abscisse
     * @param y La position en ordonnee
     * @param famille La famille de piece
     * @param couleur La couleur de la piece
     */
    public Piece(int x, int y, String famille, String couleur, Plateau plateau){
    	this.x = x;
    	this.y = y;
    	this.famille = famille;
    	this.couleur = couleur;
    	this.plateau = plateau;
    }

    /**
     * Retourne la famille de la piece
     * @return Famille de la piece
     */
    public String getFamille(){
        return famille;
    }
    
    /**
     * Donne la couleur de piece
     * @return La couleur de la piece
     */
    public String getCouleur(){
    	return couleur;
    }
    
    /**
     * Retourne la coordonnee X
     * @return x
     */
    public int getX(){
    	return this.x;
    }
    
    /**
     * Retourne la coordonnee Y
     * @return y
     */
    public int getY(){
    	return this.y;
    }
    
    /**
     * Setter x
     * @param x
     */
    public void setX(int x){
    	this.x = x;
    }
    
    /**
     * Setter x
     * @param y
     */
    public void setY(int y){
    	this.y = y;
    }
    
    /**
     * Setter pour x et y a la foi
     * @param x
     * @param y
     */
    public void setXY(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    /**
     * Deplace la piece (change les coordonnees)
     * @param x Le x d'arrivee
     * @param y Le y d'arrivee
     */
    public boolean deplacer(int x,int y){
        if(coupPossible(x, y)){
            if(mouvementPossible(x,y)){
            	//Coup
            	CoupPGN coup = new CoupPGN();
            	coup.departMemoire.x = this.x;
            	coup.departMemoire.y = this.y;
            	coup.arrivee.x = x;
            	coup.arrivee.y = y;
            	coup.setNomPiece(this.famille);
            	coup.referencePiece = this;
            	
            	//Prises
	            if(plateau.getCase(x, y) != null){
	            	if(plateau.getCase(x, y).getCouleur() != this.getCouleur()){
	            		this.plateau.getJeu().getPrises().add(plateau.getCase(x, y));
	            		coup.isPrise = true;
	            	}
	            }
	            
	            //Petit roque / grand roque
	            if(this.famille.equals("ROI") && Math.abs(this.x - x) > 1){
	            	//petit roque
	            	if(x > this.x){
	            		coup.isPetitRoque = true;
	            		if(this.couleur.equals("BLANC")){
	            			Tour t  = (Tour) plateau.getCase(7, 0);
	            			t.x = 5;
	                        t.y = 0;
	                        t.setPremierCoup(false);
	            			plateau.setCase(7, 0, null);
	                        plateau.setCase(5, 0, t);
	            		}else{
	            			Tour t  = (Tour) plateau.getCase(7, 7);
	            			t.x = 5;
	                        t.y = 7;
	                        t.setPremierCoup(false);
	            			plateau.setCase(7, 7, null);
	                        plateau.setCase(5, 7, t);
	            		}
	            		//grand roque
	            	}else{
	            		coup.isGrandRoque = true;
	            		if(this.couleur.equals("BLANC")){
	            			Tour t  = (Tour) plateau.getCase(0, 0);
	            			t.x = 3;
	                        t.y = 0;
	                        t.setPremierCoup(false);
	            			plateau.setCase(0, 0, null);
	                        plateau.setCase(3, 0, t);
	            		}else{
	            			Tour t  = (Tour) plateau.getCase(0, 7);
	            			t.x = 3;
	                        t.y = 7;
	                        t.setPremierCoup(false);
	            			plateau.setCase(0, 7, null);
	                        plateau.setCase(3, 7, t);
	            		}
	            	}
	            }
	            
	            //si c'est un roi/tour/pion on test son premier coup
	            if(this.famille.equals("PION")){
	            	Pion p = (Pion) this;
	            	p.setPremierCoup(false);
	            }else if(this.famille.equals("TOUR")){
	            	Tour t = (Tour) this;
	            	t.setPremierCoup(false);
	            }else if(this.famille.equals("ROI")){
	            	Roi r = (Roi)this;
	            	r.setPremierCoup(false);
	            }
	            
	            //Deplacement de la piece
	        	plateau.setCase(this.x, this.y, null);
	            this.x = x;
	            this.y = y;
	            plateau.setCase(this.x, this.y, this);
	            
	            //Test de la promotion du pion
	            if(this.famille.equals("PION")){
	            	Pion p = (Pion) this;
					if(p.isPromotion()){
						coup.isTransformation = true;
						String reponse = "";
						if(!plateau.getJeu().getJoueurCourant().estHumain() || plateau.getJeu().isVsInternet()){
							reponse = "Reine";
						}else{
							String choix = plateau.getJeu().getFenetre().showTransformations();
							if(choix != null){
								reponse = choix;
							}else{
								reponse = "Reine";
							}
						}
						if(reponse.equals("Reine")){
							Reine r = new Reine(p.getX(), p.getY(), p.getCouleur(), plateau);
							plateau.setCase(p.getX(), p.getY(), r);
							coup.nomPieceTransformation = CoupPGN.REINE;
						}else if(reponse.equals("Tour")){
							Tour t = new Tour(p.getX(), p.getY(), p.getCouleur(), plateau);
							plateau.setCase(p.getX(), p.getY(), t);
							coup.nomPieceTransformation = CoupPGN.TOUR;
						}else if(reponse.equals("Fou")){
							Fou f = new Fou(p.getX(), p.getY(), p.getCouleur(), plateau);
							plateau.setCase(p.getX(), p.getY(), f);
							coup.nomPieceTransformation = CoupPGN.FOU;
						}else if(reponse.equals("Cavalier")){
							Cavalier c = new Cavalier(p.getX(), p.getY(), p.getCouleur(), plateau);
							plateau.setCase(p.getX(), p.getY(), c);
							coup.nomPieceTransformation = CoupPGN.CAVALIER;
						}
					}
	            }
	            plateau.getJeu().getHistorique().addCoupPGN(coup);
	            return true;
            }
            else{
            	if(!plateau.getJeu().isServer()){
            		plateau.getJeu().getFenetre().addLogPartie("Il y a une piece qui gene le deplacement");
            	}
                return false;
            }
        }
        else{
        	if(!plateau.getJeu().isServer()){
        		plateau.getJeu().getFenetre().addLogPartie("Deplacement impossible");
        	}
            return false;
        }
        
    }
    
    /**
     * Recupere les coordonnees de toutes les cases ou peut aller la piece
     * @return ArrayList<Coordonnee> de tous les coups possibles
     */
    public ArrayList<Coordonnee> casesPossibles(){
    	return new ArrayList<Coordonnee>();
    }
    
    /**
     * Ne fais rien, fonction surcharger par les class heritee
     * @param x
     * @param y
     * @return
     */
    public boolean coupPossible(int x, int y){
        // JE FAIS RIEN !
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
    
    @Override
    public String toString(){
    	return famille+" "+couleur+" ("+x+","+y+")";
    }

}
