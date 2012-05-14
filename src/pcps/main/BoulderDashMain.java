package pcps.main;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import pcps.factories.*;
import pcps.parser.TerrainParser;
import pcps.services.MoteurJeuService;


public class BoulderDashMain extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int TAILLE_CASE = 16;
	
	public BoulderDashMain(String fichierNiveau) throws IOException {
		Factory.createFactory();
		Factory factory = Factory.getFactory();
		
		
    	// Factory.createFactoryWithContracts(); // a décommenter quand on veut tester avec les contrats
    	
    	MoteurJeuService mj = TerrainParser.depuisFichier(fichierNiveau,factory); 
    	//		MoteurJeuFactory.depuisFichier(fichierNiveau);
        add(new Board(mj));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(mj.getTerrain().getLargeur()*TAILLE_CASE, (mj.getTerrain().getHauteur()+1)*TAILLE_CASE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setTitle("Boulder Dash");

        setResizable(false);
        setVisible(true);
     //   System.out.print(mj.toString());
    }

    public static void main(String[] args) throws IOException{
        new BoulderDashMain("t_file");
    }

}
