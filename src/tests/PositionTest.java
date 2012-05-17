package tests;

import static org.junit.Assert.*;
import org.junit.*;

import enums.Direction;
import factories.Factory;

import services.PositionService;

public class PositionTest {
	private PositionService position;
	
	public PositionTest() {
		position = null;
	}
	
	@Before
	public void beforeTests() {
		position = Factory.getFactory().creerPosition();
	}
	
	@After
	public void afterTests() {
		position = null;
	}
	
	public void checkInvariant() {
		// inv: 0 <= getX() < getLargeur()
		assertTrue(0 <= position.getX() && position.getX() < position.getLargeur());
		
		// inv: 0 <= getY() < getHauteur()
		assertTrue(0 <= position.getHauteur() && position.getY() < position.getHauteur());
	}
	
	
	/** Couverture des préconditions **/

	@Test
	public void Position_init_pre_true() {
		// oracle
		position.init(5, 5, 2, 3);
		assertTrue(true);
		checkInvariant();
	}

	@Test
	public void Position_init_pre_false1() {
		try {
			// oracle
			position.init(0, 5, 2, 3);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Position_init_pre_false2() {
		try {
			// oracle
			position.init(5, 0, 2, 3);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void Position_init_pre_false3() {
		try {
			// oracle
			position.init(5, 5, -1, 3);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void Position_init_pre_false4() {
		try {
			// oracle
			position.init(5, 5, 2, -1);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	
	/** Couverture des postconditions **/

	@Test
	public void Position_init_post1() {
		// contenu
		position.init(5, 4, 2, 3);
		
		// oracle
		assertTrue(position.getLargeur() == 5);
		checkInvariant();
	}

	@Test
	public void Position_init_post2() {
		// contenu
		position.init(5, 4, 2, 3);
		
		// oracle
		assertTrue(position.getHauteur() == 4);
		checkInvariant();
	}

	@Test
	public void Position_init_post3() {
		// contenu
		position.init(5, 4, 2, 3);
		
		// oracle
		assertTrue(position.getX() == 2%5);
		checkInvariant();
	}
	
	@Test
	public void Position_init_post4() {
		// contenu
		position.init(5, 4, 2, 3);
		
		// oracle
		assertTrue(position.getY() == 3%4);
		checkInvariant();
	}

	@Test
	public void Position_deplacerVersDirection_post1_1() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.GAUCHE);
		
		// oracle
		assertTrue(position.getX() == (2 - 1) % 5);
		checkInvariant();
	}

	@Test
	public void Position_deplacerVersDirection_post1_2() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.DROITE);
		
		// oracle
		assertTrue(position.getX() == (2 + 1) % 5);
	}

	@Test
	public void Position_deplacerVersDirection_post1_3() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.HAUT);
		
		// oracle
		assertTrue(position.getX() == 2);
	}

	@Test
	public void Position_deplacerVersDirection_post2() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.HAUT);
		
		// oracle
		assertTrue(position.getY() == (3 - 1) % 4);
	}

	@Test
	public void Position_deplacerVersDirection_post2_2() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.BAS);
		
		// oracle
		assertTrue(position.getY() == (3 + 1) % 4);
	}

	@Test
	public void Position_deplacerVersDirection_post2_3() {
		// préambule
		position.init(5, 4, 2, 3);
		checkInvariant();
		
		// contenu
		position.deplacerVersDirection(Direction.GAUCHE);
		
		// oracle
		assertTrue(position.getY() == 3);
	}
	
	
	/** Couverture des transitions **/

	@Test
	public void Position_deplacerVersDirection_trans1() {
		// préambule
		position.init(5, 4, 2, 3);
		
		// contenu
		position.deplacerVersDirection(Direction.HAUT);
		
		// oracle
		assertTrue(position.getX() == 2 && position.getY() == (3 - 1) % 4);
		checkInvariant();
	}
	
	@Test
	public void Position_deplacerVersDirection_trans2() {
		// préambule
		position.init(5, 4, 2, 3);
		
		// contenu
		position.deplacerVersDirection(Direction.BAS);
		
		// oracle
		assertTrue(position.getX() == 2 && position.getY() == (3 + 1) % 4);
		checkInvariant();
	}
	
	@Test
	public void Position_deplacerVersDirection_trans3() {
		// préambule
		position.init(5, 4, 2, 3);
		
		// contenu
		position.deplacerVersDirection(Direction.GAUCHE);
		
		// oracle
		assertTrue(position.getX() == (2 - 1) % 5 && position.getY() == 3);
		checkInvariant();
	}
	
	@Test
	public void Position_deplacerVersDirection_trans4() {
		// préambule
		position.init(5, 4, 2, 3);
		
		// contenu
		position.deplacerVersDirection(Direction.DROITE);
		
		// oracle
		assertTrue(position.getX() == (2 + 1) % 5 && position.getY() == 3);
		checkInvariant();
	}
}
