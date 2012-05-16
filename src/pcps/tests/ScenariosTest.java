package pcps.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.*;

import pcps.services.*;
import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.factories.Factory;

public class ScenariosTest {
	private MoteurJeuService mj;
	private TerrainService ter;

	public ScenariosTest() {
		Factory.createFactory();
		mj = null;
		ter = null;
	}

	@Before
	public void beforeTests() {
		mj = Factory.getFactory().creerMoteurJeu();
	}

	@After
	public void afterTests() {
		mj = null;
		ter = null;
	}

	public void checkMoteurJeuInvariant() {
		TerrainService terrain = mj.getTerrain();
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest;
		Direction dir;

		// inv: isPartieTerminee() == (getPasRestants() == 0 || !getTerrain().isHeroVivant() || isPartieGagnee)
		assertTrue(mj.isPartieTerminee() == (mj.getPasRestants() == 0 || !mj.getTerrain().isHeroVivant() || mj.isPartieGagnee()));

		// inv: isPartieGagnee() == (getTerrain().getPosSortie() == getTerrain().getPosHero())
		assertTrue(mj.isPartieGagnee() == (mj.getTerrain().getPosSortie().equals(mj.getTerrain().getPosHero())));

		// inv: \forall dir:Direction \in { GAUCHE, DROITE }, isDeplacementHeroPossible(dir) ==
		//          \let* terrain = getTerrain()
		//          \and blocHero = terrain.getBlocHero()
		//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
		//          \in !blocDest.isSolide()
		//              || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest, dir).isVide()
		dir = Direction.GAUCHE;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		assertTrue(mj.isDeplacementHeroPossible(dir) == (!blocDest.isSolide() || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest,  dir).isVide())));
		dir = Direction.DROITE;
		blocDest = terrain.getBlocVersDirection(blocHero, dir);
		assertTrue(mj.isDeplacementHeroPossible(dir) == (!blocDest.isSolide() || (blocDest.isDeplacable() && terrain.getBlocVersDirection(blocDest, dir).isVide())));

		// inv: \forall dir:Direction \in { HAUT, BAS }, isDeplacementHeroPossible(dir) ==
		//          \let* terrain = getTerrain()
		//          \and blocHero = terrain.getBlocHero()
		//          \and blocDest = terrain.getBlocVersDirection(blocHero, dir)
		//          \in !blocDest.isSolide()
		dir = Direction.HAUT;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		assertTrue(mj.isDeplacementHeroPossible(dir) == !blocDest.isSolide());

		dir = Direction.BAS;
		blocDest  = terrain.getBlocVersDirection(blocHero, dir);
		assertTrue(mj.isDeplacementHeroPossible(dir) == !blocDest.isSolide());
	}

	public void checkTerrainInvariant() {
		Set<Direction> allDirections = new TreeSet<Direction>();
		allDirections.add(Direction.HAUT);
		allDirections.add(Direction.BAS);
		allDirections.add(Direction.GAUCHE);
		allDirections.add(Direction.DROITE);

		// inv: getBlocHero() == getBlocDepuisPosition(getPosHero())
		if (ter.isHeroVivant()) { // pre de getBlocHero()
			assertTrue(ter.getBlocHero() == ter.getBlocDepuisPosition(ter.getPosHero()));
		}

		// inv:
		//   \forall bloc:Bloc \in getBlocs() {
		//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
		//           \let nouvellePosition = bloc.getPosition().deplacerVersDirection(dir)
		//           \in getBlocVersDirection(bloc, dir) == getBlocDepuisPosition(nouvellePosition)
		//       }
		//   }
		PositionService nouvellePosition;
		for (BlocService bloc : ter.getBlocs()) {
			for (Direction dir : allDirections) {
				nouvellePosition = bloc.getPosition().copy();
				nouvellePosition.deplacerVersDirection(dir);
				assertTrue(ter.getBlocVersDirection(bloc, dir) == ter.getBlocDepuisPosition(nouvellePosition));
			}
		}

		// inv: isHeroVivant() == (\existe bloc \in getBlocs(), bloc.getType() == HERO)
		boolean isHeroVivant = false;
		for (BlocService bloc : ter.getBlocs()) {
			if (bloc.getType() == TypeBloc.HERO) {
				isHeroVivant = true;
				break;
			}
		}
		assertTrue(ter.isHeroVivant() == isHeroVivant);

		// inv: isDiamantsRestants() == (\existe bloc \in getBlocs(), bloc.getType() == DIAMANT)
		boolean isDiamantsRestants = false;
		for (BlocService bloc : ter.getBlocs()) {
			if (bloc.getType() == TypeBloc.DIAMANT) {
				isDiamantsRestants = true;
				break;
			}
		}
		assertTrue(ter.isDiamantsRestants() == isDiamantsRestants);

		// inv:
		//   \forall bloc:Bloc \in getBlocs() {
		//       \forall dir:Direction \in { HAUT, BAS, GAUCHE, DROITE } {
		//           isDeplacementBlocPossible(bloc, dir) == !getBlocVersDirection(bloc, dir).isSolide()
		//       }
		//   }
		for (BlocService bloc : ter.getBlocs()) {
			for (Direction dir : allDirections) {
				assertTrue(ter.isDeplacementBlocPossible(bloc, dir) == !ter.getBlocVersDirection(bloc, dir).isSolide());
			}
		}

		// inv: getBlocDepuisPosition(pos) == getBloc(pos.getX(), pos.getY())

		// inv: getBlocs() == \sum { ((getBloc(x, y) \for x \in [0..getLargeur() - 1]) \for y \in [0..getHauteur() - 1]) }
		Set<BlocService> allBlocs = new HashSet<BlocService>();
		for (int x = 0; x <= ter.getLargeur() - 1; x++) {
			for (int y = 0; y <= ter.getHauteur() - 1; y++) {
				allBlocs.add(ter.getBloc(x, y));
			}
		}
		assertTrue(ter.getBlocs().equals(allBlocs));
	}


	@Test
	public void Scenario1_PartieGagnee() {
		TerrainService ter1 = Stub.getTER1();
		PositionService posHero;
		PositionService posSortie;

		// préambule
		mj.init(Stub.getTER1(), 30);
		ter = mj.getTerrain();

		/** A. Récupérer le diamant **/

		// contenu
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.DROITE));
		mj.deplacerHero(Direction.DROITE);
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.DROITE));
		mj.deplacerHero(Direction.DROITE);

		// oracle
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.DROITE);
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.DROITE);
		assertTrue(mj.getPasRestants() == 30 - 2 && ter.equals(ter1));
		checkMoteurJeuInvariant();

		/** B. Mettre à jour le terrain **/

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 4, 1);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					if (!bloc.isVide()) fail();
				} else if (x == 1 && y == 2) {
					if (bloc.getType() != TypeBloc.ROCHER) fail();
				} else if (x == 4 && y == 2) {
					if (bloc.getType() != TypeBloc.SORTIE_OUVERTE) fail();
				} else if (!bloc.equals(ter1.getBloc(x, y))) {
					 fail();
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkTerrainInvariant();

		/** C. Rejoindre la sortie **/

		// contenu
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.BAS));
		mj.deplacerHero(Direction.BAS);

		// oracle
		ter1.fairePasDeMiseAJour();
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.BAS);
		assertTrue(mj.getPasRestants() == 28 - 1 && ter.equals(ter1));
		checkMoteurJeuInvariant();

		/** D. Mettre à jour le terrain **/

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 4, 2);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (!bloc.equals(ter1.getBloc(x, y)))
					fail();
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));

		checkTerrainInvariant();

		/** E. Constater la victoire **/

		// oracle
		assertTrue(mj.isPartieGagnee());
	}


	@Test
	public void Scenario2_PartiePerdu_ManqueDePas() {
		TerrainService ter1 = Stub.getTER1();
		PositionService posHero;
		PositionService posSortie;

		// préambule
		mj.init(Stub.getTER1(), 2);
		ter = mj.getTerrain();

		/** A. Se déplacer jusqu'à épuiser le nombre de pas **/

		// contenu
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.DROITE));
		mj.deplacerHero(Direction.DROITE);
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.DROITE));
		mj.deplacerHero(Direction.DROITE);

		// oracle
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.DROITE);
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.DROITE);
		assertTrue(mj.getPasRestants() == 2 - 2 && ter.equals(ter1));
		checkMoteurJeuInvariant();

		/** B. Mettre à jour le terrain **/

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 4, 1);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					if (!bloc.isVide()) fail();
				} else if (x == 1 && y == 2) {
					if (bloc.getType() != TypeBloc.ROCHER) fail();
				} else if (x == 4 && y == 2) {
					if (bloc.getType() != TypeBloc.SORTIE_OUVERTE) fail();
				} else if (!bloc.equals(ter1.getBloc(x, y))) {
					 fail();
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkTerrainInvariant();

		/** C. Constater la défaite **/

		// oracle
		assertTrue(mj.isPartieTerminee() && !mj.isPartieGagnee());
	}


	@Test
	public void Scenario3_PartiePerdu_MortDuHero() {
		TerrainService ter1 = Stub.getTER1();
		PositionService posHero;
		PositionService posSortie;

		// préambule
		mj.init(Stub.getTER1(), 30);
		ter = mj.getTerrain();

		/** A. Se déplacer sous le rocher **/

		// contenu
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.BAS));
		mj.deplacerHero(Direction.BAS);
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.GAUCHE));
		mj.deplacerHero(Direction.GAUCHE);

		// oracle
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.BAS);
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.GAUCHE);
		assertTrue(mj.getPasRestants() == 30 - 2 && ter.equals(ter1));
		checkMoteurJeuInvariant();

		/** B. Mettre à jour le terrain **/

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 1, 2);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					if (!bloc.isVide()) fail();
				} else if (x == 1 && y == 2) {
					if (bloc.getType() != TypeBloc.ROCHER) fail();
				} else if (!bloc.equals(ter1.getBloc(x, y))) {
					 fail();
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkTerrainInvariant();

		/** C. Constater la mort du héro **/

		// oracle
		assertTrue(!ter.isHeroVivant());
	}


	@Test
	public void Scenario4_DeplacerUnRocher() {
		TerrainService ter1 = Stub.getTER1();
		PositionService posHero;
		PositionService posSortie;

		// préambule
		mj.init(Stub.getTER1(), 30);
		ter = mj.getTerrain();

		/** A. Pousser le rocher vers la gauche **/

		// contenu
		assertTrue(!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(Direction.GAUCHE));
		mj.deplacerHero(Direction.GAUCHE);

		// oracle
		ter1.deplacerBlocVersDirection(ter1.getBloc(1, 1), Direction.GAUCHE);
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.GAUCHE);
		assertTrue(mj.getPasRestants() == 30 - 1 && ter.equals(ter1));
		checkMoteurJeuInvariant();

		/** B. Constater le déplacement du rocher **/

		// oracle
		assertTrue(ter.getBloc(0, 1).getType() == TypeBloc.ROCHER);

		/** C. Mettre à jour le terrain **/

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 1, 1);
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 0 && y == 1) {
					if (!bloc.isVide()) fail();
				} else if (x == 0 && y == 2) {
					if (bloc.getType() != TypeBloc.ROCHER) fail();
				} else if (!bloc.equals(ter1.getBloc(x, y))) {
					 fail();
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkTerrainInvariant();
	}
}
