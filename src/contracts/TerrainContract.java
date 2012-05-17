package contracts;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import decorators.TerrainDecorator;
import enums.Direction;
import enums.TypeBloc;
import factories.Factory;

import services.BlocService;
import services.PositionService;
import services.TerrainService;

public class TerrainContract extends TerrainDecorator {

	public TerrainContract(TerrainService delegate) {
		super(delegate);
	}
	
	@Override
	public TerrainService copy() {
		return new TerrainContract(super.copy());
	}
	
	public void checkInvariant() {
		Set<Direction> allDirections = new TreeSet<Direction>();
		allDirections.add(Direction.HAUT);
		allDirections.add(Direction.BAS);
		allDirections.add(Direction.GAUCHE);
		allDirections.add(Direction.DROITE);

		// inv: getBlocHero() == getBlocDepuisPosition(getPosHero())
		if (isHeroVivant()) { // pre de getBlocHero()
			if (!(getBlocHero() == getBlocDepuisPosition(getPosHero())))
				Contractor.defaultContractor().invariantError("TerrainService", "getBlocHero() doit retourner le bloc à la position retournée par getPosHero().");
		}
		
		// inv:
		//   \forall bloc:Bloc \in getBlocs() {
		//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
		//           \let nouvellePosition = bloc.getPosition().deplacerVersDirection(dir)
		//           \in getBlocVersDirection(bloc, dir) == getBlocDepuisPosition(nouvellePosition)
		//       }
		//   }
		PositionService nouvellePosition;
		for (BlocService bloc : getBlocs()) {
			for (Direction dir : allDirections) {
				nouvellePosition = bloc.getPosition().copy();
				nouvellePosition.deplacerVersDirection(dir);
				if (!(getBlocVersDirection(bloc, dir) == getBlocDepuisPosition(nouvellePosition)))
					Contractor.defaultContractor().invariantError("TerrainService", "Le bloc obtenu après déplacement dans une direction doit être le bloc situé à la position du premier bloc, déplacée d'un pas dans la direction voulue.");
			}
		}
		
		// inv: isHeroVivant() == (\existe bloc \in getBlocs(), bloc.getType() == HERO)
		boolean isHeroVivant = false;
		for (BlocService bloc : getBlocs()) {
			if (bloc.getType() == TypeBloc.HERO) {
				isHeroVivant = true;
				break;
			}
		}
		if (!(isHeroVivant() == isHeroVivant))
			Contractor.defaultContractor().invariantError("TerrainService", "Le héro doit être considéré vivant s'il existe un bloc de type HERO.");
		
		// inv: isDiamantsRestants() == (\existe bloc \in getBlocs(), bloc.getType() == DIAMANT)
		boolean isDiamantsRestants = false;
		for (BlocService bloc : getBlocs()) {
			if (bloc.getType() == TypeBloc.DIAMANT) {
				isDiamantsRestants = true;
				break;
			}
		}
		if (!(isDiamantsRestants() == isDiamantsRestants))
			Contractor.defaultContractor().invariantError("TerrainService", "On doit considérer qu'il reste des diamants sur le terrain s'il y a au moins un bloc de type DIAMANT.");
		
		// inv:
		//   \forall bloc:Bloc \in getBlocs() {
		//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
		//           isDeplacementBlocPossible(bloc, dir) == !getBlocVersDirection(bloc, dir).isSolide()
		//       }
		//   }
		for (BlocService bloc : getBlocs()) {
			for (Direction dir : allDirections) {
				if (!(isDeplacementBlocPossible(bloc, dir) == !getBlocVersDirection(bloc, dir).isSolide()))
					Contractor.defaultContractor().invariantError("TerrainService", "Le bloc peut être déplacé dans une direction uniquement si le bloc adjacent n'est pas considéré solide.");
			}
		}
		
		// inv: getBlocDepuisPosition(pos) == getBloc(pos.getX(), pos.getY())
		
		// inv: getBlocs() == \sum { ((getBloc(x, y) \for x \in [0..getLargeur() - 1]) \for y \in [0..getHauteur() - 1]) }
		Set<BlocService> allBlocs = new HashSet<BlocService>();
		for (int x = 0; x <= getLargeur() - 1; x++) {
			for (int y = 0; y <= getHauteur() - 1; y++) {
				allBlocs.add(getBloc(x, y));
			}
		}
		if (!(getBlocs().equals(allBlocs)))
			Contractor.defaultContractor().invariantError("TerrainService", "L'ensemble des blocs retournés par le service doit être l'ensemble des blocs situés à toutes les coordonnées possibles du terrain.");
	}

	@Override
	public BlocService getBlocHero() {
		// pre: isHeroVivant()
		if (!(isHeroVivant()))
			Contractor.defaultContractor().preconditionError("TerrainService", "getBlocHero", "Le héro doit être vivant pour pouvoir retourner son bloc.");
		
		return super.getBlocHero();
	}
	
