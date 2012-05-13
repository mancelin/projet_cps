package pcps.main;

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

	public BoulderDashMain(String fichierNiveau) throws IOException {
    	IFactory factory = new Factory();
    	// IFactory factory = new FactoryWithContracts(); // a décommenter quand on veut tester avec les contrats
    	
    	MoteurJeuService mj = TerrainParser.depuisFichier(fichierNiveau, factory); 
    	//		MoteurJeuFactory.depuisFichier(fichierNiveau);
        add(new Board(mj));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(mj.getTerrain().getLargeur()*30, mj.getTerrain().getHauteur()*30);
        setLocationRelativeTo(null);
        setTitle("Boulder Dash");

        setResizable(false);
        setVisible(true);
        System.out.print(mj.toString());
    }

    public static void main(String[] args) throws IOException{
        new BoulderDashMain("t_file");
    }

}
