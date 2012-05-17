package factories;

import components.*;
import contracts.*;

import services.*;

public class Factory {
	private static Factory _instance = null;
	protected boolean withContracts = false;
	
	private Factory(boolean withContracts) {
		this.withContracts = withContracts;
	}
	
	public static synchronized void createFactory() {
		_instance = new Factory(false);
	}
	
	public static synchronized void createFactoryWithContracts() {
		_instance = new Factory(true);
	}
	
	public static synchronized Factory getFactory() {
		if (_instance == null)
			Factory.createFactory();
		return _instance;
	}
	
	public MoteurJeuService creerMoteurJeu() {
		MoteurJeuService mj = new MoteurJeu();
		if (withContracts)
			mj = new MoteurJeuContract(mj);
		return mj;
	}

	public TerrainService creerTerrain() {
		TerrainService t = new Terrain();
		if (withContracts)
			t = new TerrainContract(t);
		return t;
	}

	public BlocService creerBloc() {
		BlocService b = new Bloc();
		if (withContracts)
			b = new BlocContract(b);
		return b;
	}

	public PositionService creerPosition() {
		PositionService p = new Position();
		if (withContracts)
			p = new PositionContract(p);
		return p;
	}

}
