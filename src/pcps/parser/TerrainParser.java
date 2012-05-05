package pcps.parser;

import java.io.*;

import pcps.components.MoteurJeu;
import pcps.components.Terrain;
import pcps.services.MoteurJeuService;
import pcps.services.TerrainService;

public class TerrainParser {

	public static void lireFichier()

	{
		String fichier = "";
		String ligne = "";

		try
		{
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Fichier de niveau : ");
			fichier = clavier.readLine();

			// try {
			BufferedReader ficTexte = new BufferedReader(new FileReader(new File(fichier)));
			if (ficTexte == null) {
				throw new FileNotFoundException("Fichier non trouvé: " + fichier);
			}
			//lecture ligne entéte fichier	
			
			/* dgb
			
			ligne = ficTexte.readLine();
			String tab[] =ligne.split(" ");
			
			
			System.out.print(">>>>\n");
			int sum=0;
			for(int i=0;i<tab.length;i++){
				System.out.println("tab["+i+"] : " + tab[i]);
				sum+=Integer.parseInt(tab[i]);
			}
			System.out.println("sum : " + sum);
			*/
			do {
				ligne = ficTexte.readLine();
				if (ligne != null) {
					System.out.println(ligne + " => " + ligne.length());
				}
				
			} while (ficTexte != null);
			ficTexte.close();
			System.out.println("\n");
		}

		catch (FileNotFoundException e) {

			System.out.println(e.getMessage());
		}

		catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}
	
	public static MoteurJeuService terrainDeFichier(){
		String fichier = "";
		String ligne = "";
		
		MoteurJeuService mj = new MoteurJeu();
		TerrainService t = new Terrain();

		try
		{
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Fichier de niveau : ");
			fichier = clavier.readLine();

			// try {
			BufferedReader ficTexte = new BufferedReader(new FileReader(new File(fichier)));
			if (ficTexte == null)
				throw new FileNotFoundException("Fichier non trouvé: " + fichier);
			
			ligne = ficTexte.readLine();
			String tabEntete[] =ligne.split(" ");
			
			int largeur = Integer.parseInt(tabEntete[0]);
			int hauteur = Integer.parseInt(tabEntete[1]);
			int nbPas = Integer.parseInt(tabEntete[2]);
			
			t.init(largeur, hauteur);
	
			do {
				ligne = ficTexte.readLine();
				if (ligne != null) {
					System.out.println(ligne + " => " + ligne.length());
				}
				
			} while (ficTexte != null);
			ficTexte.close();
			System.out.println("\n");
		}

		catch (FileNotFoundException e) {

			System.out.println(e.getMessage());
		}

		catch (IOException e) {

			System.out.println(e.getMessage());
		}
		return mj;
	}
	
	

	public static void main (String[] args)

	{
	//	lireFichier();
		terrainDeFichier();
	//	System.exit(0);
	}

}
