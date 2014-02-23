package echecs.graphisme;


import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import echecs.Echecs;
import echecs.graphisme.replay.ReplayFenetre;

/**
 * Menu du jeu d'echec
 * Acces au different mode de jeu
 */
public class Menu extends JFrame implements ActionListener, MouseListener{
	
	/**
	 * Bouton un joueur
	 */
	private JButton unJoueur;
	
	/**
	 * Bouton deux joueurs
	 */
	private JButton deuxJoueurs;
	
	/**
	 * Bouton a Propos
	 */
	private JButton aPropos;
	
	/**
	 * Bouton quitter
	 */
	private JButton quitter;
	
	/**
	 * Bouton en ligne
	 */
	private JButton enLigne;
	
	/**
	 * Bouton replay
	 */
	private JButton replay;
	
	/**
	 * Bouton IA contre IA
	 */
	private JButton iavsia;
	
	/**
	 * Reference du content panel de la fenetre
	 */
	private JPanel conteneur;
	
	/**
	 * Image 1
	 */
	private RandomPiece image1;
	
	/**
	 * Image 2
	 */
	private RandomPiece image2;
	
	/**
	 * Image 3
	 */
	private RandomPiece image3;
	
	/**
	 * Image 4
	 */
	private RandomPiece image4;
	
	/**
	 * Image 5
	 */
	private RandomPiece image5;
	
	/**
	 * Image 6
	 */
	private RandomPiece image6;
	
	/**
	 * Constructeur
	 */
	public Menu(){
		super("Jeu d'echecs");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(Case.CASE_LENGTH * 9, Case.CASE_LENGTH * 9);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		initFenetre();
		this.setVisible(true);
		this.setIconImage(Echecs.ICON);
	}
	
