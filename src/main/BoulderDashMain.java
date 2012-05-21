package main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import factories.*;

import parser.TerrainParser;
import services.MoteurJeuService;


public class BoulderDashMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private final int TAILLE_CASE = 16;

	public BoulderDashMain() throws IOException {
		Factory factory = Factory.getFactory();
		JFrame mainWindow = new JFrame();
		final JFileChooser fc = new JFileChooser();
		String repCourant = System.getProperty("user.dir" );
		fc.setDialogTitle("Ouvrir fichier de niveau");
		fc.setCurrentDirectory(new File(repCourant + File.separator +"niveaux"));
		int returnVal = fc.showOpenDialog(BoulderDashMain.this);
		

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fichierNiveau = fc.getSelectedFile().getAbsoluteFile();
			System.out.println(fichierNiveau.getName());
			MoteurJeuService mj = TerrainParser.depuisFichier(fichierNiveau.toString(),factory); 
			int largeur_fenetre = mj.getTerrain().getLargeur()*TAILLE_CASE;
			int hauteur_fenetre = (mj.getTerrain().getHauteur()+2)*TAILLE_CASE +50;

			mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			mainWindow.setSize(largeur_fenetre, hauteur_fenetre);
			mainWindow.setBackground(Color.BLACK);
			mainWindow.setLocationRelativeTo(null);
			mainWindow.setTitle("Boulder Dash");

			setResizable(false);
			mainWindow.pack();
			mainWindow.setVisible(true);
			mainWindow.add(new Board(mainWindow,mj,largeur_fenetre,hauteur_fenetre));
		}
	}

	public static void main(String[] args) throws IOException{
		if (args.length > 0 && Arrays.asList(args).contains("--with-contracts")) {
			System.out.println("withContracts: true");
			Factory.createFactoryWithContracts();
		} else {
			System.out.println("withContracts: false");
			Factory.createFactory();
		}
		new BoulderDashMain();
	}

}
