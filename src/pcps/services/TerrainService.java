package pcps.services;

import java.lang.reflect.Type;

public interface TerrainService {
	/** Observateur: largeur du terrain */
	public int getLargeur();
	
	/** Observateur: hauteur du terrain */
	public int getHauteur();
	
	/** Observateur: retourne la position de la sortie */
	public PositionService getPosSortie();
	
	/** Observateur: retourne la position du hero */
	public PositionService getPosHero();
	
	/** Observateur: retourne le bloc a la position du hero 
	 * pre: isHeroVivant()
	 */
	public BlocService getBlocHero();
	
	/** Observateur: retourne le bloc a une position donnée */
	public BlocService getBloc(PositionService pos);
	
	/** Observateur: retourne le bloc voisin  de "bloc" dans la direction "dir" */
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir);
	
	/** Observateur: le heros est vivant? (pas ecrasé par un ROCHER ou a court de temps) */
	public boolean isHeroVivant();
	
	/** Observateur: il reste de diamants dans le terrain? (pas ecrasé par un ROCHER ou a court de temps) */
	public boolean isDiamantsRestants();
	
	/** Observateur: le bloc "bloc" peut être déplacé dans la direction "dir" ?*/
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir);
	
	
	// inv: getBlocVersDirection(bloc, dir) == getBloc(Position::deplacerVersDirection(Bloc::getPosition(), dir))
	// inv: isHeroVivant() == ∃x,y tq. Bloc::getType(getBloc(x, y)) = HERO
	// inv: isDiamantsRestants() == ∃x,y tq. Bloc::getType(getBloc(x, y)) = DIAMANT
	// inv: isDeplacementBlocPossible(bloc, dir) == in ¬Bloc::isSolide(getBlocVersDirection(bloc, dir))
			
	/** initialisation
	 *  pre: l > 0 ^ h > 0
	 *  post: getLargeur() == l
	 *  post: getHauteur() == h
	 *  post: getPosSortie() == null
	 *  post: getPosHero() == null
	 *  post: getBlocHero() == null
	 *  post: getBloc(p) == null
	 */
	public void init(int l, int h);
	
	/** definition d' un bloc de type "type" a une coordonnée (x,y) du terrain 
	 * post: Bloc::getType(getBloc(x,y)) = type
	 * post: getPosSortie(setBloc(type, x, y)) =
	 *			if type = SORTIE_FERMEE 
	 *			then
	 *				Position::init(t, x, y)
	 *			else
	 *				getPosSortie()
	 * post: getPosHero(setBloc(type, x, y)) =
	 *			if type = HERO 
	 *			then
	 *				Position::init(t, x, y)
	 *			else
	 *				getPosHero()
	 * post: getBlocHero(setBloc(type, x, y)) =
	 *			if type = HERO 
	 *			then
	 *				Bloc::init(type, Position::init(t, x, y))
	 *			else
	 *				getBlocHero()
	 * post: getBloc(setBloc(t, type, x, y), pos) =
	 *			let posBloc = Position::init(t, x, y)
	 *			in
	 *				if pos = posBloc then
	 *					Bloc::init(type, pos)
	 *				else
	 *					getBloc(pos)
	 */
	public void setBloc(TypeBloc type, int x, int y);
	
	/** déplace le bloc "bloc" d' un case dans la direction "dir"
	 * pre: isDeplacementBlocPossible(bloc, dir)
	 * post: getPosSortie() = getPosSortie()@pre
	 * 
	 */
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir);
	
	/** met a jour le terrain 
	 * post: getPosSortie() = getPosSortie()@pre 
	 * post: getPosHero() = getPosHero()@pre
	 * post: getBlocHero() = getBlocHero()@pre
	 */
	public void fairePasDeMiseAJour();
	
}

/*
 * 
 *Service: Terrain

Observations:

	
	[deplacerBlocVersDirection]
		getPosHero(deplacerBlocVersDirection(t, bloc, dir)) =
			if bloc = getBlocHero(t) then
				Bloc::getPosition(getBlocVersDirection(t, bloc, dir))
			else
				getPosHero(t)
		getBlocHero(deplacerBlocVersDirection(t, bloc, dir)) =
			if bloc = getBlocHero(t) then
				Bloc::setType(getBlocVersDirection(t, bloc, dir), HERO)
			else
				getBlocHero(t)
		getBloc(deplacerBlocVersDirection(t, bloc, dir), pos) =
			let* blocDest = getBlocVersDirection(t, bloc, dir)
			and posDest = Bloc::getPosition(blocDest)
			and posOrig = Bloc::getPosition(bloc)
			in
				if pos = posDest then
					Bloc::setType(blocDest, Bloc::getType(bloc))
				else if pos = posOrig then
					Bloc::setType(bloc, VIDE)
				else
					getBloc(t, pos)
	
	[fairePasDeMiseAJour]

		∀x ∈ 0..getLargeur(t) - 1, y ∈ getHauteur(t) - 1..0, pos = (x, y) getBloc(fairePasDeMiseAJour(t), pos) =
			let bloc = getBloc(t, pos)
			in
				if Bloc::isSortieFermee(bloc) ^ ¬isDiamantsRestants(t) then
					Bloc::setType(bloc, SORTIE_OUVERTE)
				else if Bloc::isTombable(bloc) ^ Bloc::isVide(getBlocVersDirection(t, bloc, BAS))
					Bloc::setType(bloc, VIDE)
				else if Bloc::isVide(bloc) ^ Bloc::isTombable(getBlocVersDirection(t, bloc, HAUT))
					Bloc::setType(bloc, Bloc::getType(getBlocVersDirection(t, bloc, HAUT)))
				else
					getBloc(t, pos)
					 
 */
