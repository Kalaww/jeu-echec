package echecs.graphisme;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import echecs.Echecs;
import echecs.reseau.client.Client;
import echecs.reseau.serveur.Server;

/**
 * Fenetre de configuration du mode en ligne
 */
public class EcranEnLigne extends JFrame implements ActionListener{
	/**
	 * Reference du content panel de la fenetre
	 */
	private JPanel pan;
	
	/**
	 * Label du bouton rejoindre
	 */
	private JLabel rej;
	
	/**
	 * Champs de texte de l'adresse IP de la partie
	 */
	private JTextField ip_field;
	
	/**
	 * Bouton rejoindre serveur et creer serveur
	 */
	private JButton rejb, creerb;
	
	/**
	 * Reference du menu
	 */
	private Menu menu;
	
	/**
	 * Constructeur
	 * @param x position en x de la fenetre
	 * @param y position en y de la fenetre
	 * @param menu reference du menu
	 */
	public EcranEnLigne(int x, int y, Menu menu){
		super();
		this.setSize(400, 200);
		this.menu = menu;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initFenetre();
		this.setIconImage(Echecs.ICON);
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.setVisible(true);
	}
	
	/**
	 * Initialise les variables et positionne les elements sur la fenetre
	 */
	public void initFenetre(){
		//initialisation des variables
		pan = new JPanel();
		ip_field = new JTextField();
		ip_field.setText("127.0.0.1");
		ip_field.setMargin(new Insets(2,5,2,5));
		ip_field.setPreferredSize(new Dimension(140, 25));
		rejb = new JButton("Rejoindre");
		rejb.addActionListener(this);
		creerb = new JButton("Creer un Serveur");
		creerb.addActionListener(this);
		rej = new JLabel("REJOINDRE UNE PARTIE EN LIGNE");
		
		//Positionnement
		pan.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//positionnement
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(10, 10, 10, 10);
		pan.add(rej, gbc);
		
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		pan.add(ip_field, gbc);
		
		gbc.gridx = 1;
		pan.add(rejb, gbc);
		
		gbc.gridy = 2;
		pan.add(creerb, gbc);
		
		this.setContentPane(pan);
	}
	
	@Override
	public void actionPerformed(ActionEvent a) {
		Object e = a.getSource();
		if(e == rejb){
			if(ip_field.getText() != null && !ip_field.getText().equals("L'IP de la partie")){
				String adresse = ip_field.getText();
				new Client(adresse);
				this.setVisible(false);
				this.dispose();
				this.menu.setVisible(false);
				this.menu.dispose();
			}
		}
		if(e == creerb){
			new Server();
		}
	}
}
