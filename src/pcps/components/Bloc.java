package pcps.components;

import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;

public class Bloc implements 
	/* provide */
	BlocService {

	protected TypeBloc type;
	protected PositionService position;
	
	
	public Bloc () {
		// do nothing
	}
	
	@Override
	public BlocService copy() {
		BlocService copy = new Bloc();
		copy.init(getType(), getPosition().copy());
		return copy;
	}
	
	@Override
	public void init(TypeBloc tb, PositionService pos) {
		type=tb;
		position=pos;
		System.out.println("bloc init ");
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
		if(type == TypeBloc.VIDE)
			return true;
		return false;
	}


	@Override
	public boolean isSolide() {
		if(type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.MUR || type == TypeBloc.ROCHER )
			return true;
		return false;
	}


	@Override
	public boolean isDeplacable() {
		if(type == TypeBloc.ROCHER)
			return true;
		return false;
	}


	@Override
	public boolean isTombable() {
		if(type == TypeBloc.ROCHER || type == TypeBloc.DIAMANT)
			return true;
		return false;
	}

	@Override
	public boolean isSortie() {
		if (type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.SORTIE_OUVERTE)
			return true;
		return false;
	}

	@Override
	public boolean isSortieFermee() {
		if(type == TypeBloc.SORTIE_FERMEE)
			return true;
		return false;
	}
}
