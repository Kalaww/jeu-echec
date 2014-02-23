package echecs.graphisme;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import echecs.Echecs;
import echecs.jeu.piece.Piece;

/**
 * Une case de la grille
 */
public class Case extends JPanel{

	/**
	 * Taille d'une case, la taille de l'image d'une piece
	 */
	public static final int CASE_LENGTH = 60;
	
	/**
	 * Etat de la case : si elle est selectionee, que l'on peut deplacer sa piece dessus, ou rien
	 */
	public enum Etat{
		RIEN, SELECTIONE, DEPLACEMENT_POSSIBLE, DERNIER_COUP
	};
	
	/**
	 * Coordonnee en abscisse
	 */
	protected int x;
	
	/**
	 * Coordonnee en ordonnee
	 */
	protected int y;
	
	/**
	 * Famille de la piece sur la case
	 */
	protected String famille;
	
	/**
	 * Couleur de la piece sur la case
	 */
	protected String couleur;
	
	/**
	 * Boolean : si la case contient une piece alors vrai
	 */
	protected boolean contientPiece;
	
	/**
	 * Couleur du fond : noir ou blanc
	 */
	protected Color backgroundColor;
	
	/**
	 * Etat de la piece
	 */
	protected Etat etat;
	
	/**
	 * Constructeur d'une case vide
	 * @param x coordonnee en abscisse
	 * @param y coordonnee en ordonnee
	 * @param backgroundColor couleur du fond
	 */
	public Case(int x, int y, Color backgroundColor){
		this.x = x;
		this.y = y;
		this.backgroundColor = backgroundColor;
		this.contientPiece = false;
		this.etat = Etat.RIEN;
		this.setSize(CASE_LENGTH, CASE_LENGTH);
	}
	
/**
	 * Gere l'affichage de la case selon son etat et la piece qu'elle contient
	 */
	public void paintComponent(Graphics g){
		//fond
		g.setColor(backgroundColor);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(!etat.equals(Etat.RIEN)){
			Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, 0.3f));
	        Color select = null;
	        if(etat.equals(Etat.SELECTIONE)){
	        	select = new Color(0, 102, 51);
	        }else if(etat.equals(Etat.DEPLACEMENT_POSSIBLE)){
	        	select = new Color(0, 204, 0);
	        }else if(etat.equals(Etat.DERNIER_COUP)){
	        	select = new Color(0, 0, 255);
	        }
	        g2d.setColor(select);
	        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
	        g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, 1.0f));
		}
		
		
		if(contientPiece){
			//Dessine l'image de la piece
			char couleurFile = (couleur.equals("NOIR"))? 'n': 'b';
			Image imgPiece = null;
			try{
				imgPiece = ImageIO.read(getClass().getResource(Echecs.RES_PATH+famille.toLowerCase()+"_"+couleurFile+".png"));
				g.drawImage(imgPiece, 0, 0, this);
			}catch(IOException e){
				System.out.println("Impossible de charger l'image "+getClass().getResource(Echecs.RES_PATH+famille.toLowerCase()+"_"+couleurFile+".png"));
			}
		}
	}
	
	/**
	 * Met a jour la case en y placant la piece passee en argument
	 * @param p Piece a placer sur la case
	 */
	public void updateCase(Piece p){
		if(p == null){
			this.famille = "";
			this.couleur = "";
			this.contientPiece = false;
		}else{
			this.famille = p.getFamille();
			this.couleur = p.getCouleur();
			this.contientPiece = true;
		}
	}
	
	/**
	 * Getter de l'abscisse de la Case sur la grille
	 * @return x
	 */
	public int getXTableau(){
		return x;
	}
	
	/**
	 * Getter de l'ordonnee de la Case sur la grille
	 * @return y
	 */
	public int getYTableau(){
		return y;
	}
	
	/**
	 * Getter de la couleur de la piece sur la case
	 * @return String
	 */
	public String getCouleur(){
		return couleur;
	}
	
	/**
	 * Getter de l'etat de la case
	 * @return etat
	 */
	public Etat getEtat(){
		return etat;
	}
	
	/**
	 * Setter de l'etat de la case
	 * @param e
	 */
	public void setEtat(Etat e){
		this.etat = e;
	}
	
	/**
	 * Permet de savoir si la case contient une piece
	 * @return le resultat du test
	 */
	public boolean contientPiece(){
		return contientPiece;
	}
}
