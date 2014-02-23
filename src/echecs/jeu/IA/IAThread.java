package echecs.jeu.IA;

import echecs.Echecs;
import echecs.graphisme.Menu;
import echecs.jeu.Jeu;

/**
 * Thread qui gere les calcules de l'ia
 */
public class IAThread extends Thread{
	
	/**
	 * Reference du joueue IA
	 */
	private IA joueurIA;
	
	/**
	 * Vrai si le thread est en run
	 */
	private boolean vivant;
	
	/**
	 * Reference du jeu
	 */
	private Jeu jeu;
	
	/**
	 * Vrai si le thread est en pause
	 */
	private boolean pause;
	
	/**
	 * Vrai si le thread est en train de chercher un coup a jouer
	 */
	private boolean reflechi;
	
	/**
	 * Couleur de l'ia
	 */
	private String couleur;
	
	/**
	 * Niveau de l'ia
	 */
	private int niveau;
	
	/**
	 * Valeurs des constantes pour une ia de niveau 3, null sinon
	 */
	private ValeursEvaluation valeurs;
	
	/**
	 * Constructeur
	 * @param niveau niveau de l'ia
	 * @param couleur couleur de l'ia
	 * @param jeu reference du jeu
	 * @param valeurs valeurs des constantes pour une ia de niveau 3, null sinon
	 */
	public IAThread(int niveau, String couleur, Jeu jeu, ValeursEvaluation valeurs){
		this.jeu = jeu;
		this.pause = true;
		this.vivant = true;
		this.reflechi = false;
		this.couleur = couleur;
		this.niveau = niveau;
		this.valeurs = valeurs;
		if(niveau == 3){
			joueurIA = new IAcomplet(couleur, jeu, this, valeurs);
		}else if(niveau == 2){
			joueurIA = new IAaleatoire2(couleur, jeu);
		}else{
			joueurIA = new IAaleatoire(couleur, jeu);
		}
	}
	
	@Override
	public void run(){
		if(Echecs.DEBUG) Echecs.addLog("Thread de l'ia commence", Echecs.TypeLog.INFO);
		while(vivant){
			checkPause();
			if(!vivant) break;
			if(Echecs.DEBUG) Echecs.addLog("IA joue ...", Echecs.TypeLog.INFO);
			this.reflechi = true;
			joueurIA.jouer();
			this.reflechi = false;
			jeu.jouerIA(joueurIA.getCoordonneeAJouer());
			pause = true;
		}
		if(Echecs.DEBUG) Echecs.addLog("Thread de l'IA termine", Echecs.TypeLog.INFO);
	}
	
	/**
	 * Active/desactive la pause du thread
	 * @param pauseValeur 
	 */
	public synchronized void pause(boolean pauseValeur){
		this.pause = pauseValeur;
		if(!this.pause){
			this.notify();
		}
	}
	
	/**
	 * Verifie si le thread doit rester en pause
	 */
	private synchronized void checkPause(){
		while(this.pause){
			try{
				this.wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Active/desactive le thread. Une fois passer a false, le thread ne peut pas etre reactive
	 * @param vivant
	 */
	public synchronized void setVivant(boolean vivant){
		this.vivant = vivant;
		this.pause = false;
		this.notify();
	}
	
	/**
	 * Test si le serveur est en pause
	 * @return
	 */
	public boolean isPause(){
		return pause;
	}
	
	/**
	 * Test si le serveur reflechi
	 * @return
	 */
	public boolean isReflechi(){
		return reflechi;
	}
	
	/**
	 * Getter ia
	 * @return
	 */
	public IA getIA(){
		return joueurIA;
	}
	
	/**
	 * Getter couleur
	 * @return
	 */
	public String getCouleur(){
		return couleur;
	}
	
	/**
	 * Getter niveau
	 * @return
	 */
	public int getNiveau(){
		return niveau;
	}
	
	/**
	 * Getter des constantes de l'ia niveau 3, null sinon
	 * @return
	 */
	public ValeursEvaluation getValeurs(){
		return valeurs;
	}
}
