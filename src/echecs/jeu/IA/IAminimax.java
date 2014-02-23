package echecs.jeu.IA;

import java.util.ArrayList;

import echecs.Echecs;
import echecs.graphisme.jeu.Barre;
import echecs.jeu.Jeu;
import echecs.jeu.Joueur;
import echecs.jeu.piece.Piece;
import echecs.jeu.piece.Roi;
import echecs.sauvegarde.CoupPGN;
import echecs.utils.Coordonnee;

/**
 * Une IA qui joue selon l'algorithme du MiniMax
 * Explication complete <a href="https://fr.wikipedia.org/wiki/Minimax">ICI</a>
 */
public class IAminimax extends Joueur implements IA{
	
	/**
	 * Profondeur maximale de descente dans l'arbre recursif
	 */
	private static int MAX_PROFONDEUR = 4;
	
	/**
	 * Profondeur maximale de la descente dans l'arbre recursif choisis par l'utilisateur
	 */
	public static int MAX_PROFONDEUR_TEMP = 4;
	
	/**
	 * Compteur du nombre d'evaluation
	 */
	public static long compteur = 0;
	
	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Coordonnee que l'ia joue
	 */
	private Coordonnee coordonneeAJouer;
	
	/**
	 * Reference du Thread dans lequel est l'ia
	 */
	private IAThread iaThread;
	
	/**
	 * Reference des variables d'evaluation
	 */
	private ValeursEvaluation valeurs;
	
	/**
	 * Constructeur
	 * @param couleur couleur du joueur
	 * @param jeu reference du jeu
	 * @param iaThread reference du thread dans lequel est l'ia
	 * @param valeurs reference des varibles d'evaluation
	 */
	public IAminimax(String couleur, Jeu jeu, IAThread iaThread, ValeursEvaluation valeurs){
		super(couleur);
		this.estHumain = false;
		this.jeu = jeu;
		this.iaThread = iaThread;
		this.valeurs = valeurs;
	}
	
	@Override
	public void jouer(){
		NoeudMiniMax aJouer = minimax();
		jeu.setPieceSelectionee(aJouer.depart.x, aJouer.depart.y);
		this.coordonneeAJouer = aJouer.arrivee;
	}

	/**
	 * Algorithme du MiniMax pour le premier appel
	 * @return le meilleur coup a jouer
	 */
	private NoeudMiniMax minimax(){
		//Recupere tous les coups possibles jouable par le joueur courant (l'ia)
		ArrayList<NoeudMiniMax> noeuds = tousCoupsPossibles();
		
		//Appel recursif
		for(NoeudMiniMax noeud : noeuds){
			long mem = compteur;
			if(Echecs.DEBUG) Echecs.addLog("Evaluation en "+noeud.depart.toString()+">"+noeud.arrivee.toString(), Echecs.TypeLog.INFO);
			minimax(noeud, 1);
			jeu.getPlateau().annulerDernierCoup(true);
			if(Echecs.DEBUG) Echecs.addLog("  => "+noeud.evaluation+" compteur :"+(compteur-mem), Echecs.TypeLog.INFO);
		}
		
		//Renvoi l'evaluation max
		NoeudMiniMax noeud = max(noeuds);
		if(Echecs.DEBUG) Echecs.addLog("IA JOUE : "+noeud.depart.toString()+">"+noeud.arrivee.toString()+" : "+noeud.evaluation.toString(), Echecs.TypeLog.INFO);
		if(Echecs.DEBUG) Echecs.addLog("Historique du meilleur coup : "+noeud.evaluation.historique, Echecs.TypeLog.INFO);
		if(Echecs.DEBUG) Echecs.addLog("Compteur total : "+compteur, Echecs.TypeLog.INFO);
		compteur = 0;
		
		MAX_PROFONDEUR = MAX_PROFONDEUR_TEMP;
		return noeud;
	}
	
