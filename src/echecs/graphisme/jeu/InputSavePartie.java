package echecs.graphisme.jeu;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import echecs.Echecs;
import echecs.graphisme.Menu;
import echecs.jeu.Jeu;
import echecs.sauvegarde.Partie;
import echecs.sauvegarde.Sauvegarde;

/**
 * Fenetre du formulaire de creation de sauvegarde de partie terminee au format PGN
 */
public class InputSavePartie extends JFrame implements ActionListener{
	
	/**
	 * content panel de la fenetre
	 */
	private JPanel conteneurGeneral;
	
	/**
	 * conteneur du nom du fichier
	 */
	private JPanel conteneurNomFichier;
	
	/**
	 * conteneur des meta donnee de la partie
	 */
	private JPanel conteneurMetaDonnee;
	
	/**
	 * conteneur des boutons de validation
	 */
	private JPanel conteneurBoutons;
	
	/**
	 * Reference de la fenetre de jeu
	 */
	private Fenetre fenetre;
	
	/**
	 * Bouton creation sauvegarde
	 */
	private JButton creerSave;
	
	/**
	 * Bouton quitter
	 */
	private JButton quitter;
	
	/**
	 * informations de la partie
	 */
	private Partie partie;
	
	/**
	 * label event
	 */
	private JLabel labelEvent;
	
	/**
	 * Label site
	 */
	private JLabel labelSite;
	
	/**
	 * Label date
	 */
	private JLabel labelDate;
	
	/**
	 * Label round
	 */
	private JLabel labelRound;
	
	/**
	 * Label joueur blanc
	 */
	private JLabel labelBlanc;
	
	/**
	 * Label joueur noir
	 */
	private JLabel labelNoir;
	
	/**
	 * Label nom du fichier
	 */
	private JLabel labelNomFichier;
	
	/**
	 * Champs de texte du nom de fichier
	 */
	private JTextField textNomFichier;
	
	/**
	 * Champs de texte de l'event
	 */
	private JTextField textEvent;
	
	/**
	 * Champs de texte du site
	 */
	private JTextField textSite;
	
	/**
	 * Champs de texte de la date
	 */
	private JFormattedTextField textDate;
	
	/**
	 * Champs de texte du round
	 */
	private JTextField textRound;
	
	/**
	 * Champs de texte du joueur blanc
	 */
	private JTextField textBlanc;
	
	/**
	 * Champs de texte du joueur noir
	 */
	private JTextField textNoir;
	
	/**
	 * Resultat de la partie
	 */
	private String resultat;
	
	/**
	 * Constructeur
	 * @param jeu reference du jeu
	 * @param fenetre reference de la fenetre de jeu
	 */
	public InputSavePartie(Fenetre fenetre, String resultat){
		super("Sauvegarde la partie");
		this.fenetre = fenetre;
		this.resultat = resultat;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(fenetre);
		this.setSize(400, 400);
		this.setResizable(false);
		initFenetre();
		this.setVisible(true);
		this.setIconImage(Echecs.ICON);
		this.partie = new Partie();
	}
	
