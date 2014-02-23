package echecs.graphisme;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import echecs.graphisme.jeu.Fenetre;

/**
 * Selection du mode de jeu : normal ou blitz
 */
public class SelectionMode extends JFrame implements ActionListener{
	
	/**
	 * Bouton du mode de jeu normal
	 */
	private JButton bnormal;
	
	/**
	 * Bouton du mode de jeu Blitz
	 */
	private JButton bblitz;
	
	/**
	 * Reference du menu
	 */
	private Menu menu;
	
	/**
	 * Constructeur
	 * @param x position en x de la fenetre
	 * @param y position en y de la fenetre
	 * @param parent reference du menu
	 */
	public SelectionMode(int x, int y, Menu menu){
		this.menu = menu;
		JPanel pan = new JPanel(new GridLayout(1,2,1,1));
		this.setSize(300,200);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		bnormal = new JButton("Mode Normal");
		bnormal.addActionListener(this);
		bblitz = new JButton("Mode Blitz");
		bblitz.addActionListener(this);
		this.setContentPane(pan);
		pan.add(bnormal);
		pan.add(bblitz);
		this.setLocation(x - this.getWidth()/2, y - this.getHeight()/2);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == bnormal){
			menu.setVisible(false);
			menu.dispose();
			this.setVisible(false);
			this.dispose();
			new Fenetre(this.getX() + this.getWidth(), this.getY() + this.getHeight());
		}
		
		if(source == bblitz){
			this.setVisible(false);
			this.dispose();
			new SelectionDureeBlitz(menu, this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
		}
	}
}
