package echecs.graphisme.jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import echecs.Echecs;
import echecs.graphisme.Menu;
import echecs.jeu.IA.IAcomplet;
import echecs.jeu.IA.IAminimax;

import echecs.sauvegarde.Sauvegarde;

/**
 * Barre de menu de la fenetre
 */
public class Barre extends JMenuBar implements ActionListener, MouseListener, ItemListener, ChangeListener{
	
	/**
	 * Reference de la fenetre
	 */
	private Fenetre fenetre;
	
	/**
	 * Menu fichier
	 */
	private JMenu Fichier;
	
	/**
	 * Menu option
	 */
	private JMenu Option;
	
	/**
	 * Active/desactive l'aide au deplacement
	 */
	private JCheckBoxMenuItem aideCouleur;
	
	/**
	 * Active/desactive le mode debug
	 */
	private JCheckBoxMenuItem modeDebug;
	
	/**
	 * Action nouvelle partie
	 */
	private JMenuItem nouvellePartie;
	
	/**
	 * Action quitter
	 */
	private JMenuItem fermer;
	
	/**
	 * Action sauvegarder
	 */
	private JMenuItem sauvegarder;
	
	/**
	 * Action charger une partie
	 */
	private JMenuItem charger;
	
	/**
	 * Action retour menu principal
	 */
	private JMenuItem menuPrincipal;
	
	/**
	 * Modificationd de la profondeur de recherche pour l'IA niveau 3
	 */
	private JMenuItem profondeurIA;
	
	/**
	 * Slider pour la vitesse de sleep de l'IA
	 */
	private JSlider vitesseIA;
	
	/**
	 * Label du slider de la vitesse de sleep de l'IA
	 */
	private JLabel vitesseIAAffichage;
	
	/**
	 * Sauvegarde
	 */
	private String sav;
	
