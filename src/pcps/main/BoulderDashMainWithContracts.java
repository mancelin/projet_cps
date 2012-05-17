package pcps.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import pcps.factories.*;
import pcps.parser.TerrainParser;
import pcps.services.MoteurJeuService;


public class BoulderDashMainWithContracts extends JFrame {

	private static final long serialVersionUID = 1L;
	private final int TAILLE_CASE = 16;

	public BoulderDashMainWithContracts() throws IOException {
		Factory.createFactoryWithContracts();
		Factory factory = Factory.getFactory();

		final JFileChooser fc = new JFileChooser();
		String repCourant = System.getProperty("user.dir" );
		fc.setDialogTitle("Ouvrir fichier de niveau");
		fc.setCurrentDirectory(new File(repCourant + File.separator +"niveaux"));
		int returnVal = fc.showOpenDialog(BoulderDashMainWithContracts.this);
		

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichierNiveau = fc.getSelectedFile().getAbsoluteFile();
			System.out.println(fichierNiveau.getName());
			MoteurJeuService mj = TerrainParser.depuisFichier(fichierNiveau.toString(),factory); 
			int largeur_fenetre = mj.getTerrain().getLargeur()*TAILLE_CASE;
			int hauteur_fenetre = (mj.getTerrain().getHauteur()+2)*TAILLE_CASE;
			if(largeur_fenetre < 339) largeur_fenetre = 339;
			if(hauteur_fenetre < 153) hauteur_fenetre = 153;
			add(new Board(mj,largeur_fenetre,hauteur_fenetre));

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			setSize(largeur_fenetre, hauteur_fenetre );
			setBackground(Color.BLACK);
			setLocationRelativeTo(null);
			setTitle("Boulder Dash");

			setResizable(false);
			setVisible(true);
		}
	}

	public static void main(String[] args) throws IOException{
		new BoulderDashMainWithContracts();

	}

}
