package pcps.components;

import pcps.enums.Direction;
import pcps.services.PositionService;

public class Position implements
	/* provides */
	PositionService {

	protected int largeur;
	protected int hauteur;
	protected int x;
	protected int y;

	public Position() {}

	@Override
	public PositionService copy() {
		PositionService copy = new Position();
		copy.init(getLargeur(), getHauteur(), getX(), getY());
		return copy;
	}
	
	@Override
	public void init(int l, int h, int x, int y) {
		largeur = l;
		hauteur = h;
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
		switch(dir){
			case GAUCHE:
				x = (x - 1) % largeur;
				if(x < 0){
					x += largeur;
				}
				break;
			case DROITE:
				x = (x + 1) % largeur;
				break;
				
			case HAUT:
				y = (y - 1) % hauteur;
				if(y < 0){
					y += hauteur;
				}
				break;
				
			case BAS:
				y = (y + 1) % hauteur;
				break;
		}
	}
}
