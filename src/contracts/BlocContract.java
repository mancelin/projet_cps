package contracts;

import decorators.BlocDecorator;
import enums.TypeBloc;
import services.BlocService;
import services.PositionService;

public class BlocContract extends BlocDecorator {

	public BlocContract(BlocService delegate) {
		super(delegate);
	}
	
	@Override
	public BlocService copy() {
		return new BlocContract(super.copy());
	}

	public void checkInvariant() {
		// inv: isVide() == (getType() == VIDE)
		if (!(isVide() == (getType() == TypeBloc.VIDE)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc considéré vide doit être de type VIDE.");
		
		// inv: isSolide() == (getType() \in { SORTIE_FERMEE, MUR, ROCHER })
		if (!(isSolide() == (getType() == TypeBloc.SORTIE_FERMEE || getType() == TypeBloc.MUR || getType() == TypeBloc.ROCHER)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc considéré solide doit être de type SORTIE_FERMEE, MUR ou ROCHER.");
		
		// inv: isDeplacable() == (getType() == ROCHER)
		if (!(isDeplacable() == (getType() == TypeBloc.ROCHER)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc considéré déplaçable doit être de type ROCHER.");
		
		// inv: isTombable() == (getType() \in { ROCHER, DIAMANT })
		if (!(isTombable() == (getType() == TypeBloc.ROCHER || getType() == TypeBloc.DIAMANT)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc considéré tombable doit être de type ROCHER ou DIAMANT.");
		
		// inv: isSortie() == (getType() \in { SORTIE_FERMEE, SORTIE_OUVERTE }
		if (!(isSortie() == (getType() == TypeBloc.SORTIE_FERMEE || getType() == TypeBloc.SORTIE_OUVERTE)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc étant une sortie doit être de type SORTIE_FERMEE ou SORTIE_OUVERTE.");

		// inv: isSortieFermee() == (getType() == SORTIE_FERMEE)
		if (!(isSortieFermee() == (getType() == TypeBloc.SORTIE_FERMEE)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc étant une sortie fermée doit être de type SORTIE_FERMEE.");
		
		// inv: isHero() == (getType() == HERO)
		if (!(isHero() == (getType() == TypeBloc.HERO)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc étant considéré héro doit être de type HERO.");
		
		// inv: isTerre() == (getType() == TERRE)
		if (!(isTerre() == (getType() == TypeBloc.TERRE)))
			Contractor.defaultContractor().invariantError("BlocService", "Un bloc de terre doit être de type TERRE.");
	}

	@Override
	public void init(TypeBloc tb, PositionService pos) {
		// run
		super.init(tb, pos);
		
		// invariant@post
		checkInvariant();
		
		// post: getType() == tb
		if (!(getType() == tb))
			Contractor.defaultContractor().postconditionError("BlocService", "init", "getType() devrait retourner le paramètre de type passé au constructeur.");
		
		// post: getPosition() == pos
		if (!(getPosition() == pos))
			Contractor.defaultContractor().postconditionError("BlocService", "init", "getPosition() devrait retourner le paramètre de position passé au constructeur.");
	}

	@Override
	public void setType(TypeBloc tb) {
		// captures
		PositionService getPositionAtPre = getPosition();
		
		// invariant@pre
		checkInvariant();
		
		// run
		super.setType(tb);
		
		// invariant@post
		checkInvariant();
		
		// post: getType() == tb
		if (!(getType() == tb))
			Contractor.defaultContractor().postconditionError("BlocService", "setType", "getType() devrait retourner le nouveau type passé en paramètre à setType().");
		
		// post: getPosition() == getPosition()@pre
		if (!(getPosition() == getPositionAtPre))
			Contractor.defaultContractor().postconditionError("BlocService", "setType", "La position du bloc devrait rester inchangée après l'utilisation de setType().");
	}
}
