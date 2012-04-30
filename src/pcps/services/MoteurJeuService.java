package pcps.services;

import pcps.enums.Direction;

public interface MoteurJeuService {
	/** Observator: terrain associé au moteur de jeu */
	public TerrainService getTerrain();

	/** Observator: nombre de pas restants pour que la partie soit interrompue */
	public int getPasRestants();

	/** Observator: le héro peut-il se déplacer dans la direction donnée ? */
	public boolean isDeplacementHeroPossible(Direction dir);

	/** Observator: la partie est-elle terminée ? */
	public boolean isPartieTerminee();

	/** Observator: la partie est-elle gagnée ? */
	public boolean isPartieGagnee();

	/** Invariant */
	// inv: isPartieTerminee() == (getPasRestants() == 0 ||
	// !getTerrain().isHeroVivant() || getTerrain().getPosSortie ==
	// getTerrain().getPosHero())
	// inv: isPartieGagnee() == (isPartieTerminee() &&
	// getTerrain().isHeroVivant()
	// inv: \forall dir:Direction \in { GAUCHE, DROITE }, isDeplacementHeroPossible(dir) ==
	//          \let* terrain = getTerrain()
	//          \and blocHero = terrain.getBlocHero()
	//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
	//          \in !blocDest.isSolide()
	//              || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest, dir).isVide()
	// inv: \forall dir:Direction \in { HAUT, BAS }, isDeplacementHeroPossible(dir) ==
	//          \let* terrain = getTerrain()
	//          \and blocHero = terrain.getBlocHero()
	//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
	//          \in !blocDest.isSolide()
	
	/**
	 * Constructor init:
	 *   pre: nbPas > 0
	 *   post: getTerrain() = t
	 *   post: getPasRestants() = nbPas
	 */
	public void init(TerrainService t, int nbPas);

	/**
	 * Operator deplacerHero: déplacer le héro d'une case dans la direction donnée
	 *   pre: !isPartieTerminee() && isDeplacementHeroPossible(dir)
	 *   post:
	 *     \let* terrain = getTerrain()
	 *     \and blocHero = terrain.getBlocHero()
	 *     \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
	 *     \in
	 *         \if !blocDest.isSolide() \then
	 *             getTerrain() = getTerrain()@pre.deplacerBlocVersDirection(blocHero, dir)
	 *         \else \if blocDest.isDeplacable() && dir \in { GAUCHE, DROITE } \then
	 *             getTerrain()@pre.deplacerBlocVersDirection(blocDest, dir)
	 *             getTerrain() == getTerrain()@pre.deplacerBlocVersDirection(blocHero, dir)
	 *   post: getPasRestants() = getPasRestants()@pre - 1
	 */
	public void deplacerHero(Direction dir);
}