	/**
	 * Vitesse de sleep de l'IA de depart (aucune)
	 */
	public static int VITESSE_IA = 0;

	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre
	 */
	public Barre(Fenetre fenetre){
		this.fenetre = fenetre;
		Fichier = new JMenu("Fichier");
		Option = new JMenu("Option");
		nouvellePartie = new JMenuItem("Nouvelle Partie");
		fermer = new JMenuItem("Fermer");
		sauvegarder = new JMenuItem("Sauvegarder");
		charger = new JMenuItem("Charger");
		menuPrincipal = new JMenuItem("Menu Principal");
		profondeurIA = new JMenuItem("Profondeur de recherche de l'IA");
		aideCouleur = new JCheckBoxMenuItem("Aide Couleur", true);
		modeDebug = new JCheckBoxMenuItem("Mode debug", Echecs.DEBUG);
		vitesseIA = new JSlider(0, 100, 0);
		vitesseIA.addChangeListener(this);
		vitesseIAAffichage = new JLabel("0 ms");

		sav="Entrez un nom pour votre sauvegarde";
		
		this.Fichier.add(nouvellePartie);
		this.Fichier.addSeparator();
		
		
		nouvellePartie.addActionListener(this);
		fermer.addActionListener(this);
		sauvegarder.addActionListener(this);
		charger.addActionListener(this);
		menuPrincipal.addActionListener(this);
		aideCouleur.addItemListener(this);
		modeDebug.addItemListener(this);
		profondeurIA.addActionListener(this);
		
		this.Fichier.add(sauvegarder);
		this.Fichier.add(charger);	
		this.Fichier.addSeparator();
		this.Fichier.add(menuPrincipal);
		this.Fichier.add(fermer);
		
		this.Option.add(aideCouleur);
		this.Option.add(modeDebug);
		
		this.add(Fichier);
		this.add(Option);
		
		if(fenetre.getJeu().isVsIA() && fenetre.getJeu().getIAThread().getIA().getClass().equals(IAcomplet.class)){
			this.Option.add(profondeurIA);
			this.add(vitesseIA);
			this.add(vitesseIAAffichage);
			vitesseIA.setVisible(Echecs.DEBUG);
			vitesseIAAffichage.setVisible(Echecs.DEBUG);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		
		if(source == nouvellePartie){
			int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment commencer une nouvelle partie ?" , "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(choix != JOptionPane.NO_OPTION && choix != JOptionPane.CLOSED_OPTION)
			fenetre.getJeu().reset();
		}
		
		if(source == fermer){
			System.exit(0);
		}
		
		if(source == sauvegarder){
			String name = JOptionPane.showInputDialog(null, sav, "Sauvegarde", JOptionPane.QUESTION_MESSAGE);
			if(name != null){
				if(name.length() == 0)			
					JOptionPane.showMessageDialog(null, "Le nom de la sauvegarde doit avoir au moin un caractere !", "Nom non valide", JOptionPane.ERROR_MESSAGE);
				else{					
					boolean resultat = Sauvegarde.creerSauvegardeJeuNonFini(fenetre.getJeu(), name);
					if(resultat){
						JOptionPane.showMessageDialog(null, "Votre sauvegarde a ete effectue avec succes","Sauvegarde reussi" , JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
		
		if(source == charger){
			File f = new File (Echecs.SAVE_PATH);
			String[] nomsFichiers = f.list();
			ArrayList<String> temp = new ArrayList<String>();
			
			for(int i = 0; i < nomsFichiers.length; i++){
				if(nomsFichiers[i].endsWith(".sv")){
					nomsFichiers[i] = nomsFichiers[i].substring(0, nomsFichiers[i].length() - 3);
					temp.add(nomsFichiers[i]);
				}
			}
			
			String[] fichiersChargeable = new String[temp.size()];
			for(int i = 0; i < temp.size(); i++){
				fichiersChargeable[i] = temp.get(i);
			}
			
			JOptionPane pan = new JOptionPane();
			if (fichiersChargeable.length != 0){
				String nomFichier = (String)pan.showInputDialog(null, "Quel fichier charger ?", "Chargement", JOptionPane.QUESTION_MESSAGE, null, fichiersChargeable, fichiersChargeable[0]);
				if(nomFichier != null){
					Sauvegarde.lireSauvegardeJeuNonFini(fenetre.getJeu(), nomFichier);
				}
			}else{
				JOptionPane.showMessageDialog(null, "Il n'y a aucune sauvegarde dans le dossier", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(source == menuPrincipal){
			int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment retourner au menu principal ?" , "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(choix != JOptionPane.NO_OPTION && choix != JOptionPane.CLOSED_OPTION ){
				fenetre.getJeu().fermerThreadIA();
				fenetre.getJeu().getFenetre().setVisible(false);
				fenetre.getJeu().getFenetre().dispose();
				new Menu();
			}
		}
		
		if(source == profondeurIA){
			String[] profondeurs = {"1", "2", "3", "4", "5"};
			String choix = (String)JOptionPane.showInputDialog(null, "Profondeur de recherche de l'IA\nMettre une profondeur importante va grandement augmenter le temps de jeu de l'IA", "Profondeur de l'IA", JOptionPane.QUESTION_MESSAGE, null, profondeurs, profondeurs[0]);
			if(choix != null){
				IAminimax.MAX_PROFONDEUR_TEMP = Integer.parseInt(choix);
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		if(source == aideCouleur){
			int etat = e.getStateChange();
			System.out.println(etat);
			if( etat == ItemEvent.SELECTED ){
				fenetre.getGrille().setAffichageAideDeplacement(true);	
			}
			else{ 
				fenetre.getGrille().setAffichageAideDeplacement(false);
			}
		}
		
		if(source == modeDebug){
			Echecs.DEBUG = modeDebug.isSelected();
			vitesseIA.setVisible(Echecs.DEBUG);
			vitesseIAAffichage.setVisible(Echecs.DEBUG);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		VITESSE_IA = vitesseIA.getValue();
		vitesseIAAffichage.setText(VITESSE_IA+" ms");
	}
}
		
	