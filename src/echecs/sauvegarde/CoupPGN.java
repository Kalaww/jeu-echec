package echecs.sauvegarde;

import echecs.jeu.piece.Piece;
import echecs.utils.Coordonnee;

/**
 * Contient un coup au format PGN
 * Detail sur la notation PGN ici : <a href="http://fr.wikipedia.org/wiki/Portable_Game_Notation">Notation PGN</a>
 */
public class CoupPGN {
	
	/**
	 * Notation anglaise de la tour
	 */
	public static char TOUR = 'R';
	
	/**
	 * Notation anglaise du cavalier
	 */
	public static char CAVALIER = 'N';
	
	/**
	 * Notation anglaise du fou
	 */
	public static char FOU = 'B';
	
	/**
	 * Notation anglaise de la reine
	 */
	public static char REINE = 'Q';
	
	/**
	 * Notation anglaise du roi
	 */
	public static char ROI = 'K';

	/**
	 * Coup est un petit roque
	 */
	public boolean isPetitRoque;
	
	/**
	 * Coup est un grand roque
	 */
	public boolean isGrandRoque;
	
	/**
	 * Notation de la piece
	 */
	public char nomPiece;
	
	/**
	 * Coordonnee de depart du coup
	 */
	public Coordonnee depart;
	
	/**
	 * Coordonnee d'arrivee du coup
	 */
	public Coordonnee arrivee;
	
	/**
	 * Coordonnee memoire d'arrivee du coup
	 */
	public Coordonnee departMemoire;
	
	/**
	 * Coup est une prise
	 */
	public boolean isPrise;
	
	/**
	 * Coup est une promotion du pion
	 */
	public boolean isTransformation;
	
	/**
	 * Notation de la piece pour la promotion du pion
	 */
	public char nomPieceTransformation;
	
	/**
	 * Coup est un echec
	 */
	public boolean isEchec;
	
	/**
	 * Coup est un echec et mat
	 */
	public boolean isEchecMat;
	
	/**
	 * Representation du coup au format PGN
	 */
	public String representationString;
	
	/**
	 * Reference de la piece joue
	 */
	public Piece referencePiece;
	
	/**
	 * Constructeur
	 */
	public CoupPGN(){
		this.isPetitRoque = false;
		this.isGrandRoque = false;
		this.isPrise = false;
		this.isTransformation = false;
		this.isEchec = false;
		this.isEchecMat = false;
		
		this.nomPiece = 'Z';
		this.nomPieceTransformation = 'Z';
		
		this.depart = new Coordonnee(-1);
		this.departMemoire = new Coordonnee(-1);
		this.arrivee = new Coordonnee(-1);
		
		this.representationString = "";
		this.referencePiece = null;
	}
	
