package decorators;

import java.util.Set;

import enums.Direction;
import enums.TypeBloc;

import services.BlocService;
import services.PositionService;
import services.TerrainService;

public class TerrainDecorator implements TerrainService {
	private TerrainService delegate;
	
	public TerrainDecorator(TerrainService delegate) {
		this.delegate = delegate;
	}
	
	protected TerrainService getDelegate() {
		return delegate;
	}
	
	@Override
	public TerrainService copy() {
		return getDelegate().copy();
	}
	
	@Override
	public String toString() {
		return getDelegate().toString();
	}
	
	@Override
	public boolean equals(Object other) {
		return getDelegate().equals(other);
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
	public BlocService getBlocDepuisPosition(PositionService pos) {
		return getDelegate().getBlocDepuisPosition(pos);
	}
	
	@Override
	public BlocService getBloc(int x, int y) {
		return getDelegate().getBloc(x, y);
	}
	
	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		return getDelegate().getBlocVersDirection(bloc, dir);
	}

	@Override
	public Set<BlocService> getBlocs() {
		return getDelegate().getBlocs();
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
