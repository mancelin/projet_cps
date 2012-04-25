package pcps.components;

import pcps.services.BlocService;
import pcps.services.Direction;
import pcps.services.PositionService;
import pcps.services.TerrainService;
import pcps.services.TypeBloc;

public class Position implements PositionService, TerrainService {

	@Override
	public PositionService getPosSortie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositionService getPosHero() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlocService getBlocHero() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlocService getBloc(PositionService pos) {
		// TODO Auto-generated method stub
		return null;
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
	public void init(int l, int h) {
		// TODO Auto-generated method stub

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

	@Override
	public int getLargeur() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHauteur() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init(TerrainService t, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deplacerVersDirection(Direction dir) {
		// TODO Auto-generated method stub

	}

}
