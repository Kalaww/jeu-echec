package echecs.jeu;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import echecs.graphisme.replay.ReplayFenetre;
import echecs.jeu.piece.Cavalier;
import echecs.jeu.piece.Fou;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Pion;
import echecs.jeu.piece.Reine;
import echecs.jeu.piece.Roi;
import echecs.jeu.piece.Tour;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Partie;

/**
 * Jeu special pour le mode replay
 */
public class Replay{
	
	/**
	 * Index du coup courant a jouer
	 */
	private int coupCourant;
	
	/**
	 * Reference du joueur blanc
	 */
	private Joueur joueurBlanc;
	
	/**
	 * Reference du joueur noir
	 */
	private Joueur joueurNoir;
	
	/**
	 * Reference du joueur courant
	 */
	private Joueur joueurCourant;
	
	/**
	 * Reference de la fenetre
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * Reference des pieces prises
	 */
	private ArrayList<Piece> prises;
	
	/**
	 * Reference du plateau
	 */
	private PlateauReplay plateau;
	
	/**
	 * Reference des informations de la partie rejoue en replay
	 */
	private Partie partie;

	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre
	 */
	public Replay(ReplayFenetre fenetre) {
		super();
		this.coupCourant = -1;
		this.fenetre = fenetre;
		this.plateau = new PlateauReplay(this);
		this.joueurBlanc = new Joueur("BLANC");
		this.joueurNoir = new Joueur("NOIR");
		this.joueurCourant = this.joueurBlanc;
		this.prises = new ArrayList<Piece>();
	}
	
	/**
	 * Charge les informations de la partie
	 * @param p partie a charger
	 */
	public void chargerPartie(Partie p){
		this.coupCourant = -1;
		this.partie = p;
		joueurCourant = joueurBlanc;
		plateau.miseEnPlacePlateau();
		fenetre.updateHistorique();
		fenetre.getGrille().resetEtatCases();
		testSuivantPrecedent();
		fenetre.repaint();
		
	}
	
	/**
	 * Swhitche de joueur courant
	 */
	public void switchJoueur(){
		joueurCourant = (joueurCourant == joueurBlanc)? joueurNoir : joueurBlanc;
	}
	
	/**
	 * Verifie si les boutons suivant/precedent peuvent etre active
	 */
	public void testSuivantPrecedent(){
		if(coupCourant < 0){
			fenetre.getBoutonPrecedent().setEnabled(false);
		}else{
			fenetre.getBoutonPrecedent().setEnabled(true);
		}
		
		if(coupCourant >= partie.coups.getList().size()-1){
			fenetre.getBoutonSuivant().setEnabled(false);
		}else{
			fenetre.getBoutonSuivant().setEnabled(true);
		}
	}
	
	/**
	 * Reset de la partie
	 */
	public void reset(){
		this.coupCourant = -1;
		this.partie = null;
		joueurCourant = joueurBlanc;
		plateau.miseEnPlacePlateau();
		fenetre.getGrille().resetEtatCases();
		fenetre.repaint();
		fenetre.ouvrirSelectionPartie();
		testSuivantPrecedent();
	}
	
