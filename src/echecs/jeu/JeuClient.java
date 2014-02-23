package echecs.jeu;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import echecs.Echecs;
import echecs.graphisme.Menu;
import echecs.graphisme.jeu.Fenetre;
import echecs.jeu.piece.Cavalier;
import echecs.jeu.piece.Fou;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Tour;
import echecs.reseau.client.JoueurClient;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Historique;
import echecs.sauvegarde.Partie;

/**
 * Jeu special pour le client dans le mode en ligne
 */
public class JeuClient extends Jeu{

	/**
	 * Reference du client
	 */
	private JoueurClient client;
	
	/**
	 * Vrai si c'est au tour de ce client de jouer
	 */
	private boolean tourDeJouer;
	
	/**
	 * Coup courant demande a jouer au serveur par ce client
	 */
	private CoupPGN coupCourant;

	public boolean retourmenu = false;
	
	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre
	 * @param client reference du client
	 */
	public JeuClient(Fenetre fenetre, JoueurClient client){
		super();
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		this.client = client;
		joueurBlanc = new Joueur("BLANC");
		joueurNoir = new Joueur("NOIR");
		joueurCourant = joueurBlanc;
		historique = new Historique();
		this.fenetre = fenetre;
		this.vsInternet = true;
		this.tourDeJouer = false;
		plateau.miseEnPlacePlateau();
	}
	
	/**
	 * Recois un message du serveur
	 * @param message message a lire
	 */
	public void lectureMessage(String message){
		boolean kill = false;
		boolean joue = false;
		CoupPGN coup = null;
		boolean autoriser = false;
		boolean startGame = false;
		Event event = null;
		
		String[] parser = message.split(";");
		for(int i = 0; i < parser.length; i++){
			String[] parserI = parser[i].split(":");
			if(parserI[0].equals("j")){
				joue = Boolean.parseBoolean(parserI[1]);
			}else if(parserI[0].equals("c")){
				coup = new CoupPGN(parserI[1]);
				coup.departMemoire.x = Integer.parseInt(parserI[2]);
				coup.departMemoire.y = Integer.parseInt(parserI[3]);
				coup.referencePiece = plateau.getCase(coup.departMemoire.x, coup.departMemoire.y);
			}else if(parserI[0].equals("a")){
				autoriser = Boolean.parseBoolean(parserI[1]);
			}else if(parserI[0].equals("e")){
				String e = parserI[1];
				if(e.equals("ECHEC_MAT_SUR_SOI")) event = Event.ECHEC_MAT_SUR_SOI;
				else if(e.equals("ECHEC_SUR_SOI")) event = Event.ECHEC_SUR_SOI;
				else if(e.equals("RIEN")) event = Event.RIEN;
				else if(e.equals("ECHEC")) event = Event.ECHEC;
				else if(e.equals("ECHEC_MAT")) event = Event.ECHEC_MAT;
				else if(e.equals("PAT")) event = Event.PAT;
			}else if(parserI[0].equals("s")){
				startGame = Boolean.parseBoolean(parserI[1]);
			}else if(parser[0].startsWith("B") || (parser[0].startsWith("N"))){
				String a = parser[0].substring(1);
				if(a.startsWith("/say ")){
					a = a.substring(5);
					if(parser[0].startsWith("B")){
						a = "BLANC dit : " + a;
					}else{
						a = "NOIR dit : " + a;
					}
					fenetre.addLogPartie(a);
				}
			}else if(parserI[0].equals("k")){
				kill = Boolean.parseBoolean(parserI[1]);
			}
		}
		
		//Si le joueur adverse s'est deconnecte
		if(kill){
			JOptionPane.showMessageDialog(null, "Le joueur adverse s'est déconnecte", "Joueur adverse deconnecte", JOptionPane.INFORMATION_MESSAGE);
			fenetre.setVisible(false);
			fenetre.dispose();
			new Menu();
		}
		
		//Si c'est au tour de ce joueur de jouer
		this.tourDeJouer = joue;
		
		//Si c'est le premier coup de la partie
		if(startGame){
			fenetre.miseEnAttenteReseau(false);
			fenetre.repaint();
			return;
		}
		
		//S'il y a un echec et mat
		if(event != null && event.equals(Event.ECHEC_MAT)){
			fenetre.fenetreSauvegarde((joueurCourant.getCouleur().equals("BLANC"))? Partie.BLACK_WIN : Partie.WHITE_WIN);
		}else if(event != null && event.equals(Event.ECHEC_MAT_SUR_SOI)){
			fenetre.fenetreSauvegarde((joueurCourant.getCouleur().equals("BLANC"))? Partie.WHITE_WIN : Partie.BLACK_WIN);
		}else if(event != null && event.equals(Event.PAT)){
			fenetre.fenetreSauvegarde(Partie.EGALITE);
		}
		
		//Coup non autorise
		if(!autoriser){
			//coup non autoriser car le joueur s'est mis en echec
			if(event != null && event.equals(Event.ECHEC_SUR_SOI)){
				JOptionPane.showMessageDialog(null, "Vous ne pouvez pas mettre votre roi en echec", "Impossible de jouer ce coup", JOptionPane.WARNING_MESSAGE);
				fenetre.addLogPartie("Vous ne pouvez pas mettre votre roi en echec");
				fenetre.miseEnAttenteReseau(false);
				fenetre.repaint();
				return;
			}else if(event != null && event.equals(Event.RIEN)){
				JOptionPane.showMessageDialog(null, "Vous ne pouvez pas jouer ce coup", "Impossible de jouer ce coup", JOptionPane.WARNING_MESSAGE);
				fenetre.miseEnAttenteReseau(false);
				fenetre.repaint();
				return;
			}
		}else{
			if(event != null && event.equals(Event.ECHEC)){
				JOptionPane.showMessageDialog(null, "Mise en echec du joueur adverse", "Echec", JOptionPane.INFORMATION_MESSAGE);
			}else if(event != null && event.equals(Event.ECHEC_SUR_SOI)){
				JOptionPane.showMessageDialog(null, "Echec sur le votre roi", "Echec", JOptionPane.INFORMATION_MESSAGE);
			}
			
			if(coupCourant == null){
				coupCourant = coup;
			}
			this.jouerCoup();
		}
	}
	
