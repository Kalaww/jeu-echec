package echecs.graphisme.jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;

import echecs.graphisme.Menu;
import echecs.jeu.JeuClient;

/**
 * Barre de menu pour le mode en ligne
 */
public class BarreMulti extends JMenuBar implements ActionListener, ItemListener{
	private Fenetre fenetre;
	
	private JMenu Fichier;
	private JMenu Option;
	
	private JCheckBoxMenuItem aideCouleur;
	
	private JMenuItem fermer;
	private JMenuItem menuPrincipal;

	/**
	 * Constructeur
	 * @param fenetre reference de la fenetre
	 */
	public BarreMulti(Fenetre fenetre){
		this.fenetre = fenetre;
		Fichier = new JMenu("Fichier");
		Option = new JMenu("Option");
		
		fermer = new JMenuItem("Fermer");
		
		menuPrincipal = new JMenuItem("Menu Principal");
		
		aideCouleur = new JCheckBoxMenuItem("Aide Couleur", true);
		
		fermer.addActionListener(this);
		menuPrincipal.addActionListener(this);
		aideCouleur.addItemListener(this);

		this.Fichier.add(menuPrincipal);
		this.Fichier.add(fermer);
		this.Option.add(aideCouleur);
		
		this.add(Fichier);
		this.add(Option);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		
		if(source == fermer){
			System.exit(0);
		}
		
		if(source == menuPrincipal){
			int choix = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment retourner au menu principal ?" , "Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(choix != JOptionPane.NO_OPTION && choix != JOptionPane.CLOSED_OPTION ){
				JeuClient jeu = (JeuClient) fenetre.getJeu();
				jeu.deconnexion();
				jeu.retourmenu =true;
				fenetre.setVisible(false);
				fenetre.dispose();
				//new Menu();
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int etat = e.getStateChange();
		if( etat == ItemEvent.SELECTED ){
			fenetre.getGrille().setAffichageAideDeplacement(true);	
		}
		else{ 
			fenetre.getGrille().setAffichageAideDeplacement(false);
		}
	}
}
		
	