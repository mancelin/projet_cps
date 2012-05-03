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
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				PositionService pos = new Position();
				pos.init(l, h, x, y);
				BlocService bloc = new Bloc();
				bloc.init(TypeBloc.VIDE, pos);
				matriceTerrain[x-1][y-1] = bloc;
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
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				if(matriceTerrain[x-1][y-1].getType() == TypeBloc.SORTIE_FERMEE ||
						matriceTerrain[x-1][y-1].getType() == TypeBloc.SORTIE_OUVERTE){
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
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				if(matriceTerrain[x-1][y-1].getType() == TypeBloc.HERO ){
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
		PositionService posHero = getPosHero();
		return getBlocDepuisPosition(posHero);
	}

	@Override
	public BlocService getBlocDepuisPosition(PositionService pos) {
		return matriceTerrain[pos.getX()-1][pos.getY()-1];
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		PositionService	posBloc = bloc.getPosition();
		posBloc.deplacerVersDirection(dir);
		return getBlocDepuisPosition(posBloc);
	}

	@Override
	public boolean isHeroVivant() {
		if(getPosHero() != null){
			return true;
		}
		return false;
	}

	@Override
	public boolean isDiamantsRestants() {
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				if(matriceTerrain[x-1][y-1].getType() == TypeBloc.DIAMANT ){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isDeplacementBlocPossible(BlocService bloc, Direction dir) {
		if(bloc.isDeplacable()){
			BlocService blocVersDirection = getBlocVersDirection(bloc,dir);
			if (blocVersDirection.getType() == TypeBloc.VIDE){
				return true;
			}
			return false;
		}
		
		// si bloc non deplaçable ( pas ROCHER )
		return false;
	}

	

	@Override
	public void setBloc(TypeBloc type, int x, int y) {
		getBloc(x,y).setType(type);
	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		TypeBloc tb = bloc.getType();
		bloc.setType(TypeBloc.VIDE);
		BlocService blocVersDirection = getBlocVersDirection(bloc,dir);
		blocVersDirection.setType(tb);
	}

	@Override
	public void fairePasDeMiseAJour() {
		// TODO Auto-generated method stub

	}


	@Override
	public BlocService getBloc(int x, int y) {
		return matriceTerrain[x-1][y-1];
	}


	@Override
	public Set<BlocService> getBlocs() {
		Set<BlocService> setBlocs = new HashSet<BlocService>();
		for(int x=1;x<=largeur;x++){
			for(int y=1;y<=hauteur;y++){
				setBlocs.add(matriceTerrain[x-1][y-1]);
			}
		}
		return setBlocs;
	}

}