	/**
	 * Algorithme du MiniMax pour les appels recurcifs
	 * @param noeudActuel noeud courant dans l'arbre
	 * @param profondeur profondeur actuelle dans l'arbre
	 */
	private void minimax(NoeudMiniMax noeudActuel, int profondeur){
		//Pour le mode debug, met en pause l'affichage
		try{
			int i = Barre.VITESSE_IA;
			if(i != 0){
				iaThread.sleep(i);
			}
		}catch(Exception e){
			Echecs.addLog("Mise en sleep du Thread de l'ia a echoue", Echecs.TypeLog.ERREUR);
			e.printStackTrace();
		}
		
		if(profondeur >= MAX_PROFONDEUR){
			jouerCoup(noeudActuel);
			//if(Echecs.DEBUG) jeu.getPlateau().affiche();
			//if(Echecs.DEBUG) System.out.println("HISTO :"+jeu.getHistorique().toStringSavePGN());
			noeudActuel.evaluation.profondeur = profondeur;
			noeudActuel.evaluation.evaluerPlateau();
		}else{
			//Joue le noeud et test si le coup jouer creer un echec
			if(jouerCoup(noeudActuel)){
				noeudActuel.evaluation.evaluerPlateau();
				noeudActuel.evaluation.profondeur = profondeur;
				return;
			}
			
			//Une fois jouer, recupere tous les coups possibles
			ArrayList<NoeudMiniMax> noeuds = tousCoupsPossibles();
			
			//Si c'est au tour de l'ia de jouer
			if(jeu.getJoueurCourant().equals(couleur)){
				NoeudMiniMax noeudMax = null;
				NoeudMiniMax noeudCourant = null;
				for(int i = 0; i < noeuds.size(); i++){
					noeudCourant = noeuds.get(i);
					minimax(noeudCourant, profondeur+1);
					jeu.getPlateau().annulerDernierCoup(true);
					if(i == 0){
						noeudMax = noeudCourant;
					}else if(noeudMax.evaluation != noeudCourant.evaluation.compare(noeudMax.evaluation)){
						noeudMax = noeudCourant;
					}
				}
				noeudActuel.evaluation.add(noeudMax.evaluation);
			//Si c'est au tour de l'adversaire de l'ia de jouer
			}else{
				NoeudMiniMax noeudMin = null;
				NoeudMiniMax noeudCourant = null;
				for(int i = 0; i < noeuds.size(); i++){
					noeudCourant = noeuds.get(i);
					minimax(noeudCourant, profondeur+1);
					jeu.getPlateau().annulerDernierCoup(true);
					if(i == 0){
						noeudMin = noeudCourant;
					}else if(noeudMin.evaluation != noeudCourant.evaluation.compare(noeudMin.evaluation)){
						noeudMin = noeudCourant;
					}
				}
				noeudActuel.evaluation.add(noeudMin.evaluation);
			}
		}
	}
	
	/**
	 * Recupere tous les coups possibles de toutes les pieces du joueur courant
	 * @return la liste de tous les coups possibles pour chaques pieces
	 */
	private ArrayList<NoeudMiniMax> tousCoupsPossibles(){
		ArrayList<NoeudMiniMax> noeuds = new ArrayList<NoeudMiniMax>();
		ArrayList<Piece> piecesPossibles = (jeu.getJoueurCourant().getCouleur().equals("BLANC"))? jeu.getPlateau().getPiecesBlanches() : jeu.getPlateau().getPiecesNoires();
		for(Piece piece : piecesPossibles){
			ArrayList<Coordonnee> casesPossibles = piece.casesPossibles();
			for(Coordonnee coord : casesPossibles){
				noeuds.add(new NoeudMiniMax(piece.getX(), piece.getY(), coord.x, coord.y, jeu, valeurs));
			}
		}
		return noeuds;
	}
	
	/**
	 * Le noeud qui a l'evaluation maximale
	 * @param noeuds une list de noeud a comparer
	 * @return le noeud max
	 */
	private NoeudMiniMax max(ArrayList<NoeudMiniMax> noeuds){
		if(noeuds == null || noeuds.size() == 0){
			return null;
		}
		
		NoeudMiniMax noeudMax = noeuds.get(0);
		for(int i = 1; i < noeuds.size(); i++){
			if(noeudMax.evaluation != noeuds.get(i).evaluation.compare(noeudMax.evaluation)){
				noeudMax = noeuds.get(i);
			}
		}
		return noeudMax;
	}
	
