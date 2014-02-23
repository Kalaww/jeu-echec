package echecs.graphisme.replay;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import echecs.Echecs;
import echecs.graphisme.Case;
import echecs.graphisme.PiecesPrises;
import echecs.jeu.piece.Piece;
import echecs.jeu.Replay;

/**
 * Zone d'affichage des pieces prises pour l'interface graphique du mode replay
 */
public class PiecesPrisesReplay extends PiecesPrises{
	
	/**
	 * Refenrence du jeu
	 */
	private Replay replay;
	
	/**
	 * Constructeur
	 * @param replay reference du jeu
	 * @param priseBlanc si vrai contient des prises blanches, sinon noires
	 */
	public PiecesPrisesReplay(Replay replay, boolean priseBlanc){
		super(priseBlanc);
		this.replay = replay;
	}
	
	@Override
	public void paintComponent(Graphics g){
		ArrayList<Piece> priseTemp = replay.getPrises();
		String couleur = (priseBlanc) ? "BLANC" : "NOIR";
		int x=0;
		int y=0;
		for(int i=0; i<priseTemp.size(); i++){
			Piece temp = priseTemp.get(i);
			if(couleur.equals(priseTemp.get(i).getCouleur())){
				//Dessine l'image de la pi�ce
				char couleurFile = (couleur.equals("NOIR"))? 'n': 'b';
				Image imgPiece = null;
				try{
					imgPiece = ImageIO.read(getClass().getResource(Echecs.RES_PATH+temp.getFamille().toLowerCase()+"_"+couleurFile+".png"));
					g.drawImage(imgPiece, x, y, this);
					x+=Case.CASE_LENGTH;
					if(x>=Case.CASE_LENGTH * 2){
						y+=Case.CASE_LENGTH;
						x=0;
					}
					
				}catch(IOException e){
					System.out.println("Impossible de charger l'image "+getClass().getResource(Echecs.RES_PATH+temp.getFamille().toLowerCase()+"_"+couleurFile+".png"));
				}
			}
			
		}
	}

}
