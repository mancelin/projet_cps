package pcps.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import pcps.components.MoteurJeu;
import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.MoteurJeuService;
import pcps.services.PositionService;
import pcps.services.TerrainService;


public class MoteurJeuFactory {
	public static MoteurJeuService depuisFichier(String fichier) throws IOException {
		String ligne = "";
		BufferedReader ficTexte = null;
		int largeur;
		int hauteur;
		int nbPas=0;
		
		MoteurJeuService mj = new MoteurJeu();
		TerrainService t;// = new Terrain();


		try
		{
			ficTexte = new BufferedReader(new FileReader(new File(fichier)));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture");
		}
		ligne = ficTexte.readLine();
		String tabEntete[] =ligne.split(" ");

		largeur = Integer.parseInt(tabEntete[0]);
		hauteur = Integer.parseInt(tabEntete[1]);
		nbPas = Integer.parseInt(tabEntete[2]);
	//	System.out.println("nbPas : "+nbPas);
		t = TerrainFactory.create(largeur, hauteur);
		int y = 0;
		while ((ligne = ficTexte.readLine()) != null){
			System.out.printf("largeur : %d, hauteur : %d\n", largeur, hauteur);
			
			for(int i=0;i<ligne.length();i++){
				System.out.print(ligne.charAt(i)); 
			
				PositionService pos = PositionFactory.create(largeur, hauteur, i, y);//
				BlocService bloc = BlocFactory.create(typeBlocDeChar(ligne.charAt(i)), pos);//
				System.out.printf(">> pos.getX : %d pos.getY : %d \n", pos.getX(),pos.getY());
				t.setBloc(bloc.getType(), pos.getX(), pos.getY());
				
				//t.setBloc(typeBlocDeChar(ligne.charAt(i)), i+1, y);
				
				//System.out.printf("pos.getX : %d pos.getY : %d \n", pos.getX(),pos.getY());
				//System.out.printf("in the for x=%d y=%d \n", i+1,y);
			}
			y++;
		}
		ficTexte.close();
		//System.out.printf("mj.init(t, nbPas=%d)\n",nbPas);
		mj.init(t, nbPas);
		return mj;
	}

	
	
	public static TypeBloc typeBlocDeChar(char c){
		switch(c){
			case '#' : return TypeBloc.MUR;
			case 'x' : return TypeBloc.HERO;
			case 'O' : return TypeBloc.ROCHER;
			case 'Y' : return TypeBloc.DIAMANT;
			case '-' : return TypeBloc.TERRE;
			case '.' : return TypeBloc.VIDE;
			case '?' : return TypeBloc.SORTIE_FERMEE;
			default : throw new IllegalArgumentException("Aucun type de bloc ne correspond au caractére "+c);
		}
	}
}
