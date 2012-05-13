package pcps.components;

import pcps.enums.Direction;
import pcps.parser.TerrainParser;
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
		assert(!isPartieTerminee() && isDeplacementHeroPossible(dir));
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
	}


	public String toString(){
		String res = "";
		if(isPartieTerminee())
			return "Game Over!";
		if(isPartieGagnee())
			return "You Win!";
		res += Integer.toString(nbPasRestants) +" pas restants\n";
		TerrainService t = this.getTerrain();
		for(int y=0;y<t.getHauteur();y++){
			for(int x=0;x<t.getLargeur();x++){
				res += TerrainParser.charDeTypeBloc(t.getBloc(x, y).getType());
			}
			res +="\n";
		}

		return res;		
	}

}
