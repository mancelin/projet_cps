package pcps.components;

import java.util.HashSet;
import java.util.Set;

import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.services.BlocService;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class Terrain implements 
/* provide */
TerrainService {

	protected int largeur;
	protected int hauteur;	
	protected BlocService[][] matriceTerrain;


	@Override
	public void init(int l, int h) {
		largeur = l;
		hauteur = h;
		matriceTerrain = new BlocService[largeur][hauteur];
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				PositionService pos = new Position();
				pos.init(l, h, x, y);
				BlocService bloc = new Bloc();
				bloc.init(TypeBloc.VIDE, pos);
				matriceTerrain[x][y] = bloc;
			}
		}
	}

	@Override
	public TerrainService copy() {
		TerrainService copy = new Terrain();
		copy.init(getLargeur(), getHauteur());
		return copy;
	}

	@Override
	public int getLargeur() {
		return largeur;
	}

	@Override
	public int getHauteur() {
		return hauteur;
	}

	@Override
	public PositionService getPosSortie() {
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				if(matriceTerrain[x][y].getType() == TypeBloc.SORTIE_FERMEE){// ||
						//matriceTerrain[x][y].getType() == TypeBloc.SORTIE_OUVERTE){
					PositionService pos = new Position();
					pos.init(this.largeur, this.hauteur, x, y);
					return pos;
				}
			}
		}
		// si sortie non trouvée
		return null;
	}

	@Override
	public PositionService getPosHero() {
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				if(matriceTerrain[x][y].getType() == TypeBloc.HERO ){
					PositionService pos = new Position();
					pos.init(this.largeur, this.hauteur, x, y);
					return pos;
				}
			}
		}
		// si hero non trouvé
		return null;
	}

	@Override
	public BlocService getBlocHero() {
		if(isHeroVivant()){
			PositionService posHero = getPosHero();
			return getBlocDepuisPosition(posHero);
		}
		return null;
	}

	@Override
	public BlocService getBlocDepuisPosition(PositionService pos) {
	//	System.out.printf(" getBlocDepuisPosition  => x: %d, y : %d\n", pos.getX(),pos.getY());
		return matriceTerrain[pos.getX()][pos.getY()];
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		PositionService	posBloc = bloc.getPosition();
		PositionService posBlocCurrent = posBloc.copy();
	//	System.out.printf("getBlocVersDirection\n   o => x: %d, y : %d, type :%s\n", posBlocCurrent.getX(),posBlocCurrent.getY(), bloc.getType());
		posBlocCurrent.deplacerVersDirection(dir);
//		System.out.printf("   d => x: %d, y : %d\n", posBlocCurrent.getX(),posBlocCurrent.getY());
		return getBlocDepuisPosition(posBlocCurrent);
	}

	@Override
	public boolean isHeroVivant() {
		return (getPosHero() != null);
	}

	@Override
	public boolean isDiamantsRestants() {
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				if(matriceTerrain[x][y].getType() == TypeBloc.DIAMANT ){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir) {
		BlocService blocDest = getBlocVersDirection(bloc, dir);
		return ((blocDest.isTerre() && bloc.isHero()) || !blocDest.isSolide());
	}



	@Override
	public void setBloc(TypeBloc type, int x, int y) {
		//getBloc(x,y).setType(type);
		
		BlocService bloc = new Bloc();
		PositionService pos = new Position();
		pos.init(largeur, hauteur, x, y);
		bloc.init(type, pos);
		matriceTerrain[x][y] = bloc;
		
	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		assert(isDeplacementBlocPossible(bloc, dir));
		TypeBloc tb = bloc.getType();
		/*
		PositionService posBloc = bloc.getPosition().copy();
		setBloc(TypeBloc.VIDE, posBloc.getX(),posBloc.getY());
		*/
		bloc.setType(TypeBloc.VIDE);
	//	System.out.println("bloc o: " + tb + " -> " + "VIDE");
		BlocService blocVersDirection = getBlocVersDirection(bloc,dir);
	//	System.out.print("bloc d: " + blocVersDirection.getType() + " -> ");
		blocVersDirection.setType(tb);
		/*
		PositionService posBlocVersDirection = blocVersDirection.getPosition().copy();
		setBloc(tb, posBlocVersDirection.getX(),posBlocVersDirection.getY());
		*/
	//	System.out.println(	 blocVersDirection.getType() );
	}


	@Override
	public void fairePasDeMiseAJour() {
		/*
		PositionService posHero = getPosHero().copy(); 
		System.out.printf("posHero : (%d,%d)\n",posHero.getX(),posHero.getY());
		*/
		PositionService posSor = getPosSortie().copy();
		System.out.printf("posSor : (%d,%d)\n",posSor.getX(),posSor.getY());
		if(!isDiamantsRestants()){
			System.out.print("plus de diamants restants\n");
			PositionService posSortie = getPosSortie().copy();
			System.out.printf("posSortie : (%d,%d)\n",posSortie.getX(),posSortie.getY());
			BlocService blocSortie = getBlocDepuisPosition(posSortie);
			blocSortie.setType(TypeBloc.SORTIE_OUVERTE);
		//	//setBloc(TypeBloc.SORTIE_OUVERTE, posSortie.getX(), posSortie.getY());
		}
		for(int x=0;x<largeur;x++){
			for(int y=hauteur-1;y>0;y--){
		//		System.out.printf("getBloc(x:%d,y:%d)\n",x,y);
				BlocService bloc = getBloc(x,y);
				if(bloc.isTombable()){
					if(getBlocVersDirection(bloc, Direction.BAS).isVide()){
						deplacerBlocVersDirection(bloc,Direction.BAS);  // Hero peut être écrasé, diamants et rochers tombent tous d' un cran
					}
				}
					//if(isDeplacementBlocPossible(bloc,Direction.BAS)){
					//	deplacerBlocVersDirection(bloc,Direction.BAS);  // Hero peut être écrasé, diamants et rochers tombent tous d' un cran
					//}
				//}
			}
		}
	}


	@Override
	public BlocService getBloc(int x, int y) {
		BlocService bloc = new Bloc();
		bloc = matriceTerrain[x][y];
	//	System.out.printf(" (%d,%d){%s} \n",bloc.getPosition().getX(),bloc.getPosition().getY(),bloc.getType());
		return bloc;
	}


	@Override
	public Set<BlocService> getBlocs() {
		Set<BlocService> setBlocs = new HashSet<BlocService>();
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				setBlocs.add(matriceTerrain[x][y]);
			}
		}
		return setBlocs;
	}

}
