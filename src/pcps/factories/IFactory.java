package pcps.factories;

import pcps.services.*;

public interface IFactory {
	MoteurJeuService creerMoteurJeu();
	TerrainService creerTerrain();
	BlocService creerBloc();
	PositionService creerPosition();
}
