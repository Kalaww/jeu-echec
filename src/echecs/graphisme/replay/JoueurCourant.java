package echecs.graphisme.replay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import echecs.graphisme.Case;

/**
 * Affiche le joueur courant pour la fenetre de replay
 */
public class JoueurCourant extends JPanel{
	
	/**
	 * Affichage du joueur blanc
	 */
	private CaseJoueur joueurBlanc;
	
	/**
	 * Affichage du joueur noir
	 */
	private CaseJoueur joueurNoir;

	/**
	 * Reference de la fenetre de replay auquel appartient l'affichage joueur courant
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * Constructeur
	 * @param fenetre
	 */
	public JoueurCourant(ReplayFenetre fenetre){
		super();
		this.fenetre = fenetre;
		initConteneur();
	}
	
	/**
	 * Instancie et positionne les elements
	 */
	private void initConteneur(){
		joueurBlanc = new CaseJoueur(true);
		joueurBlanc.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		
		joueurNoir = new CaseJoueur(false);
		joueurNoir.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		
		JLabel textTour = new JLabel("TOUR", JLabel.CENTER);
		textTour.setPreferredSize(new Dimension(Case.CASE_LENGTH * 2, Case.CASE_LENGTH));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//placement joueur blanc
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		this.add(joueurBlanc, gbc);
		
		//placement texte Tour
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(textTour, gbc);
		
		//placement joueur noir
		gbc.gridx = 2;
		this.add(joueurNoir, gbc);
	}
	
	/**
	 * Mise a jour du joueur courant
	 */
	public void update(){
		if(fenetre.getReplay() == null || fenetre.getReplay().getJoueurCourant().getCouleur().equals("BLANC")){
			joueurBlanc.isTour = true;
			joueurNoir.isTour = false;
		}else{
			joueurBlanc.isTour = false;
			joueurNoir.isTour = true;
		}
	}
}

/**
 * Un element de l'affichagedu joueur courant
 */
class CaseJoueur extends JPanel{
	
	/**
	 * Vrai si c'est le tour de cet element
	 */
	public boolean isTour;
	
	/**
	 * Couleur de l'element
	 */
	private Color couleur;
	
	/**
	 * Constructeur
	 * @param isBlanc
	 */
	public CaseJoueur(boolean isBlanc){
		super();
		this.couleur = (isBlanc)? Color.white : Color.black;
		this.isTour = isBlanc;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(couleur);
		if(isTour){
			g.fillRect(this.getWidth() * 3/8, this.getHeight() * 3/8, this.getWidth() / 4, this.getHeight() / 4);
			g.setColor(Color.GRAY);
			g.drawRect(this.getWidth() * 3/8, this.getHeight() * 3/8, this.getWidth() / 4, this.getHeight() / 4);
		}else{
			g.fillRect(this.getWidth() / 4, this.getHeight() / 4, this.getWidth() / 2, this.getHeight() / 2);
			g.setColor(Color.GRAY);
			g.drawRect(this.getWidth() / 4, this.getHeight() / 4, this.getWidth() / 2, this.getHeight() / 2);
		}
	}
}
