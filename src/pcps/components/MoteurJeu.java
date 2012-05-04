package pcps.components;

import pcps.enums.Direction;
import pcps.services.BlocService;
import pcps.services.MoteurJeuService;
import pcps.services.TerrainService;

public class MoteurJeu implements 
	/* provide */
	MoteurJeuService {
	
	protected TerrainService terrain;
	protected int nbPasRestants; 

	@Override
	public TerrainService getTerrain() {
		return terrain;
	}

	@Override
	public int getPasRestants() {
		return nbPasRestants;
	}

	@Override
	public boolean isDeplacementHeroPossible(Direction dir) {
		return !(terrain.getBlocVersDirection(terrain.getBlocHero(), dir).isSolide());
	}

	@Override
	public boolean isPartieTerminee() {
		return ((nbPasRestants==0) || !terrain.isHeroVivant() || (terrain.getPosSortie() == terrain.getPosHero()) );
	}

	@Override
	public boolean isPartieGagnee() {
		return (isPartieTerminee() && terrain.isHeroVivant());
	}

	@Override
	public void init(TerrainService t, int nbPas) {
		terrain = t;
		nbPasRestants = nbPas;
	}

	@Override
	public void deplacerHero(Direction dir) {
		if(!isPartieTerminee() && isDeplacementHeroPossible(dir)){
			BlocService blocHero = terrain.getBlocHero();
			BlocService blocDest = terrain.getBlocVersDirection(blocHero, dir);
			if(!blocDest.isSolide()){
				terrain.deplacerBlocVersDirection(blocHero, dir);
			} else {
				if(blocDest.isDeplacable() && ((dir == Direction.DROITE) || (dir == Direction.GAUCHE)) ){
					terrain.deplacerBlocVersDirection(blocDest, dir);
					terrain.deplacerBlocVersDirection(blocHero, dir);
				}
			}
			nbPasRestants--;
		}
	}

}
