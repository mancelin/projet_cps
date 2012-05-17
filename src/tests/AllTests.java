package tests;

import org.junit.BeforeClass;
import org.junit.runner.*;
import org.junit.runners.Suite;

import factories.Factory;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	BlocTest.class,
	PositionTest.class,
	TerrainTest.class,
	MoteurJeuTest.class,
	ScenariosTest.class
})

public class AllTests {
	@BeforeClass
	public static void setUp() {
		Factory.createFactory();
	}
}
