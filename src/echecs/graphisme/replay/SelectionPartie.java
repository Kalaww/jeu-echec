package echecs.graphisme.replay;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import echecs.Echecs;
import echecs.sauvegarde.CoupPGN;
import echecs.sauvegarde.Historique;
import echecs.sauvegarde.Partie;
import echecs.sauvegarde.Sauvegarde;

/**
 * Fenetre de selection d'une partie d'echec au format PGN
 */
public class SelectionPartie extends JFrame implements ListSelectionListener, ActionListener{
	
	/**
	 * Le conteneur general de la fenetre
	 */
	private JPanel conteneurGeneral;
	
	/**
	 * Reference de la fenetre de replay
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * List qui affiche les fichiers pgn
	 */
	private JList listFichiers;
	
	/**
	 * Contient les noms de fichiers
	 */
	private DefaultListModel fichiers;
	
	/**
	 * Contient les events du fichier selectionne
	 */
	private DefaultListModel parties;
	
	/**
	 * List qui affiche les events du fichier selectionne
	 */
	private JList listParties;
	
	/**
	 * Label du site de la partie selectionnee
	 */
	private JLabel site;
	
	/**
	 * Label de la date de la partie selectionnee
	 */
	private JLabel date;
	
	/**
	 * Label du round de la partie selectionnee
	 */
	private JLabel round;
	
	/**
	 * Label du joueur blanc de la partie selectionnee
	 */
	private JLabel joueurBlanc;
	
	/**
	 * Label du joueur noir de la partie selectionnee
	 */
	private JLabel joueurNoir;
	
	/**
	 * Label du resultat de la partie selectionnee
	 */
	private JLabel resultat;
	
	/**
	 * Contient tous les coups de la partie selectionnee
	 */
	private DefaultListModel toursHistorique;
	
	/**
	 * List qui affiche les coups de la partie selectionnee
	 */
	private JList historique;
	
	/**
	 * Contient toutes les informations de la partie selectionnee
	 */
	private JPanel detailPartie;
	
	/**
	 * Bouton pour lancer la partie selectionnee
	 */
	private JButton lancerPartie;
	
	/**
	 * Stocke les informations de la partie selectionnee
	 */
	private Partie partie;
	
	/**
	 * Faux s'il y a une erreur dans le chargement d'une partie
	 */
	private boolean erreurChargement;
	
	/**
	 * Constructeur
	 * @param fenetre la fenetre du replay
	 * @param fichiers la liste des fichiers dans le dossier save au format PGN
	 */
	public SelectionPartie(ReplayFenetre fenetre, String[] fichiers){
		super("Selection d'une partie");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(700,500);
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(fenetre);
		this.fenetre = fenetre;
		this.partie = new Partie();
		this.erreurChargement = false;
		initFenetre(fichiers);
		this.setVisible(true);
		this.setIconImage(Echecs.ICON);
	}
	
	/**
	 * Initialise et positionne l'ensemble des elements de la fenetre
	 * @param fichiersTable la liste des fichiers dans le dossier save au format PGN
	 */
	private void initFenetre(String[] fichiersTable){
		//conteneur generale
		conteneurGeneral = new JPanel();
		
		//List des fichiers
		fichiers = new DefaultListModel();
		for(String s : fichiersTable){
			fichiers.addElement(s);
		}
		listFichiers = new JList(fichiers);
		listFichiers.addListSelectionListener(this);
		JScrollPane scrollZoneFichiers = new JScrollPane(listFichiers);
		scrollZoneFichiers.setPreferredSize(new Dimension(150, 400));
		scrollZoneFichiers.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollZoneFichiers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//list des parties d'un fichiers
		parties = new DefaultListModel();
		listParties = new JList(parties);
		listParties.addListSelectionListener(this);
		JScrollPane scrollZoneParties = new JScrollPane(listParties);
		scrollZoneParties.setPreferredSize(new Dimension(200, 400));
		scrollZoneParties.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollZoneParties.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//historique
		toursHistorique = new DefaultListModel();
		historique = new JList(toursHistorique);
		historique.setLayoutOrientation(JList.VERTICAL_WRAP);
		historique.setVisibleRowCount(0);
		JScrollPane scrollZoneHistorique = new JScrollPane(historique);
		scrollZoneHistorique.setPreferredSize(new Dimension(250, 200));
		scrollZoneHistorique.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollZoneHistorique.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Detail de la partie
		detailPartie = new JPanel();
		detailPartie.setPreferredSize(new Dimension(250, 150));
		detailPartie.setLayout(new GridLayout(6,1));
		//site
		site = new JLabel("Site : ");
		detailPartie.add(site);
		//date
		date = new JLabel("Date : ");
		detailPartie.add(date);
		//round
		round = new JLabel("Round : ");
		detailPartie.add(round);
		//joueur blanc
		joueurBlanc = new JLabel("Joueur blanc : ");
		detailPartie.add(joueurBlanc);
		//joueur noir
		joueurNoir = new JLabel("Joueur noir : ");
		detailPartie.add(joueurNoir);
		//resultat
		resultat = new JLabel("Resultat : ");
		detailPartie.add(resultat);
		
		//button pour lancer la partie
		lancerPartie = new JButton("Lancer cette partie");
		lancerPartie.setEnabled(false);
		lancerPartie.addActionListener(this);
		
		
		//titre list des fichiers
		JLabel titreListFichiers = new JLabel("FICHIERS");
		
		//titre list des parties
		JLabel titreListParties = new JLabel("EVENTS");
		
		//titre info partie
		JLabel titreInfoPartie = new JLabel("INFORMATIONS SUR LA PARTIE");
		
		
		//Positionnement
		conteneurGeneral.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//positionnement du titre list des fichiers
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		conteneurGeneral.add(titreListFichiers, gbc);
		
		//positionnement du titre des parties
		gbc.gridx = 1;
		conteneurGeneral.add(titreListParties, gbc);
		
		//positionnement du titre des infos de la partie
		gbc.gridx = 2;
		conteneurGeneral.add(titreInfoPartie, gbc);
		
		//positionnement de la list des fichiers
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 3;
		gbc.insets = new Insets(10,10,10,0);
		conteneurGeneral.add(scrollZoneFichiers, gbc);
		
		//postionnement de la list des parties
		gbc.gridx = 1;
		conteneurGeneral.add(scrollZoneParties, gbc);
		
		//positionnement des details de la partie
		gbc.gridx = 2;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10,10,10,10);
		conteneurGeneral.add(detailPartie, gbc);
		
