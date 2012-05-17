package services;

import java.util.Set;

import enums.Direction;
import enums.TypeBloc;



public interface TerrainService {
	public TerrainService copy();
	
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
	public BlocService getBlocDepuisPosition(PositionService pos);
	
	/** Observator: bloc à des coordonnées données */
	public BlocService getBloc(int x, int y);
	
	/** Observator: bloc voisin  d'un bloc donné dans une direction donnée */
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir);
	
	/** Observator: ensemble des blocs du terrain */
	public Set<BlocService> getBlocs();
	
	/** Observator: le heros est-il vivant ? (= y a t-il un bloc HERO sur le terrain) */
	public boolean isHeroVivant();
	
	/** Observator: reste-t-il des diamants sur le terrain? */
	public boolean isDiamantsRestants();
	
	/** Observator: le bloc donné peut-il être déplacé dans la direction donnée ? */
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir);
	
	/** Invariants */
	// inv: getBlocHero() == getBlocDepuisPosition(getPosHero())
	// inv:
	//   \forall bloc:Bloc \in getBlocs() {
	//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
	//           \let nouvellePosition = bloc.getPosition().deplacerVersDirection(dir)
	//           \in getBlocVersDirection(bloc, dir) == getBlocDepuisPosition(nouvellePosition)
	//       }
	//   }
	// inv: isHeroVivant() == (\existe bloc \in getBlocs(), bloc.getType() == HERO)
	// inv: isDiamantsRestants() == (\existe bloc \in getBlocs(), bloc.getType() == DIAMANT)
	// inv:
	//   \forall bloc:Bloc \in getBlocs() {
	//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
	//           isDeplacementBlocPossible(bloc, dir) == !getBlocVersDirection(bloc, dir).isSolide()
	//       }
	//   }
	// inv: getBlocDepuisPosition(pos) == getBloc(pos.getX(), pos.getY())
	// inv: getBlocs() == \sum { ((getBloc(x, y) \for x \in [0..getLargeur() - 1]) \for y \in [0..getHauteur() - 1]) }
	
	/**
	 * Constructor init:
	 *   pre: l > 0 && h > 0
	 *   post: getLargeur() == l
	 *   post: getHauteur() == h
	 *   post: getPosSortie() == null
	 *   post: getPosHero() == null
	 *   post: getBlocHero() == null
	 *   post:
	 *     \forall x:integer \in [0..getLargeur()-1] {
	 *         \forall y:integer \in [0..getHauteur()-1] {
	 *             \let* bloc = getBloc(x, y)
	 *             \and blocPos = bloc.getPosition()
	 *             \in bloc.isVide() && blocPos.getX() == x && blocPos.getY() == y
	 *         }
	 *     }
	 */
	public void init(int l, int h);
	
	/**
	 * Operator setBloc: définie le type du bloc aux coordonnées données 
	 *   post:
	 *     \if type \in { SORTIE_FERMEE, SORTIE_OUVERTE } \then
	 *         getPosSortie() == getBloc(x, y)@pre.getPosition()
	 *     \else
	 *         getPosSortie() == getPosSortie()@pre
	 *   post:
	 *     \if type == HERO \then
	 *         getPosHero() == getBloc(x, y)@pre.getPosition()
	 *     \else
	 *         getPosHero() == getPosHero()@pre
	 *   post:
	 *     \forall x':integer \in [0..getLargeur()-1] {
	 *     \forall y':integer \in [0..getHauteur()-1] {
	 *         if (x == x' && y == y')
	 *             getBloc(x', y') == getBloc(x', y')@pre.setType(type)
	 *         else
	 *             getBloc(x', y') == getBloc(x', y')@pre
	 *     }}
	 */
	public void setBloc(TypeBloc type, int x, int y);
	
	/**
	 * Operator deplacerBlocVersDirection: déplacer le bloc donné d'une case vers la direction donnée
	 *   pre: isDeplacementBlocPossible(bloc, dir)
	 *   post: getPosSortie() == getPosSortie()@pre
	 *   post:
	 *     \if bloc == getBlocHero() \then
	 *         getPosHero() == getBlocVersDirection(bloc, dir).getPosition()
	 *     \else
	 *         getPosHero() == getPosHero()@pre
	 *   post:
	 *     \forall x':integer \in [0..getLargeur()-1] {
	 *     \forall y':integer \in [0..getHauteur()-1] {
	 *         \let* blocPos = bloc.getPosition()
	 *         \and blocX = blocPos.getX()
	 *         \and blocY = blocPos.getY() 
	 *         \and blocDest = getBlocVersDirection(bloc, dir)
	 *         \and blocDestPos = blocDest.getPosition()
	 *         \and blocDestX = blocDestPos.getX()
	 *         \and blocDestY = blocDestPos.getY()
	 *         \in
	 *             \if blocX == x' && blocY == y' \then
	 *                 getBloc(x', y') == bloc@pre.setType(VIDE)
	 *             \else \if blocDestX == x' && blocDestY == y' \then
	 *                 getBloc(x', y') == blocDest@pre.setType(bloc@pre.getType())
	 *             \else
	 *                 getBloc(x', y') == getBloc(x', y')@pre
	 */
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir);
	
	/**
	 * Operator fairePasDeMiseAJour: fait un pas de mise à jour du terrain,
	 * soit:
	 * 	- ouvre la sortie si tous les diamants sont ramassés
	 *  - déplace les objets tombables d'une case vers le bas 
	 * 
	 *   post: getPosSortie() == getPosSortie()@pre 
	 *   post: getPosHero() == getPosHero()@pre
	 *   post:
	 *     \forall x:integer \in [0..getLargeur()-1] {
	 *     \forall y:integer \in [0..getHauteur()-1] {
	 *         \let bloc = getBloc@pre(x, y)
	 *         \in
	 *             \if bloc.isSortieFermee() && !isDiamantsRestants() \then
	 *                 getBloc(x, y) == bloc.setType(SORTIE_OUVERTE)
	 *             \else \if bloc.isTombable() && getBlocVersDirection(bloc, BAS).isVide() \then
	 *                 getBloc(x, y) == bloc.setType(VIDE)
	 *             \else \if bloc.isVide() && getBlocVersDirection(bloc, HAUT).isTombable() \then
	 *                 getBloc(x, y) == bloc.setType(bloc, getBlocVersDirection(bloc, HAUT).getType)
	 *             \else
	 *                 getBloc(x, y) == bloc
	 *     }
	 *     }
	 */
	public void fairePasDeMiseAJour();	
}