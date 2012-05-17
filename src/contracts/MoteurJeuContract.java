package contracts;

import decorators.MoteurJeuDecorator;
import enums.Direction;
import services.BlocService;
import services.MoteurJeuService;
import services.TerrainService;

public class MoteurJeuContract extends MoteurJeuDecorator {

	public MoteurJeuContract(MoteurJeuService delegate) {
		super(delegate);
	}
	
	public void checkInvariant() {
		TerrainService terrain = getTerrain();
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest;
		Direction dir;
		
		// inv: isPartieTerminee() == (getPasRestants() == 0 || !getTerrain().isHeroVivant() || isPartieGagnee)
		if (!(isPartieTerminee() == (getPasRestants() == 0 || !getTerrain().isHeroVivant() || isPartieGagnee())))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "La partie doit être considérée terminée quand il n'y a plus de pas restants, quand le héro est mort ou quand la position du héro est situé sur la position de la sortie.");
		
		// inv: isPartieGagnee() == (getTerrain().getPosSortie() == getTerrain().getPosHero())
		if (!(isPartieGagnee() == (getTerrain().getPosSortie().equals(getTerrain().getPosHero()))))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "La partie doit être considérée gagnée lorsqu'elle est terminée et que le héro est toujours vivant.");
		
		// inv: \forall dir:Direction \in { GAUCHE, DROITE }, isDeplacementHeroPossible(dir) ==
		//          \let* terrain = getTerrain()
		//          \and blocHero = terrain.getBlocHero()
		//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
		//          \in !blocDest.isSolide()
		//              || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest, dir).isVide()
		dir = Direction.GAUCHE;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		if (!(isDeplacementHeroPossible(dir) == (!blocDest.isSolide() || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest,  dir).isVide()))))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "Le déplacement du héro vers la GAUCHE ne doit être possible que si le bloc de destination n'est pas solide, ou alors s'il est déplaçable et que son bloc voisin dans la même direction est vide.");
		dir = Direction.DROITE;
		blocDest = terrain.getBlocVersDirection(blocHero, dir);
		if (!(isDeplacementHeroPossible(dir) == (!blocDest.isSolide() || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest, dir).isVide()))))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "Le déplacement du héro vers la DROITE ne doit être possible que si le bloc de destination n'est pas solide, ou alors s'il est déplaçable et que son bloc voisin dans la même direction est vide.");
		
		// inv: \forall dir:Direction \in { HAUT, BAS }, isDeplacementHeroPossible(dir) ==
		//          \let* terrain = getTerrain()
		//          \and blocHero = terrain.getBlocHero()
		//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
		//          \in !blocDest.isSolide()
		dir = Direction.HAUT;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		if (!(isDeplacementHeroPossible(dir) == !blocDest.isSolide()))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "Le déplacement du héro vers le HAUT ne doit être possible que si le bloc de destination n'est pas solide.");

		dir = Direction.BAS;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		if (!(isDeplacementHeroPossible(dir) == !blocDest.isSolide()))
			Contractor.defaultContractor().invariantError("MoteurJeuService", "Le déplacement du héro vers le BAS ne doit être possible que si le bloc de destination n'est pas solide.");	
	}

	@Override
	public void init(TerrainService t, int nbPas) {
		// pre: nbPas > 0
		if (!(nbPas > 0))
			Contractor.defaultContractor().postconditionError("MoteurJeuService", "init", "Le nombre de pas de jeu doit être strictement positif.");
				
		// run
		super.init(t, nbPas);
		
		// invariant@post
		checkInvariant();
		
		// post: getTerrain() == t
		if (!(getTerrain() == t))
			Contractor.defaultContractor().postconditionError("MoteurJeuService", "init", "getTerrain() doit retourner le terrain passé au constructeur.");
		
		// post: getPasRestants() == nbPas
		if (!(getPasRestants() == nbPas))
			Contractor.defaultContractor().postconditionError("MoteurJeuService", "init", "getPasRestants() doit retourner le nombre de pas passé au constructeur.");
	}

	@Override
	public void deplacerHero(Direction dir) {
		// captures
		int getPasRestantsAtPre = getPasRestants();
		TerrainService getTerrainAtPre = getTerrain();
		
		// pre: !isPartieTerminee() && isDeplacementHeroPossible(dir)
		if (!(!isPartieTerminee() && isDeplacementHeroPossible(dir)))
			Contractor.defaultContractor().postconditionError("MoteurJeuService", "deplacerHero", "Pour pouvoir déplacer le héro, il faut que la partie ne soit pas terminée et que le déplacement du héro dans la direction donnée soit possible.");
		
		// invariant@pre
		checkInvariant();
		
		// run
		super.deplacerHero(dir);
		
		// invariant@post
		checkInvariant();
		
		// post:
		//   \let* terrain = getTerrain()
		//   \and blocHero = terrain.getBlocHero()
		//   \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
		//   \in
		//       \if !blocDest.isSolide() \then
		//           getTerrain() = getTerrain()@pre.deplacerBlocVersDirection(blocHero, dir)
		//       \else \if blocDest.isDeplacable() && dir \in { GAUCHE, DROITE } \then
		//           getTerrain()@pre.deplacerBlocVersDirection(blocDest, dir)
		//           getTerrain() == getTerrain()@pre.deplacerBlocVersDirection(blocHero, dir)
		TerrainService terrain = getTerrain();
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest = terrain.getBlocVersDirection(blocHero, dir);
		if (!blocDest.isSolide()) {
			getTerrainAtPre.deplacerBlocVersDirection(blocHero, dir);
			if (!(getTerrain() == getTerrainAtPre))
				Contractor.defaultContractor().postconditionError("MoteurJeuService", "deplacerHero", "Après un déplacement du héro, le terrain doit avoir déplacé le héro d'une case dans la direction voulue.");
		} else if (blocDest.isDeplacable() && (dir == Direction.GAUCHE || dir == Direction.DROITE)) {
			getTerrainAtPre.deplacerBlocVersDirection(blocDest, dir);
			getTerrainAtPre.deplacerBlocVersDirection(blocHero, dir);
			if (!(getTerrain() == getTerrainAtPre))
				Contractor.defaultContractor().postconditionError("MoteurJeuService", "deplacerHero", "Après un déplacement horizontal du héro entravé d'un objet déplaçable, le terrain doit avoir déplacé le héro ainsi que l'obstacle d'une case dans la direction voulue.");
		}
		
		// post: getPasRestants() == getPasRestants()@pre - 1
		if (!(getPasRestants() == getPasRestantsAtPre - 1))
			Contractor.defaultContractor().postconditionError("MoteurJeuService", "deplacerHero", "Le nombre de pas restants doit être décrémenté de 1 après avoir déplacé le héro.");
	}
}