	/**
	 * Joue le coup suivant
	 */
	public void jouerCoupSuivant(){
		coupCourant++;
		
		if(coupCourant >= partie.coups.getList().size()){
			return;
		}
		
		CoupPGN coup = partie.coups.getList().get(coupCourant);
		
		if(coup.isPrise){
			if(plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nIl n'y a pas de prise\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			this.prises.add(plateau.getCase(coup.arrivee.x, coup.arrivee.y));
		}
		
		if(coup.isPetitRoque){
			int colonne = (joueurCourant.getCouleur().equals("BLANC"))? 0 : 7;
			if(plateau.getCase(7, colonne) == null || !(plateau.getCase(7, colonne) instanceof Tour)){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nIl n'y a pas de tour pour le petit roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			Tour t = (Tour) plateau.getCase(7, colonne);
			if(!t.getPremierCoup()){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nCe n'est pas le premier coup de la tour pour le petit roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			t.setPremierCoup(false);
			t.setX(5);
			plateau.setCase(7, colonne, null);
			plateau.setCase(5, colonne, t);
			
			if(plateau.getCase(4, colonne) == null || !(plateau.getCase(4, colonne) instanceof Roi)){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nIl n'y a pas de roi pour le petit roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			Roi r = (Roi) plateau.getCase(4, colonne);
			if(!r.getPremierCoup()){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nCe n'est pas le premier coup du roi pour le petit roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			coup.departMemoire.x = 4;
			coup.departMemoire.y = colonne;
			r.setPremierCoup(false);
			r.setX(6);
			plateau.setCase(4, colonne, null);
			plateau.setCase(6, colonne, r);
			coup.arrivee.x = 6;
			coup.arrivee.y = colonne;
		}else if(coup.isGrandRoque){
			int colonne = (joueurCourant.getCouleur().equals("BLANC"))? 0 : 7;
			if(plateau.getCase(0, colonne) == null || !(plateau.getCase(0, colonne) instanceof Tour)){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nIl n'y a pas de tour pour le grand roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			Tour t = (Tour) plateau.getCase(0, colonne);
			if(!t.getPremierCoup()){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nCe n'est pas le premier coup de la tour pour le grand roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			t.setPremierCoup(false);
			t.setX(3);
			plateau.setCase(0, colonne, null);
			plateau.setCase(3, colonne, t);
			
			if(plateau.getCase(4, colonne) == null || !(plateau.getCase(4, colonne) instanceof Roi)){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nIl n'y a pas de roi pour le grand roque\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
			Roi r = (Roi) plateau.getCase(4, colonne);
			if(!r.getPremierCoup()){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nCe n'est pas le premier coup du roi pour le grand roque\nCoup : "+coup.toString());
				this.reset();
				return;
			}
			coup.departMemoire.x = 4;
			coup.departMemoire.y = colonne;
			r.setPremierCoup(false);
			r.setX(2);
			plateau.setCase(4, colonne, null);
			plateau.setCase(2, colonne, r);
			coup.arrivee.x = 2;
			coup.arrivee.y = colonne;
		}else if(coup.nomPiece == ' '){
			ArrayList<Pion> pions = plateau.getPions(joueurCourant.getCouleur());
			boolean coupValide = false;
			for(Pion p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						if(coup.isTransformation){
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(p.getX(), p.getY(), null);
							if(coup.nomPieceTransformation == CoupPGN.CAVALIER){
								Cavalier c = new Cavalier(coup.arrivee.x, coup.arrivee.y, p.getCouleur(), plateau);
								plateau.setCase(coup.arrivee.x, coup.arrivee.y, c);
							}else if(coup.nomPieceTransformation == CoupPGN.FOU){
								Fou c = new Fou(coup.arrivee.x, coup.arrivee.y, p.getCouleur(), plateau);
								plateau.setCase(coup.arrivee.x, coup.arrivee.y, c);
							}else if(coup.nomPieceTransformation == CoupPGN.TOUR){
								Tour c = new Tour(coup.arrivee.x, coup.arrivee.y, p.getCouleur(), plateau);
								plateau.setCase(coup.arrivee.x, coup.arrivee.y, c);
							}else if(coup.nomPieceTransformation == CoupPGN.REINE){
								Reine c = new Reine(coup.arrivee.x, coup.arrivee.y, p.getCouleur(), plateau);
								plateau.setCase(coup.arrivee.x, coup.arrivee.y, c);
							}
						}else{
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
							plateau.setCase(p.getX(), p.getY(), null);
							p.setX(coup.arrivee.x);
							p.setY(coup.arrivee.y);
						}
						coupValide = true;
						break;
					}
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nAucun pion de correspond au coup\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}else if(coup.nomPiece == CoupPGN.TOUR){
			ArrayList<Tour> pions = plateau.getTours(joueurCourant.getCouleur());
			boolean coupValide = false;
			for(Tour p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
						plateau.setCase(p.getX(), p.getY(), null);
						coup.departMemoire.x = p.getX();
						coup.departMemoire.y = p.getY();
						p.setX(coup.arrivee.x);
						p.setY(coup.arrivee.y);
						coupValide = true;
						break;
					}
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nAucune tour ne correspond au coup\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}else if(coup.nomPiece == CoupPGN.FOU){
			ArrayList<Fou> pions = plateau.getFous(joueurCourant.getCouleur());
			boolean coupValide = false;
			for(Fou p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
						plateau.setCase(p.getX(), p.getY(), null);
						coup.departMemoire.x = p.getX();
						coup.departMemoire.y = p.getY();
						p.setX(coup.arrivee.x);
						p.setY(coup.arrivee.y);
						coupValide = true;
						break;
					}
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nAucun fou ne correspond au coup\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}else if(coup.nomPiece == CoupPGN.CAVALIER){
			ArrayList<Cavalier> pions = plateau.getCavaliers(joueurCourant.getCouleur());
			boolean coupValide = false;
			for(Cavalier p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
						plateau.setCase(p.getX(), p.getY(), null);
						coup.departMemoire.x = p.getX();
						coup.departMemoire.y = p.getY();
						p.setX(coup.arrivee.x);
						p.setY(coup.arrivee.y);
						coupValide = true;
						break;
					}
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nAucun cavalier ne correspond au coup\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}else if(coup.nomPiece == CoupPGN.REINE){
			ArrayList<Reine> pions = plateau.getReines(joueurCourant.getCouleur());
			boolean coupValide = false;
			for(Reine p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
						plateau.setCase(p.getX(), p.getY(), null);
						coup.departMemoire.x = p.getX();
						coup.departMemoire.y = p.getY();
						p.setX(coup.arrivee.x);
						p.setY(coup.arrivee.y);
						coupValide = true;
						break;
					}
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nAucune reine ne correspond au coup\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}else if(coup.nomPiece == CoupPGN.ROI){
			Roi p = (joueurCourant.getCouleur().equals("BLANC"))? plateau.getRoiB() : plateau.getRoiN();
			boolean coupValide = false;
			if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
					&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
					&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
				if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
						|| coup.depart.x != -1 && coup.depart.x == p.getX()
						|| coup.depart.y != -1 && coup.depart.y == p.getY()
						|| coup.depart.x == -1 && coup.depart.y == -1){
					plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
					plateau.setCase(p.getX(), p.getY(), null);
					coup.departMemoire.x = p.getX();
					coup.departMemoire.y = p.getY();
					p.setX(coup.arrivee.x);
					p.setY(coup.arrivee.y);
					coupValide = true;
				}
			}
			if(!coupValide){
				JOptionPane.showMessageDialog(null, "Erreur du coup\nLe roi ne peut pas faire ce mouvement\nCoup : "+coup.toString(), "Erreur du coup", JOptionPane.ERROR_MESSAGE);
				this.reset();
				return;
			}
		}
		testSuivantPrecedent();
		fenetre.getGrille().resetEtatCases();
		fenetre.getGrille().setCaseDernierCoup(coup.departMemoire.x, coup.departMemoire.y);
		fenetre.getGrille().setCaseDernierCoup(coup.arrivee.x, coup.arrivee.y);
		switchJoueur();
	}
	
	/**
	 * Getter du plateau
	 * @return
	 */
	public PlateauReplay getPlateau(){
		return plateau;
	}

	/**
	 * Getter des pieces prises
	 * @return
	 */
	public ArrayList<Piece> getPrises(){
		return prises;
	}
	
	/**
	 * Getter du joueur courant
	 * @return
	 */
	public Joueur getJoueurCourant(){
		return joueurCourant;
	}
	
	/**
	 * Getter de l'index du coup courant
	 * @return
	 */
	public int getCoupCourant(){
		return coupCourant;
	}
	
	/**
	 * Setter de l'index du coup courant
	 * @param i index
	 */
	public void setCoupCourant(int i){
		this.coupCourant = i;
	}
	
	/**
	 * Getter de la fenetre
	 * @return
	 */
	public ReplayFenetre getFenetre(){
		return fenetre;
	}
	
	/**
	 * Getter des informations de la partie
	 * @return
	 */
	public Partie getPartie(){
		return partie;
	}
	
	/**
	 * Getter du coup a une certaine position
	 * @param position position du coup dans l'historique
	 * @return le coup
	 */
	public CoupPGN getCoup(int position){
		return partie.coups.getList().get(position);
	}
}
