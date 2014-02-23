package echecs.graphisme.replay;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import echecs.Echecs;
import echecs.graphisme.Case;
import echecs.jeu.Replay;

/**
 * Interface graphique du mode replay
 */
public class ReplayFenetre extends JFrame implements ActionListener{

	/**
	 * Conteneur general de la fenetre
	 */
	private JPanel conteneurGeneral;
	
	/**
	 * Reference de la grille
	 */
	private GrilleReplay grille;
	
	/**
	 * Reference du conteneur qui dessine des prises blanches
	 */
	private PiecesPrisesReplay prisesBlanches;
	
	/**
	 * Reference du conteneur qui dessine des prises noires
	 */
	private PiecesPrisesReplay prisesNoires;
	
	/**
	 * Affichage du joueur courant
	 */
	private JoueurCourant joueurCourant;
	
	/**
	 * Reference du replay
	 */
	private Replay replay;
	
	/**
	 * Bouton coup precedent
	 */
	private JButton coupPrecedent;
	
	/**
	 * Bouton coup suivant
	 */
	private JButton coupSuivant;
	
	/**
	 * Zone d'affichage des coordonnees ABCDEFGH
	 */
	private JPanel coordAbscisse;
	
	/**
	 * Zone d'affichage des coordonnes 12345678
	 */
	private JPanel coordOrdonnee;
	
	/**
	 * Reference de la Menu bar 
	 */
	private MenuBarReplay menuBar;
	
	/**
	 * Affiche une liste de l'historique
	 */
	private JList listHistorique;
	
	/**
	 * Contient l'historique complet
	 */
	private DefaultListModel historique;
	
	/**
	 * Contructeur avec la fenetre placer au coordonnes donnees
	 * @param x position de la fenetre en x
	 * @param y position de la fenetre en y
	 */
	public ReplayFenetre(int x, int y){
		super("Replay");
		this.replay = new Replay(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Case.CASE_LENGTH * 14, Case.CASE_LENGTH * 13);
		this.setMinimumSize(this.getSize());
		initReplay();
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.setVisible(true);
		this.ouvrirSelectionPartie();
		this.setIconImage(Echecs.ICON);
	}
	
	/**
	 * Initialise tous les elements et les positionnent dans la fenetre
	 */
	private void initReplay(){
		
		//Creation de tous les conteneurs
		//Menu bar
		menuBar = new MenuBarReplay(this);
		
		//Conteneur general de la fenetre
		conteneurGeneral = new JPanel();
		conteneurGeneral.setPreferredSize(this.getPreferredSize());
		
		
		//Affichage du joueur courant
		joueurCourant = new JoueurCourant(this);
		joueurCourant.setPreferredSize(new Dimension(Case.CASE_LENGTH * 4, Case.CASE_LENGTH));
		
		//Grille du plateau de jeu
		grille = new GrilleReplay(this);
		grille.setPreferredSize(new Dimension(Case.CASE_LENGTH * 8, Case.CASE_LENGTH * 8));
		
		//Initialisation des conteneur de pieces prises.
		//Prises blanches
		prisesBlanches = new PiecesPrisesReplay(replay, true);
		prisesBlanches.setPreferredSize(new Dimension(Case.CASE_LENGTH*2, Case.CASE_LENGTH*8));
		JPanel blancPriseConteneur = new JPanel();
		blancPriseConteneur.add(prisesBlanches);
		blancPriseConteneur.setBorder(BorderFactory.createTitledBorder("Prises blanches"));
		blancPriseConteneur.setPreferredSize(new Dimension(Case.CASE_LENGTH*2, Case.CASE_LENGTH*8));
		
		//Prises noires
		prisesNoires = new PiecesPrisesReplay(replay, false);
		prisesNoires.setPreferredSize(new Dimension(Case.CASE_LENGTH*2, Case.CASE_LENGTH*8));
		JPanel noirPriseConteneur = new JPanel();
		noirPriseConteneur.add(prisesNoires);
		noirPriseConteneur.setBorder(BorderFactory.createTitledBorder("Prises noires"));
		noirPriseConteneur.setPreferredSize(new Dimension(Case.CASE_LENGTH*2, Case.CASE_LENGTH*8));
		
		//Buttons avant apres
		coupPrecedent = new JButton("<=");
		coupPrecedent.addActionListener(this);
		coupSuivant = new JButton("=>");
		coupSuivant.addActionListener(this);
		
		//Historique
		historique = new DefaultListModel();
		listHistorique = new JList(historique);
		listHistorique.setEnabled(false);
		listHistorique.setLayoutOrientation(JList.VERTICAL_WRAP);
		listHistorique.setVisibleRowCount(0);
		JScrollPane scrollHistorique = new JScrollPane(listHistorique);
		scrollHistorique.setPreferredSize(new Dimension(Case.CASE_LENGTH * 8, Case.CASE_LENGTH * 2));
		scrollHistorique.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Coords
		coordAbscisse = new JPanel();
		coordAbscisse.setLayout(new GridLayout(1, 8));
		for(char i = 'A'; i <= 'H'; i++){
			JLabel c = new JLabel(i+"", JLabel.CENTER);
			c.setPreferredSize(new Dimension(Case.CASE_LENGTH, 10));
			coordAbscisse.add(c);
		}
		coordOrdonnee = new JPanel();
		coordOrdonnee.setLayout(new GridLayout(8, 1));
		for(int i = 8; i >= 1; i--){
			JLabel c = new JLabel(i+"");
			c.setPreferredSize(new Dimension(10, Case.CASE_LENGTH));
			coordOrdonnee.add(c);
		}
		
		this.setJMenuBar(menuBar);
		
		//Positionnement sur le GridBagLayout
		conteneurGeneral.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//Placement de affichage joueur courant
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		conteneurGeneral.add(joueurCourant, gbc);
		
		//Placement prise des pieces blanches
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 0, 0);
		conteneurGeneral.add(blancPriseConteneur, gbc);
		
