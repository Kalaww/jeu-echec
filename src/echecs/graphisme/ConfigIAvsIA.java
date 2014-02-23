package echecs.graphisme;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import echecs.Echecs;
import echecs.graphisme.jeu.Fenetre;
import echecs.jeu.IA.ValeursEvaluation;

/**
 * Fenetre de selection des IA pour un mode de jeu IA contre IA
 */
public class ConfigIAvsIA extends JFrame implements ActionListener{
	
	/**
	 * Reference du menu
	 */
	private Menu menu;
	
	/**
	 * Reference du content panel de la fenetre
	 */
	private JPanel conteneurGeneral;
	
	/**
	 * Boutons des niveaux d'IA blanc
	 */
	private JRadioButton niveau1Blanc, niveau2Blanc, niveau3Blanc;
	
	/**
	 * Boutons des niveaux d'IA noir
	 */
	private JRadioButton niveau1Noir, niveau2Noir, niveau3Noir;
	
	/**
	 * Label des choix de niveaux d'IA blanc
	 */
	private JLabel choixNiveauxBlanc, choixNiveauxNoir;
	
	/**
	 * Label des choix de niveaux d'IA noir
	 */
	private JCheckBox activeConfigBlanc, activeConfigNoir;
	
	/**
	 * Label des pieces blanches
	 */
	private JLabel piecesBlanches, piecesNoires;
	
	/**
	 * Label des pieces noires
	 */
	private JLabel pionBlanc, tourBlanc, cavalierBlanc, fouBlanc, reineBlanc;
	
	/**
	 * Label des pieces blanches
	 */
	private JLabel pionNoir, tourNoir, cavalierNoir, fouNoir, reineNoir;
	
	/**
	 * Champs de texte des pieces blanches
	 */
	private JTextField textPionBlanc, textTourBlanc, textCavalierBlanc, textFouBlanc, textReineBlanc;
	
	/**
	 * Champs de texte des pieces noires
	 */
	private JTextField textPionNoir, textTourNoir, textCavalierNoir, textFouNoir, textReineNoir;
	
	/**
	 * Label des differentes strategies
	 */
	private JLabel strategieBlanc, strategieNoir, defenseBlanc, defenseNoir, dangerBlanc, dangerNoir, attaqueBlanc, attaqueNoir;
	
	/**
	 * Champs de texte des differentes strategies
	 */
	private JTextField textDefenseBlanc, textAttaqueBlanc, textDangerBlanc, textDefenseNoir, textAttaqueNoir, textDangerNoir;
	
	/**
	 * Bouton valider et reset
	 */
	private JButton reset, valider;
	
