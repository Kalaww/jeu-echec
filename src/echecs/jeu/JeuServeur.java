package echecs.jeu;

import java.util.ArrayList;

import echecs.reseau.serveur.ServerGameManager;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Historique;
import echecs.jeu.piece.Cavalier;
import echecs.jeu.piece.Fou;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Roi;
import echecs.jeu.piece.Tour;

/**
 * Jeu special pour le serveur en mode en ligne
 */
public class JeuServeur extends Jeu{
	
	/**
	 * Reference du serveur
	 */
	private ServerGameManager serveur;
	
	/**
	 * Constructeur
	 * @param serveur reference du serveur
	 */
	public JeuServeur(ServerGameManager serveur){
		super();
		this.plateau = new Plateau(this);
		this.prises = new ArrayList<Piece>();
		this.historique = new Historique();
		this.serveur = serveur;
		this.joueurBlanc = new Joueur("BLANC");
		this.joueurNoir = new Joueur("NOIR");
		this.joueurCourant = this.joueurBlanc;
		this.vsInternet = true;
		this.estServeur = true;
		plateau.miseEnPlacePlateau();
	}

	/**
	 * Demarre la partie, demande au joueur blanc de jouer
	 */
	public void demarrerPartie(){
		String message = "s:True";
		serveur.sendB(message);
	}
	
	/**
	 * Recois un message d'un client et le lit
	 * @param message message a lire
	 */
	public void lectureMessage(String message){
		CoupPGN coup = null;
		
		char couleurMessage = message.charAt(0);
		message = message.substring(1);
		if(joueurCourant.getCouleur().charAt(0) != couleurMessage){
			return;
		}
		
		String[] parser = message.split(";");
		for(int i = 0; i < parser.length; i++){
			String[] parserI = parser[i].split(":");
			if(parserI[0].equals("c")){
				coup = new CoupPGN(parserI[1]);
				coup.departMemoire.x = Integer.parseInt(parserI[2]);
				coup.departMemoire.y = Integer.parseInt(parserI[3]);
				coup.referencePiece = plateau.getCase(coup.departMemoire.x, coup.departMemoire.y);
			}
		}
		
		if(coup != null){
			this.setPieceSelectionee(coup.departMemoire.x, coup.departMemoire.y);
			this.jouerCoup(coup);
		}
	}
	
	/**
	 * Joue le coup et verifie s'il est valide
	 * @param coup coup a jouer
	 */
	public void jouerCoup(CoupPGN coup){
		Roi[] rois = new Roi[2];
		rois[0] = plateau.getRoiB();
		rois[1] = plateau.getRoiN();
		
		String couleurJoueurCourant = joueurCourant.getCouleur();
		String couleurJoueurAdverse = couleurJoueurCourant.equals("BLANC")? "NOIR" : "BLANC";
		
		if(coup.isPetitRoque){
			coup.arrivee.x = plateau.getRoi(couleurJoueurCourant).getX() +2;
			coup.arrivee.y = plateau.getRoi(couleurJoueurCourant).getY();
		}else if(coup.isGrandRoque){
			coup.arrivee.x = plateau.getRoi(couleurJoueurCourant).getX() -2;
			coup.arrivee.y = plateau.getRoi(couleurJoueurCourant).getY();
		}
		
		if(pieceSelectionee.deplacer(coup.arrivee.x, coup.arrivee.y)){
			if(coup.isTransformation && coup.nomPieceTransformation != CoupPGN.REINE){
				Piece p = plateau.getCase(coup.arrivee.x, coup.arrivee.y);
				if(coup.nomPieceTransformation == CoupPGN.CAVALIER){
					Cavalier c = new Cavalier(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(c.getX(), c.getY(), c);
				}else if(coup.nomPieceTransformation == CoupPGN.FOU){
					Fou f = new Fou(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(f.getX(), f.getY(), f);
				}else if(coup.nomPieceTransformation == CoupPGN.TOUR){
					Tour t = new Tour(p.getX(), p.getY(), p.getCouleur(), plateau);
					plateau.setCase(t.getX(), t.getY(), t);
				}
			}
			boolean messageEnvoyer = false;
			for(int i = 0; i < rois.length; i++){
				//si le roi est en echec
				if(rois[i].estEchec()){
					if(joueurCourant.getCouleur().equals(rois[i].getCouleur())){
						String envoyer = "a:False;e:ECHEC_SUR_SOI";
						envoyerMessage(envoyer, couleurJoueurCourant);
						messageEnvoyer = true;
						plateau.annulerDernierCoup(true);
						break;
					}else if(rois[i].estEchecEtMat()){
						String envoyer = "a:True;e:ECHEC_MAT";
						envoyerMessage(envoyer, couleurJoueurCourant);
						envoyer = "j:True;a:True;c:"+coup.toString()+":"+coup.departMemoire.x+":"+coup.departMemoire.y+";e:ECHEC_MAT_SUR_SOI";
						envoyerMessage(envoyer, couleurJoueurAdverse);
						return;
					}else{
						String envoyer = "a:True;e:ECHEC";
						envoyerMessage(envoyer, couleurJoueurCourant);
						envoyer = "j:True;a:True;c:"+coup.toString()+":"+coup.departMemoire.x+":"+coup.departMemoire.y+";e:ECHEC_SUR_SOI";
						envoyerMessage(envoyer, couleurJoueurAdverse);
						messageEnvoyer = true;
					}
				}else{
					if(!rois[i].getCouleur().equals(joueurCourant.getCouleur()) && rois[i].estPat()){
						String envoyer = "a:True;e:PAT";
						envoyerMessage(envoyer, couleurJoueurCourant);
						envoyer = "j:True;a:True;c:"+coup.toString()+":"+coup.departMemoire.x+":"+coup.departMemoire.y+";e:PAT";
						envoyerMessage(envoyer, couleurJoueurAdverse);
						return;
					}
				}
			}
			
			if(!messageEnvoyer){
				String envoyer = "a:True";
				envoyerMessage(envoyer, couleurJoueurCourant);
				envoyer = "j:True;a:True;c:"+coup.toString()+":"+coup.departMemoire.x+":"+coup.departMemoire.y;
				envoyerMessage(envoyer, couleurJoueurAdverse);
			}
			switchJoueur();
		}else{
			String envoyer = "a:False;e:RIEN";
			envoyerMessage(envoyer, couleurJoueurCourant);
		}
	}
	
	/**
	 * Envoi un message au client
	 * @param message message a envoyer
	 * @param couleur couleur du client a qui envoyer le message
	 */
	private void envoyerMessage(String message, String couleur){
		if(couleur.equals("BLANC")){
			serveur.sendB(message);
		}else{
			serveur.sendN(message);
		}
	}
}
