package pcps.decorators;

import pcps.enums.Direction;
import pcps.services.PositionService;

public class PositionDecorator implements PositionService {
	private PositionService delegate;
	
	public PositionDecorator(PositionService delegate) {
		this.delegate = delegate;
	}
	
	protected PositionService getDelegate() {
		return delegate;
	}
	
	@Override
	public PositionService copy() {
		return getDelegate().copy();
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
	public void init(int l, int h, int x, int y) {
		getDelegate().init(l, h, x, y);
	}

	@Override
	public void deplacerVersDirection(Direction dir) {
		getDelegate().deplacerVersDirection(dir);
	}
}
