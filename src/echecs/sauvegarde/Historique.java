package echecs.sauvegarde;

import java.util.ArrayList;

public class Historique{

	/**
	 * La liste qui stocke les coups
	 */
	private ArrayList<CoupPGN> list;
	
	/**
	 * Constructeur d'un historique vide
	 */
	public Historique(){
		list = new ArrayList<CoupPGN>();
	}
	
	/**
	 * Converti l'historique en notation algebrique
	 * @return un string le la notation algebrique
	 */
	public String toStringSavePGN(){
		String contenu = "";
		int compteur = 80;
		for(int i = 0; i < list.size(); i+=2){			
			String tour = (i/2+1)+".";
			if(contenu.length()+tour.length() >= compteur){
				contenu += "\n";
				compteur += 80;
			}
			contenu += tour;
			
			String coupBlanc = list.get(i).formatPGN()+" ";
			if(contenu.length()+coupBlanc.length() >= compteur){
				contenu += "\n";
				compteur += 80;
			}
			contenu += coupBlanc;
			
			if((i+1) < list.size()){
				String coupNoir = list.get(i+1).formatPGN()+" ";
				if(contenu.length()+coupNoir.length() >= compteur){
					contenu += "\n";
					compteur += 80;
				}
				contenu += coupNoir;
			}
		}
		return contenu;
	}
	
	/**
	 * Transforme l'historique en un arraylist contenant la representation PGN de chaque tour
	 * @return ArrayList<String>
	 */
	public ArrayList<String> toStringParTour(){
		ArrayList<String> tours = new ArrayList<String>();
		for(int i = 0; i < list.size(); i+=2){
			String s = (i/2+1)+"."+list.get(i).formatPGN();
			if((i+1) < list.size()){
				s += " "+list.get(i+1).formatPGN();
			}
			tours.add(s);
		}
		return tours;
	}
	
	/**
	 * Ajoute un coup a la list
	 * @param c Coup a ajouter
	 */
	public void addCoupPGN(CoupPGN c){
		list.add(c);
	}
	
	/**
	 * Getter de la list des coups
	 * @return la reference la list
	 */
	public ArrayList<CoupPGN> getList(){
		return list;
	}
	
	/**
	 * Retourne le dernier coup joue
	 * @return le dernier coup
	 */
	public CoupPGN getDernierCoup(){
		return list.get(list.size()-1);
	}
	
	/**
	 * Supprime le dernier coup joueur
	 */
	public void supprimeDernierCoup(){
		list.remove(list.size()-1);
	}
	
	/**
	 * Retourne la list sans le dernier coup
	 * @return ArrayList<String>
	 */
	public ArrayList<CoupPGN> listSansDernierCoup(){
		this.list.remove(this.list.size()-1);
		return list;
	}
	
	/**
	 * Test si la list est vide
	 * @return true si vide
	 */
	public boolean isEmpty(){
		return list.isEmpty();
	}
}
