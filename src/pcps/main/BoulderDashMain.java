package pcps.main;

import java.io.IOException;

import javax.swing.JFrame;

import pcps.factories.*;
import pcps.services.MoteurJeuService;


public class BoulderDashMain extends JFrame {

    public BoulderDashMain(String fichierNiveau) throws IOException {
    	IFactory factory = new Factory();
    	// IFactory factory = new FactoryWithContracts(); // a décommenter quand on veut tester avec les contrats
    	
    	MoteurJeuService mj = MoteurJeuFactory.depuisFichier(fichierNiveau);
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
