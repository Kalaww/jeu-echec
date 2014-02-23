package echecs.jeu;

import echecs.graphisme.jeu.Fenetre;

/**
 * Thread pour un chronometre
 */
public class Chrono extends Thread{
	
	/**
	 * Reference de la fenetre
	 */
	private Fenetre fen;
	
	/**
	 * Temps en seconde
	 */
	private int s;
	
	/**
	 * Temps en minute
	 */
	private int m;
	
	/**
	 * Couleur du chrono, truc si blanc
	 */
	private boolean estBlanc;
	
	/**
	 * Mise en pause du thread
	 */
	private boolean pause = false;
	
	/**
	 * Constructeur
	 */
	public Chrono(){
		
	}
	
	/**
	 * Constructeur
	 * @param m minute
	 * @param s seconde
	 * @param fen reference de la fenetre
	 * @param estBlanc vrai si c'est un chrono du joueur blanc
	 */
	public Chrono(int m, int s, Fenetre fen, boolean estBlanc){
		this.s = s;
		this.m = m;
		this.fen= fen;
		this.estBlanc = estBlanc;
	}
	
	@Override
	public void run(){
		while(s>=0 && m >=0){
			if(!pause){
				
			try {
				this.sleep(1000);
				if(this.estBlanc){
					fen.updateChronoB(m, s);
				}
				else{
					fen.updateChronoN(m, s);	
				}
				if(s <= 0){
					m--;
					s = 60;
				}
				s--;
				

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
			if(m == 0 & s ==0){
				fen.getJeu().reset();
			}
		}
	}
	
	/**
	 * Active / desactive la mise en pause du thread
	 * @param pause
	 */
	public void pause(boolean pause){
		this.pause = pause;
	}
	
	@Override
	public String toString(){
		String contenu = "";
		if(m < 10){
			contenu += "0"+m;
		}else{
			contenu += m;
		}
		
		if(s < 10){
			contenu += "0"+s;
		}else{
			contenu += s;
		}
		return contenu;
	}
	
	/**
	 * Renvoie le nombre de secondes
	 * @return
	 */
	public int getSeconde(){
		return s;
	}
	
	/**
	 * Renvoie le nombre de minutes
	 * @return
	 */
	public int getMinute(){
		return m;
	}
	
	/**
	 * Configure le nombre de secondes
	 * @param sec le nombre de secondes
	 */
	public void setSeconde(int sec){
		this.s = sec;
	}
	
	/**
	 * Configure le nombre de minutes
	 * @param min
	 */
	public void setMinute(int min){
		this.m = min;
	}
}
