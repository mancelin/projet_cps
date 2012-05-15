package pcps.tests;

import static org.junit.Assert.*;
import org.junit.*;

import pcps.enums.TypeBloc;
import pcps.factories.Factory;
import pcps.services.*;

public class BlocTest {
	private BlocService bloc;
	
	public BlocTest() {
		Factory.createFactory();
		bloc = null;
	}
	
	@Before
	public void beforeTests() {
		bloc = Factory.getFactory().creerBloc();
	}
	
	@After
	public void afterTests() {
		bloc = null;
	}
	
	public void checkInvariant() {
		// inv: isVide() == (getType() == VIDE)
		assertTrue(bloc.isVide() == (bloc.getType() == TypeBloc.VIDE));
			
		// inv: isSolide() == (getType() \in { SORTIE_FERMEE, MUR, ROCHER })
		assertTrue(bloc.isSolide() == (bloc.getType() == TypeBloc.SORTIE_FERMEE || bloc.getType() == TypeBloc.MUR || bloc.getType() == TypeBloc.ROCHER));
		
		// inv: isDeplacable() == (getType() == ROCHER)
		assertTrue(bloc.isDeplacable() == (bloc.getType() == TypeBloc.ROCHER));
		
		// inv: isTombable() == (getType() \in { ROCHER, DIAMANT })
		assertTrue(bloc.isTombable() == (bloc.getType() == TypeBloc.ROCHER || bloc.getType() == TypeBloc.DIAMANT));
		
		// inv: isSortie() == (getType() \in { SORTIE_FERMEE, SORTIE_OUVERTE }
		assertTrue(bloc.isSortie() == (bloc.getType() == TypeBloc.SORTIE_FERMEE || bloc.getType() == TypeBloc.SORTIE_OUVERTE));
		
		// inv: isSortieFermee() == (getType() == SORTIE_FERMEE)
		assertTrue(bloc.isSortieFermee() == (bloc.getType() == TypeBloc.SORTIE_FERMEE));
		
		// inv: isHero() == (getType() == HERO)
		assertTrue(bloc.isHero() == (bloc.getType() == TypeBloc.HERO));
		
		// inv: isTerre() == (getType() == TERRE)
		assertTrue(bloc.isTerre() == (bloc.getType() == TypeBloc.TERRE));
	}
	
	/** Couverture des invariants **/
	
	@Test
	public void Bloc_invariant1() {
		// préambule
		bloc.init(TypeBloc.VIDE, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isVide());
	}
	
	@Test
	public void Bloc_invariant2() {
		// préambule
		bloc.init(TypeBloc.ROCHER, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isSolide());
	}
	
	@Test
	public void Bloc_invariant3() {
		// préambule
		bloc.init(TypeBloc.ROCHER, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isDeplacable());
	}
	
	@Test
	public void Bloc_invariant4() {
		// préambule
		bloc.init(TypeBloc.ROCHER, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isTombable());
	}
	
	@Test
	public void Bloc_invariant5() {
		// préambule
		bloc.init(TypeBloc.SORTIE_OUVERTE, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isSortie());
	}
	
	@Test
	public void Bloc_invariant6() {
		// préambule
		bloc.init(TypeBloc.SORTIE_FERMEE, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isSortieFermee());
	}
	
	@Test
	public void Bloc_invariant7() {
		// préambule
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isHero());
	}
	
	@Test
	public void Bloc_invariant8() {
		// préambule
		bloc.init(TypeBloc.TERRE, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.isTerre());
	}
	
	
	/** Couverture des postconditions **/
	
	@Test
	public void Bloc_init_post1() {
		// contenu
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.getType() == TypeBloc.HERO);
		checkInvariant();
	}
	
	@Test
	public void Bloc_init_post2() {
		// contenu
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// oracle
		assertTrue(bloc.getPosition().equals(Stub.getPOS1()));
		checkInvariant();
	}
	
	@Test
	public void Bloc_setType_post1() {
		// préambule
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// contenu
		bloc.setType(TypeBloc.ROCHER);
		
		// oracle
		assertTrue(bloc.getType() == TypeBloc.ROCHER);
		checkInvariant();
	}
	
	@Test
	public void Bloc_setType_post2() {
		// préambule
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// contenu
		bloc.setType(TypeBloc.ROCHER);
		
		// oracle
		assertTrue(bloc.getPosition().equals(Stub.getPOS1()));
		checkInvariant();
	}
	
	
	
	/** Couverture des transitions **/
	
	@Test
	public void Bloc_setType_trans() {
		// préambule
		bloc.init(TypeBloc.HERO, Stub.getPOS1());
		
		// contenu
		bloc.setType(TypeBloc.ROCHER);
		
		// oracle
		assertTrue(bloc.getType() == TypeBloc.ROCHER && bloc.getPosition().equals(Stub.getPOS1()));
		checkInvariant();
	}
}