	@Override
	public void deplacerPiece(int x, int y){
		
		CoupPGN coup = new CoupPGN();
		coup.departMemoire.x = pieceSelectionee.getX();
		coup.departMemoire.y = pieceSelectionee.getY();
		coup.arrivee.x = x;
		coup.arrivee.y = y;
		coup.setNomPiece(pieceSelectionee.getFamille());
		
		if(plateau.getCase(x, y) != null && plateau.getCase(x, y).getCouleur().equals(pieceSelectionee.getCouleur())){
			coup.isPrise = true;
		}
		
		coup.setPrerequis(this.recherchePrerequis(pieceSelectionee, x, y));
		
		if(pieceSelectionee.getFamille().equals("ROI") && Math.abs(pieceSelectionee.getX() - x) > 1){
			if(x > pieceSelectionee.getX()){
				coup.isPetitRoque = true;
			}else{
				coup.isGrandRoque = true;
			}
		}
		
		if(pieceSelectionee.getFamille().equals("PION")){
			if((pieceSelectionee.getCouleur().equals("BLANC") && y == 7 && pieceSelectionee.getY()+1 == y && 
					((pieceSelectionee.getX() == x && plateau.getCase(x, y) == null) || 
					((pieceSelectionee.getX()+1 == x  || pieceSelectionee.getX()-1 == x) && plateau.getCase(x, y) != null && plateau.getCase(x, y).getCouleur().equals("NOIR"))))
				|| (pieceSelectionee.getCouleur().equals("NOIR") && y == 0 && pieceSelectionee.getY()-1 == y && 
						((pieceSelectionee.getX() == x && plateau.getCase(x, y) == null) || 
						((pieceSelectionee.getX()+1 == x  || pieceSelectionee.getX()-1 == x) && plateau.getCase(x, y) != null && plateau.getCase(x, y).getCouleur().equals("BLANC"))))){
				coup.isTransformation = true;
				String reponse = "";
				reponse = fenetre.showTransformations();
				if(reponse.equals("Reine")){
					coup.nomPieceTransformation = CoupPGN.REINE;
				}else if(reponse.equals("Tour")){
					coup.nomPieceTransformation = CoupPGN.TOUR;
				}else if(reponse.equals("Fou")){
					coup.nomPieceTransformation = CoupPGN.FOU;
				}else if(reponse.equals("Cavalier")){
					coup.nomPieceTransformation = CoupPGN.CAVALIER;
				}
			}
		}
		
		//Mise en attente graphiquement du jeu
		fenetre.miseEnAttenteReseau(true);
		
		coupCourant = coup;
		String envoyer = "c:"+coup.toString()+":"+coup.departMemoire.x+":"+coup.departMemoire.y;
		client.send(envoyer);
	}
	