	@Override
	public BlocService getBlocDepuisPosition(PositionService pos) {
		return getDelegate().getBlocDepuisPosition(pos);
	}
	
	@Override
	public BlocService getBloc(int x, int y) {
		return getDelegate().getBloc(x, y);
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		return super.getBlocVersDirection(bloc, dir);
	}
	
	@Override
	public Set<BlocService> getBlocs() {
		return super.getBlocs();
	}

	@Override
	public boolean isHeroVivant() {
		return super.isHeroVivant();
	}

	@Override
	public boolean isDiamantsRestants() {
		return super.isDiamantsRestants();
	}

	@Override
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir) {
		return super.isDeplacementBlocPossible(bloc, dir);
	}

	@Override
	public void init(int l, int h) {
		// pre: l > 0 && h > 0
		if (!(l > 0 && h > 0))
			Contractor.defaultContractor().preconditionError("TerrainService", "init", "La largeur et la hauteur du terrain passés au constructeur doivent être strictement positifs.");
		
		// run
		super.init(l, h);
		
		// invariant@post
		checkInvariant();
		
		// post: getLargeur() == l
		if (!(getLargeur() == l))
			Contractor.defaultContractor().postconditionError("TerrainService", "init", "getLargeur() doit retourner la largeur passé en argument.");
		
		// post: getHauteur() == h
		if (!(getHauteur() == h))
			Contractor.defaultContractor().postconditionError("TerrainService", "init", "getHauteur() doit retourner la hauteur passé en argument.");
		
		// post: getPosSortie() == null
		if (!(getPosSortie() == null))
			Contractor.defaultContractor().postconditionError("TerrainService", "init", "La position de la sortie doit être null à l'initialisation.");
		
		// post: getPosHero() == null
		if (!(getPosHero() == null))
			Contractor.defaultContractor().postconditionError("TerrainService", "init", "La position du héro doit être null à l'initialisation.");
		
		// post:
		//   \forall x:integer \in [0..getLargeur()-1] {
		//       \forall y:integer \in [0..getHauteur()-1] {
		//           \let* bloc = getBloc(x, y)
		//           \and blocPos = bloc.getPosition()
		//           \in bloc.isVide() && blocPos.getX() == x && blocPos.getY() == y
		//       }
		//   }
		BlocService bloc;
		PositionService blocPos;
		for (int x1 = 0; x1 <= getLargeur() - 1; x1++) {
			for (int y1 = 0; y1 <= getHauteur() - 1; y1++) {
				bloc = getBloc(x1, y1);
				blocPos = bloc.getPosition();
				if (!(bloc.isVide() && blocPos.getX() == x1 && blocPos.getY() == y1))
					Contractor.defaultContractor().postconditionError("TerrainService", "init", "Tous les blocs doivent être de type VIDE et avoir leur position correctement positionnée à l'initialisation.");
			}
		}
	}

	@Override
	public void setBloc(TypeBloc type, int x, int y) {
		// captures
		PositionService getPosSortieAtPre = getPosSortie();
		if (getPosSortieAtPre != null)
			getPosSortieAtPre = getPosSortieAtPre.copy();
		PositionService getPosHeroAtPre = getPosHero();
		if (getPosHeroAtPre != null)
			getPosHeroAtPre = getPosHeroAtPre.copy();
		BlocService getBlocXYAtPre = getBloc(x, y).copy();
		
		// on fait une copie manuelle du terrain pour éviter la boucle dans le contrat
		TerrainService terrainAtPre = Factory.getFactory().creerTerrain();
		terrainAtPre.init(getLargeur(), getHauteur());
		for(int y2 = 0; y2 < getHauteur(); y2++) {
			for(int x2 = 0; x2 < getLargeur(); x2++) {
				((TerrainContract)terrainAtPre).getDelegate().setBloc(getBloc(x2, y2).getType(), x2, y2);
			}
		}
		
		// invariant@pre
		checkInvariant();
		
		// run
		super.setBloc(type, x, y);
		
		// invariant@post
		checkInvariant();
		
		// post:
		//   \if type \in { SORTIE_FERMEE, SORTIE_OUVERTE } \then
		//       getPosSortie() == getBloc(x, y)@pre.getPosition()
		//   \else
		//       getPosSortie() == getPosSortie()@pre
		if (type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.SORTIE_OUVERTE) {
			if (!(getPosSortie().equals(getBlocXYAtPre.getPosition())))
				Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Si le type du bloc défini est une sortie, alors la position retournée par getPosSortie() doit être les coordonnées données.");
		} else {
			if (!((getPosSortieAtPre == null && getPosSortie() == getPosSortieAtPre) || getPosSortie().equals(getPosSortieAtPre)))
				Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Si le type du bloc défini n'est pas une sortie, alors la position retournée par getPosSorite() ne doit pas changer.");
		}
		
		// post:
		//   \if type == HERO \then
		//       getPosHero() == getBloc(x, y)@pre.getPosition()
		//   \else
		//       getPosHero() == getPosHero()@pre
		if (type == TypeBloc.HERO) {
			if (!(getPosHero().equals(getBlocXYAtPre.getPosition())))
				Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Si le type du bloc défini est HERO, alors la position retournée par getPosHero() doit être les coordonnées données.");
		} else {
			if (!((getPosHeroAtPre == null && getPosHero() == getPosHeroAtPre) || getPosHero().equals(getPosHeroAtPre)))
				Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Si le type du bloc défini n'est pas HERO, alors la position retournée par getPosHero() ne doit pas changer.");
		}
		
		// post:
		//  \forall x':integer \in [0..getLargeur()-1] {
		//  \forall y':integer \in [0..getHauteur()-1] {
		//      if (x == x' && y == y')
		//          getBloc(x', y') == getBloc(x', y')@pre.setType(type)
		//      else
		//          getBloc(x', y') == getBloc(x', y')@pre
		//  }}
		for (int x1 = 0; x1 <= getLargeur() - 1; x1++) {
		for (int y1 = 0; y1 <= getHauteur() - 1; y1++) {
			if (x == x1 && y == y1) {
				terrainAtPre.getBloc(x1, y1).setType(type);
				if (!(getBloc(x1, y1).equals(terrainAtPre.getBloc(x1, y1))))
					Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Le type du bloc situé aux coordonnées données doit être celui donné en argument à setBloc().");
			} else {
				if (!(getBloc(x1, y1).equals(terrainAtPre.getBloc(x1, y1))))
					Contractor.defaultContractor().postconditionError("TerrainService", "setBloc", "Le type des blocs n'appartenant pas aux coordonnées données doivent rester inchangés.");
			}
		}}
	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		// captures
		boolean isBlocHero = (bloc == getBlocHero());
		PositionService getPosSortieAtPre = getPosSortie();
		if (getPosSortieAtPre != null) getPosSortieAtPre = getPosSortieAtPre.copy();
		PositionService getPosHeroAtPre = getPosHero();
		if (getPosHeroAtPre != null) getPosHeroAtPre = getPosHeroAtPre.copy();
		BlocService blocAtPre = bloc.copy();
		TypeBloc typeBlocAtPre = blocAtPre.getType();
		BlocService blocDestAtPre = getBlocVersDirection(bloc, dir).copy();
		TerrainService terrainAtPre = this.copy();
		
		// pre: isDeplacementBlocPossible(bloc, dir)
		if (!(isDeplacementBlocPossible(bloc, dir)))
			Contractor.defaultContractor().preconditionError("TerrainService", "deplacerBlocVersDirection", "L'utilisation de deplacerBlocVersDirection() n'est possible que si le bloc peut être déplacé dans la direction donnée.");
		
		// invariant@pre
		checkInvariant();
		
		// run
		super.deplacerBlocVersDirection(bloc, dir);
		
		// invariant@post
		checkInvariant();
		
		// post: getPosSortie() == getPosSortie()@pre
		if (!(getPosSortie().equals(getPosSortieAtPre)))
			Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDirection", "La position de la sortie ne doit pas changer durant le déplacement d'un bloc.");
		
		// post:
		//   \if bloc == getBlocHero() \then
		//       getPosHero() == getBlocVersDirection(bloc, dir).getPosition()
		//   \else
		//       getPosHero() == getPosHero()@pre
		if (isBlocHero) {
			if (!(getPosHero().equals(getBlocVersDirection(bloc, dir).getPosition())))
				Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDirection", "Si le bloc à déplacer est le héro, alors la position retournée par getPosHero() doit être les coordonnées données.");
		} else {
			if (!(getPosHero().equals(getPosHeroAtPre)))
				Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDirection", "Si le bloc à déplacer n'est pas le héro, alors la position du héro doit rester inchangée.");
		}
		
		// post:
		//   \forall x':integer \in [0..getLargeur()-1] {
		//   \forall y':integer \in [0..getHauteur()-1] {
		//       \let* blocPos = bloc.getPosition()
		//       \and blocX = blocPos.getX()
		//       \and blocY = blocPos.getY() 
		//       \and blocDest = getBlocVersDirection(bloc, dir)
		//       \and blocDestPos = blocDest.getPosition()
		//       \and blocDestX = blocDestPos.getX()
		//       \and blocDestY = blocDestPos.getY()
		//       \in
		//           \if blocX == x' && blocY == y' \then
		//               getBloc(x', y') == bloc@pre.setType(VIDE)
		//           \else \if blocDestX == x' && blocDestY == y' \then
		//               getBloc(x', y') == blocDest@pre.setType(bloc@pre.getType())
		//           \else
		//               getBloc(x', y') == getBloc(x', y')@pre
		for (int x = 0; x <= getLargeur() - 1; x++) {
		for (int y = 0; y <= getHauteur() - 1; y++) {
			PositionService blocPos = bloc.getPosition();
			int blocX = blocPos.getX();
			int blocY = blocPos.getY();
			BlocService blocDest = getBlocVersDirection(bloc, dir);
			PositionService blocDestPos = blocDest.getPosition();
			int blocDestX = blocDestPos.getX();
			int blocDestY = blocDestPos.getY();
			if (blocX == x && blocY == y) {
				blocAtPre.setType(TypeBloc.VIDE);
				if (!(getBloc(x, y).equals(blocAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDirection", "L'ancienne position du bloc déplacé doit être vide.");
			} else if (blocDestX == x && blocDestY == y) {
				blocDestAtPre.setType(typeBlocAtPre);
				if (!(getBloc(x, y).equals(blocDestAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDestination", "La position de destination du bloc déplacé doit être du type du bloc déplacé.");
			} else {
				if (!getBloc(x, y).equals(terrainAtPre.getBloc(x, y)))
					Contractor.defaultContractor().postconditionError("TerrainService", "deplacerBlocVersDestination", "Les blocs non impactés par le déplacement doivent rester inchangés.");
			}
		}}
	}

	@Override
	public void fairePasDeMiseAJour() {
		// captures
		PositionService getPosSortieAtPre = getPosSortie().copy();
		PositionService getPosHeroAtPre = getPosHero().copy();
		TerrainService terrainAtPre = this.copy();
		
		// invariant@pre
		checkInvariant();
		
		// run
		super.fairePasDeMiseAJour();
		
		// invariant@post
		checkInvariant();
		
		// post: getPosSortie() == getPosSortie()@pre
		if (!(getPosSortie().equals(getPosSortieAtPre)))
			Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "La position de la sortie ne doit pas changer durant un pas de mise à jour.");
		
		// post: getPosHero() == getPosHero()@pre
		if (!(getPosHero().equals(getPosHeroAtPre)))
			Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "La position du héro ne doit pas changer durant un pas de mise à jour.");
		
		// post:
		//   \forall x:integer \in [0..getLargeur()-1] {
		//   \forall y:integer \in [0..getHauteur()-1] {
		//       \let bloc = getBloc@pre(x, y)
		//       \in
		//           \if bloc.isSortieFermee() && !isDiamantsRestants() \then
		//               getBloc(x, y) == bloc.setType(SORTIE_OUVERTE)
		//           \else \if bloc.isTombable() && getBlocVersDirection(bloc, BAS).isVide() \then
		//               getBloc(x, y) == bloc.setType(VIDE)
		//           \else \if bloc.isVide() && getBlocVersDirection(bloc, HAUT).isTombable() \then
		//               getBloc(x, y) == bloc.setType(bloc, getBlocVersDirection(bloc, HAUT).getType)
		//           \else
		//               getBloc(x, y) == bloc
		//   }
		//   }
		BlocService blocAtPre;
		
		for (int x = 0; x <= getLargeur() - 1; x++) {
		for (int y = 0; y <= getHauteur() - 1; y++) {
			blocAtPre = terrainAtPre.getBloc(x, y);
			if (blocAtPre.isSortieFermee() && !isDiamantsRestants()) {
				blocAtPre.setType(TypeBloc.SORTIE_OUVERTE);
				if (!(getBloc(x, y).equals(blocAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "Tout bloc de type SORTIE_FERMEE doit être ouvert après un pas de mise à jour s'il n'y a plus de diamants.");
			} else if (blocAtPre.isTombable() && getBlocVersDirection(blocAtPre, Direction.BAS).isVide()) {
				blocAtPre.setType(TypeBloc.VIDE);
				if (!(getBloc(x, y).equals(blocAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "Tout bloc tombable doit être VIDE après un pas de mise à jour.");
			} else if (blocAtPre.isVide() && getBlocVersDirection(blocAtPre, Direction.HAUT).isTombable()) {
				blocAtPre.setType(getBlocVersDirection(blocAtPre, Direction.HAUT).getType());
				if (!(getBloc(x, y).equals(blocAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "Tout bloc situé sous un bloc tombable doit devenir du type du bloc tombable.");
			} else {
				if (!(getBloc(x, y).equals(blocAtPre)))
					Contractor.defaultContractor().postconditionError("TerrainService", "fairePasDeMiseAJour", "Tout bloc non impacté par un pas de mise à jour doit rester inchangé.");
			}
		}}
	}
}
