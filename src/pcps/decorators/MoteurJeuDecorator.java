package pcps.decorators;

import pcps.enums.Direction;
import pcps.services.MoteurJeuService;
import pcps.services.TerrainService;

public class MoteurJeuDecorator implements MoteurJeuService {
	private final MoteurJeuService delegate;
	
	public MoteurJeuDecorator(MoteurJeuService delegate) {
		this.delegate = delegate;
	}
	
	protected MoteurJeuService getDelegate() {
		return delegate;
	}
	
	@Override
	public MoteurJeuService copy() {
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
	public TerrainService getTerrain() {
		return getDelegate().getTerrain();
	}

	@Override
	public int getPasRestants() {
		return getDelegate().getPasRestants();
	}

	@Override
	public boolean isDeplacementHeroPossible(Direction dir) {
		return getDelegate().isDeplacementHeroPossible(dir);
	}

	@Override
	public boolean isPartieTerminee() {
		return getDelegate().isPartieTerminee();
	}

	@Override
	public boolean isPartieGagnee() {
		return getDelegate().isPartieGagnee();
	}

	@Override
	public void init(TerrainService t, int nbPas) {
		getDelegate().init(t, nbPas);
	}

	@Override
	public void deplacerHero(Direction dir) {
		getDelegate().deplacerHero(dir);
	}
}
