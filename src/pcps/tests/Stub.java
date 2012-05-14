package pcps.tests;

import pcps.services.*;
import pcps.factories.Factory;

public class Stub {

	static PositionService getPOS1() {
		PositionService POS1 = Factory.getFactory().creerPosition();
		POS1.init(5, 3, 1, 1);
		return POS1;
	}
}
