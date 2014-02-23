package echecs.jeu;

import echecs.graphisme.jeu.Fenetre;
import echecs.jeu.IA.IAThread;
import echecs.jeu.IA.ValeursEvaluation;
import echecs.jeu.piece.Cavalier;
import echecs.jeu.piece.Fou;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Pion;
import echecs.jeu.piece.Reine;
import echecs.jeu.piece.Roi;
import echecs.jeu.piece.Tour;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Historique;
import echecs.sauvegarde.Partie;
import echecs.utils.Coordonnee;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Class qui gere les differentes instances d'une partie d'echecs
 */
public class Jeu{
	
	/**
	 * Event du jeu pour la partie client/serveur
	 */
	protected enum Event{
		ECHEC_MAT_SUR_SOI, ECHEC_SUR_SOI, RIEN, ECHEC, ECHEC_MAT, PAT
	};
	
	/**
	 * Plateau courant
	 */
	protected Plateau plateau;
	
	/**
	 * Liste des pieces prises
	 */
	protected ArrayList<Piece> prises;
	
	/**
	 * Variable vrai si le jeu est en mode joueur vs ia ou ia vs ia
	 */
	private boolean vsIA;
	
	/**
	 * Si le jeu est un serveur
	 */
	protected boolean estServeur;
	
	/**
	 * Vrai si le jeu est en reseau
	 */
	protected boolean vsInternet;
	
	/**
	 * Instance du Joueur blanc
	 */
	protected Joueur joueurBlanc;
	
	/**
	 * Instance du Joueur noir
	 */
	protected Joueur joueurNoir;
	
	/**
	 * Instance du Joueur courant pendant la partie
	 */
	protected Joueur joueurCourant;
	
	/**
	 * Thread pour l'ia
	 */
	private IAThread iaThread;
	
	/**
	 * Deuxieme thread d'ia si IAvsIA
	 */
	private IAThread iaThread2;
	
	/**
	 * Instance de la piece selectionnee par le joueur
	 */
	protected Piece pieceSelectionee;
	
	/**
	 * Liste de l'historique des coups
	 */
	protected Historique historique;
	
	/**
	 * Instance de la fenetre de jeu
	 */
	protected Fenetre fenetre;
	
	/**
	 * Jeu est mode blitz
	 */
	private boolean modeBlitz;
	
	/**
	 * Reference du crhono blanc si mode blitz
	 */
	private Chrono chrono_blanc;
	
	/**
	 * Reference du chrono noir si mode blitz
	 */
	private Chrono chrono_noir;
	
	/**
	 * Constructeur
	 */
	public Jeu() {
		super();
		plateau = null;
		prises = null;
		vsIA = false;
		vsInternet = false;
		joueurBlanc = null;
		joueurNoir = null;
		joueurCourant = null;
		iaThread = null;
		iaThread2 = null;
		pieceSelectionee = null;
		historique = null;
		fenetre = null;
		modeBlitz = false;
		chrono_blanc = null;
		chrono_noir = null;
		estServeur = false;
	}
	
	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre
	 */
	public Jeu(Fenetre fenetre){
		super();
		this.modeBlitz = fenetre.modeBlitz();
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		joueurBlanc = new Joueur("BLANC");
		joueurNoir = new Joueur("NOIR");
		joueurCourant = joueurBlanc;
		historique = new Historique();
		this.fenetre = fenetre;
		vsIA = false;
		plateau.miseEnPlacePlateau();
		
		chrono_blanc = new Chrono(15, 0, this.fenetre, true);
		chrono_noir = new Chrono(15, 0, this.fenetre, false);
		chrono_blanc.start();
		chrono_noir.start();
		chrono_blanc.pause(true);
		chrono_noir.pause(true);
		if(modeBlitz){
			chrono_blanc.pause(false);
		}
	}
	
