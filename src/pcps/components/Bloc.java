package pcps.components;

import pcps.services.BlocService;
import pcps.services.PositionService;
import pcps.services.TypeBloc;

public class Bloc implements 
	/* provide */
	BlocService {

	protected PositionService position;
	protected TypeBloc type;
	
	
	public Bloc () {}
	
	@Override
	public void init(TypeBloc tb, PositionService pos) {
		// TODO Auto-generated method stub	
	}

	@Override
	public TypeBloc getType() {
		return type;
	}
	
	@Override
	public void setType(TypeBloc tb) {
		type = tb;
	}

	@Override
	public PositionService getPosition() {
		return position;
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
}
