package pcps.services;


public interface TerrainService {
	/** Observator: largeur du terrain */
	public int getLargeur();
	
	/** Observator: hauteur du terrain */
	public int getHauteur();
	
	/** Observator: position de la sortie */
	public PositionService getPosSortie();
	
	/** Observator: position courante du héro */
	public PositionService getPosHero();
	
	/**
	 * Observator: bloc représentant le héro
	 *   pre: isHeroVivant()
	 */
	public BlocService getBlocHero();
	
	/** Observator: bloc à une position donnée */
	public BlocService getBloc(PositionService pos);
	
	/** Observator: bloc voisin  d'un bloc donné dans une direction donnée */
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir);
	
	/** Observator: le heros est-il vivant ? (= y a t-il un bloc HERO sur le terrain) */
	public boolean isHeroVivant();
	
	/** Observator: reste-t-il des diamants sur le terrain? */
	public boolean isDiamantsRestants();
	
	/** Observator: le bloc donné peut-il être déplacé dans la direction donnée ? */
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir);
	
	/** Invariants */
	// inv: getBlocVersDirection(bloc, dir) == bloc.getPosition().deplacerVersDirection(dir)
	// inv: isHeroVivant() == (\existe x,y \tq getBloc(x, y).getType() == HERO)
	// inv: isDiamantsRestants() == (\existe x,y \tq getBloc(x, y).getType() == DIAMANT)
	// inv: isDeplacementBlocPossible(bloc, dir) == !getBlocVersDirection(bloc, dir).isSolide()
			
	/**
	 * Constructor init:
	 *   pre: l > 0 ^ h > 0
	 *   post: getLargeur() == l
	 *   post: getHauteur() == h
	 *   post: getPosSortie() == null
	 *   post: getPosHero() == null
	 *   post: getBlocHero() == null
	 *   post: getBloc(p) == null
	 */
	public void init(int l, int h);
	
	/**
	 * Operator setBloc: définie le type du bloc aux coordonnées données 
	 *   post:
	 *     \if type == SORTIE_FERMEE \then
	 *         getPosSortie() == Position().init(this, x, y)
	 *     \else
	 *         getPosSortie() == getPosSortie()@pre
	 *   post:
	 *     \if type == HERO \then
	 *         getPosHero() == Position().init(this, x, y)
	 *     \else
	 *         getPosHero() == getPosHero()@pre
	 *   post:
	 *     \if type == HERO \then
	 *         getBlocHero() == Bloc().init(type, Position().init(this, x, y))
	 *     \else
	 *         getBlocHero() == getBlocHero()@pre
	 *   post:
	 *     \let posBloc = Position().init(this, x, y) \in
	 *         \if pos == posBloc \then
	 *             getBloc(pos) == Bloc().init(type, pos)
	 *         \else
	 *             getBloc(pos) == getBloc(pos)@pre
	 */
	public void setBloc(TypeBloc type, int x, int y);
	
	/**
	 * Operator deplacerBlocVersDirection: déplacer le bloc donné d'une case vers la direction donnée
	 *   pre: isDeplacementBlocPossible(bloc, dir)
	 *   post: getPosSortie() == getPosSortie()@pre
	 *   post:
	 *     \if bloc == getBlocHero() \then
	 *         getPosHero() == getBlocVersDirection(bloc, dir)
	 *     \else
	 *         getPosHero() == getPosHero()@pre
	 *   post:
	 *     \if bloc == getBlocHero() \then
	 *         getBlocHero() == getBlocVersDirection(bloc, dir).setType(HERO)
	 *     \else
	 *         getBlocHero() == getBlocHero()@pre
	 *   post:
	 *     \let* blocDest = getBlocVersDirection(bloc, dir)
	 *     \and posDest = blocDest.getPosition()
	 *     \and posOrig = bloc.getPosition()
	 *     \in
	 *         \if pos == posDest \then
	 *             getBloc(pos) == blocDest.setType(bloc.getType())
	 *         \else \if pos == posOrig \then
	 *             getBloc(pos) == bloc.setType(VIDE)
	 *         \else
	 *             getBloc(pos) == getBloc(pos)@pre
	 */
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir);
	
	/**
	 * Operator fairePasDeMiseAJour: fait un pas de mise à jour du terrain,
	 * soit:
	 * 	- ouvre la sortie si tous les diamants sont ramassés
	 *  - déplace les objets tombables d'une case vers le bas 
	 * 
	 *   post: getPosSortie() = getPosSortie()@pre 
	 *   post: getPosHero() = getPosHero()@pre
	 *   post: getBlocHero() = getBlocHero()@pre
	 *   \forall x:integer \in [0..getLargeur()-1] {
	 *       \forall y:integer \in [getHauteur()-1..0] {
	 *           \let bloc = getBloc(Position().init(this, x, y)) \in
	 *               \if bloc.isSortieFermee() && !isDiamantsRestants() \then
	 *                   bloc == bloc@pre.setType(SORTIE_OUVERTE)
	 *               \else \if bloc.isTombable() && getBlocVersDirection(bloc, BAS).isVide() \then
	 *                   bloc == bloc@pre.setType(VIDE)
	 *               \else \if bloc.isVide() && getBlocVersDirection(bloc, HAUT).isTombable() \then
	 *                   bloc == bloc@pre.setType(bloc, getBlocVersDirection(bloc, HAUT).getType)
	 *               \else
	 *                   bloc == bloc@pre
	 *       }
	 *   }
	 */
	public void fairePasDeMiseAJour();	
}