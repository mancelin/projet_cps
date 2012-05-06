package pcps.parser;

import java.io.*;
import java.lang.reflect.Type;

import pcps.components.MoteurJeu;
import pcps.components.Terrain;
import pcps.enums.TypeBloc;
import pcps.services.MoteurJeuService;
import pcps.services.TerrainService;

public class TerrainParser {

	public static void lire(String fichier) throws IOException //throws IOException
	{
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		try
		{
			lecteurAvecBuffer = new BufferedReader(new FileReader(new File(fichier)));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture");
		}
		while ((ligne = lecteurAvecBuffer.readLine()) != null)
			System.out.println(ligne);
		lecteurAvecBuffer.close();
	}

	public static void lireFichier(String fichier) throws IOException

	{
		//String fichier = "";
		String ligne = "";
		BufferedReader ficTexte = null;

		try
		{
			ficTexte = new BufferedReader(new FileReader(new File(fichier)));
		}
		catch(FileNotFoundException exc)
		{
			System.out.println("Erreur d'ouverture");
		}
		while ((ligne = ficTexte.readLine()) != null)
			System.out.println(ligne + " => " + ligne.length());
		ficTexte.close();
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

	public static MoteurJeuService terrainDeFichier(String fichier) throws IOException

	{
		//String fichier = "";
		String ligne = "";
		BufferedReader ficTexte = null;
		int largeur;
		int hauteur;
		int nbPas=0;
		
		MoteurJeuService mj = new MoteurJeu();
		TerrainService t = new Terrain();


		
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
		t.init(largeur, hauteur);
		int y = 0;
		while ((ligne = ficTexte.readLine()) != null){
			y++;
			for(int i=0;i<ligne.length();i++){
				//	x++;
				System.out.print(ligne.charAt(i)); 
				t.setBloc(typeBlocDeChar(ligne.charAt(i)), i+1, y);
				System.out.printf("in the for x=%d y=%d \n", i+1,y);
			}
		}
		ficTexte.close();
		mj.init(t, nbPas);
		return mj;
	}
		



	public static void main (String[] args)

	{
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Fichier de niveau : ");
		String fichier;
		try {
			fichier = clavier.readLine();
			MoteurJeuService mj = terrainDeFichier(fichier);
			System.out.println("Fin");
			System.out.println(mj.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		lireFichier();
		//	MoteurJeuService mj = terrainDeFichier();
		//	System.out.println(">>>>");
		//	System.out.println(mj.toString());
			System.exit(0);
	}

}
