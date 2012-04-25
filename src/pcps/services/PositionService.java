package pcps.services;

public interface PositionService {
	/** Observateur: largeur du terrain (sert aux coordonées cycliques */
	public int getLargeur();
	
	/** Observateur: hauteur du terrain (sert aux coordonées cycliques */
	public int getHauteur();
	
	/** Observateur: coordonnée X de la position */
	public int getX();
	
	/** Observateur: coordonnée Y de la position */
	public int getY();
	
	
	/** initialisation
	 *  pre:  x >= 0 ^ y >= 0
	 *  post: getLargeur() = Terrain::getLargeur()
	 *  post: getHauteur() = Terrain::getHauteur()
	 *  post: getX() = x mod Terrain::getLargeur()
	 *  post: getY() = y mod Terrain::getHauteur()
	 */
	 public void init(int lim);

	/** déplacement vers une direction
	 * post: if dir = GAUCHE
	 *       then
	 *         getX()=(getX()@pre - 1) mod getLargeur()
	 *         getY()=getY()@pre 
	 *       else if dir = DROITE
	 *       then
	 *         getX()=(getX()@pre + 1) mod getLargeur()
	 *         getY()=getY()@pre 
	 *       else if dir = HAUT
	 *       then
	 *         getX()=getX()@pre
	 *         getY()=(getY()@pre - 1) mod getHauteur()
	 *       else if dir = BAS 
	 *       then
	 *         getX()=getX()@pre
	 *         getY()=(getY()@pre + 1) mod getHauteur() 
	 */
	 
	 public void deplacerVersDirection(Direction dir);
}
