package pcps.tests;

import static org.junit.Assert.*;
import org.junit.*;

import pcps.services.*;
import pcps.enums.Direction;
import pcps.factories.Factory;

public class MoteurJeuTest {
	private MoteurJeuService mj;
	
	public MoteurJeuTest() {
		Factory.createFactory();
		mj = null;
	}
	
	@Before
	public void beforeTests() {
		mj = Factory.getFactory().creerMoteurJeu();
	}
	
	@After
	public void afterTests() {
		mj = null;
	}
	
	public void checkInvariant() {
		TerrainService terrain = mj.getTerrain();
		BlocService blocHero = terrain.getBlocHero();
		BlocService blocDest;
		Direction dir;
		
		// inv: isPartieTerminee() == (getPasRestants() == 0 || !getTerrain().isHeroVivant() || getTerrain().getPosSortie == getTerrain().getPosHero())
		assertTrue(mj.isPartieTerminee() == (mj.getPasRestants() == 0 || !terrain.isHeroVivant() || terrain.getPosSortie() == terrain.getPosHero()));
		
		// inv: isPartieGagnee() == (isPartieTerminee() && getTerrain().isHeroVivant() 
		assertTrue(mj.isPartieGagnee() == (mj.isPartieTerminee() && terrain.isHeroVivant()));
		
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
	
	
	/** Couverture des préconditions **/

	@Test
	public void MoteurJeu_init_pre_true() {
		// oracle
		mj.init(Stub.getTER1(), 30);
		assertTrue(true);
		checkInvariant();
	}

	@Test
	public void MoteurJeu_init_pre_false() {
		try {
			// oracle
			mj.init(Stub.getTER1(), 0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void MoteurJeu_deplacerHero_pre_true() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		checkInvariant();
		
		// oracle
		mj.deplacerHero(Direction.DROITE);
		assertTrue(true);
		checkInvariant();
	}
	
	@Test
	public void MoteurJeu_deplacerHero_pre_false1() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		checkInvariant();
		
		try {
			// oracle
			mj.deplacerHero(Direction.HAUT);
			fail();
		} catch (Exception e) {
			assertTrue(true);
			checkInvariant();
		}
	}

	@Test
	public void MoteurJeu_deplacerHero_pre_false2() {
		// préambule
		mj.init(Stub.getTER1(), 1);
		mj.deplacerHero(Direction.DROITE);
		checkInvariant();
		
		try {
			// oracle
			mj.deplacerHero(Direction.GAUCHE);
			fail();
		} catch(Exception e) {
			assertTrue(true);
			checkInvariant();
		}
	}
	
	/** Couverture des invariants **/

	@Test
	public void MoteurJeu_invariant1_true() {
		// préambule
		mj.init(Stub.getTER1(), 1);
		mj.deplacerHero(Direction.DROITE);
		
		// oracle
		assertTrue(mj.isPartieTerminee());
	}
	
	@Test
	public void MoteurJeu_invariant1_false() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertFalse(mj.isPartieTerminee());
	}

	@Test
	public void MoteurJeu_invariant2_true() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		mj.deplacerHero(Direction.DROITE);
		mj.getTerrain().fairePasDeMiseAJour();
		mj.deplacerHero(Direction.DROITE);
		mj.getTerrain().fairePasDeMiseAJour();
		mj.deplacerHero(Direction.BAS);
		mj.getTerrain().fairePasDeMiseAJour();
		
		// oracle
		assertTrue(mj.isPartieGagnee());
	}

	@Test
	public void MoteurJeu_invariant2_false() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertFalse(mj.isPartieGagnee());
	}

	@Test
	public void MoteurJeu_invariant3_conseq() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertTrue(mj.isDeplacementHeroPossible(Direction.DROITE));
	}

	@Test
	public void MoteurJeu_invariant3_alt() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertTrue(mj.isDeplacementHeroPossible(Direction.DROITE));
	}
	
	
	/** Couverture des postconditions **/

	@Test
	public void MoteurJeu_init_post1() {
		// contenu
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertTrue(mj.getPasRestants() == 30);
		checkInvariant();
	}

	@Test
	public void MoteurJeu_init_post2() {
		// contenu
		mj.init(Stub.getTER1(), 30);
		
		// oracle
		assertTrue(mj.getTerrain().equals(Stub.getTER1()));
		checkInvariant();
	}

	@Test
	public void MoteurJeu_deplacerHero_post1() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// contenu
		mj.deplacerHero(Direction.DROITE);
		
		// oracle
		assertTrue(mj.getPasRestants() == 30 - 1);
		checkInvariant();
	}
	
	@Test
	public void MoteurJeu_deplacerHero_post2_1() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		// contenu
		mj.deplacerHero(Direction.DROITE);
		
		// oracle
		TerrainService ter1 = Stub.getTER1();
		ter1.deplacerBlocVersDirection(ter1.getBlocHero(), Direction.DROITE);
		assertTrue(mj.getTerrain().equals(ter1));
		checkInvariant();
	}

	@Test
	public void MoteurJeu_deplacerHero_post2_2() {
		// préambule
		mj.init(Stub.getTER1(), 30);
		
		//contenu
		mj.deplacerHero(Direction.GAUCHE);
		
		// oracle
		TerrainService ter1 = Stub.getTER1();
		BlocService blocHero = ter1.getBlocHero();
		BlocService blocDest = ter1.getBlocVersDirection(blocHero, Direction.GAUCHE);
		ter1.deplacerBlocVersDirection(blocDest, Direction.GAUCHE);
		ter1.deplacerBlocVersDirection(blocHero, Direction.GAUCHE);
		assertTrue(mj.getTerrain().equals(ter1));
		checkInvariant();
	}
}
