package pcps.parser;

import java.io.*;

import pcps.enums.TypeBloc;
import pcps.factories.Factory;
import pcps.services.BlocService;
import pcps.services.MoteurJeuService;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class TerrainParser {

	public static TypeBloc typeBlocDeChar(char c){
		switch(c){
			case '#' : return TypeBloc.MUR;
			case 'x' : return TypeBloc.HERO;
			case 'O' : return TypeBloc.ROCHER;
			case 'Y' : return TypeBloc.DIAMANT;
			case '-' : return TypeBloc.TERRE;
			case '.' : return TypeBloc.VIDE;
			case '?' : return TypeBloc.SORTIE_FERMEE;
			case '!' : return TypeBloc.SORTIE_OUVERTE;
			default : throw new IllegalArgumentException("Aucun type de bloc ne correspond au caractére "+c);
		}
	}
	
	public static char charDeTypeBloc(TypeBloc tb){
		switch(tb){
			case MUR : return '#';
			case HERO : return 'x';
			case ROCHER : return 'O';
			case DIAMANT : return 'Y';
			case TERRE : return '-';
			case VIDE : return '.';
			case SORTIE_FERMEE : return '?';
			case SORTIE_OUVERTE : return '!';
			default : throw new IllegalArgumentException("Aucun caractére ne correspond au type de bloc  "+tb);
		}
	}
	
		
	public static MoteurJeuService depuisFichier(String fichier, Factory factory) throws IOException {
		String ligne = "";
		BufferedReader ficTexte = null;
		int largeur;
		int hauteur;
		int nbPas=0;
		
		MoteurJeuService mj = factory.creerMoteurJeu();
		TerrainService t = factory.creerTerrain();


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
		System.out.println("nbPas : "+nbPas);
		t.init(largeur, hauteur);
		int y = 0;
		while ((ligne = ficTexte.readLine()) != null){
			System.out.printf("largeur : %d, hauteur : %d\n", largeur, hauteur);
			
			for(int i=0;i<ligne.length();i++){
			//	System.out.print(ligne.charAt(i)); 
			
				PositionService pos = factory.creerPosition(); 
				pos.init(largeur, hauteur, i, y);
				BlocService bloc = factory.creerBloc();
				bloc.init(typeBlocDeChar(ligne.charAt(i)), pos);
			//	System.out.printf(">> pos.getX : %d pos.getY : %d \n", pos.getX(),pos.getY());
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
	
}
