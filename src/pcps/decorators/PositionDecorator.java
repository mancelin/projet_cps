package pcps.decorators;

import pcps.enums.Direction;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class PositionDecorator implements PositionService {
	private PositionService delegate;
	
	public PositionDecorator(PositionService delegate) {
		this.delegate = delegate;
	}
	
	protected PositionService getDelegate() {
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
	public int getX() {
		return getDelegate().getX();
	}

	@Override
	public int getY() {
		return getDelegate().getY();
	}

	@Override
	public void init(TerrainService terrain, int x, int y) {
		getDelegate().init(terrain, x, y);
	}

	@Override
	public void deplacerVersDirection(Direction dir) {
		getDelegate().deplacerVersDirection(dir);
	}
}
