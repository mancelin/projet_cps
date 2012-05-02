package pcps.services;

import pcps.enums.TypeBloc;

public interface BlocService {
	public BlocService copy();
	
	/** Observator: type du bloc */
	public TypeBloc getType();
	
	/** Observator: position du bloc */
	public PositionService getPosition();
	
	/** Observator: le bloc est-il de type VIDE ? */
	public boolean isVide();
	
	/** Observator: le bloc est-il solide ? (type SORTIE_FERMEE, MUR ou ROCHER) */
	public boolean isSolide();
	
	/** Observator: le bloc est-il "potentiellement" deplaçable ? (de type ROCHER) */
	public boolean isDeplacable();
	
	/** Observator: le bloc peut-il tomber ? (de type ROCHER ou DIAMANT) */
	public boolean isTombable();
	
	/** Observator: le bloc est-il une sortie ouverte ou fermée ? */
	boolean isSortie();
	
	/** Observator: le bloc est-il une sortie fermée ? */
	public boolean isSortieFermee();
	
	
	/** Invariant */
	// inv: isVide() == (getType() == VIDE)
	// inv: isSolide() == (getType() \in { SORTIE_FERMEE, MUR, ROCHER })
	// inv: isDeplacable() == (getType() == ROCHER)
	// inv: isTombable() == (getType() \in { ROCHER, DIAMANT })
	// inv: isSortie() == (getType() \in { SORTIE_FERMEE, SORTIE_OUVERTE }
	// inv: isSortieFermee() == (getType() == SORTIE_FERMEE)
					
	
	/**
	 * Constructor init:
	 *   post: getType() == tb
	 *   post: getPosition() == pos
	 */
	public void init(TypeBloc tb, PositionService pos);
	
	/**
	 * Operator setType: changer le type du bloc
	 *   post: getType() == tb
	 *   post: getPosition() == getPosition()@pre
	 */
	public void setType(TypeBloc tb);
}
