package pcps.components;

import pcps.services.BlocService;
import pcps.services.Direction;
import pcps.services.PositionService;
import pcps.services.TerrainService;
import pcps.services.TypeBloc;

public class Bloc implements 
	/* provide */
	BlocService, PositionService {

	private Position pos;
	private TypeBloc type;
	
	
	public Bloc () {
		// do nothing ? ou contructeur avec init() ??
	}
	
	@Override
	public int getLargeur() {
		return pos.getLargeur();
	}

	@Override
	public int getHauteur() {
		return pos.getHauteur();
	}

	@Override
	public int getX() {
		return pos.getX();
	}

	@Override
	public int getY() {
		return pos.getY();
	}

	@Override
	public void init(TerrainService t, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deplacerVersDirection(Direction dir) {
		// TODO Auto-generated method stub

	}

	@Override
	public TypeBloc getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeBloc getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSolide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDeplacable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTombable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSortieFermee() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(TypeBloc tb, PositionService pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setType(TypeBloc tb) {
		// TODO Auto-generated method stub

	}

}
