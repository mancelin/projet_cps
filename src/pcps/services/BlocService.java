package pcps.services;

public interface BlocService {
	/** Observateur: type du bloc */
	public TypeBloc getType();
	
	/** Observateur: position du bloc */
	public TypeBloc getPosition();
	
	/** Observateur: le bloc est de type VIDE ? */
	public boolean isVide();
	
	/** Observateur: le bloc est solide ? ( est de type SORTIE_FERMEE, MUR ou ROCHER ) */
	public boolean isSolide();
	
	/** Observateur: le bloc est "potentiellement" deplaçable ? ( est de type ROCHER ) */
	public boolean isDeplacable();
	
	/** Observateur: le bloc peut tomber ( est de type ROCHER ou DIAMANT ) */
	public boolean isTombable();
	
	/** Observateur: le bloc est une sortie fermmée */
	public boolean isSortieFermee();
	
	// inv: isVide() == getType() = VIDE
	// inv: isSolide() == getType() \in { SORTIE_FERMEE, MUR, ROCHER }
	// inv: isDeplacable() == getType() = ROCHER
	// inv: isTombable() == getType() \in { ROCHER, DIAMANT }
	// inv: isSortieFermee() == getType() = SORTIE_FERMEE
					
	
	/** initialisation
	 *  pre: 
	 *  post: getType() == tb
	 *  post: getPosition() == pos
	 */
	public void init(TypeBloc tb, PositionService pos);
	
	
	/** changement du type d' un bloc
	 * post : getType() == tb
	 * post	: getPosition() = getPosition()@pre
	 */
	public void setType(TypeBloc tb);
	
	
}
