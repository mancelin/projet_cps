package pcps.components;

import java.util.HashSet;
import java.util.Set;

import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.factories.Factory;
import pcps.parser.TerrainParser;
import pcps.services.BlocService;
import pcps.services.PositionService;
import pcps.services.TerrainService;

public class Terrain implements 
/* provide */
TerrainService {

	protected int largeur;
	protected int hauteur;	
	protected BlocService[][] matriceTerrain;
	protected PositionService posSortie = null;


	@Override
	public void init(int l, int h) {
		if (!(l > 0 && h > 0)) {
			throw new IllegalArgumentException("La largeur et la hauteur du terrain doivent être strictement positifs.");
		}
		
		largeur = l;
		hauteur = h;
		matriceTerrain = new BlocService[largeur][hauteur];
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				PositionService pos = Factory.getFactory().creerPosition();
				pos.init(l, h, x, y);
				BlocService bloc = Factory.getFactory().creerBloc();
				bloc.init(TypeBloc.VIDE, pos);
				matriceTerrain[x][y] = bloc;
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof TerrainService))
			return false;
		TerrainService otherTer = (TerrainService)other;
		
		// comparaison de la largeur et la hauteur
		if (otherTer.getLargeur() != largeur || otherTer.getHauteur() != hauteur)
			return false;
		
		// comparaison de la position de sortie
		if (posSortie != otherTer.getPosSortie()) {
			if (posSortie == null || otherTer.getPosSortie() == null
			|| !(posSortie.equals(otherTer.getPosSortie())))
				return false;
		}

		// comparaison des blocs
		for (int x = 0; x < largeur; x++){
			for (int y = 0; y < hauteur; y++){
				if (!getBloc(x, y).equals(otherTer.getBloc(x, y)))
					return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuffer ter = new StringBuffer();
		for(int y = 0; y < hauteur; y++) {
			for(int x = 0; x < largeur; x++) {
				ter.append(TerrainParser.charDeTypeBloc(getBloc(x, y).getType()));
			}
			ter.append("\n");
		}
		return ter.toString();
	}

	@Override
	public TerrainService copy() {
		TerrainService copy = Factory.getFactory().creerTerrain();
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
		return posSortie;
	}

	@Override
	public PositionService getPosHero() {
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				if(matriceTerrain[x][y].isHero()){
					PositionService pos = Factory.getFactory().creerPosition();
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
		if(type == TypeBloc.SORTIE_FERMEE || type == TypeBloc.SORTIE_OUVERTE){
			posSortie = Factory.getFactory().creerPosition();
			posSortie.init(largeur, hauteur, x, y);
		}
		getBloc(x,y).setType(type);
		
		/*
		BlocService bloc = new Bloc();
		PositionService pos = new Position();
		pos.init(largeur, hauteur, x, y);
		bloc.init(type, pos);
		matriceTerrain[x][y] = bloc;
		*/
	}

	@Override
	public void deplacerBlocVersDirection(BlocService bloc, Direction dir) {
		if (!isDeplacementBlocPossible(bloc, dir)) {
			throw new RuntimeException("Le déplacement du bloc dans cette direction est impossible.");
		}
		
		TypeBloc tb = bloc.getType();
		bloc.setType(TypeBloc.VIDE);
		BlocService blocVersDirection = getBlocVersDirection(bloc,dir);
		blocVersDirection.setType(tb);
	}


	@Override
	public void fairePasDeMiseAJour() {
		if(getBlocDepuisPosition(getPosSortie()).isSortieFermee() && !isDiamantsRestants()) {
		//	System.out.print("plus de diamants restants\n");
		//	PositionService posSortie = getPosSortie().copy();
			PositionService posSortie = getPosSortie();
			/*
			System.out.printf("posSortie : (%d,%d)\n",posSortie.getX(),posSortie.getY());
			BlocService blocSortie = getBlocDepuisPosition(posSortie);
			blocSortie.setType(TypeBloc.SORTIE_OUVERTE);
			*/
			setBloc(TypeBloc.SORTIE_OUVERTE, posSortie.getX(), posSortie.getY());
		}
		for(int x=0;x<largeur;x++){
			for(int y=hauteur-1;y>0;y--){
		//		System.out.printf("getBloc(x:%d,y:%d)\n",x,y);
				BlocService bloc = getBloc(x,y);
				if(bloc.isTombable()){
					if(getBlocVersDirection(bloc, Direction.BAS).isVide()){
						deplacerBlocVersDirection(bloc,Direction.BAS);  // Hero peut être écrasé, diamants et rochers tombent tous d' un cran
					}/* later
					else if(getBlocVersDirection(bloc, Direction.BAS).isHero()){
						System.out.println("écrasage du héros");
						deplacerBlocVersDirection(bloc,Direction.BAS);  // Hero peut être écrasé, diamants et rochers tombent tous d' un cran
					}*/
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
