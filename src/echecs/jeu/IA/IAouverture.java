package echecs.jeu.IA;

import java.io.File;
import java.util.ArrayList;

import echecs.Echecs;
import echecs.utils.Coordonnee;
import echecs.jeu.piece.Cavalier;
import echecs.jeu.piece.Fou;
import echecs.jeu.Jeu;
import echecs.jeu.Joueur;
import echecs.jeu.piece.Pion;
import echecs.jeu.Plateau;
import echecs.jeu.piece.Reine;
import echecs.jeu.piece.Roi;
import echecs.jeu.piece.Tour;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Partie;
import echecs.sauvegarde.Sauvegarde;

/**
 * IA qui joue comme les parties sauvegardees. Il prend la partie la plsu jouer exactement comme la partie en cours
 */
public class IAouverture extends Joueur implements IA{
	
	/**
	 * Liste de l'ensemble des fichiers PGN dans le dossier save
	 */
	private String[] fichiersPGN;
	
	/**
	 * Coordonnee que l'ia joue
	 */
	private Coordonnee coordonneeAJouer;
	
	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Constructeur
	 * @param couleur couleur de l'ia
	 * @param jeu reference du jeu
	 */
	public IAouverture(String couleur, Jeu jeu){
		super(couleur);
		this.jeu = jeu;
		this.estHumain = false;
		recupererFichiersPGN();
	}

	@Override
	public void jouer(){
		//Si c'est le tout premier coup, ouverture classique e4
		if(jeu.getHistorique().isEmpty()){
			jeu.setPieceSelectionee(4,  1);
			this.coordonneeAJouer = new Coordonnee(4, 3);
			return;
		}
		
		CoupPGN coup = recherche();
		if(coup == null){
			if(Echecs.DEBUG) Echecs.addLog("Aucune partie similaire trouvee", Echecs.TypeLog.INFO);
			this.coordonneeAJouer = null;
			return;
		}
		jeu.setPieceSelectionee(coup.departMemoire.x, coup.departMemoire.y);
		this.coordonneeAJouer = coup.arrivee;
	}
	
	/**
	 * Cherche le coup le plus joue dans l'ensemble des fichiers PGN
	 * @return le coup le plus joue
	 */
	private CoupPGN recherche(){
		ArrayList<Partie> partiesCompatibles = new ArrayList<Partie>();
		//Parcours tous les fichiers
		for(String fichier : fichiersPGN){
			//recupere le nombre de partie dans le fichier
			int nombrePartie = Sauvegarde.nombrePartiesFichierPGN(fichier);
			//parcours toutes les parties du fichier
			for(int i = 0; i < nombrePartie; i++){
				Partie p = Sauvegarde.lireSauvegardePGN(fichier, i);
				ArrayList<CoupPGN> historique = jeu.getHistorique().getList();
				boolean memeHistorique = true;
				//test si l'historique demarre exactement pareil
				if(p.coups.getList().size() >= historique.size()){
					for(int k = 0; k < historique.size(); k++){
						if(!historique.get(k).formatPGN().equals(p.coups.getList().get(k).formatPGN())){
							memeHistorique = false;
							break;
						}
					}
				}else{
					memeHistorique = false;
				}
				//Si oui, alors on l'ajoute aux parties compatibles
				if(memeHistorique){
					partiesCompatibles.add(p);
				}
			}
		}
		
		//Si aucune partie compatible trouvee
		if(partiesCompatibles.size() == 0){
			return null;
		}
		
		//Choix du coup le plus "populaire"
		CoupEtResultat.setSigne((jeu.getJoueurCourant().getCouleur().equals("BLANC"))? 1 : -1);
		int indexProchainCoup = jeu.getHistorique().getList().size();
		ArrayList<CoupEtResultat> coupsPossibles = new ArrayList<CoupEtResultat>();
		for(Partie p : partiesCompatibles){
			CoupEtResultat cr = new CoupEtResultat(p.coups.getList().get(indexProchainCoup), p.result);
			boolean existeDeja = false;
			for(CoupEtResultat coup : coupsPossibles){
				if(cr.compareTo(coup)){
					existeDeja = true;
					coup.compteur++;
					break;
				}
			}
			if(!existeDeja){
				coupsPossibles.add(cr);
			}
		}
		
		//for(CoupEtResultat c : coupsPossibles){
		//	System.out.println(c.toString());
		//}
		
		CoupEtResultat meilleur = coupsPossibles.get(0);
		for(CoupEtResultat c : coupsPossibles){
			if(c.compteur > meilleur.compteur){
				meilleur = c;
			}else if(c.compteur == meilleur.compteur){
				if(c.resultat > meilleur.resultat){
					meilleur = c;
				}
			}
		}
		
		coordonneesCoupAJouer(meilleur.coup);
		
		return meilleur.coup;
	}
	
