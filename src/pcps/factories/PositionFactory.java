package pcps.factories;

import pcps.components.Position;
import pcps.services.PositionService;

public class PositionFactory {
	public static PositionService create(int l, int h, int x, int y){
		PositionService pos = new Position();
		pos.init(l, h, x, y);
		return pos;
	}
}