	/**
	 * Constructeur a partir d'une notation PGN
	 * @param coupString notation PGN du coup
	 */
	public CoupPGN(String coupString){
		this();
		this.representationString = coupString;
		
		try{
			//Recherche de la piece
			if(coupString.charAt(0) == 'O'){
				if(coupString.startsWith("O-O-O")){
					this.isGrandRoque = true;
					coupString = coupString.substring(5);
				}else if(coupString.startsWith("O-O")){
					this.isPetitRoque = true;
					coupString = coupString.substring(3);
				}
				return;
			}else if(coupString.charAt(0) == 'K' || coupString.charAt(0) == 'Q' || coupString.charAt(0) == 'B' || coupString.charAt(0) == 'N' || coupString.charAt(0) == 'R'){
				this.nomPiece = coupString.charAt(0);
				coupString = coupString.substring(1);
			}else{
				this.nomPiece = ' ';
			}
			
			//Recherche prerequis
			if(coupString.length() >= 3 && isValidChar(coupString.charAt(0)) && isValidInt(coupString.charAt(1)) && (coupString.charAt(2) == 'x' || isValidChar(coupString.charAt(2)))){
				this.depart.x = convertionCharEnInt(coupString.charAt(0));
				this.depart.y = Integer.parseInt(coupString.charAt(1)+"") -1;
				coupString = coupString.substring(2);
			}else if(coupString.length() >= 2 && isValidChar(coupString.charAt(0)) && (coupString.charAt(1) == 'x' || isValidChar(coupString.charAt(1)))){
				this.depart.x = convertionCharEnInt(coupString.charAt(0));
				coupString = coupString.substring(1);
			}else if(coupString.length() >= 2 && isValidInt(coupString.charAt(0)) && (coupString.charAt(1) == 'x' || isValidChar(coupString.charAt(1)))){
				this.depart.y = Integer.parseInt(coupString.charAt(0)+"") -1;
				coupString = coupString.substring(1);
			}
			
			//Cas d'une attaque
			if(coupString.length() >= 1 && coupString.charAt(0) == 'x'){
				this.isPrise = true;
				coupString = coupString.substring(1);
			}
			
			//Coordonnee de deplacement
			if(isValidChar(coupString.charAt(0))){
				this.arrivee.x = convertionCharEnInt(coupString.charAt(0));
			}else{
				System.out.println("Il y a une erreur dans une coordonnee de case");
			}
			
			if(isValidInt(coupString.charAt(1))){
				this.arrivee.y = Integer.parseInt(coupString.charAt(1)+"")-1;
			}else{
				System.out.println("Il y a une erreur dans une coordonnee de case");
			}
			coupString = coupString.substring(2);
			
			//Transformation d'un pion
			if(coupString.length() >= 1 && coupString.charAt(0) == '='){
				this.isTransformation = true;
				if(coupString.charAt(1) == ROI || coupString.charAt(1) == REINE || coupString.charAt(1) == FOU || coupString.charAt(1) == CAVALIER || coupString.charAt(1) == TOUR){
					this.nomPieceTransformation = coupString.charAt(1);
				}else{
					System.out.println("Il y a une erreur dans la transformation d'un pion");
				}
				coupString = coupString.substring(2);
			}
			
			//Mise en echec
			if(coupString.length() >= 1 && coupString.charAt(0) == '+'){
				this.isEchec = true;
				coupString = coupString.substring(1);
			}else if(coupString.length() >= 1 && coupString.charAt(0) == '#'){
				this.isEchecMat = true;
				coupString = coupString.substring(1);
			}
		}catch(Exception e){
			System.out.println("Il y a une erreur dans la syntaxe du coup "+this.representationString);
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifie si la notation du coup est valide
	 * @param coup coup a verifier
	 * @return vrai si notation valide
	 */
	public static boolean notationValide(String coup){
		//Taille minimale
		if(coup.length() < 2){
			return false;
		}
		
		//Recherche de la piece
		if(coup.charAt(0) == 'O'){
			if(coup.startsWith("O-O-O")){
				return true;
			}else if(coup.startsWith("O-O")){
				return true;
			}else{
				return false;
			}
		}else if(coup.charAt(0) == 'K' || coup.charAt(0) == 'Q' || coup.charAt(0) == 'B' || coup.charAt(0) == 'N' || coup.charAt(0) == 'R'){
			coup = coup.substring(1);
		}
		
		//Recherche prerequis
		if(coup.length() >= 3 && isValidChar(coup.charAt(0)) && isValidInt(coup.charAt(1)) && (coup.charAt(2) == 'x' || isValidChar(coup.charAt(2)))){
			coup = coup.substring(2);
		}else if(coup.length() >= 2 && isValidChar(coup.charAt(0)) && (coup.charAt(1) == 'x' || isValidChar(coup.charAt(1)))){
			coup = coup.substring(1);
		}else if(coup.length() >= 2 && isValidInt(coup.charAt(0)) && (coup.charAt(1) == 'x' || isValidChar(coup.charAt(1)))){
			coup = coup.substring(1);
		}else if(coup.length() == 0){
			return false;
		}
		
		//Cas d'une attaque
		if(coup.length() >= 1 && coup.charAt(0) == 'x'){
			coup = coup.substring(1);
		}
		
		//Coordonnee de deplacement
		if(coup.length() == 0 || !isValidChar(coup.charAt(0))){
			return false;
		}
		coup = coup.substring(1);
		
		if(coup.length() == 0 || !isValidInt(coup.charAt(0))){
			return false;
		}
		coup = coup.substring(1);
		
		//Transformation d'un pion
		if(coup.length() >= 2 && coup.charAt(0) == '='){
			if(coup.charAt(1) == REINE || coup.charAt(1) == FOU || coup.charAt(1) == CAVALIER || coup.charAt(1) == TOUR){
				coup = coup.substring(2);
				if(coup.length() == 0) return true;
			}else{
				return false;
			}
		}
		
		//Mise en echec
		if(coup.length() == 1 && coup.charAt(0) == '+'){
			return true;
		}else if(coup.length() == 1 && coup.charAt(0) == '#'){
			return true;
		}
		
		if(coup.length() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Converti le coup au format PGN
	 * @return String
	 */
	public String formatPGN(){
		String format = "";
		
		//Recherche de la piece
		if(this.isGrandRoque){
			return "O-O-O";
		}else if(this.isPetitRoque){
			return "O-O";
		}else if(this.nomPiece != ' '){
			format += this.nomPiece;
		}
		
		//Recherche de prerequis
		if(depart.x != -1 && depart.y != -1){
			format += convertionIntEnChar(depart.x) +""+ depart.y;
		}else if(depart.x != -1){
			format += convertionIntEnChar(depart.x);
		}else if(depart.y != -1){
			format += depart.y+1;
		}
		
		//Cas d'une attaque
		if(this.isPrise){
			format += 'x';
		}
		
		//Coordonnee de deplacement
		if(this.arrivee.x != -1){
			format += convertionIntEnChar(arrivee.x);
		}
		
		if(this.arrivee.y != -1){
			format += arrivee.y+1;
		}
		
		//Transformation d'un pion
		if(this.isTransformation){
			format += "="+this.nomPieceTransformation;
		}
		
		//Mise en echec
		if(this.isEchec){
			format += "+";
		}else if(this.isEchecMat){
			format += "#";
		}
		
		this.representationString = format;
		return format;
	}
	
	/**
	 * Ajoute les prerequis du coup
	 * @param valeur
	 */
	public void setPrerequis(int valeur){
		if(this.nomPiece != ROI){
			if(valeur / 100 > 0){
				depart.x = departMemoire.x;
			}
			valeur %= 100;
			if(valeur / 10 > 0){
				depart.y = departMemoire.y+1;
			}
		}
	}
	
	/**
	 * Test si la notation d'un char est une coordonnee valide
	 * @param c
	 * @return boolean
	 */
	private static boolean isValidChar(char c){
		for(char i = 'a'; i <= 'h'; i++){
			if(c == i) return true;
		}
		return false;
	}
	
	/**
	 * Test si la notation d'un int est une coordonnee valide
	 * @param c
	 * @return boolean
	 */
	private static boolean isValidInt(char c){
		for(char i = '1'; i <= '8'; i++){
			if(c == i) return true;
		}
		return false;
	}
	
	/**
	 * Convertion d'une notation d'une coordonnee en char en notation en int
	 * @param c
	 * @return int
	 */
	private static int convertionCharEnInt(char c){
		int valeur =0;
		for(char i = 'a'; i <= 'h'; i++){
			if(c == i) return valeur;
			valeur++;
		}
		return -1;
	}
	
	/**
	 * Convertion d'une notation d'une coordonnee en int en notation en char
	 * @param a
	 * @return char
	 */
	private static char convertionIntEnChar(int a){
		int valeur = 0;
		for(char i = 'a'; i <= 'h'; i++){
			if(a == valeur) return i;
			valeur++;
		}
		return 'Z';
	}
	
	/**
	 * Selectionne la bonne notation de la piece selon la famille de la piece
	 * @param nom
	 */
	public void setNomPiece(String nom){
		if(nom.equals("TOUR")){
			this.nomPiece = TOUR;
		}else if(nom.equals("CAVALIER")){
			this.nomPiece = CAVALIER;
		}else if(nom.equals("FOU")){
			this.nomPiece = FOU;
		}else if(nom.equals("REINE")){
			this.nomPiece = REINE;
		}else if(nom.equals("ROI")){
			this.nomPiece = ROI;
		}else{
			this.nomPiece = ' ';
		}
	}
	
	/**
	 * Return la representation en string du coup (notation PGN)
	 * @return String
	 */
	public String toString(){
		return this.formatPGN();
	}
	
	/**
	 * Reprensention en String pour du debug
	 * @return String
	 */
	public String variablesToString(){
		return "Coup "+representationString+"  "+nomPiece+" "+depart.toString()+" -> "+arrivee.toString()+" memoire:"+departMemoire.toString();
	}
	
}