	/**
	 * Instancie et positionne les elements de la fenetre
	 */
	public void initFenetre(){
		//Initialise les boutons
		Dimension taille = new Dimension(150, 50);
		unJoueur = new JButton("Un Joueur");
		unJoueur.setPreferredSize(taille);
		unJoueur.addActionListener(this);
		unJoueur.addMouseListener(this);
		deuxJoueurs = new JButton("Deux Joueurs");
		deuxJoueurs.setPreferredSize(taille);
		deuxJoueurs.addActionListener(this);
		deuxJoueurs.addMouseListener(this);
		aPropos = new JButton("A propos");
		aPropos.addActionListener(this);
		quitter = new JButton("Quitter");
		quitter.addActionListener(this);
		enLigne = new JButton("En Ligne");
		enLigne.setPreferredSize(taille);
		enLigne.addMouseListener(this);
		enLigne.addActionListener(this);
		replay = new JButton("Replay");
		replay.setPreferredSize(taille);
		replay.addMouseListener(this);
		replay.addActionListener(this);
		iavsia = new JButton("IA vs IA");
		iavsia.setPreferredSize(taille);
		iavsia.addMouseListener(this);
		iavsia.addActionListener(this);
		
		//Images
		image1 = new RandomPiece(true);
		image1.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		image2 = new RandomPiece(true);
		image2.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		image3 = new RandomPiece(true);
		image3.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		image4 = new RandomPiece(false);
		image4.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		image5 = new RandomPiece(false);
		image5.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		image6 = new RandomPiece(false);
		image6.setPreferredSize(new Dimension(Case.CASE_LENGTH, Case.CASE_LENGTH));
		 
		//Initialise le Jpanel principal
		conteneur = new JPanel();
		conteneur.setLayout(new GridBagLayout());
		this.setContentPane(conteneur);
		
		//Label
		JLabel titre = new JLabel("ECHECS");
		titre.setFont(new Font("Dialog", Font.BOLD,50));
		titre.setHorizontalAlignment(JLabel.CENTER);
		
		
		//Positionnement
		GridBagConstraints gbc = new GridBagConstraints();
		
		//positionnement titre
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		conteneur.add(titre, gbc);
		
		//positionnement image 1
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		conteneur.add(image1, gbc);
		
		//positionnement image 2
		gbc.gridx = 0;
		gbc.gridy = 2;
		conteneur.add(image2, gbc);
		
		//postitionnement image 3
		gbc.gridx = 0;
		gbc.gridy = 3;
		conteneur.add(image3, gbc);
		
		//positionnement image 4
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		conteneur.add(image4, gbc);
		
		//positionnement image 5
		gbc.gridx = 3;
		gbc.gridy = 2;
		conteneur.add(image5, gbc);
		
		//postitionnement image 6
		gbc.gridx = 3;
		gbc.gridy = 3;
		conteneur.add(image6, gbc);
		
		//positionnement un joueur
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		conteneur.add(unJoueur, gbc);
		
		//postionnement deux joueurs
		gbc.gridx = 2;
		conteneur.add(deuxJoueurs, gbc);
		
		//positionnement en ligne
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		conteneur.add(enLigne, gbc);
		
		//positionnement iavsia
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		conteneur.add(iavsia, gbc);
		
		//positionnement replay
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		conteneur.add(replay, gbc);
		
		//positionnement a propos
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		conteneur.add(aPropos, gbc);
		
		//positionnement quitter
		gbc.gridx = 3;
		conteneur.add(quitter, gbc);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();

		if(source == unJoueur){
			new EcranSelection(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2, this);
		}
		if(source == deuxJoueurs){
			new SelectionMode(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2, this);
		}
		if(source == aPropos){
			new About();
		}
		if(source == quitter){
			setVisible(false);
			this.dispose();
			 System.exit(0);
		}
		if(source == enLigne){
			new EcranEnLigne(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2, this);
		}
		if(source == replay){
			setVisible(false);
			dispose();
			new ReplayFenetre(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		}
		
		if(source == iavsia){
			new ConfigIAvsIA(this, this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		Object source = e.getSource();
		if(source == unJoueur || source == deuxJoueurs){
			image1.isHover = true;
			image4.isHover = true;
		}
		
		if(source == enLigne || source == iavsia){
			image2.isHover = true;
			image5.isHover = true;
		}
		
		if(source == replay){
			image3.isHover = true;
			image6.isHover = true;
		}
		
		this.repaint();
	}
	
	public void mouseExited(MouseEvent e) {
		Object source = e.getSource();
		if(source == unJoueur || source == deuxJoueurs){
			image1.isHover = false;
			image4.isHover = false;
		}
		
		if(source == enLigne || source == iavsia){
			image2.isHover = false;
			image5.isHover = false;
		}
		
		if(source == replay){
			image3.isHover = false;
			image6.isHover = false;
		}
		this.repaint();
	}
}

/**
 * Affichage d'une piece aleatoirement
 */
class RandomPiece extends JPanel{
	
	/**
	 * Tableau des familles de piece pour le tirage au sort
	 */
	private String[] pieces = {"pion", "tour", "cavalier", "fou", "reine", "roi"};
	
	/**
	 * Couleur de la piece
	 */
	private char couleur;
	
	/**
	 * Valeur du tirage au sort
	 */
	private int index;
	
	/**
	 * Vrai : suppression de l'effet de transparence
	 */
	public boolean isHover;
	
	/**
	 * Constructeur
	 * @param isBlanc
	 */
	public RandomPiece(boolean isBlanc){
		super();
		this.couleur = (isBlanc)? 'b' : 'n';
		this.isHover = false;
		Random rand = new Random();
		index = rand.nextInt(pieces.length);
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		try{
			float transparence = (isHover)? 1f : 0.5f;
			Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, transparence));
			Image imgPiece = ImageIO.read(getClass().getResource(Echecs.RES_PATH+pieces[index]+"_"+couleur+".png"));
			g2d.drawImage(imgPiece, 0, 0, this);
		}catch(Exception e){
			System.out.println("Impossible de charger l'image "+getClass().getResource(Echecs.RES_PATH+pieces[index]+"_"+couleur+".png"));
		}
	}
}
