package decorators;

import enums.TypeBloc;
import services.BlocService;
import services.PositionService;

public class BlocDecorator implements BlocService {
	private final BlocService delegate;
	
	public BlocDecorator(BlocService delegate) {
		this.delegate = delegate;
	}
	
	protected BlocService getDelegate() {
		return delegate;
	}

	@Override
	public int compareTo(BlocService o) {
		return getDelegate().compareTo(o);
	}
	
	@Override
	public BlocService copy() {
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
	public PositionService getPosition() {
		return getDelegate().getPosition();
	}

	@Override
	public boolean isVide() {
		return getDelegate().isVide();
	}

	@Override
	public boolean isSolide() {
		return getDelegate().isSolide();
	}

	@Override
	public boolean isDeplacable() {
		return getDelegate().isDeplacable();
	}

	@Override
	public boolean isTombable() {
		return getDelegate().isTombable();
	}

	@Override
	public boolean isSortie() {
		return getDelegate().isSortie();
	}
	
	@Override
	public boolean isSortieFermee() {
		return getDelegate().isSortieFermee();
	}

	@Override
	public boolean isHero() {
		return getDelegate().isHero();
	}
	
	@Override
	public boolean isTerre() {
		return getDelegate().isTerre();
	}
	
	@Override
	public TypeBloc getType() {
		return getDelegate().getType();
	}

	@Override
	public void init(TypeBloc tb, PositionService pos) {
		getDelegate().init(tb, pos);
	}

	@Override
	public void setType(TypeBloc tb) {
		getDelegate().setType(tb);
	}
}