	/**
	 * Joue le coup selon le noeud
	 * @param noeud le noeud du coup a jouer
	 * @return true si un event est survenu, sinon false
	 */
	private boolean jouerCoup(NoeudMiniMax noeud){
		Piece pieceSelect = jeu.getPlateau().getCase(noeud.depart.x, noeud.depart.y);
		Roi[] rois = new Roi[2];
		rois[0] = jeu.getPlateau().getRoiB();
		rois[1] = jeu.getPlateau().getRoiN();
		
		int valeurPrerequis = jeu.recherchePrerequis(pieceSelect, noeud.arrivee.x, noeud.arrivee.y);
		
		//Deplace la piece
		if(pieceSelect.deplacer(noeud.arrivee.x, noeud.arrivee.y)){
			CoupPGN coup = jeu.getHistorique().getDernierCoup();
			coup.setPrerequis(valeurPrerequis);
			
			//Test les echecs
			for(int i = 0; i < rois.length; i++){
				if(rois[i].estEchec()){
					//Si le roi est en echec
					if(jeu.getJoueurCourant().getCouleur().equals(rois[i].getCouleur())){
						noeud.evaluation.event = Evaluation.Event.ERREUR;
						jeu.switchJoueur();
						return true;
					}else if(this.couleur.equals(rois[i].getCouleur())){
						noeud.evaluation.event = Evaluation.Event.ECHEC_SUR_SOI;
						jeu.switchJoueur();
						noeud.evaluation.evaluerAttaqueDefense();
						return true;
					//Si le roi est en echec et mat
					}else if(rois[i].estEchecEtMat()){
						coup.isEchecMat = true;
						noeud.evaluation.event = Evaluation.Event.ECHEC_MAT;
						jeu.switchJoueur();
						noeud.evaluation.evaluerAttaqueDefense();
						return true;
					//Sinon c'est le joueur adverse qui a cause l'echec
					}else{
						coup.isEchec = true;
						noeud.evaluation.event = Evaluation.Event.ECHEC;
						jeu.switchJoueur();
						noeud.evaluation.evaluerAttaqueDefense();
						return true;
					}
				}
			}
			jeu.switchJoueur();
			noeud.evaluation.evaluerAttaqueDefense();
		}else{
			Echecs.addLog("L'ia essai de jouer un coup impossible ! "+noeud.depart.toString()+">"+noeud.arrivee.toString(), Echecs.TypeLog.WARNING);
			jeu.getPlateau().affiche();
			System.out.println("Historique a ce moment : "+jeu.getHistorique().toStringSavePGN());
			System.out.println("Coup possible : "+pieceSelect.deplacer(noeud.arrivee.x, noeud.arrivee.y)+" | mouvement possible : "+pieceSelect.deplacer(noeud.arrivee.x, noeud.arrivee.y));
			noeud.evaluation.event = Evaluation.Event.ERREUR;
			jeu.switchJoueur();
			noeud.evaluation.evaluerAttaqueDefense();
			return true;
		}
		return false;
	}

	@Override
	public Coordonnee getCoordonneeAJouer() {
		return this.coordonneeAJouer;
	}
}

/**
 * Stocke une etaque des appels recursifs de l'algorithme MiniMax
 */
class NoeudMiniMax{
	
	/**
	 * La coordonnee de la piece a bouger
	 */
	Coordonnee depart;
	
	/**
	 * La coordonnee de deplacement de la piece
	 */
	Coordonnee arrivee;
	
	/**
	 * Reference de l'evaluation du noeud
	 */
	Evaluation evaluation;
	
	/**
	 * Constructeur
	 * @param xD x de la piece a bouger
	 * @param yD y de la piece a bouger
	 * @param xA x du deplacement de la piece
	 * @param yA y du deplacement de la piece
	 * @param jeu reference du jeu
	 * @param valeurs valeurs des varibales d'evaluation
	 */
	public NoeudMiniMax(int xD, int yD, int xA, int yA, Jeu jeu, ValeursEvaluation valeurs){
		depart = new Coordonnee(xD, yD);
		arrivee = new Coordonnee(xA, yA);
		evaluation = new Evaluation(jeu, valeurs);
		IAminimax.compteur++;
	}
	
	/**
	 * Representation en String du Noeud
	 */
	public String toString(){
		return "Evaluation de ["+conversionIntEnChar(depart.x)+","+(depart.y+1)+"] en ["+conversionIntEnChar(arrivee.x)+","+(arrivee.y+1)+"] = "+evaluation.toString();
	}
	
	/**
	 * Convertion d'un int en char pour les coordonnees du jeu d'echec
	 * @param a l'int a convertir
	 * @return le char correspondant
	 */
	private char conversionIntEnChar(int a){
		int valeur = 0;
		for(char i = 'a'; i <= 'h'; i++){
			if(a == valeur) return i;
			valeur++;
		}
		return 'Z';
	}
}