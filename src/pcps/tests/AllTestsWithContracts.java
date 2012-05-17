package pcps.tests;

import org.junit.BeforeClass;
import org.junit.runner.*;
import org.junit.runners.Suite;

import pcps.factories.Factory;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BlocTest.class,
	PositionTest.class,
	TerrainTest.class,
	MoteurJeuTest.class,
	ScenariosTest.class
})

public class AllTestsWithContracts {
	@BeforeClass
	public static void setUp() {
		Factory.createFactoryWithContracts();
	}
}
