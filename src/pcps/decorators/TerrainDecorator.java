package pcps.decorators;

import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class TerrainDecorator implements TerrainService {
	private TerrainService delegate;
	
	public TerrainDecorator(TerrainService delegate) {
		this.delegate = delegate;
	}
	
	protected TerrainService getDelegate() {
		return delegate;
	}
	
	@Override
	public int getLargeur() {
		return getDelegate().getLargeur();
	}

	@Override
	public int getHauteur() {
		return getDelegate().getHauteur();
	}

	@Override
	public PositionService getPosSortie() {
		return getDelegate().getPosSortie();
	}

	@Override
	public PositionService getPosHero() {
		return getDelegate().getPosHero();
	}

	@Override
	public BlocService getBlocHero() {
		return getDelegate().getBlocHero();
	}

	@Override
	public BlocService getBloc(PositionService pos) {
		return getDelegate().getBloc(pos);
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		return getDelegate().getBlocVersDirection(bloc, dir);
	}

	@Override
	public boolean isHeroVivant() {
		return getDelegate().isHeroVivant();
	}

	@Override
	public boolean isDiamantsRestants() {
		return getDelegate().isDiamantsRestants();
	}

	@Override
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir) {
		return getDelegate().isDeplacementBlocPossible(bloc, dir);
	}

	@Override
	public void init(int l, int h) {
		getDelegate().init(l, h);
	}

	@Override
	public void setBloc(TypeBloc type, int x, int y) {
		getDelegate().setBloc(type, x, y);
	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		getDelegate().deplacerBlocVersDirection(bloc, dir);
	}

	@Override
	public void fairePasDeMiseAJour() {
		getDelegate().fairePasDeMiseAJour();
	}

}
