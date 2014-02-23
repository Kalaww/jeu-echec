package echecs.graphisme;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import echecs.graphisme.jeu.Fenetre;

/**
 * Fenetre de selection de la duree du mode Blitz
 */
public class SelectionDureeBlitz extends JFrame implements ActionListener {
	
	/**
	 * Reference du content panel de la fenetre
	 */
	private JPanel pan;
	
	/**
	 * Menu deroulant des minutes
	 */
	private JComboBox m;
	
	/**
	 * Menu deroulant des secondes
	 */
	private JComboBox s;
	
	/**
	 * Bouton de lancement de la partie
	 */
	private JButton button;
	
	/**
	 * Label
	 */
	private JLabel lab;
	
	/**
	 * Label des minutes
	 */
	private JLabel labminute;
	
	/**
	 * Label des secondes
	 */
	private JLabel labseconde;
	
	/**
	 * Reference du menu
	 */
	private Menu menu;
	
	/**
	 * Constructeur
	 */
	public SelectionDureeBlitz(Menu menu, int x, int y){
		button = new JButton("Ok");
		button.addActionListener(this);
		this.menu = menu;
		JPanel pan = new JPanel(new GridLayout(6,5));
		labminute = new JLabel("Minutes:");
		labseconde = new JLabel("Secondes:");
		lab = new JLabel("Veuillez choisir la duree de la partie");
		pan.add(lab);
		pan.add(labminute);
	 	m = new JComboBox();
	 	for(int i=5;i<=60;i+=5){
	 		m.addItem(i);
	 	}
	 	s = new JComboBox();
	 	for(int i=0;i<=59;i+=5){
	 		s.addItem(i);
	 	}
	 	pan.add(m);
	 	pan.add(labseconde);
	 	pan.add(s);
	 	pan.add(button);
		this.setSize(300,200);
		this.setContentPane(pan);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == button){
			this.setVisible(false);
			this.dispose();
			this.menu.setVisible(false);
			this.menu.dispose();
			new Fenetre(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2, true, (Integer)m.getSelectedItem(), (Integer)s.getSelectedItem());
		}
		
	}

}