		//postionnement historique
		gbc.gridy = 2;
		gbc.insets = new Insets(0,10,10,0);
		conteneurGeneral.add(scrollZoneHistorique, gbc);
		
		//positionnement boutton
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		conteneurGeneral.add(lancerPartie, gbc);
		
		this.setContentPane(conteneurGeneral);
		
	}
	
	/**
	 * Recupere l'ensembles des parties d'un fichier
	 * @param nomFichier le fichier ou il faut recuperer les parties
	 */
	private void getParties(String nomFichier){
		try{
			parties.removeAllElements();
			File file = new File(Echecs.SAVE_PATH+File.separatorChar+nomFichier+".pgn");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String ligne = br.readLine();
			
			while(ligne != null){
				if(ligne.startsWith("[Event ")){
					ligne = ligne.substring(8, ligne.length()-2);
					parties.addElement(ligne);
				}
				ligne = br.readLine();
			}
			
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Recupere les informations d'une partie
	 * @param numero position de la partie dans le fichier
	 */
	private void getPartie(int numero){
		partie = Sauvegarde.lireSauvegardePGN((String)listFichiers.getSelectedValue(), numero);
	}

	/**
	 * Met a jour les informations de la partie selectionnee
	 */
	private void updateInfoPartie(){
		site.setText("Site : "+partie.site);
		date.setText("Date : "+partie.date);
		round.setText("Round : "+partie.round);
		joueurBlanc.setText("Joueur blanc : "+partie.white);
		joueurNoir.setText("Joueur noir : "+partie.black);
		resultat.setText("Resultat : "+partie.result);
		
		toursHistorique.removeAllElements();
		ArrayList<String> tmp = partie.coups.toStringParTour();
		for(String s : tmp){
			toursHistorique.addElement(s);
		}
	}
	
	/**
	 * Vide toutes les infos de la partie selectionnee
	 */
	private void clearInfoPartie(){
		site.setText("Site :");
		date.setText("Date :");
		round.setText("Round :");
		joueurBlanc.setText("Joueur blanc : ");
		joueurNoir.setText("Joueur noir :");
		resultat.setText("Resultat :");
		toursHistorique.removeAllElements();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if(source == listFichiers){
			getParties((String)listFichiers.getSelectedValue());
			if(!listParties.isSelectionEmpty()){
				listParties.setSelectedIndex(0);
				partie.event = (String) listParties.getSelectedValue();
				getPartie(listParties.getSelectedIndex());
				updateInfoPartie();
				
			}
			this.validate();
			this.repaint();
		}
		
		if(source == listParties && listParties.getSelectedIndex() >= 0){
			partie = new Partie();
			partie.event = (String) listParties.getSelectedValue();
			getPartie(listParties.getSelectedIndex());
			if(partie != null){
				updateInfoPartie();
				this.erreurChargement = false;
			}else{
				this.erreurChargement = true;
			}
		}
		
		if(listParties.getSelectedIndex() < 0){
			clearInfoPartie();
			this.validate();
			this.repaint();
		}
		
		if(!listFichiers.isSelectionEmpty() && !listParties.isSelectionEmpty() && !erreurChargement){
			lancerPartie.setEnabled(true);
		}else{
			lancerPartie.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == lancerPartie){
			if(!listFichiers.isSelectionEmpty() && !listParties.isSelectionEmpty()){
				fenetre.getReplay().chargerPartie(partie);
				this.setVisible(false);
				this.dispose();
			}
		}
	}

}
