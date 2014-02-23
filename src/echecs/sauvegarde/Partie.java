package echecs.sauvegarde;

/**
 * Object qui stocke toutes les informations necessaires d'une notation PGN
 */
public class Partie {
	
	/**
	 * Notation de la victoire des blancs
	 */
	public static final String WHITE_WIN = "1-0";
	
	/**
	 * Notation de la victoire des noirs
	 */
	public static final String BLACK_WIN = "0-1";
	
	/**
	 * Notation de l'egalite
	 */
	public static final String EGALITE = "1/2-1/2";
	
	/**
	 * Event
	 */
	public String event;
	
	/**
	 * Site geographique
	 */
	public String site;
	
	/**
	 * Date format AAAA:MM:JJ
	 */
	public String date;
	
	/**
	 * Round
	 */
	public String round;
	
	/**
	 * Nom du joueur blanc
	 */
	public String white;
	
	/**
	 * Nom du joueur noir
	 */
	public String black;
	
	/**
	 * Resultat
	 */
	public String result;
	
	/**
	 * Historique de la partie
	 */
	public Historique coups;
	
	/**
	 * Constructeur
	 */
	public Partie(){
		event = "?";
		site = "?";
		date = "????.??.??";
		round = "?";
		white = "?";
		black = "?";
		result = "?";
		coups = new Historique();
	}
	
	/**
	 * Return le format PGN compelt de la partie
	 * @return String
	 */
	public String formatPGN(){
		String contenu = "";
		contenu += "[Event \""+event+"\"]\n";
		contenu += "[Site \""+site+"\"]\n";
		contenu += "[Date \""+date+"\"]\n";
		contenu += "[Round \""+round+"\"]\n";
		contenu += "[White \""+white+"\"]\n";
		contenu += "[Black \""+black+"\"]\n";
		contenu += "[Result \""+result+"\"]\n";
		contenu += "\n";
		contenu += coups.toStringSavePGN()+result;
		
		return contenu;
	}
}