	/**
	 * Constructeur pour une partie en mode blitz
	 * @param fenetre reference de la fenetre
	 * @param m minute
	 * @param s seconde
	 */
	public Jeu(Fenetre fenetre, int m, int s){
		super();
		this.modeBlitz = fenetre.modeBlitz();
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		joueurBlanc = new Joueur("BLANC");
		joueurNoir = new Joueur("NOIR");
		joueurCourant = joueurBlanc;
		historique = new Historique();
		this.fenetre = fenetre;
		vsIA = false;
		plateau.miseEnPlacePlateau();
		
		chrono_blanc = new Chrono(m, s, this.fenetre, true);
		chrono_noir = new Chrono(m, s, this.fenetre, false);
		chrono_blanc.start();
		chrono_noir.start();
		chrono_blanc.pause(true);
		chrono_noir.pause(true);
		if(modeBlitz){
			chrono_blanc.pause(false);
		}
	}

	/**
	 * Constructeur pour une partie contre l'ordinateur
	 * @param fenetre reference de la fenetre
	 * @param humainEstBlanc couleur du joueur humain
	 * @param lvlia niveau de l'ia
	 */
	public Jeu(Fenetre fenetre, boolean humainEstBlanc, int lvlia){
		super();
		if(humainEstBlanc){
			joueurBlanc = new Joueur("BLANC");
			joueurNoir = new Joueur("NOIR");
			joueurNoir.setHumain(false);
			iaThread = new IAThread(lvlia, "NOIR", this, new ValeursEvaluation());
		}else{
			joueurBlanc = new Joueur("BLANC");
			joueurBlanc.setHumain(false);
			joueurNoir = new Joueur("NOIR");
			iaThread = new IAThread(lvlia, "BLANC", this, new ValeursEvaluation());
		}
		iaThread.pause(true);
		iaThread.start();
		joueurCourant = joueurBlanc;
		historique = new Historique();
		this.fenetre = fenetre;
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		vsIA = true;
		plateau.miseEnPlacePlateau();
	}
	
	/**
	 * Constructeur d'une partie ordinateur contre ordinateur
	 * @param fenetre reference de la fenetre
	 * @param niveauBlanc niveau du joueur blanc
	 * @param niveauNoir niveau du joueur noir
	 * @param valeursBlanc valeurs des constantes du joueur blanc si niveau 3, sinon null
	 * @param valeursNoir valeurs des constantes du joueur noir si niveau 3, sinon null
	 */
	public Jeu(Fenetre fenetre, int niveauBlanc, int niveauNoir, ValeursEvaluation valeursBlanc, ValeursEvaluation valeursNoir){
		super();
		joueurBlanc = new Joueur("BLANC");
		joueurBlanc.setHumain(false);
		joueurNoir = new Joueur("NOIR");
		joueurNoir.setHumain(false);
		
		iaThread = new IAThread(niveauBlanc, "BLANC", this, valeursBlanc);
		iaThread2 = new IAThread(niveauNoir, "NOIR", this, valeursNoir);
		
		iaThread.pause(true);
		iaThread2.pause(true);
		iaThread.start();
		iaThread2.start();
		
		joueurCourant = joueurBlanc;
		historique = new Historique();
		this.fenetre = fenetre;
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		this.vsIA = true;
		plateau.miseEnPlacePlateau();
	}

	/**
	 * Demarre une partie si le joueur blanc est un ordinateur
	 */
	public void demarrerPartieIA(){
		if(!joueurCourant.estHumain){
			iaThread.pause(false);
			fenetre.tourIA(true);
		}
	}
	
	/**
	 * Joue le coup de l'ia apres que celui ci est reflechi a son coup a jouer
	 * @param coord coordonnee d'arrivee du coup a jouer
	 */
	public void jouerIA(Coordonnee coord){
		fenetre.tourIA(false);
		if(coord != null){
			this.deplacerPiece(coord.x, coord.y);
		}else{
			fenetre.repaint();
			fenetre.fenetreSauvegarde((joueurCourant.getCouleur().equals("BLANC"))? Partie.WHITE_WIN : Partie.BLACK_WIN	);
		}
	}
	
