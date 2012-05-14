package pcps.components;

import pcps.enums.TypeBloc;
import pcps.factories.Factory;
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
		BlocService copy = Factory.getFactory().creerBloc(); 
		copy.init(getType(), getPosition().copy());
		return copy;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BlocService))
			return false;
		BlocService otherBloc = (BlocService)other;
		return (otherBloc.getType() == getType() && otherBloc.getPosition().equals(getPosition()));
	}

	@Override
	public void init(TypeBloc tb, PositionService pos) {
		type=tb;
		position=pos;
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
		return (type == TypeBloc.VIDE);
	}


	@Override
	public boolean isSolide() {
		return (type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.MUR || type == TypeBloc.ROCHER );
	}


	@Override
	public boolean isDeplacable() {
		return (type == TypeBloc.ROCHER);
	}


	@Override
	public boolean isTombable() {
		return (type == TypeBloc.ROCHER || type == TypeBloc.DIAMANT);
	}

	@Override
	public boolean isSortie() {
		return (type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.SORTIE_OUVERTE);
	}

	@Override
	public boolean isSortieFermee() {
		return (type == TypeBloc.SORTIE_FERMEE);
	}

	@Override
	public boolean isHero() {
		return (type == TypeBloc.HERO);
	}

	@Override
	public boolean isTerre() {
		return (type == TypeBloc.TERRE);
	}
}
