package components;

import java.util.Set;
import java.util.TreeSet;

import enums.Direction;
import enums.TypeBloc;
import factories.Factory;

import parser.TerrainParser;
import services.BlocService;
import services.PositionService;
import services.TerrainService;

public class Terrain implements 
/* provide */
TerrainService {

	protected int largeur;
	protected int hauteur;	
	protected BlocService[][] matriceTerrain;
	protected PositionService posSortie = null;
	protected PositionService posHero = null;


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
		for(int y = 0; y < hauteur; y++) {
			for(int x = 0; x < largeur; x++) {
				copy.setBloc(getBloc(x, y).getType(), x, y);
			}
		}
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
		return posHero;
	}


	@Override
	public BlocService getBlocHero() {
		if (!(isHeroVivant())) {
			throw new RuntimeException("getBlocHero ne peut être appelée si le héros n'est plus vivant");
		}
		PositionService posHero = getPosHero();
		return getBlocDepuisPosition(posHero);
	}

	@Override
	public BlocService getBlocDepuisPosition(PositionService pos) {
		return matriceTerrain[pos.getX()][pos.getY()];
	}

	@Override
	public BlocService getBlocVersDirection(BlocService bloc, Direction dir) {
		PositionService	posBloc = bloc.getPosition();
		PositionService posBlocCurrent = posBloc.copy();
		posBlocCurrent.deplacerVersDirection(dir);
		return getBlocDepuisPosition(posBlocCurrent);
	}

	@Override
	public boolean isHeroVivant() {
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				if(matriceTerrain[x][y].isHero()){
					return true;
				}
			}
		}
		return false;
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
		} else if(type == TypeBloc.HERO){
			posHero = Factory.getFactory().creerPosition();
			posHero.init(largeur, hauteur, x, y);
		}
		getBloc(x,y).setType(type);
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
		if(tb == TypeBloc.HERO){
			posHero = blocVersDirection.getPosition();
		}
	}


	@Override
	public void fairePasDeMiseAJour() {
		if(getBlocDepuisPosition(getPosSortie()).isSortieFermee() && !isDiamantsRestants()) {
			PositionService posSortie = getPosSortie();
			setBloc(TypeBloc.SORTIE_OUVERTE, posSortie.getX(), posSortie.getY());
		}
		boolean listIgnoreUpX[] = new boolean[largeur];
		for(int x=0;x<largeur;x++){
			for(int y=hauteur-1;y>=0;y--){

				// pour ne pas redeplacer bloc qui viennet de tomber de la derniére ligen du bas,
				// on mémorise la position x de ces blocs dans une liste
				BlocService bloc = getBloc(x,y);
				if(bloc.isTombable()){
					if(y == hauteur-1){
						BlocService bloc_y0 = getBloc(x,0);
						if(bloc_y0.isTombable() && getBlocVersDirection(bloc_y0, Direction.BAS).isVide()){
							deplacerBlocVersDirection(bloc_y0,Direction.BAS);
						}
						listIgnoreUpX[x]= true;
					}
					if(!(y==0 && listIgnoreUpX[x])){
						if(getBlocVersDirection(bloc, Direction.BAS).isVide() ||	 // diamants et rochers tombent tous d' un cran	
								getBlocVersDirection(bloc, Direction.BAS).isHero()){ // Hero peut être écrasé
							deplacerBlocVersDirection(bloc,Direction.BAS);
						}
					}
				}
			}
		}
	}


	@Override
	public BlocService getBloc(int x, int y) {
		BlocService bloc = new Bloc();
		bloc = matriceTerrain[x][y];
		return bloc;
	}


	@Override
	public Set<BlocService> getBlocs() {
		Set<BlocService> setBlocs = new TreeSet<BlocService>();
		for(int x=0;x<largeur;x++){
			for(int y=0;y<hauteur;y++){
				setBlocs.add(matriceTerrain[x][y]);
			}
		}
		return setBlocs;
	}

}
