package pcps.components;

import pcps.enums.Direction;	
import pcps.enums.TypeBloc;
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
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest = terrain.getBlocVersDirection(blocHero, dir);
		if(!(blocDest.isSolide())){
			return true;
		} else{
			if (blocDest.isDeplacable() && ((dir == Direction.GAUCHE) || (dir == Direction.DROITE))){
				BlocService blocDestPush = terrain.getBlocVersDirection(blocDest, dir);
				return blocDestPush.isVide();
			}
			return false;
		}
	}

	@Override
	public boolean isPartieTerminee() {
		return ((nbPasRestants==0) || !terrain.isHeroVivant() || isPartieGagnee() );
	}

	@Override
	public boolean isPartieGagnee() {
		return (terrain.getPosSortie().equals(terrain.getPosHero()));
	}

	@Override
	public void init(TerrainService t, int nbPas) {
		if (!(nbPas > 0)) {
			throw new IllegalArgumentException("Le nombre de pas doit être strictement positif");
		}
		terrain = t;
		nbPasRestants = nbPas;
	}

	@Override
	public void deplacerHero(Direction dir) {
		if (!(!isPartieTerminee() && isDeplacementHeroPossible(dir))) {
			throw new RuntimeException("La partie est terminée ou le déplacement est impossible");
		}
		
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
		
		// Si plus de pas restants, "tuer" le héros
		/*
		if(nbPasRestants == 0){
			blocHero.setType(TypeBloc.VIDE);
		}
		*/
	}


	public String toString(){
		String res = "";
		if(isPartieTerminee())
			return "Game Over!";
		if(isPartieGagnee())
			return "You Win!";
		res += Integer.toString(nbPasRestants) +" pas restants\n";
		res += getTerrain().toString();

		return res;		
	}

}
