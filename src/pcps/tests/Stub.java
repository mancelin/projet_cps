package pcps.tests;

import pcps.services.*;
import pcps.enums.TypeBloc;
import pcps.factories.Factory;

public class Stub {

	// POS1: Position(5, 3, 1, 1)
	static PositionService getPOS1() {
		PositionService POS1 = Factory.getFactory().creerPosition();
		POS1.init(5, 3, 1, 1);
		return POS1;
	}
	
	// TER1 (5, 3):
	//
	// .####
	// .OX.Y
	// ....?
	//
	static TerrainService getTER1() {
		TerrainService TER1 = Factory.getFactory().creerTerrain();
		TER1.init(5, 3);
		TER1.setBloc(TypeBloc.VIDE, 0, 0);
		TER1.setBloc(TypeBloc.MUR, 1, 0);
		TER1.setBloc(TypeBloc.MUR, 2, 0);
		TER1.setBloc(TypeBloc.MUR, 3, 0);
		TER1.setBloc(TypeBloc.MUR, 4, 0);
		TER1.setBloc(TypeBloc.VIDE, 0, 1);
		TER1.setBloc(TypeBloc.ROCHER, 1, 1);
		TER1.setBloc(TypeBloc.HERO, 2, 1);
		TER1.setBloc(TypeBloc.VIDE, 3, 1);
		TER1.setBloc(TypeBloc.DIAMANT, 4, 1);
		TER1.setBloc(TypeBloc.VIDE, 0, 2);
		TER1.setBloc(TypeBloc.VIDE, 1, 2);
		TER1.setBloc(TypeBloc.VIDE, 2, 2);
		TER1.setBloc(TypeBloc.VIDE, 3, 2);
		TER1.setBloc(TypeBloc.SORTIE_FERMEE, 4, 2);
		return TER1;
	}
}