	/**
	 * Setter des chronos
	 * @param blancMinute minute du joueur blanc
	 * @param blancSeconde seconde du joueur blanc
	 * @param noirMinute minute du joueur noir
	 * @param noirSeconde seconde du joueur noir
	 */
	public void setChronos(int blancMinute, int blancSeconde, int noirMinute, int noirSeconde){
		this.chrono_blanc.pause(true);
		this.chrono_noir.pause(true);
		this.chrono_blanc.setSeconde(blancSeconde);
		this.chrono_blanc.setMinute(blancMinute);
		this.chrono_noir.setSeconde(noirSeconde);
		this.chrono_noir.setMinute(noirMinute);
		if(joueurCourant.equals(joueurBlanc)){
			chrono_blanc.pause(false);
			chrono_noir.pause(true);
		}else{
			chrono_blanc.pause(true);
			chrono_noir.pause(false);
		}
		this.modeBlitz = true;
		fenetre.activeBlitz(true);
	}
	
	/**
	 * Designe comme piece selectionnee
	 * @param x coordonnee en abscisse de la futur piece selectionnee
	 * @param y coordonnee en ordonnee de la futur piece selectionnee
	 */
	public void setPieceSelectionee(int x, int y){
		pieceSelectionee = plateau.getCase(x, y);
		if(joueurCourant.estHumain && !estServeur){
			fenetre.getGrille().ajouterDeplacementPossible(pieceSelectionee.casesPossibles());
		}
		
		if(fenetre != null){
			fenetre.repaint();
		}
	}
	
	/**
	 * Renvoi le test boolean de si la piece selectionnee est a null
	 * @return le resultat du test
	 */
	public boolean aucunePieceSelectionee(){
		return pieceSelectionee == null;
	}
	