	/**
	 * Constructeur
	 * @param menu reference du menu
	 * @param x coordonnee x de la fenetre
	 * @param y coordonnee y de la fenetre
	 */
	public ConfigIAvsIA(Menu menu, int x, int y){
		super("Configuration de l'IA vs IA");
		this.menu = menu;
		this.setSize(new Dimension(800, 500));
		this.setIconImage(Echecs.ICON);
		this.setMinimumSize(this.getSize());
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		initFenetre();
		checkConfig();
		initValeurEvaluation();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Initialise les variables et positionne les elements de la fenetre
	 */
	private void initFenetre(){
		//Conteneur general
		conteneurGeneral = new JPanel();
		
		//Label choix niveaux
		choixNiveauxBlanc = new JLabel("Choix du niveau de l'IA blanc");
		choixNiveauxNoir = new JLabel("Choix du niveau de l'IA noir");
		
		//niveaux
		niveau1Blanc = new JRadioButton("Niveau 1", true);
		niveau1Blanc.setPreferredSize(new Dimension(100, 20));
		niveau1Blanc.addActionListener(this);
		niveau2Blanc = new JRadioButton("Niveau 2");
		niveau2Blanc.setPreferredSize(new Dimension(100, 20));
		niveau2Blanc.addActionListener(this);
		niveau3Blanc = new JRadioButton("Niveau 3");
		niveau3Blanc.setPreferredSize(new Dimension(100, 20));
		niveau3Blanc.addActionListener(this);
		niveau1Noir = new JRadioButton("Niveau 1", true);
		niveau1Noir.setPreferredSize(new Dimension(100, 20));
		niveau1Noir.addActionListener(this);
		niveau2Noir = new JRadioButton("Niveau 2");
		niveau2Noir.setPreferredSize(new Dimension(100, 20));
		niveau2Noir.addActionListener(this);
		niveau3Noir = new JRadioButton("Niveau 3");
		niveau3Noir.setPreferredSize(new Dimension(100, 20));
		niveau3Noir.addActionListener(this);
		
		//Active config
		activeConfigBlanc = new JCheckBox("Modifier les valeurs d'evaluation", false);
		activeConfigBlanc.setHorizontalTextPosition(SwingConstants.LEFT);
		activeConfigBlanc.addActionListener(this);
		activeConfigNoir = new JCheckBox("Modifier les valeurs d'evaluation", false);
		activeConfigNoir.setHorizontalTextPosition(SwingConstants.LEFT);
		activeConfigNoir.addActionListener(this);
		
		//label pieces
		piecesBlanches = new JLabel("Valeur des pieces blanches");
		piecesNoires = new JLabel("Valeur des pieces noires");
		
		//Label des pieces
		pionBlanc = new JLabel("Pion");
		tourBlanc = new JLabel("Tour");
		cavalierBlanc = new JLabel("Cavalier");
		fouBlanc = new JLabel("Fou");
		reineBlanc = new JLabel("Reine");
		pionNoir = new JLabel("Pion");
		tourNoir = new JLabel("Tour");
		cavalierNoir = new JLabel("Cavalier");
		fouNoir = new JLabel("Fou");
		reineNoir = new JLabel("Reine");
		
		//textfield des pieces
		Dimension textFieldDimension = new Dimension(40, 20);
		Insets textInsets = new Insets(2,5,2,5);
		textPionBlanc = new JTextField();
		textPionBlanc.setPreferredSize(textFieldDimension);
		textPionBlanc.setMargin(textInsets);
		textTourBlanc = new JTextField();
		textTourBlanc.setPreferredSize(textFieldDimension);
		textTourBlanc.setMargin(textInsets);
		textCavalierBlanc = new JTextField();
		textCavalierBlanc.setPreferredSize(textFieldDimension);
		textCavalierBlanc.setMargin(textInsets);
		textFouBlanc = new JTextField();
		textFouBlanc.setPreferredSize(textFieldDimension);
		textFouBlanc.setMargin(textInsets);
		textReineBlanc = new JTextField();
		textReineBlanc.setPreferredSize(textFieldDimension);
		textReineBlanc.setMargin(textInsets);
		textPionNoir = new JTextField();
		textPionNoir.setPreferredSize(textFieldDimension);
		textPionNoir.setMargin(textInsets);
		textTourNoir = new JTextField();
		textTourNoir.setPreferredSize(textFieldDimension);
		textTourNoir.setMargin(textInsets);
		textCavalierNoir = new JTextField();
		textCavalierNoir.setPreferredSize(textFieldDimension);
		textCavalierNoir.setMargin(textInsets);
		textFouNoir = new JTextField();
		textFouNoir.setPreferredSize(textFieldDimension);
		textFouNoir.setMargin(textInsets);
		textReineNoir = new JTextField();
		textReineNoir.setPreferredSize(textFieldDimension);
		textReineNoir.setMargin(textInsets);
		
		//label des strategies
		strategieBlanc = new JLabel("Strategie des blancs");
		strategieNoir = new JLabel("Strategie des noirs");
		defenseBlanc = new JLabel("Defense");
		dangerBlanc = new JLabel("Danger");
		attaqueBlanc = new JLabel("Attaque");
		defenseNoir = new JLabel("Defense");
		dangerNoir = new JLabel("Danger");
		attaqueNoir = new JLabel("Attaque");
		
		//textfield des strategies
		textDefenseBlanc = new JTextField();
		textDefenseBlanc.setPreferredSize(textFieldDimension);
		textDefenseBlanc.setMargin(textInsets);
		textDangerBlanc = new JTextField();
		textDangerBlanc.setPreferredSize(textFieldDimension);
		textDangerBlanc.setMargin(textInsets);
		textAttaqueBlanc = new JTextField();
		textAttaqueBlanc.setPreferredSize(textFieldDimension);
		textAttaqueBlanc.setMargin(textInsets);
		textDefenseNoir = new JTextField();
		textDefenseNoir.setPreferredSize(textFieldDimension);
		textDefenseNoir.setMargin(textInsets);
		textDangerNoir = new JTextField();
		textDangerNoir.setPreferredSize(textFieldDimension);
		textDangerNoir.setMargin(textInsets);
		textAttaqueNoir = new JTextField();
		textAttaqueNoir.setPreferredSize(textFieldDimension);
		textAttaqueNoir.setMargin(textInsets);
		
		//Boutons
		reset = new JButton("Reset les valeurs");
		reset.addActionListener(this);
		valider = new JButton("Valider");
		valider.addActionListener(this);
		
		
		//-----------------------------------------------
		//Positionnement
		conteneurGeneral.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		int margeMilieu = 80;
		
		//postionnement choix niveaux
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 6;
		gbc.weightx = 1;
		gbc.insets = new Insets(10,10,10,margeMilieu);
		conteneurGeneral.add(choixNiveauxBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 10;
		conteneurGeneral.add(choixNiveauxNoir, gbc);
		
		//positionnement niveaux
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,0,0);
		conteneurGeneral.add(niveau1Blanc, gbc);
		gbc.gridx = 2;
		conteneurGeneral.add(niveau2Blanc, gbc);
		gbc.gridx = 4;
		gbc.insets.right = margeMilieu;
		conteneurGeneral.add(niveau3Blanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(niveau1Noir, gbc);
		gbc.gridx = 8;
		conteneurGeneral.add(niveau2Noir, gbc);
		gbc.gridx = 10;
		conteneurGeneral.add(niveau3Noir, gbc);
		
		//postitionnement active config
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 6;
		gbc.insets = new Insets(30, 0, 10, margeMilieu);
		gbc.anchor = GridBagConstraints.LINE_END;
		conteneurGeneral.add(activeConfigBlanc, gbc);
		gbc.insets.right = 0;
		gbc.gridx = 6;
		conteneurGeneral.add(activeConfigNoir, gbc);
		
		//postionnement label pieces
		gbc.gridy = 3;
		gbc.gridx = 0;
		gbc.insets = new Insets(0,0,10,margeMilieu);
		gbc.anchor = GridBagConstraints.CENTER;
		conteneurGeneral.add(piecesBlanches, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(piecesNoires, gbc);
		
		//positionnement label des pieces
		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0,0,0,0);
		conteneurGeneral.add(pionBlanc, gbc);
		gbc.gridx = 1;
		conteneurGeneral.add(tourBlanc, gbc);
		gbc.gridx = 2;
		conteneurGeneral.add(cavalierBlanc, gbc);
		gbc.gridx = 3;
		conteneurGeneral.add(fouBlanc, gbc);
		gbc.gridx = 4;
		gbc.insets.right = margeMilieu;
		conteneurGeneral.add(reineBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(pionNoir, gbc);
		gbc.gridx = 7;
		conteneurGeneral.add(tourNoir, gbc);
		gbc.gridx = 8;
		conteneurGeneral.add(cavalierNoir, gbc);
		gbc.gridx = 9;
		conteneurGeneral.add(fouNoir, gbc);
		gbc.gridx = 10;
		conteneurGeneral.add(reineNoir, gbc);
		
		//positionnement des textfield des pieces
		gbc.gridy = 5;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 20, 0);
		conteneurGeneral.add(textPionBlanc, gbc);
		gbc.gridx = 1;
		conteneurGeneral.add(textTourBlanc, gbc);
		gbc.gridx = 2;
		conteneurGeneral.add(textCavalierBlanc, gbc);
		gbc.gridx = 3;
		conteneurGeneral.add(textFouBlanc, gbc);
		gbc.gridx = 4;
		gbc.insets.right = margeMilieu;
		conteneurGeneral.add(textReineBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(textPionNoir, gbc);
		gbc.gridx = 7;
		conteneurGeneral.add(textTourNoir, gbc);
		gbc.gridx = 8;
		conteneurGeneral.add(textCavalierNoir, gbc);
		gbc.gridx = 9;
		conteneurGeneral.add(textFouNoir, gbc);
		gbc.gridx = 10;
		conteneurGeneral.add(textReineNoir, gbc);
		
		//positionnement label strategie
		gbc.gridy = 6;
		gbc.gridx = 0;
		gbc.gridwidth = 6;
		gbc.insets = new Insets(10, 0, 10, margeMilieu);
		conteneurGeneral.add(strategieBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(strategieNoir, gbc);
		
		
		//positionnement des lables des differentes strategies
		gbc.gridy = 7;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 0, 0);
		conteneurGeneral.add(defenseBlanc, gbc);
		gbc.gridx = 2;
		conteneurGeneral.add(attaqueBlanc, gbc);
		gbc.gridx = 4;
		gbc.insets.right = margeMilieu;
		conteneurGeneral.add(dangerBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(defenseNoir, gbc);
		gbc.gridx = 8;
		conteneurGeneral.add(attaqueNoir, gbc);
		gbc.gridx = 10;
		conteneurGeneral.add(dangerNoir, gbc);
		
		//positionnement textfield des strategies
		gbc.gridy = 8;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 30, 0);
		conteneurGeneral.add(textDefenseBlanc, gbc);
		gbc.gridx = 2;
		conteneurGeneral.add(textAttaqueBlanc, gbc);
		gbc.gridx = 4;
		gbc.insets.right = margeMilieu;
		conteneurGeneral.add(textDangerBlanc, gbc);
		gbc.gridx = 6;
		gbc.insets.right = 0;
		conteneurGeneral.add(textDefenseNoir, gbc);
		gbc.gridx = 8;
		conteneurGeneral.add(textAttaqueNoir, gbc);
		gbc.gridx = 10;
		conteneurGeneral.add(textDangerNoir, gbc);
		
		//positionnement des boutons
		gbc.gridy = 9;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,0,0);
		conteneurGeneral.add(reset, gbc);
		gbc.gridx = 2;
		gbc.gridwidth = 8;
		gbc.anchor = GridBagConstraints.CENTER;
		conteneurGeneral.add(valider, gbc);
		
		
		this.setContentPane(conteneurGeneral);
	}
	
	/**
	 * Active ou desactive les champs en fonction de quel niveau est selectionne
	 */
	private void checkConfig(){
		boolean etat = this.niveau3Blanc.isSelected();
		this.activeConfigBlanc.setEnabled(etat);
		if(!etat && this.activeConfigBlanc.isSelected()) this.activeConfigBlanc.setSelected(false);
		
		etat = this.niveau3Blanc.isSelected() && this.activeConfigBlanc.isSelected();
		this.piecesBlanches.setEnabled(etat);
		this.pionBlanc.setEnabled(etat);
		this.tourBlanc.setEnabled(etat);
		this.cavalierBlanc.setEnabled(etat);
		this.fouBlanc.setEnabled(etat);
		this.reineBlanc.setEnabled(etat);
		this.textPionBlanc.setEnabled(etat);
		this.textTourBlanc.setEnabled(etat);
		this.textCavalierBlanc.setEnabled(etat);
		this.textFouBlanc.setEnabled(etat);
		this.textReineBlanc.setEnabled(etat);
		this.strategieBlanc.setEnabled(etat);
		this.defenseBlanc.setEnabled(etat);
		this.attaqueBlanc.setEnabled(etat);
		this.dangerBlanc.setEnabled(etat);
		this.textDangerBlanc.setEnabled(etat);
		this.textDefenseBlanc.setEnabled(etat);
		this.textAttaqueBlanc.setEnabled(etat);
		
		etat = niveau3Noir.isSelected();
		this.activeConfigNoir.setEnabled(etat);
		if(!etat && this.activeConfigNoir.isSelected()) this.activeConfigNoir.setSelected(false);
		
		etat = this.niveau3Noir.isSelected() && this.activeConfigNoir.isSelected();
		this.piecesNoires.setEnabled(etat);
		this.pionNoir.setEnabled(etat);
		this.tourNoir.setEnabled(etat);
		this.cavalierNoir.setEnabled(etat);
		this.fouNoir.setEnabled(etat);
		this.reineNoir.setEnabled(etat);
		this.textPionNoir.setEnabled(etat);
		this.textTourNoir.setEnabled(etat);
		this.textCavalierNoir.setEnabled(etat);
		this.textFouNoir.setEnabled(etat);
		this.textReineNoir.setEnabled(etat);
		this.strategieNoir.setEnabled(etat);
		this.defenseNoir.setEnabled(etat);
		this.attaqueNoir.setEnabled(etat);
		this.dangerNoir.setEnabled(etat);
		this.textDangerNoir.setEnabled(etat);
		this.textDefenseNoir.setEnabled(etat);
		this.textAttaqueNoir.setEnabled(etat);
	}
	
	/**
	 * Met les champs de text au valeur de l'ia complet par defaut
	 */
	private void initValeurEvaluation(){
		ValeursEvaluation valeurs = new ValeursEvaluation();
		this.textPionBlanc.setText(valeurs.PION+"");
		this.textTourBlanc.setText(valeurs.TOUR+"");
		this.textCavalierBlanc.setText(valeurs.CAVALIER+"");
		this.textFouBlanc.setText(valeurs.FOU+"");
		this.textReineBlanc.setText(valeurs.REINE+"");
		this.textDangerBlanc.setText(valeurs.DANGER+"");
		this.textDefenseBlanc.setText(valeurs.DEFENSE+"");
		this.textAttaqueBlanc.setText(valeurs.ATTAQUE+"");
		
		this.textPionNoir.setText(valeurs.PION+"");
		this.textTourNoir.setText(valeurs.TOUR+"");
		this.textCavalierNoir.setText(valeurs.CAVALIER+"");
		this.textFouNoir.setText(valeurs.FOU+"");
		this.textReineNoir.setText(valeurs.REINE+"");
		this.textDangerNoir.setText(valeurs.DANGER+"");
		this.textDefenseNoir.setText(valeurs.DEFENSE+"");
		this.textAttaqueNoir.setText(valeurs.ATTAQUE+"");
	}
	
	/**
	 * Verifie que l'utilisateur a entree des valeurs correctes dans les differents champs
	 * @return resultat du test
	 */
	private boolean checkValeur(){
		char[] table = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		String[] valeurs = {this.textPionBlanc.getText(), 
				this.textTourBlanc.getText(), 
				this.textCavalierBlanc.getText(), 
				this.textFouBlanc.getText(), 
				this.textReineBlanc.getText(), 
				this.textDefenseBlanc.getText(), 
				this.textDangerBlanc.getText().substring(1), 
				this.textAttaqueBlanc.getText(),
				this.textPionNoir.getText(), 
				this.textTourNoir.getText(), 
				this.textCavalierNoir.getText(), 
				this.textFouNoir.getText(), 
				this.textReineNoir.getText(), 
				this.textDefenseNoir.getText(), 
				this.textDangerNoir.getText().substring(1), 
				this.textAttaqueNoir.getText()};
		
		for(int i = 0; i < valeurs.length; i++){
			for(int j = 0; j < valeurs[i].length(); j++){
				boolean faux = false;
				for(char c : table){
					if( c == valeurs[i].charAt(j)) faux = true;
				}
				if(!faux) return false;
			}
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == niveau1Blanc || source == niveau2Blanc || source == niveau3Blanc){
			niveau1Blanc.setSelected(false);
			niveau2Blanc.setSelected(false);
			niveau3Blanc.setSelected(false);
			JRadioButton radio = (JRadioButton)source;
			radio.setSelected(true);
		}
		
		if(source == niveau1Noir || source == niveau2Noir || source == niveau3Noir){
			niveau1Noir.setSelected(false);
			niveau2Noir.setSelected(false);
			niveau3Noir.setSelected(false);
			JRadioButton radio = (JRadioButton)source;
			radio.setSelected(true);
		}
		
		if(source == reset){
			initValeurEvaluation();
		}
		
		if(source == valider){
			if(niveau3Blanc.isSelected() || niveau3Noir.isSelected()){
				if(checkValeur()){
					ValeursEvaluation valeursBlanc = new ValeursEvaluation();
					valeursBlanc.PION = Integer.parseInt(this.textPionBlanc.getText());
					valeursBlanc.TOUR = Integer.parseInt(this.textTourBlanc.getText());
					valeursBlanc.CAVALIER = Integer.parseInt(this.textCavalierBlanc.getText());
					valeursBlanc.FOU = Integer.parseInt(this.textFouBlanc.getText());
					valeursBlanc.REINE = Integer.parseInt(this.textReineBlanc.getText());
					valeursBlanc.DANGER = Integer.parseInt(this.textDangerBlanc.getText());
					valeursBlanc.DEFENSE = Integer.parseInt(this.textDefenseBlanc.getText());
					valeursBlanc.ATTAQUE = Integer.parseInt(this.textAttaqueBlanc.getText());
					
					ValeursEvaluation valeursNoir = new ValeursEvaluation();
					valeursNoir.PION = Integer.parseInt(this.textPionNoir.getText());
					valeursNoir.TOUR = Integer.parseInt(this.textTourNoir.getText());
					valeursNoir.CAVALIER = Integer.parseInt(this.textCavalierNoir.getText());
					valeursNoir.FOU = Integer.parseInt(this.textFouNoir.getText());
					valeursNoir.REINE = Integer.parseInt(this.textReineNoir.getText());
					valeursNoir.DANGER = Integer.parseInt(this.textDangerNoir.getText());
					valeursNoir.DEFENSE = Integer.parseInt(this.textDefenseNoir.getText());
					valeursNoir.ATTAQUE = Integer.parseInt(this.textAttaqueNoir.getText());
					
					int niveauBlanc = 1;
					if(niveau2Blanc.isSelected()) niveauBlanc = 2;
					else if(niveau3Blanc.isSelected()) niveauBlanc = 3;
					
					int niveauNoir = 1;
					if(niveau2Noir.isSelected()) niveauNoir = 2;
					else if(niveau3Noir.isSelected()) niveauNoir = 3;
					
					new Fenetre(this.getX(), this.getY(), niveauBlanc, niveauNoir, valeursBlanc, valeursNoir);
					this.setVisible(false);
					this.dispose();
					this.menu.setVisible(false);
					this.menu.dispose();
				}else{
					JOptionPane.showMessageDialog(null, "Une valeur n'est pas un nombre entier", "Erreur", JOptionPane.INFORMATION_MESSAGE);
				}
			}else{
				int niveauBlanc = 1;
				if(niveau2Blanc.isSelected()) niveauBlanc = 2;
				else if(niveau3Blanc.isSelected()) niveauBlanc = 3;
				
				int niveauNoir = 1;
				if(niveau2Noir.isSelected()) niveauNoir = 2;
				else if(niveau3Noir.isSelected()) niveauNoir = 3;
				
				new Fenetre(this.getX(), this.getY(), niveauBlanc, niveauNoir, null, null);
				this.setVisible(false);
				this.dispose();
				this.menu.setVisible(false);
				this.menu.dispose();
			}
		}
		
		checkConfig();
		
	}

}
