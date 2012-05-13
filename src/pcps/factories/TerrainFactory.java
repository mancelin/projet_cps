package pcps.factories;

import pcps.components.Terrain;
import pcps.services.TerrainService;

public class TerrainFactory {	// n' apelle pas les factory de bloc et position -> probléme ?
	public static TerrainService create(int l, int h){
		TerrainService terrain = new Terrain();
		terrain.init(l, h);
		return terrain;
	}
}
