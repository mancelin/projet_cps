package pcps.tests;

import org.junit.runner.*;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BlocTest.class,
	PositionTest.class,
	TerrainTest.class,
	MoteurJeuTest.class,
	ScenariosTest.class
})

public class AllTests {}
