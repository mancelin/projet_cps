package pcps.services;

public interface MoteurJeuService {
	/** Observateur: terrain associé au moteur de jeu */
	public TerrainService getTerrain();
	
	/** Observateur: nombre de pas restants pour que la partie soit terminée ( et perdue ) */
	public int getPasRestants();
	
	/** Observateur: déplacement du hero possible dans la direction "dir" ? */
	public boolean isDeplacementHeroPossible(Direction dir);
	
	/** Observateur: la partie est terminée ? */
	public boolean isPartieTerminee();
	
	/** Observateur: la partie est gagnée ? */
	public boolean isPartieGagnee();
	
	// inv: isPartieTerminee() == 
	//  		getPasRestants() = 0
	//			V ¬Terrain::isHeroVivant(getTerrain())
	//			V Terrain::getPosSortie(getTerrain()) = Terrain::getPosHero(getTerrain())
	// inv: isPartieGagnee() == isPartieTerminee() ^ Terrain::isHeroVivant(getTerrain()) 
	// inv: isDeplacementHeroPossible(dir) ==
	// 			let* terrain = getTerrain()
	//			and blocHero = Terrain::getBlocHero()
	//			and blocDest = Terrain::getBlocVersDirection(blocHero, dir)
	//			in
	//				¬Bloc::isSolide()
	//				V (Bloc::isDeplacable() ^ Bloc::isVide(Terrain::getBlocVersDirection(blocDest, dir))

	
	/** initialisation
	 * pre: nbPas > 0
	 * post: getTerrain() = t
	 * post: getPasRestants() = nbPas
	 */
	public void init(TerrainService t, int nbPas);
	
	/** deplace le hero d' une case dans la direction "dir"
	 * pre: ¬isPartieTerminee() ^ isDeplacementHeroPossible(dir)
	 * post: getTerrain() =
	 *			let* terrain = getTerrain()
	 *			and blocHero = Terrain::getBlocHero()
	 *			and blocDest = Terrain::getBlocVersDirection(blocHero, dir)
	 *			in
	 *				if ¬Bloc::isSolide(blocDest) then
	 *					Terrain::deplacerBlocVersDirection(blocHero, dir)
	 *				else if Bloc::isDeplacable(blocDest) and dir \in { GAUCHE, DROITE } then
	 *					let terrain' = Terrain::deplacerBlocVersDirection(blocDest, dir)
	 *					in Terrain::deplacerBlocVersDirection(terrain', blocHero, dir)
 	 * post: getPasRestants() = getPasRestants()@pre - 1
	 */
	public void deplacerHero(Direction dir);
	
	
}

/*
	

Observations:
	
	[deplacerHero]
		getTerrain(deplacerHero(mj, dir)) =
			let* terrain = getTerrain(mj)
			and blocHero = Terrain::getBlocHero(terrain)
			and blocDest = Terrain::getBlocVersDirection(terrain, blocHero, dir)
			in
				if ¬Bloc::isSolide(blocDest) then
					Terrain::deplacerBlocVersDirection(terrain, blocHero, dir)
				else if Bloc::isDeplacable(blocDest) and dir \in { GAUCHE, DROITE } then
					let terrain' = Terrain::deplacerBlocVersDirection(terrain, blocDest, dir)
					in Terrain::deplacerBlocVersDirection(terrain', blocHero, dir)
		getPasRestants(deplacerHero(mj, dir)) = getPasRestants(mj) - 1					
*/