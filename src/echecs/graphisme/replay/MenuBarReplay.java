package echecs.graphisme.replay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import echecs.graphisme.Menu;

/**
 * Menu bar de l'interface graphique du mode replay
 */
public class MenuBarReplay extends JMenuBar implements ActionListener{
	
	/**
	 * Reference de la fenetre qui contient la Menu bar
	 */
	private ReplayFenetre fenetre;
	
	/**
	 * Menu fichier
	 */
	private JMenu fichier;
	
	/**
	 * Sous menu pour ouvrir une partie
	 */
	private JMenuItem ouvrirPartie;
	
	/**
	 * Sous menu pour revenir au menu du jeu
	 */
	private JMenuItem retourMenu;
	
	/**
	 * Sous menu pour quitter le jeu
	 */
	private JMenuItem quitter;

	/**
	 * Constructeur
	 * @param fenetre la reference de la fenetre qui contient la Menu bar
	 */
	public MenuBarReplay(ReplayFenetre fenetre){
		this.fenetre = fenetre;
		initBarre();
	}
	
	/**
	 * Initialise et positionne les elements de la MenuBar
	 */
	private void initBarre(){
		fichier = new JMenu("Fichier");
		ouvrirPartie = new JMenuItem("Ouvrir une partie");
		ouvrirPartie.addActionListener(this);
		retourMenu = new JMenuItem("Retour au Menu");
		retourMenu.addActionListener(this);
		quitter = new JMenuItem("Quitter");
		quitter.addActionListener(this);
		
		fichier.add(ouvrirPartie);
		fichier.add(retourMenu);
		fichier.add(quitter);
		
		this.add(fichier);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == quitter){
			System.exit(0);
		}
		
		if(source == retourMenu){
			fenetre.setVisible(false);
			fenetre.dispose();
			Menu m = new Menu();
		}
		
		if(source == ouvrirPartie){
			fenetre.ouvrirSelectionPartie();
		}
		
	}
	
}
