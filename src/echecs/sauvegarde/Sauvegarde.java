package echecs.sauvegarde;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import echecs.Echecs;
import echecs.graphisme.Menu;
import echecs.jeu.Jeu;
import echecs.jeu.Replay;

/**
 * Contient diverses fonctions de lecture / ecriture de sauvegarde
 * <ul>
 * 	<li>Format .sv : sauvegarde des partie non terminee</li>
 * 	<li>Format .pgn : format standardise, plus de detail ici : <a href="http://fr.wikipedia.org/wiki/Portable_Game_Notation">Notation PGN</a></li>
 * </ul>
 */
public class Sauvegarde {
	
	/**
	 * Lit un fichier contenant l'historique d'une partie non terminee
	 * @param jeu la reference du jeu
	 * @param nomFichier le nom du fichier
	 * @return vrai si la lecture a reussi
	 */
	public static boolean lireSauvegardeJeuNonFini(Jeu jeu, String nomFichier){
		try{
			File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".sv");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String ligne = br.readLine();
			if(ligne == null || ligne.length() == 0){
				JOptionPane.showMessageDialog(null, "Aucun historique de partie trouve", "Aucun historique de partie trouve", JOptionPane.WARNING_MESSAGE);
				return false;
			}
			ligne = ligne.replaceAll("\\.\\p{Space}", ".");
			String[] tours = ligne.split("\\d+\\.");
			
			ArrayList<CoupPGN> coups = new ArrayList<CoupPGN>();
			
			if(tours.length > 1){
				for(int i = 0; i < tours.length; i++){
					if(tours[i].length() > 0){
						String[] coupsTour = tours[i].split(" +");
						for(int j = 0; j < coupsTour.length; j++){
							if(coupsTour[j].length() > 0){
								if(CoupPGN.notationValide(coupsTour[j])){
								coups.add(new CoupPGN(coupsTour[j]));
								}else{
									JOptionPane.showMessageDialog(null, "La notation ["+coupsTour[j]+"] n'est pas une notation PGN valide\nImpossible de charger la sauvegarde", "Notation invalide", JOptionPane.WARNING_MESSAGE);
									return false;
								}
							}
						}
					}
				}
			}
			
			ligne  = br.readLine();
			
			//sauvegarde de temps pour le blitz
			if(ligne != null){
				if(ligne.length() != 8){
					int reponse = JOptionPane.showConfirmDialog(null, "Le format d'ecriture du temps pour le mode blitz est incorrect\nRaison : Mauvaise longueur\nSouhaitez-vous continuer la partie sans le mode Blitz ?", "Echec de la lecture de la sauvegarde", JOptionPane.YES_NO_OPTION);
					if(reponse != 0){
						jeu.getFenetre().setVisible(false);
						jeu.getFenetre().dispose();
						new Menu();
					}
					return false;
				}
				char[] valeurs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
				for(int i = 0; i < ligne.length(); i++){
					boolean valide = false;
					for(char c : valeurs){
						if(ligne.charAt(i) == c){
							valide = true;
						}
					}
					if(!valide){
						int reponse = JOptionPane.showConfirmDialog(null, "Le format d'ecriture du temps pour le mode blitz est incorrect\nRaison : un des caracteres n'est pas un chiffre\nSouhaitez-vous continuer la partie sans le mode Blitz ?", "Echec de la lecture de la sauvegarde", JOptionPane.YES_NO_OPTION);
						if(reponse != 0){
							jeu.getFenetre().setVisible(false);
							jeu.getFenetre().dispose();
							new Menu();
						}
						return false;
					}
				}
				
				int chronos = Integer.parseInt(ligne);
				int blancMinute = chronos/1000000;
				int blancSeconde = (chronos/10000)%100;
				int noirMinute = (chronos/100)%100;
				int noirSeconde = chronos%100;
				if(blancMinute > 60 || blancSeconde > 60 || noirMinute > 60 || noirSeconde > 60){
					int reponse = JOptionPane.showConfirmDialog(null, "Le format d'ecriture du temps pour le mode blitz est incorrect\nRaison : format depasse 60 min/sec\nSouhaitez-vous continuer la partie sans le mode Blitz ?", "Echec de la lecture de la sauvegarde", JOptionPane.YES_NO_OPTION);
					if(reponse != 0){
						jeu.getFenetre().setVisible(false);
						jeu.getFenetre().dispose();
						new Menu();
					}
					return false;
				}
				int reponse = JOptionPane.showConfirmDialog(null, "Souhaitez-vous reprendre la partie en mode Blitz ?", "Mode Blitz", JOptionPane.YES_NO_OPTION);
				if(reponse == 0){
					jeu.setChronos(blancMinute, blancSeconde, noirMinute, noirSeconde);
				}else if(jeu.isBlitz()){
					jeu.desactiveBlitz();
				}
			//Aucune sauvegarde de temps pour le blitz
			}else{
				if(jeu.isBlitz()){
					jeu.desactiveBlitz();
				}
			}
			jeu.jouerSauvegarde(coups);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Chargement de la sauvegarde avec succes", "Succes", JOptionPane.INFORMATION_MESSAGE);
		
		return true;
	}

