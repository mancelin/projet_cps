package pcps.factory;

import pcps.components.Bloc;
//import pcps.components.Position;
import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;

public class BlocFactory {
	public static BlocService create(TypeBloc tb, PositionService pos){
		BlocService bloc = new Bloc();
		bloc.init(tb, pos);
		return bloc;
	}
	
	public static BlocService create(TypeBloc tb, int l, int h, int x, int y){
		/*
		PositionService pos = new Position();
		pos.init(l, h, x, y);
		*/
		PositionService pos = PositionFactory.create(l, h, x, y);
		BlocService bloc = new Bloc();
		bloc.init(tb, pos);
		return bloc;
	}

}
