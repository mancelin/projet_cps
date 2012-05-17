package services;

import enums.Direction;

public interface PositionService {
	public PositionService copy();
	
	/** Observator: largeur du terrain (sert au calcul des coordonnées cycliques) */
	public int getLargeur();
	
	/** Observator: hauteur du terrain (sert au calcul des coordonnées cycliques) */
	public int getHauteur();
	
	/** Observator: coordonnée X de la position */
	public int getX();
	
	/** Observator: coordonnée Y de la position */
	public int getY();
	
	
	/**
	 * Constructor init:
	 *   pre: l > 0 && h > 0 && x >= 0 && y >= 0
	 *   post: getLargeur() == l
	 *   post: getHauteur() == h
	 *   post: getX() == x mod l
	 *   post: getY() == y mod h
	 */
	 public void init(int l, int h, int x, int y);

	/**
	 * Operator deplacerVersDirection: déplacer la position d'un pas dans une direction
	 *   post:
	 *     \if dir == GAUCHE \then
	 *         getX() == (getX()@pre - 1) mod getLargeur()
	 *         getY() == getY()@pre 
	 *     \else \if dir == DROITE \then
	 *         getX() == (getX()@pre + 1) mod getLargeur()
	 *         getY() == getY()@pre 
	 *     \else \if dir == HAUT \then
	 *         getX() == getX()@pre
	 *         getY() == (getY()@pre - 1) mod getHauteur()
	 *     \else \if dir == BAS \then 
	 *         getX() == getX()@pre
	 *         getY() == (getY()@pre + 1) mod getHauteur() 
	 */
	 public void deplacerVersDirection(Direction dir);
}
