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
		System.out.printf("pos init : l=%d h=%d x=%d y=%d\n",l,h,x,y);
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
			
			case DROITE:
				x = (x + 1) % largeur;
				
			case HAUT:
				y = (y - 1) % hauteur;
				
			case BAS:
				y = (y + 1) % hauteur;
		}
	}
}
