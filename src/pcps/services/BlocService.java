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
}


/*
Service: Bloc

Types: boolean, enum TypeBloc VIDE, TERRE, MUR, HERO, SORTIE_FERMEE, SORTIE_OUVERTE, ROCHER, DIAMANT


Constructors:
	init : TypeBloc * Position -> [Bloc]
	
Operators:
	setType : [Bloc] * TypeBloc -> [Bloc]
	
Observations:
	[invariant]
		isVide(b) ={min} getType(b) = VIDE
		isSolide(b) ={min} getType(b) \in { SORTIE_FERMEE, MUR, ROCHER }
		isDeplacable(b) ={min} getType(b) = ROCHER
		isTombable(b) ={min} getType(b) \in { ROCHER, DIAMANT }
		isSortieFermee(b) ={min} getType(b) = SORTIE_FERMEE
		
	[init]
		getType(init(tb, pos)) = tb
		getPosition(init(tb, pos)) = pos
		
	[setType]
		getType(setType(b, tb)) = tb
		getPosition(setType(b, tb)) = getPosition(b)
*/