	/**
	 * Joue un coup apres autorisation du serveur
	 */
	public void jouerCoup(){
		Piece pieceAJouer = plateau.getCase(coupCourant.departMemoire.x, coupCourant.departMemoire.y);
		
		if(coupCourant.isPetitRoque){
			String couleurJoueurCourant = joueurCourant.getCouleur();
			coupCourant.arrivee.x = plateau.getRoi(couleurJoueurCourant).getX() +2;
			coupCourant.arrivee.y = plateau.getRoi(couleurJoueurCourant).getY();
		}else if(coupCourant.isGrandRoque){
			String couleurJoueurCourant = joueurCourant.getCouleur();
			coupCourant.arrivee.x = plateau.getRoi(couleurJoueurCourant).getX() -2;
			coupCourant.arrivee.y = plateau.getRoi(couleurJoueurCourant).getY();
		}
		
		if(pieceAJouer.deplacer(coupCourant.arrivee.x, coupCourant.arrivee.y)){
			if(coupCourant.isTransformation && coupCourant.nomPieceTransformation != CoupPGN.REINE){
				Piece p = plateau.getCase(coupCourant.arrivee.x, coupCourant.arrivee.y);
				if(coupCourant.nomPieceTransformation == CoupPGN.CAVALIER){
					Cavalier c = new Cavalier(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(c.getX(), c.getY(), c);
				}else if(coupCourant.nomPieceTransformation == CoupPGN.FOU){
					Fou f = new Fou(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(f.getX(), f.getY(), f);
				}else if(coupCourant.nomPieceTransformation == CoupPGN.TOUR){
					Tour t = new Tour(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(t.getX(), t.getY(), t);
				}
			}
			switchJoueur();
		}else{
			Echecs.addLog("Impossible de jouer le coupCourant malgre l'accord du serveur : "+coupCourant.toString(), Echecs.TypeLog.ERREUR);
		}
		
		pieceSelectionee= null;
		fenetre.getGrille().updateGrille();
		fenetre.getGrille().resetEtatCases();
		fenetre.getGrille().setCaseDernierCoup(coupCourant.departMemoire.x, coupCourant.departMemoire.y);
		fenetre.getGrille().setCaseDernierCoup(coupCourant.arrivee.x, coupCourant.arrivee.y);
		
		if(tourDeJouer){
			fenetre.miseEnAttenteReseau(false);
		}
		
		coupCourant = null;
		fenetre.repaint();
	}
	
	/**
	 * Envoi un message au serveur
	 * @param message message
	 */
	public void envoyer(String message){
		client.send(message);
	}
	
	/**
	 * Delais d'attente depasse avec le serveur
	 */
	public void timeout(){
		if(!this.retourmenu){
			JOptionPane.showMessageDialog(null, "Le delais d'attente avec le serveur est depasse", "Erreur", JOptionPane.INFORMATION_MESSAGE);
			fenetre.setVisible(false);
			fenetre.dispose();
		}
			new Menu();

	}
	public void deconnexion(){
		client.getClient().deconnexion();
	}
}
