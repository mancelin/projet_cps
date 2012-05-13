package pcps.factories;

import pcps.components.Bloc;
import pcps.components.MoteurJeu;
import pcps.components.Position;
import pcps.components.Terrain;
import pcps.services.BlocService;
import pcps.services.MoteurJeuService;
import pcps.services.PositionService;
import pcps.services.TerrainService;
import pcps.contracts.*;

public class FactoryWithContracts implements IFactory {
	
	@Override
	public MoteurJeuService creerMoteurJeu() {
		return new MoteurJeuContract(new MoteurJeu());
	}

	@Override
	public TerrainService creerTerrain() {
		return new TerrainContract(new Terrain());
	}

	@Override
	public BlocService creerBloc() {
		return new BlocContract(new Bloc());
	}

	@Override
	public PositionService creerPosition() {
		return new PositionContract(new Position());
	}
}
