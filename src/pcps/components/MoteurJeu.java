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
	//	System.out.println("isDeplacementHeroPossible, type bloc dest : " + 
	//			terrain.getBlocVersDirection(terrain.getBlocHero(), dir).getType() + " , dir :" + dir);
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest = terrain.getBlocVersDirection(blocHero, dir);
		if(!(blocDest.isSolide())){
			return true;
		} else{
			if (blocDest.isDeplacable()){
				BlocService blocDestPush = terrain.getBlocVersDirection(blocDest, dir);
				return blocDestPush.isVide();
			}
			return false;
		}
	}

	@Override
	public boolean isPartieTerminee() {
		//return ((nbPasRestants==0) || !terrain.isHeroVivant() || (terrain.getPosSortie().equals(terrain.getPosHero())) );
		// version verbeuse, pour test ou ça plantait
		if(nbPasRestants==0){
			System.out.println("game over : plus de pas restants");
			return true;
		}
		if(!terrain.isHeroVivant()){
			System.out.println("game over : héros écrasé");
			return true;
		}
		System.out.printf("pos hero (%d,%d)\npos sortie (%d,%d)\n", terrain.getPosHero().getX(),
				terrain.getPosHero().getY(),terrain.getPosSortie().getX(),
				terrain.getPosSortie().getY());
		
		if(terrain.getPosSortie().equals(terrain.getPosHero()) ){
			System.out.println("game over : sortie atteinte");
			return true;
		}
		
		return false;
		
	}

	@Override
	public boolean isPartieGagnee() {
		System.out.printf("partie gagne ? %b\n",(terrain.getPosSortie().equals(terrain.getPosHero())));
		System.out.println(getTerrain().toString());
		//return (isPartieTerminee() && terrain.isHeroVivant());
		
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
	//	System.out.printf("pos bloc hero =>  x : %d, y: %d\n", blocHero.getPosition().getX(),blocHero.getPosition().getY());
		BlocService blocDest = terrain.getBlocVersDirection(blocHero, dir);
	//	System.out.printf("pos bloc dest =>  x : %d, y: %d\n", blocDest.getPosition().getX(),blocDest.getPosition().getY());
		if(!blocDest.isSolide()){
		//	System.out.println("bloc dest is not solid");
			terrain.deplacerBlocVersDirection(blocHero, dir);
		} else {
			if(blocDest.isDeplacable() && ((dir == Direction.DROITE) || (dir == Direction.GAUCHE)) ){
			//	System.out.println("bloc dest is deplaçable");
				terrain.deplacerBlocVersDirection(blocDest, dir);
				terrain.deplacerBlocVersDirection(blocHero, dir);
			}
		}
		nbPasRestants--;
		
		// Si plus de pas restants, "tuer" le héros
		if(nbPasRestants == 0){
			blocHero.setType(TypeBloc.VIDE);
		}
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
