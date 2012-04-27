package pcps.components;

import pcps.services.Direction;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class Position implements
	/* provides */
	PositionService {

	protected int largeur;
	protected int hauteur;
	protected int x;
	protected int y;

	public Position() {}

	@Override
	public void init(TerrainService terrain, int x, int y) {
		largeur = terrain.getLargeur();
		hauteur = terrain.getHauteur();
		this.x = x % largeur;
		this.y = y % hauteur;
	}

	@Override
	public int getLargeur() {
		return largeur;
	}

	@Override
	public int getHauteur() {
		return hauteur;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void deplacerVersDirection(Direction dir) {
	}
}