	/**
	 * Cherche les coordonnees depart/arrivee du coup PGN a jouer
	 * @param coup le coup PGN a jouer
	 */
	private void coordonneesCoupAJouer(CoupPGN coup){
		Joueur joueurCourant = jeu.getJoueurCourant();
		Plateau plateau = jeu.getPlateau();
		
		if(coup.isPetitRoque){
			int colonne = (joueurCourant.getCouleur().equals("BLANC"))? 0 : 7;
			coup.departMemoire.x = 4;
			coup.departMemoire.y = colonne;
			coup.arrivee.x = 6;
			coup.arrivee.y = colonne;
		}else if(coup.isGrandRoque){
			int colonne = (joueurCourant.getCouleur().equals("BLANC"))? 0 : 7;
			coup.departMemoire.x = 4;
			coup.departMemoire.y = colonne;
			coup.arrivee.x = 2;
			coup.arrivee.y = colonne;
		}else if(coup.nomPiece == ' '){
			ArrayList<Pion> pions = plateau.getPions(joueurCourant.getCouleur());
			for(Pion p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						break;
					}
				}
			}
		}else if(coup.nomPiece == CoupPGN.TOUR){
			ArrayList<Tour> pions = plateau.getTours(joueurCourant.getCouleur());
			for(Tour p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						break;
					}
				}
			}
		}else if(coup.nomPiece == CoupPGN.FOU){
			ArrayList<Fou> pions = plateau.getFous(joueurCourant.getCouleur());
			for(Fou p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						break;
					}
				}
			}
		}else if(coup.nomPiece == CoupPGN.CAVALIER){
			ArrayList<Cavalier> pions = plateau.getCavaliers(joueurCourant.getCouleur());
			for(Cavalier p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						break;
					}
				}
			}
		}else if(coup.nomPiece == CoupPGN.REINE){
			ArrayList<Reine> pions = plateau.getReines(joueurCourant.getCouleur());
			for(Reine p: pions){
				if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
						&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
						&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
					if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
							|| coup.depart.x != -1 && coup.depart.x == p.getX()
							|| coup.depart.y != -1 && coup.depart.y == p.getY()
							|| coup.depart.x == -1 && coup.depart.y == -1){
						coup.departMemoire.set(p.getX(), p.getY());
						break;
					}
				}
			}
		}else if(coup.nomPiece == CoupPGN.ROI){
			Roi p = (joueurCourant.getCouleur().equals("BLANC"))? plateau.getRoiB() : plateau.getRoiN();
			if((plateau.getCase(coup.arrivee.x, coup.arrivee.y) == null || !plateau.getCase(coup.arrivee.x, coup.arrivee.y).getCouleur().equals(p.getCouleur())) 
					&& p.mouvementPossible(coup.arrivee.x, coup.arrivee.y) 
					&& p.coupPossible(coup.arrivee.x, coup.arrivee.y)){
				if(coup.depart.x != -1 && coup.depart.y != -1 && coup.depart.x == p.getX() && coup.depart.y == p.getY()
						|| coup.depart.x != -1 && coup.depart.x == p.getX()
						|| coup.depart.y != -1 && coup.depart.y == p.getY()
						|| coup.depart.x == -1 && coup.depart.y == -1){
					coup.departMemoire.set(p.getX(), p.getY());
				}
			}
		}
	}
	
	/**
	 * Recupere l'ensemble des nom de fichiers PGN du dossier save
	 */
	private void recupererFichiersPGN(){
		File dossier = new File(Echecs.SAVE_PATH);
		String[] tmp = dossier.list();
		
		ArrayList<String> arrayTmp = new ArrayList<String>();
		for(int i = 0; i < tmp.length; i++){
			if(tmp[i].endsWith(".pgn")){
				arrayTmp.add(tmp[i].substring(0, tmp[i].length() -4));
			}
		}
		
		fichiersPGN = new String[arrayTmp.size()];
		for(int i = 0; i < fichiersPGN.length; i++){
			fichiersPGN[i] = arrayTmp.get(i);
		}
	}

	@Override
	public Coordonnee getCoordonneeAJouer() {
		return this.coordonneeAJouer;
	}
}

/**
 * Stocke un coup, avec son nombre d'occurence dans les fichiers PGN et le resultat de la partie lie
 */
class CoupEtResultat{
	
	/**
	 * Coup pgn
	 */
	public CoupPGN coup;
	
	/**
	 * Resultat de la partie du coup
	 */
	public int resultat;
	
	/**
	 * compteur du combre d'occurence du coup
	 */
	public int compteur;
	
	/**
	 * Variable qui evalue le resultat en fonction de la couleur de l'ia
	 */
	public static int signe;
	
	/**
	 * Constructeur
	 * @param coup coup PGN
	 * @param resultat resultat lie au coup PGN
	 */
	public CoupEtResultat(CoupPGN coup, String resultat){
		this.coup = coup;
		this.compteur = 1;
		
		if(resultat.equals(Partie.WHITE_WIN)){
			this.resultat = 1*signe;
		}else if(resultat.equals(Partie.EGALITE)){
			this.resultat = 0;
		}else if(resultat.equals(Partie.BLACK_WIN)){
			this.resultat = -1*signe;
		}
	}
	
	/**
	 * Compare l'objet a celui passer en parametre
	 * @param c l'objet a comparer
	 * @return vrai si c'est le meme coup
	 */
	public boolean compareTo(CoupEtResultat c){
		return this.coup.formatPGN().equals(c.coup.formatPGN()) && this.resultat >= c.resultat;
	}
	
	/**
	 * Representation en string
	 */
	public String toString(){
		return "Coup "+coup.formatPGN()+" ("+resultat+") compteur:"+compteur;
	}
	
	/**
	 * Recupere la variable qui evalue le resultat en fonction de la couleur de l'ia
	 * @param a
	 */
	public static void setSigne(int a){
		signe = a;
	}
}
