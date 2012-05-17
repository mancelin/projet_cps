package decorators;

import enums.Direction;
import services.PositionService;

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