	/**
	 * Instancie et positionne les elements de la fenetre
	 */
	private void initFenetre(){
		//conteneurs
		conteneurGeneral = new JPanel();
		conteneurMetaDonnee = new JPanel();
		conteneurMetaDonnee.setPreferredSize(new Dimension(450, 280));
		conteneurNomFichier = new JPanel();
		conteneurNomFichier.setPreferredSize(new Dimension(450, 50));
		conteneurBoutons = new JPanel();
		conteneurBoutons.setPreferredSize(new Dimension(450, 50));
		
		//Les labels
		labelEvent = new JLabel("Event :", JLabel.RIGHT);
		labelEvent.setPreferredSize(new Dimension(60, 30));
		labelSite = new JLabel("Site :", JLabel.RIGHT);
		labelSite.setPreferredSize(new Dimension(60, 30));
		labelDate = new JLabel("Date :", JLabel.RIGHT);
		labelDate.setPreferredSize(new Dimension(60, 30));
		labelRound = new JLabel("Round :", JLabel.RIGHT);
		labelRound.setPreferredSize(new Dimension(60, 30));
		labelBlanc = new JLabel("Blanc :", JLabel.RIGHT);
		labelBlanc.setPreferredSize(new Dimension(60, 30));
		labelNoir = new JLabel("Noir :", JLabel.RIGHT);
		labelNoir.setPreferredSize(new Dimension(60, 30));
		labelNomFichier = new JLabel("Nom du fichier :");
		labelNomFichier.setPreferredSize(new Dimension(90, 30));
		
		//les textField;
		textEvent = new JTextField();
		textEvent.setMargin(new Insets(2,5,2,5));
		textEvent.setPreferredSize(new Dimension(260, 30));
		textSite = new JTextField();
		textSite.setMargin(new Insets(2,5,2,5));
		textSite.setPreferredSize(new Dimension(260, 30));
		try {
			textDate = new JFormattedTextField(new MaskFormatter("####.##.##"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		textDate.setText("20130130");
		textDate.setMargin(new Insets(2,5,2,5));
		textDate.setPreferredSize(new Dimension(90, 30));
		textRound = new JTextField();
		textRound.setMargin(new Insets(2,5,2,5));
		textRound.setPreferredSize(new Dimension(260, 30));
		textBlanc = new JTextField();
		textBlanc.setMargin(new Insets(2,5,2,5));
		textBlanc.setPreferredSize(new Dimension(260, 30));
		textNoir = new JTextField();
		textNoir.setMargin(new Insets(2,5,2,5));
		textNoir.setPreferredSize(new Dimension(260, 30));
		textNomFichier = new JTextField();
		textNomFichier.setMargin(new Insets(2,5,2,5));
		textNomFichier.setPreferredSize(new Dimension(200, 30));
		
		//boutons
		creerSave = new JButton("Creer la sauvegarde");
		creerSave.addActionListener(this);
		quitter = new JButton("Quitter sans sauvegarder");
		quitter.addActionListener(this);
		
		
		//Positionnement nom du fichier
		conteneurNomFichier.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 10, 0, 10);
		conteneurNomFichier.add(labelNomFichier, gbc);
		gbc.gridx = 1;
		conteneurNomFichier.add(textNomFichier, gbc);
		
		//Positionnement metadonnee
		conteneurMetaDonnee.setLayout(new GridBagLayout());
		conteneurMetaDonnee.setBorder(BorderFactory.createTitledBorder("Information sur la partie"));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5,5,5,5);
		conteneurMetaDonnee.add(labelEvent, gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textEvent, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		conteneurMetaDonnee.add(labelSite, gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textSite, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		conteneurMetaDonnee.add(labelDate, gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textDate, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		conteneurMetaDonnee.add(labelRound, gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textRound, gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		conteneurMetaDonnee.add(labelBlanc, gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textBlanc, gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		conteneurMetaDonnee.add(labelNoir, gbc);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		conteneurMetaDonnee.add(textNoir, gbc);
		
		//Positionnement des boutons
		conteneurBoutons.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.NONE;
		conteneurBoutons.add(quitter, gbc);
		gbc.gridx = 1;
		conteneurBoutons.add(creerSave, gbc);
		
		
		//Postionnement general
		conteneurGeneral.setLayout(new BoxLayout(conteneurGeneral, BoxLayout.PAGE_AXIS));
		conteneurGeneral.add(conteneurNomFichier);
		conteneurGeneral.add(conteneurMetaDonnee);
		conteneurGeneral.add(conteneurBoutons);
		
		
		this.setContentPane(conteneurGeneral);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == quitter){
			this.setVisible(false);
			if(fenetre.getJeu().isVsInternet()){
				fenetre.setVisible(false);
				fenetre.dispose();
				this.dispose();
				new Menu();
				return;
			}
			int retourQuestion = JOptionPane.showConfirmDialog(null, "Partie terminee,  voulez-vous rejouer ?", "Partie terminee", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(retourQuestion == 0){
				fenetre.getJeu().reset();
			}else{
				fenetre.setVisible(false);
				fenetre.dispose();
				new Menu();
			}
			this.dispose();
		}
		
		if(source == creerSave){
			partie.event = textEvent.getText();
			partie.site = textSite.getText();
			partie.date = textDate.getText();
			partie.round = textRound.getText();
			partie.white = textBlanc.getText();
			partie.black = textNoir.getText();
			partie.coups = fenetre.getJeu().getHistorique();
			partie.result = this.resultat;
			if(Sauvegarde.creerSauvegardePGN(partie, textNomFichier.getText())){
				this.setVisible(false);
				if(fenetre.getJeu().isVsInternet()){
					fenetre.setVisible(false);
					fenetre.dispose();
					this.dispose();
					new Menu();
					return;
				}
				int retourQuestion = JOptionPane.showConfirmDialog(null, "Partie terminee,  voulez-vous rejouer ?", "Partie terminee", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if(retourQuestion == 0){
					fenetre.getJeu().reset();
				}else{
					fenetre.setVisible(false);
					fenetre.dispose();
					new Menu();
				}
				this.dispose();
			}
		}
		
	}

}