	/**
	 * Deplace la piece selectionnee sur la coordonnee (x,y)
	 * @param x la coordonnee en abscisse 
	 * @param y la coordonnee en ordonee
	 */
	public void deplacerPiece(int x, int y){
		boolean deplacementSucces = false;
		
		Roi[] rois = new Roi[2];
		rois[0] = plateau.getRoiB();
		rois[1] = plateau.getRoiN();
		
		int valeurPrerequis = recherchePrerequis(pieceSelectionee, x, y);
		
		//Deplace la piece selectionne
		if(pieceSelectionee.deplacer(x, y)){
			CoupPGN coup = historique.getDernierCoup();
			coup.setPrerequis(valeurPrerequis);
			for(int i = 0; i < rois.length; i++){
				//Si le roi est en echec
				if(rois[i].estEchec()){
					fenetre.addLogPartie("Le roi "+rois[i].getCouleur().toLowerCase()+" est en echec");
					//Si le joueur viens de mettre son roi en echec
					if(joueurCourant.getCouleur().equals(rois[i].getCouleur())){
						plateau.annulerDernierCoup(true);
						fenetre.addLogPartie("Vous ne pouvez pas mettre votre roi en echec");
						break;
					//Si le roi est en echec et mat
					}else if(rois[i].estEchecEtMat()){
						coup.isEchecMat = true;
						fenetre.addLogPartie("Echec et Mat sur le roi "+rois[i].getCouleur().toLowerCase());
						fermerThreadIA();
						fenetre.repaint();
						fenetre.fenetreSauvegarde((joueurCourant.getCouleur().equals("BLANC"))? Partie.WHITE_WIN : Partie.BLACK_WIN	);
					//Sinon c'est le joueur adverse qui a cause l'echec
					}else{
						coup.isEchec = true;
					}
				}else{
					if(!rois[i].getCouleur().equals(joueurCourant.getCouleur()) && rois[i].estPat()){
						fenetre.addLogPartie("Egalite");
						fenetre.repaint();
						fenetre.fenetreSauvegarde(Partie.EGALITE);
					}
				}
			}
			switchJoueur();
			deplacementSucces = true;
		}
		
		pieceSelectionee = null;
		fenetre.getGrille().updateGrille();
		fenetre.getGrille().resetEtatCases();
		if(deplacementSucces){
			CoupPGN coup = historique.getDernierCoup();
			fenetre.getGrille().setCaseDernierCoup(coup.departMemoire.x, coup.departMemoire.y);
			fenetre.getGrille().setCaseDernierCoup(coup.arrivee.x, coup.arrivee.y);
		}
		fenetre.repaint();
		
		if(!joueurBlanc.estHumain() && !joueurNoir.estHumain() && !iaThread.isReflechi() && !iaThread2.isReflechi()){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//Si c'est le tour de l'ia
		if(!joueurCourant.estHumain()){
			if(joueurCourant.getCouleur().equals(iaThread.getCouleur())){
				fenetre.tourIA(true);
				iaThread.pause(false);
			}else{
				fenetre.tourIA(true);
				iaThread2.pause(false);
			}
		}
	}
	
	/**
	 * Joue l'historique
	 * @param historiqueSave historique a rejoeur
	 * @return vrai si on a reussi a jouer tous les coups
	 */
	public boolean jouerSauvegarde(ArrayList<CoupPGN> historiqueSave){
		//reset de la partie
		reset();
		//Joue chaque coup
		for(int i = 0; i < historiqueSave.size(); i++){
			CoupPGN coup = historiqueSave.get(i);
			
			if(coup.isPrise){
				if(plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" n'est pas une prise.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				this.prises.add(plateau.getCase(coup.arrivee.x, coup.arrivee.y));
			}
			
			if(coup.isPetitRoque){
				int colonne = (joueurCourant.getCouleur().equals("BLANC"))? 0 : 7;
				if(plateau.getCase(7,  colonne) == null || !(plateau.getCase(7, colonne ) instanceof Tour)){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" n'a pas la tour en position de roquer.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				Tour t = (Tour) plateau.getCase(7, colonne);
				if(!t.getPremierCoup()){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" : la tour s'est deja deplace.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				t.setPremierCoup(false);
				t.setX(5);
				plateau.setCase(7, colonne, null);
				plateau.setCase(5, colonne, t);
				
				if(plateau.getCase(4,  colonne) == null || !(plateau.getCase(4, colonne ) instanceof Roi)){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" n'a pas le roi en position de roquer.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				Roi r = (Roi) plateau.getCase(4, colonne);
				if(!r.getPremierCoup()){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" : le roi s'est deja deplace.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
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
				if(plateau.getCase(0,  colonne) == null || !(plateau.getCase(0, colonne ) instanceof Tour)){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" n'a pas la tour en position de roquer.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				Tour t = (Tour) plateau.getCase(0, colonne);
				if(!t.getPremierCoup()){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" : la tour s'est deja deplace.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				t.setPremierCoup(false);
				t.setX(3);
				plateau.setCase(0, colonne, null);
				plateau.setCase(3, colonne, t);
				
				if(plateau.getCase(4,  colonne) == null || !(plateau.getCase(4, colonne ) instanceof Roi)){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" n'a pas le roi en position de roquer.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
				Roi r = (Roi) plateau.getCase(4, colonne);
				if(!r.getPremierCoup()){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : coup "+coup.toString()+" : le roi s'est deja deplace.", "Erreur lors du chargement de l'historique", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
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
					if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
						if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
								|| coup.depart.x != -1 && coup.depart.x == p.getX()
								|| coup.depart.y != -1 && coup.depart.y == p.getY()
								|| coup.depart.x == -1 && coup.depart.y == -1){
							if(coup.isTransformation){
								plateau.setCase(p.getX(), p.getY(), null);
								coup.departMemoire.set(p.getX(), p.getY());
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
							p.setPremierCoup(false);
							coupValide = true;
							break;
						}
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : aucun pion ne correspond au coup.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}else if(coup.nomPiece == CoupPGN.TOUR){
				ArrayList<Tour> pions = plateau.getTours(joueurCourant.getCouleur());
				boolean coupValide = false;
				for(Tour p: pions){
					if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
						if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
								|| coup.depart.x != -1 && coup.depart.x == p.getX()
								|| coup.depart.y != -1 && coup.depart.y == p.getY()
								|| coup.depart.x == -1 && coup.depart.y == -1){
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
							plateau.setCase(p.getX(), p.getY(), null);
							p.setX(coup.arrivee.x);
							p.setY(coup.arrivee.y);
							coupValide = true;
							break;
						}
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : aucune tour ne correspond au coup.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}else if(coup.nomPiece == CoupPGN.FOU){
				ArrayList<Fou> pions = plateau.getFous(joueurCourant.getCouleur());
				boolean coupValide = false;
				for(Fou p: pions){
					if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
						if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
								|| coup.depart.x != -1 && coup.depart.x == p.getX()
								|| coup.depart.y != -1 && coup.depart.y == p.getY()
								|| coup.depart.x == -1 && coup.depart.y == -1){
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
							plateau.setCase(p.getX(), p.getY(), null);
							p.setX(coup.arrivee.x);
							p.setY(coup.arrivee.y);
							coupValide = true;
							break;
						}
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : aucun fou ne correspond au coup.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}else if(coup.nomPiece == CoupPGN.CAVALIER){
				ArrayList<Cavalier> pions = plateau.getCavaliers(joueurCourant.getCouleur());
				boolean coupValide = false;
				for(Cavalier p: pions){
					if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
						if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
								|| coup.depart.x != -1 && coup.depart.x == p.getX()
								|| coup.depart.y != -1 && coup.depart.y == p.getY()
								|| coup.depart.x == -1 && coup.depart.y == -1){
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
							plateau.setCase(p.getX(), p.getY(), null);
							p.setX(coup.arrivee.x);
							p.setY(coup.arrivee.y);
							coupValide = true;
							break;
						}
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : aucun cavalier ne correspond au coup.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}else if(coup.nomPiece == CoupPGN.REINE){
				ArrayList<Reine> pions = plateau.getReines(joueurCourant.getCouleur());
				boolean coupValide = false;
				for(Reine p: pions){
					if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
						if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
								|| coup.depart.x != -1 && coup.depart.x == p.getX()
								|| coup.depart.y != -1 && coup.depart.y == p.getY()
								|| coup.depart.x == -1 && coup.depart.y == -1){
							coup.departMemoire.set(p.getX(), p.getY());
							plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
							plateau.setCase(p.getX(), p.getY(), null);
							p.setX(coup.arrivee.x);
							p.setY(coup.arrivee.y);
							coupValide = true;
							break;
						}
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : aucune reine ne correspond au coup.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}else if(coup.nomPiece == CoupPGN.ROI){
				Roi p = (joueurCourant.getCouleur().equals("BLANC"))? plateau.getRoiB() : plateau.getRoiN();
				boolean coupValide = false;
				if(p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) && p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						plateau.setCase(coup.arrivee.x, coup.arrivee.y, p);
						plateau.setCase(p.getX(), p.getY(), null);
						p.setX(coup.arrivee.x);
						p.setY(coup.arrivee.y);
						coupValide = true;
					}
				}
				if(!coupValide){
					JOptionPane.showMessageDialog(null, "Erreur lors du chargement de l'historique des coups.\nRaison : "+coup.toString()+" : le roi ne peut pas faire se deplacement.", "Erreur lors du chargement de l'historique des coups", JOptionPane.ERROR_MESSAGE);
					this.reset();
					return false;
				}
			}
			historique.addCoupPGN(coup);
			switchJoueur();
		}
		
		aucunePieceSelectionee();
		fenetre.getGrille().resetEtatCases();
		fenetre.repaint();
		
		return true;
	}
	
	/**
	 * Recherche les prerequis pour un coups
	 * @param p piece sur laquelle il faut rechercher les prerequis
	 * @param x deplacement de la piece en x
	 * @param y deplacement de la piece en y
	 * @return un code de la valeur de prerequis
	 */
	public int recherchePrerequis(Piece p, int x, int y){
		int valeur = 1;
		if(p.getClass().equals(Pion.class)){
			ArrayList<Pion> pions = plateau.getPions(p.getCouleur());
			for(Pion a : pions){
				if(a != p){
					if(a.coupPossible(x, y) && a.mouvementPossible(x, y)){
						if(a.getX() == p.getX()){
							valeur += 10;
						}
						if(a.getY() == p.getY()){
							valeur  += 100;
						}else{
							if(valeur %100 < 90) valeur += 10;
						}
					}
				}
			}
		}else if(p.getClass().equals(Tour.class)){
			ArrayList<Tour> tours = plateau.getTours(p.getCouleur());
			for(Tour a : tours){
				if(a != p){
					if(a.coupPossible(x, y) && a.mouvementPossible(x, y)){
						if(a.getX() == p.getX()){
							valeur += 10;
						}
						if(a.getY() == p.getY()){
							valeur  += 100;
						}else{
							if(valeur %100 < 90) valeur += 10;
						}
					}
				}
			}
		}else if(p.getClass().equals(Cavalier.class)){
			ArrayList<Cavalier> cavaliers = plateau.getCavaliers(p.getCouleur());
			for(Cavalier a : cavaliers){
				if(a != p){
					if(a.coupPossible(x, y) && a.mouvementPossible(x, y)){
						if(a.getX() == p.getX()){
							valeur += 10;
						}
						if(a.getY() == p.getY()){
							valeur  += 100;
						}else{
							if(valeur%100 < 90) valeur += 10;
						}
					}
				}
			}
		}else if(p.getClass().equals(Fou.class)){
			ArrayList<Fou> fous = plateau.getFous(p.getCouleur());
			for(Fou a : fous){
				if(a != p){
					if(a.coupPossible(x, y) && a.mouvementPossible(x, y)){
						if(a.getX() == p.getX()){
							valeur += 10;
						}
						if(a.getY() == p.getY()){
							valeur  += 100;
						}else{
							if(valeur %100 < 90) valeur += 10;
						}
					}
				}
			}
		}else if(p.getClass().equals(Reine.class)){
			ArrayList<Reine> reines = plateau.getReines(p.getCouleur());
			for(Reine a : reines){
				if(a != p){
					if(a.coupPossible(x, y) && a.mouvementPossible(x, y)){
						if(a.getX() == p.getX()){
							valeur += 10;
						}
						if(a.getY() == p.getY()){
							valeur  += 100;
						}else{
							if(valeur %100 < 90) valeur += 10;
						}
					}
				}
			}
		}
		
		return valeur;
	}
	
	/**
	 * Remet une partie a zero
	 */
	public void reset(){
		if(vsIA){
			this.fermerThreadIA();
			if(!joueurBlanc.estHumain() && !joueurNoir.estHumain()){
				iaThread = new IAThread(iaThread.getNiveau(), "BLANC", this, iaThread.getValeurs());
				iaThread2 = new IAThread(iaThread2.getNiveau(), "NOIR", this, iaThread2.getValeurs());
				iaThread.pause(true);
				iaThread2.pause(true);
				iaThread.start();
				iaThread2.start();
				joueurBlanc = new Joueur("BLANC");
				joueurNoir = new Joueur("NOIR");
				joueurBlanc.setHumain(false);
				joueurNoir.setHumain(false);
			}else if(!joueurBlanc.estHumain()){
				iaThread = new IAThread(iaThread.getNiveau(), "BLANC", this, iaThread.getValeurs());
				iaThread.pause(true);
				iaThread.start();
				joueurBlanc = new Joueur("BLANC");
				joueurNoir = new Joueur("NOIR");
				joueurBlanc.setHumain(false);
			}else{
				iaThread = new IAThread(iaThread.getNiveau(), "NOIR", this, iaThread.getValeurs());
				iaThread.pause(true);
				iaThread.start();
				joueurBlanc = new Joueur("BLANC");
				joueurNoir = new Joueur("NOIR");
				joueurNoir.setHumain(false);
			}
		}else{
			joueurBlanc = new Joueur("BLANC");
			joueurNoir = new Joueur("NOIR");
		}
		
		joueurCourant = joueurBlanc;
        plateau = new Plateau(this);
        prises = new ArrayList<Piece>();
		historique = new Historique();
		pieceSelectionee = null;
		
		plateau.miseEnPlacePlateau();
		
		fenetre.clearLogsPartie();
		fenetre.repaint();
		
		this.demarrerPartieIA();
	}
	
	/**
	 * Change le joueur courant (Si c'etait le joueur blanc, joueurCourant devient le joueur noir)
	 */
	public void switchJoueur(){
		joueurCourant = (joueurCourant == joueurBlanc)? joueurNoir : joueurBlanc;
		
		if(modeBlitz){
			if(joueurCourant.getCouleur().equals("BLANC")){
				chrono_blanc.pause(false);
				chrono_noir.pause(true);
			}
			else{
				chrono_noir.pause(false);
				chrono_blanc.pause(true);
			}
		}
	}
	
	/**
	 * Getter pour historique
	 * @return la reference de l'historique de Coup
	 */
	public Historique getHistorique(){
		return historique;
	}
	
	/**
	 * Affiche les prises dans la console
	 */
	public void afficherPrises(){
		System.out.print("Piece Prises : [");
		for(int i=0; i<prises.size(); i++){
			System.out.print(prises.get(i).getFamille() + ""+ prises.get(i).getCouleur()+", ");
		}
		System.out.println("]");
	}
	
	/**
	 * Getter pour le joueurCourant
	 * @return la reference du joueurCourant
	 */
	public Joueur getJoueurCourant(){
		return joueurCourant;
	}
	
	/**
	 * Getter pour le joueur blanc
	 * @return la reference du joueurBlanc
	 */
	public Joueur getJoueurBlanc(){
		return joueurBlanc;
	}
	
	/**
	 * Getter pour le joueur noir
	 * @return la reference du joueurNoir
	 */
	public Joueur getJoueurNoir(){
		return joueurNoir;
	}
	
	/**
	 * Setter pour joueurCourant
	 * @param j joueurCourant
	 */
	public void setJoueurCourant(Joueur j){
		this.joueurCourant = j;
	}
	
	/**
	 * Getter pour la fenetre lie au jeu
	 * @return fenetre
	 */
	public Fenetre getFenetre(){
		return this.fenetre;
	}
	
	/**
	 * Getter du plateau
	 * @return Plateau
	 */
	public Plateau getPlateau(){
		return this.plateau;
	}
	
	/**
	 * Getter des pieces prises
	 * @return ArrayList<Piece>
	 */
	public ArrayList<Piece> getPrises(){
		return this.prises;
	}
	
	/**
	 * Getter du chrono blanc
	 * @return
	 */
	public Chrono getChronoBlanc(){
		return chrono_blanc;
	}
	
	/**
	 * Getter du chrono noir
	 * @return
	 */
	public Chrono getChronoNoir(){
		return chrono_noir;
	}
	
	/**
	 * Test si le jeu est en mode blitz
	 * @return true si mode blitz
	 */
	public boolean isBlitz(){
		return this.modeBlitz;
	}
	
	/**
	 * Desactive le mode blitz, met en pause les timers des joueurs
	 */
	public void desactiveBlitz(){
		this.modeBlitz = false;
		this.chrono_blanc.pause(true);
		this.chrono_noir.pause(true);
		this.fenetre.activeBlitz(false);
	}
	
	/**
	 * Test si le jeu est en mode vs IA
	 * @return true si contre IA
	 */
	public boolean isVsIA(){
		return this.vsIA;
	}
	
	/**
	 * Getter du thread traitant les ia
	 * @return IAThread
	 */
	public IAThread getIAThread(){
		return this.iaThread;
	}
	
	/**
	 * Getter du thread 2 pour la deuxieme ia si partie de type ia vs ia
	 * @return
	 */
	public IAThread getIAThread2(){
		return this.iaThread2;
	}
	
	/**
	 * Getter pour savoir si le jeu est un serveur
	 * @return boolean
	 */
	public boolean isServer(){
		return estServeur;
	}
	
	/**
	 * Getter pour savoir si le jeu est en reseau
	 * @return boolean
	 */
	public boolean isVsInternet(){
		return this.vsInternet;
	}
	
	/**
	 * Stop les thread des IA
	 */
	public void fermerThreadIA(){
		if(iaThread != null && iaThread.isAlive()){
			if(iaThread.isReflechi()){
				iaThread.stop();
			}
			iaThread.setVivant(false);
		}
		
		if(iaThread2 != null && iaThread2.isAlive()){
			if(iaThread2.isReflechi()){
				iaThread2.stop();
			}
			iaThread2.setVivant(false);
		}
	}
}
