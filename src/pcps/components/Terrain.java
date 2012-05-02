package pcps.components;

import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class Terrain implements 
	/* provide */
	TerrainService {
	
	protected int largeur;
	protected int hauteur;	
	protected BlocService[][] matriceTerrain;

	@Override
	public void init(int l, int h) {
		largeur = l;
		hauteur = h;
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				PositionService pos = new Position();
				pos.init(l, h, x, y);
				BlocService bloc = new Bloc();
				bloc.init(TypeBloc.VIDE, pos);
				matriceTerrain[x-1][y-1] = bloc;
			}
		}
	}
	
	
	@Override
	public int getLargeur() {
		return largeur;
	}

	@Override
	public int getHauteur() {
		return hauteur;
	}

	@Override
	public PositionService getPosSortie() {
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				if(matriceTerrain[x-1][y-1].getType() == TypeBloc.SORTIE_FERMEE ||
						matriceTerrain[x-1][y-1].getType() == TypeBloc.SORTIE_OUVERTE){
					PositionService pos = new Position();
					pos.init(this.largeur, this.hauteur, x, y);
					return pos;
				}
			}
		}
		// si sortie non trouvée
		return null;
	}

	@Override
	public PositionService getPosHero() {
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				if(matriceTerrain[x-1][y-1].getType() == TypeBloc.HERO ){
					PositionService pos = new Position();
					pos.init(this.largeur, this.hauteur, x, y);
					return pos;
				}
			}
		}
		// si hero non trouvé
		return null;
	}

	@Override
	public BlocService getBlocHero() {
		PositionService posHero = getPosHero();
		return getBloc(posHero);
	}

	@Override
	public BlocService getBloc(PositionService pos) {
		return matriceTerrain[pos.getX()-1][pos.getY()-1];
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHeroVivant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDiamantsRestants() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir) {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public void setBloc(TypeBloc type, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fairePasDeMiseAJour() {
		// TODO Auto-generated method stub

	}

}