		//Placement des ordonnees
		gbc.gridx = 1;
		conteneurGeneral.add(coordOrdonnee, gbc);
		
		//Placement de la grille
		gbc.gridx = 2;
		gbc.insets = new Insets(0, 10, 0, 10);
		conteneurGeneral.add(grille, gbc);
		
		//Placement prise des pieces noires
		gbc.gridx = 3;
		gbc.insets = new Insets(0, 10,  0, 10);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurGeneral.add(noirPriseConteneur, gbc);
		
		//placement des abscisses
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10,10,0,10);
		conteneurGeneral.add(coordAbscisse, gbc);
		
		//Placement bouton precedent
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		conteneurGeneral.add(coupPrecedent, gbc);
		
		//Placement historique
		gbc.gridx = 2;
		conteneurGeneral.add(scrollHistorique, gbc);
		
		//Placement bouton suivant
		gbc.gridx = 3;
		conteneurGeneral.add(coupSuivant, gbc);
		
		this.setContentPane(conteneurGeneral);
	}
	
	@Override
	public void repaint(){
		grille.updateGrille();
		joueurCourant.update();
		super.repaint();
	}
	
	/**
	 * Ouvre une fenetre de selection d'une partie
	 */
	public void ouvrirSelectionPartie(){
		File dossier = new File(Echecs.SAVE_PATH);
		String[] nomFichiers = dossier.list();
		ArrayList<String> fichiersPGN = new ArrayList<String>();
		
		for(int i = 0; i < nomFichiers.length; i++){
			if(nomFichiers[i].endsWith(".pgn")){
				nomFichiers[i] = nomFichiers[i].substring(0, nomFichiers[i].length() -4);
				fichiersPGN.add(nomFichiers[i]);
			}
		}
		
		String[] fichiers = new String[fichiersPGN.size()];
		for(int i = 0; i < fichiers.length; i++){
			fichiers[i] = fichiersPGN.get(i);
		}
		
		new SelectionPartie(this, fichiers);
	}
	
	/**
	 * mise a jour de l'affichage de l'historique
	 */
	public void updateHistorique(){
		historique.removeAllElements();
		ArrayList<String> tours = replay.getPartie().coups.toStringParTour();
		for(String tour : tours){
			historique.addElement(tour);
		}
		listHistorique.setSelectedIndex(0);
	}
	
	/**
	 * Getter replay
	 * @return Replay
	 */
	public Replay getReplay(){
		return replay;
	}
	
	/**
	 * Getter grille
	 * @return GrilleReplay
	 */
	public GrilleReplay getGrille(){
		return grille;
	}
	
	/**
	 * Getter historique
	 * @return historique
	 */
	public DefaultListModel getHistorique(){
		return historique;
	}
	
	/**
	 * Getter bouton du coup precedent
	 * @return JButton
	 */
	public JButton getBoutonPrecedent(){
		return this.coupPrecedent;
	}
	
	/**
	 * Getter bouton du coup suivant
	 * @return JButton
	 */
	public JButton getBoutonSuivant(){
		return this.coupSuivant;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == coupSuivant){
			replay.jouerCoupSuivant();
			listHistorique.setSelectedIndex(replay.getCoupCourant()/2);
			repaint();
		}
		
		if(source == coupPrecedent){
			replay.getPlateau().annulerDernierCoup(true);
			listHistorique.setSelectedIndex(replay.getCoupCourant()/2);
			grille.resetEtatCases();
			if(replay.getCoupCourant() > 0){
				grille.setCaseDernierCoup(replay.getPartie().coups.getList().get(replay.getCoupCourant()).departMemoire.x, replay.getPartie().coups.getList().get(replay.getCoupCourant()).departMemoire.y);
				grille.setCaseDernierCoup(replay.getPartie().coups.getList().get(replay.getCoupCourant()).arrivee.x, replay.getPartie().coups.getList().get(replay.getCoupCourant()).arrivee.y);
			}
			repaint();
		}
	}
}
