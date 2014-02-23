package echecs;

import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import echecs.graphisme.Menu;

/**
 * Main qui lance le menu du jeu
 */
public class Echecs {
	
	/**
	 * Chemin des images du jeu
	 */
	public static final String RES_PATH = "/res/";
	
	/**
	 * Chemin des sauvegardes du jeu
	 */
	public static String SAVE_PATH = "";
	
	/**
	 * Icone des fenetres
	 */
	public static Image ICON = null;
	
	/**
	 * Active le mode DEBUG
	 */
	public static boolean DEBUG = false;
	
	/**
	 * Different type de log
	 */
	public static enum TypeLog{
		INFO, ERREUR, WARNING
	};

	/**
	 * Main
	 * @param args
	 */
    public static void main(String[] args){
    	recupererSauvegarde();
    	
    	//Chargement de l'icon
    	try{
			ICON = ImageIO.read(Echecs.class.getResource(Echecs.RES_PATH+"cavalier_n.png"));
		}catch(Exception e){
			System.out.println("Impossible de charger l'image "+Echecs.class.getResource(Echecs.RES_PATH+"cavalier_n.png"));
		}
    	
    	new Menu();
    }
    
    /**
     * Recupere le chemin du fichier save, le creer s'il est inexistant
     */
    private static void recupererSauvegarde(){
    	File file = new File("save");
    	//Si le dossier save existe pas
		if(!file.exists()){
			File bin = new File("bin");
			File file2 = new File("bin"+File.separatorChar+"save");
			//Si on execute le programme avec eclipse
			if(bin.exists()){
				//si le dossier save existe pas
				if(!file2.exists()){
					file2.mkdir();
				}
				SAVE_PATH = file2.getAbsolutePath();
			}else{
				file.mkdir();
				SAVE_PATH = file.getAbsolutePath();
			}
		}else{
			SAVE_PATH = file.getAbsolutePath();
		}
    }
    
    /**
     * Ajoute une ligne de log a la console
     * @param log le message a ajouter
     * @param type le type de message
     */
    public static void addLog(String log, TypeLog type){
    	String typelog = "[?]";
    	if(type.equals(TypeLog.INFO)){
    		typelog = "[INFO]";
    	}else if(type.equals(TypeLog.ERREUR)){
    		typelog = "[ERREUR]";
    	}else if(type.equals(TypeLog.WARNING)){
    		typelog = "[WARNING]";
    	}
    	
    	Date now =  new Date();
    	String formatDate = new SimpleDateFormat("HH:mm:ss").format(now);
    	System.out.println(formatDate+" "+typelog+" "+log);
    }
}
