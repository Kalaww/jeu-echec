package echecs.graphisme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Fenetre A propos
 *
 */
public class About extends JDialog {
	
	JPanel pan;
	
	/**
	 * Constructeur de la classe About
	 */
	public About(){
		super();
		this.setTitle("A propos");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pan = new JPanel();
		pan.setLayout(new BorderLayout());
		this.setSize(new Dimension(300,400));
		this.setLocationRelativeTo(null);
		this.setContentPane(pan);
		initFenetre();
		this.setVisible(true);
	}
	
	/**
	 * Construction de la fen�tre
	 */
	public void initFenetre(){
		String text = "<html>" +
				"<h1>Jeu d'Echecs</h1>" +
				"<br /><i>par<br /> " +
				"</i>Cedric DESGRANGES <br />" +
				" Arthur DIAN <br />" +
				" Julien DUARTE <br />" +
				" Thomas HAUTIER <br /><br /><br />" +
				"<i>IK3 - 2013-2014</i>" +
				"</html> ";

		JLabel credit = new JLabel(text);

		credit.setFont(new Font("Dialog", Font.PLAIN,20));
		credit.setHorizontalAlignment(JLabel.CENTER);
		pan.add(credit, BorderLayout.NORTH);
		
	}
}
