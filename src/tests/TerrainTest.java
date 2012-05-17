package tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.*;

import contracts.ContractError;

import enums.Direction;
import enums.TypeBloc;
import factories.Factory;

import services.*;

public class TerrainTest {
	private TerrainService ter;

	public TerrainTest() {
		ter = null;
	}

	@Before
	public void beforeTests() {
		ter = Factory.getFactory().creerTerrain();
	}

	@After
	public void afterTests() {
		ter = null;
	}

	public void checkInvariant() {
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


	/** Couverture des préconditions **/

	@Test
	public void Terrain_getBlocHero_pre_true() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.isHeroVivant());
		ter.getBlocHero();
		assertTrue(true);
		checkInvariant();
	}


	@Test
	public void Terrain_getBlocHero_pre_false() {
		// préambule
		ter.init(5, 5);

		// oracle
		assertFalse(ter.isHeroVivant());
		try {
			ter.getBlocHero();
			fail();
		} catch (Exception e) {
			assertTrue(true);
			checkInvariant();
		} catch (ContractError e) {
			assertTrue(true);
			checkInvariant();
		}
	}

	@Test
	public void Terrain_init_pre_true() {
		// oracle
		ter.init(5, 5);
		assertTrue(true);
		checkInvariant();
	}

	@Test
	public void Terrain_init_pre_false1() {
		try {
			// oracle
			ter.init(0, 5);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		} catch (ContractError e) {
			assertTrue(true);
		}
	}

	@Test
	public void Terrain_init_pre_false2() {
		try {
			// oracle
			ter.init(5, 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		} catch (ContractError e) {
			assertTrue(true);
		}
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_pre_true() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.isDeplacementBlocPossible(ter.getBlocHero(), Direction.DROITE));
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);
		assertTrue(true);
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_pre_false() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertFalse(ter.isDeplacementBlocPossible(ter.getBlocHero(), Direction.HAUT));
		try {
			ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.HAUT);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		} catch (ContractError e) {
			assertTrue(true);
		}
	}


	/** Couverture des invariants **/


	@Test
	public void Terrain_invariant1() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.getBlocHero() == ter.getBloc(2, 1));
	}

	@Test
	public void Terrain_invariant2() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.getBlocVersDirection(ter.getBlocHero(), Direction.DROITE) == ter.getBloc(3, 1));
	}

	@Test
	public void Terrain_invariant3_true() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.isHeroVivant());
	}

	@Test
	public void Terrain_invariant3_false() {
		// préambule
		ter.init(5, 5);

		// oracle
		assertFalse(ter.isHeroVivant());
	}

	@Test
	public void Terrain_invariant4_true() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.isDiamantsRestants());
	}

	@Test
	public void Terrain_invariant4_false() {
		// préambule
		ter.init(5, 5);

		// oracle
		assertFalse(ter.isDiamantsRestants());
	}

	@Test
	public void Terrain_invariant5_true() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.isDeplacementBlocPossible(ter.getBlocHero(), Direction.DROITE));
	}

	@Test
	public void Terrain_invariant5_false() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertFalse(ter.isDeplacementBlocPossible(ter.getBlocHero(), Direction.HAUT));
	}

	@Test
	public void Terrain_invariant6() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		assertTrue(ter.getBlocDepuisPosition(ObjectsFactory.getPOS1()) == ter.getBloc(1, 1));
	}

	@Test
	public void Terrain_invariant7() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// oracle
		// todo: compare values of blocs in sets
		Set<BlocService> terBlocs = ter.getBlocs();
		Set<BlocService> ter1Blocs = ObjectsFactory.getTER1Blocs();
		assertTrue(terBlocs.equals(ter1Blocs));
	}


	/** Couverture des postconditions **/

	@Test
	public void Terrain_init_post1() {
		// contenu
		ter.init(10, 15);

		// oracle
		assertTrue(ter.getLargeur() == 10);
		checkInvariant();
	}

	@Test
	public void Terrain_init_post2() {
		// contenu
		ter.init(10, 15);

		// oracle
		assertTrue(ter.getHauteur() == 15);
		checkInvariant();
	}

	@Test
	public void Terrain_init_post3() {
		// contenu
		ter.init(10, 15);

		// oracle
		assertNull(ter.getPosSortie());
		checkInvariant();
	}

	@Test
	public void Terrain_init_post4() {
		// contenu
		ter.init(10, 15);

		// oracle
		assertNull(ter.getPosHero());
		checkInvariant();
	}

	@Test
	public void Terrain_init_post5() {
		// contenu
		ter.init(10, 15);

		// oracle
		boolean allNull = true;
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 15; y++) {
				if (ter.getBloc(x, y).getType() != TypeBloc.VIDE) {
					allNull = false;
					break;
				}
			}
		}
		assertTrue(allNull);
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_post1_conseq() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.SORTIE_FERMEE, 1, 2);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 1, 2);
		assertTrue(ter.getPosSortie().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_post1_alt() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.ROCHER, 1, 2);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 4, 2);
		assertTrue(ter.getPosSortie().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_post2_conseq() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.HERO, 1, 2);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 1, 2);
		assertTrue(ter.getPosHero().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_post2_alt() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.VIDE, 1, 2);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 2, 1);
		assertTrue(ter.getPosHero().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_post3() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.ROCHER, 3, 1);

		// oracle
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 3 && y == 1) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_post1() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 4, 2);
		assertTrue(ter.getPosSortie().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_post2_conseq() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 3, 1);
		assertTrue(ter.getPosHero().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_post2_alt() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBloc(1, 1), Direction.BAS);

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 2, 1);
		assertTrue(ter.getPosHero().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_post3() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_post1() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 4, 2);
		assertTrue(ter.getPosSortie().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_post2() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		PositionService pos = Factory.getFactory().creerPosition();
		pos.init(5, 3, 2, 1);
		assertTrue(ter.getPosHero().equals(pos));
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_post3() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		checkInvariant();
	}


	/** Couverture des transitions **/

	@Test
	public void Terrain_setBloc_trans1() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.SORTIE_FERMEE, 1, 2);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 1, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 2, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 2) {
					assertTrue(bloc.isSortieFermee());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_trans2() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.HERO, 1, 2);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 1, 2);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 2) {
					assertTrue(bloc.isHero());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_trans1() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 3, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_trans2() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBloc(1, 1), Direction.BAS);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 2, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 2, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}


	/** Couverture des paires de transitions **/

	@Test
	public void Terrain_setBloc_deplacerBlocVersDirection_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.TERRE, 0, 0);
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 3, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 0 && y == 0) {
					assertTrue(bloc.isTerre());
				} else if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_setBloc_fairePasDeMiseAJour_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.setBloc(TypeBloc.TERRE, 0, 0);
		ter.fairePasDeMiseAJour();

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 2, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 0 && y == 0) {
					assertTrue(bloc.isTerre());
				} else if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_setBloc_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);
		ter.setBloc(TypeBloc.TERRE, 0, 0);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 3, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 0 && y == 0) {
					assertTrue(bloc.isTerre());
				} else if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_deplacerBlocVersDirection_fairePasDeMiseAJour_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);
		ter.fairePasDeMiseAJour();

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 3, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_deplacerBlocVersDirection_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();
		ter.deplacerBlocVersDirection(ter.getBlocHero(), Direction.DROITE);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 3, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 2 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 3 && y == 1) {
					assertTrue(bloc.isHero());
				} else if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}

	@Test
	public void Terrain_fairePasDeMiseAJour_setBloc_trans() {
		// préambule
		ter = ObjectsFactory.getTER1();

		// contenu
		ter.fairePasDeMiseAJour();
		ter.setBloc(TypeBloc.TERRE, 0, 0);

		// oracle
		PositionService posSortie = Factory.getFactory().creerPosition();
		posSortie.init(5, 3, 4, 2);
		PositionService posHero = Factory.getFactory().creerPosition();
		posHero.init(5, 3, 2, 1);
		TerrainService ter1 = ObjectsFactory.getTER1();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				BlocService bloc = ter.getBloc(x, y);
				if (x == 0 && y == 0) {
					assertTrue(bloc.isTerre());
				} else if (x == 1 && y == 1) {
					assertTrue(bloc.isVide());
				} else if (x == 1 && y == 2) {
					assertTrue(bloc.getType() == TypeBloc.ROCHER);
				} else {
					assertTrue(bloc.equals(ter1.getBloc(x, y)));
				}
			}
		}
		assertTrue(ter.getPosSortie().equals(posSortie) && ter.getPosHero().equals(posHero));
		checkInvariant();
	}
}
