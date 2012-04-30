package pcps.decorators;

import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;

public class BlocDecorator implements BlocService {
	private final BlocService delegate;
	
	public BlocDecorator(BlocService delegate) {
		this.delegate = delegate;
	}
	
	protected BlocService getDelegate() {
		return delegate;
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
	public boolean isSortieFermee() {
		return getDelegate().isSortieFermee();
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