	/**
	 * Ecrit une sauvegarde du jeu en cours dans le dossier 'save'
	 * @param jeu l'instance du jeu en cours
	 * @param nomFichier nom du fichier de la sauvegarde
	 * @return succes de creation de la sauvegarde
	 */
	public static boolean creerSauvegardeJeuNonFini(Jeu jeu, String nomFichier){
		File dossier = new File(Echecs.SAVE_PATH);
		dossier.mkdir();
		File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".sv");
		try{
			if(!file.createNewFile()){
				file.delete();
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			
			//Historique
			pw.println(jeu.getHistorique().toStringSavePGN());
			
			//Blitz
			if(jeu.isBlitz()){
				pw.println(jeu.getChronoBlanc().toString()+""+jeu.getChronoNoir().toString());
			}

			pw.flush();
			pw.close();
		}catch(Exception e){
			System.out.println("Exception lors de la creation de la sauvegarde");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Lit un fichier PGN et renvoi les informations de la parties dans l'objet Partie
	 * @param nomFichier nom du fichier 
	 * @param numero numero dans la partie dans le fichier
	 * @return les informations de la partie
	 */
	public static Partie lireSauvegardePGN(String nomFichier, int numero){
		Partie partie = null;
		try{
			if(numero == -1) return null;
			File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".pgn");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String ligne = br.readLine();
			partie = new Partie();
			
			while(ligne != null && numero > 0){
				ligne = br.readLine();
				while(!ligne.startsWith("[Event ")){
					ligne = br.readLine();
				}
				numero--;
			}
			
			ligne = br.readLine();
			while(ligne != null && !ligne.startsWith("[Event ")){
				if(ligne.startsWith("[Site ")){
					partie.site = ligne.substring(7, ligne.length()-2);
				}else if(ligne.startsWith("[Date ")){
					partie.date = ligne.substring(7, ligne.length()-2);
				}else if(ligne.startsWith("[Round ")){
					partie.round = ligne.substring(8, ligne.length()-2);
				}else if(ligne.startsWith("[White ")){
					partie.white = ligne.substring(8, ligne.length()-2);
				}else if(ligne.startsWith("[Black ")){
					partie.black = ligne.substring(8, ligne.length()-2);
				}else if(ligne.startsWith("[Result ")){
					partie.result = ligne.substring(9, ligne.length()-2);
				}else if(ligne.startsWith("1.")){
					String toursBrut = "";
					String resultat = "";
					while(ligne != null && ligne.length() != 0 && !ligne.startsWith("[Event ")){
						toursBrut += ligne+" ";
						ligne = br.readLine();
					}
					toursBrut = toursBrut.replaceAll("\\{Space}+", " ");
					toursBrut = toursBrut.replaceAll("\\.\\p{Space}", ".");
					String[] tours = toursBrut.split("\\d+\\.");
					
					Historique historique = new Historique();
					
					if(tours.length > 1){
						for(int i = 0; i < tours.length; i++){
							if(tours[i].length() > 0){
								String[] coupsTour = tours[i].split(" +");
								if(i == tours.length-1){
									for(int j = 0; j < coupsTour.length-1; j++){
										if(coupsTour[j].length() > 0){
											if(CoupPGN.notationValide(coupsTour[j])){
												historique.addCoupPGN(new CoupPGN(coupsTour[j]));
											}else{
												JOptionPane.showMessageDialog(null, "La notation ["+coupsTour[j]+"] n'est pas une notation PGN valide\nImpossible de charger la sauvegarde", "Notation invalide", JOptionPane.WARNING_MESSAGE);
												return null;
											}
										}
									}
									resultat = coupsTour[coupsTour.length-1];
								}else{
									for(int j = 0; j < coupsTour.length; j++){
										if(coupsTour[j].length() > 0){
											if(CoupPGN.notationValide(coupsTour[j])){
												historique.addCoupPGN(new CoupPGN(coupsTour[j]));
											}else{
												JOptionPane.showMessageDialog(null, "La notation ["+coupsTour[j]+"] n'est pas une notation PGN valide\nImpossible de charger la sauvegarde", "Notation invalide", JOptionPane.WARNING_MESSAGE);
												return null;
											}
										}
									}
								}
							}
						}
					}
					
					partie.coups = historique;
				}
				ligne = br.readLine();
			}
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return partie;
	}
	/**
	 * Ecrit une sauvegarde d'une partie finie dans le dossier 'save'
	 * @param p Partie du jeu termine
	 * @param nomFichier nom du fichier
	 * @return succes de creation de la sauvegarde
	 */
	public static boolean creerSauvegardePGN(Partie p, String nomFichier){
		try{
			File dossier = new File(Echecs.SAVE_PATH);
			dossier.mkdir();
			File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".pgn");
			
			if(!file.createNewFile()){
				System.out.println("Fichier deja existant");
				return false;
			}
			
			PrintWriter pw = new PrintWriter(new FileWriter(file));
			
			pw.println(p.formatPGN());
			
			pw.flush();
			pw.close();
			return true;
			
		}catch(Exception e){
			System.out.println("Erreur lors de la creation de la sauvegarde "+Echecs.SAVE_PATH+File.pathSeparator+nomFichier+".pgn");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Recupere le nombre de parties contenu dans le fichier PGN
	 * @param nomFichier le nom du fichier
	 * @return le nombre de partie
	 */
	public static int nombrePartiesFichierPGN(String nomFichier){
		try{
			File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".pgn");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String ligne = br.readLine();
			int compteur = 0;
			
			while(ligne != null){
				if(ligne.startsWith("[Event ")){
					compteur++;
				}
				ligne = br.readLine();
			}
			br.close();
			return compteur;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
}
