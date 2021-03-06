package contracts;

import decorators.PositionDecorator;
import enums.Direction;
import services.PositionService;

public class PositionContract extends PositionDecorator {

	public PositionContract(PositionService delegate) {
		super(delegate);
	}

	@Override
	public PositionService copy() {
		return new PositionContract(super.copy());
	}

	public void checkInvariant() {
		// inv: 0 <= getX() < getLargeur()
		if (!(0 <= getX() && getX() < getLargeur()))
			Contractor.defaultContractor().invariantError("PositionService", "La valeur de x doit être compris entre 0 et la largeur du terrain exclue.");

		// inv: 0 <= getY() < getHauteur()
		if (!(0 <= getHauteur() && getY() < getHauteur()))
			Contractor.defaultContractor().invariantError("PositionService", "La valeur de y doit être compris entre 0 et la longueur du terrain exclue.");
	}

	@Override
	public void init(int l, int h, int x, int y) {
		// pre: l > 0 && h > 0 && x >= 0 && y >= 0
		if (!(l > 0 && h > 0 && x >= 0 && y >= 0))
			Contractor.defaultContractor().preconditionError("PositionService", "init", "La largeur et la hauteur du terrain doivent être strictement positives et les coordonnées (x, y) doivent être positives.");

		// run
		super.init(l, h, x, y);

		// invariant@post
		checkInvariant();

		// post: getLargeur() == l
		if (!(getLargeur() == l))
			Contractor.defaultContractor().postconditionError("PositionService", "init", "getLargeur() doit retourner la largeur du terrain fourni au constructeur.");

		// post: getHauteur() == h
		if (!(getHauteur() == h))
			Contractor.defaultContractor().postconditionError("PositionService", "init", "getHauteur() doit retourner la hauteur du terrain fourni au constructeur.");
	}

	@Override
	public void deplacerVersDirection(Direction dir) {
		// captures
		int getXAtPre = getX();
		int getYAtPre = getY();
		int newX, newY;

		// invariant@pre
		checkInvariant();

		// run
		super.deplacerVersDirection(dir);

		// invariant@post
		checkInvariant();

		// post:
		//   \if dir == GAUCHE \then
		//       getX() == (getX()@pre - 1) mod getLargeur()
		//       getY() == getY()@pre
		//   \else \if dir == DROITE \then
		//       getX() == (getX()@pre + 1) mod getLargeur()
		//       getY() == getY()@pre
		//   \else \if dir == HAUT \then
		//       getX() == getX()@pre
		//       getY() == (getY()@pre - 1) mod getHauteur()
		//   \else \if dir == BAS \then
		//       getX() == getX()@pre
		//       getY() == (getY()@pre + 1) mod getHauteur()
		if (dir == Direction.GAUCHE) {
			newX = (getXAtPre - 1) % getLargeur();
			if (newX == -1) newX = (getLargeur() - 1);
			if (!(getX() == newX))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers la GAUCHE, la nouvelle valeur de X doit être le reste du modulo de l'ancien X décrémenté de 1 par la largeur du terrain.");
			if (!(getY() == getYAtPre))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vrs la GAUCHE, la valeur de Y doit rester inchangée.");
		} else if (dir == Direction.DROITE) {
			if (!(getX() == (getXAtPre + 1) % getLargeur()))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers la DROITE, la nouvelle valeur de X doit être le reste du modulo de l'ancien X incrémenté de 1 par la largeur du terrain.");
			if (!(getY() == getYAtPre))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vrs la DROITE, la valeur de Y doit rester inchangée.");
		} else if (dir == Direction.HAUT) {
			if (!(getX() == getXAtPre))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers le HAUT, la valeur de X doit rester inchangée.");
			newY = (getYAtPre - 1) % getHauteur();
			if (newY == -1) newY = getHauteur() - 1;
			if (!(getY() == newY))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers le HAUT, la nouvelle valeur de Y doit être le reste du modulo de l'ancien Y décrémenté de 1 par la hauteur du terrain.");
		} else if (dir == Direction.BAS) {
			if (!(getX() == getXAtPre))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers le BAS, la valeur de X doit rester inchangée.");
			if (!(getY() == (getYAtPre + 1) % getHauteur()))
				Contractor.defaultContractor().postconditionError("PositionService", "deplacerVersDirection", "Dans un déplacement vers le BAS, la nouvelle valeur de Y doit être le reste du modulo de l'ancien Y incrémenté de 1 par la hauteur du terrain.");
		}
	}
}
