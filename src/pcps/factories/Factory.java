package pcps.factories;

import pcps.services.BlocService;
import pcps.services.MoteurJeuService;
import pcps.services.PositionService;
import pcps.services.TerrainService;
import pcps.components.*;

public class Factory implements IFactory {

	@Override
	public MoteurJeuService creerMoteurJeu() {
		return new MoteurJeu();
	}

	@Override
	public TerrainService creerTerrain() {
		return new Terrain();
	}

	@Override
	public BlocService creerBloc() {
		return new Bloc();
	}

	@Override
	public PositionService creerPosition() {
		return new Position();
	}

}
