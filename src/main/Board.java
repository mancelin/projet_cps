package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import enums.Direction;
import enums.TypeBloc;

import services.MoteurJeuService;
import services.TerrainService;


public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private MoteurJeuService mj;
	private MoteurJeuService mjClone;
	
	JFrame parent;
	private int largeur;
	private int hauteur;
	private final int TAILLE_CASE = 16;
	private final int DELAI = 200;
	private int largeur_fenetre;
	private int hauteur_fenetre;
	private int x_window;
	private int y_window;

	private boolean newAction = true;
	private boolean afficherAide = false;

	private Timer timer;
	private Image c_hero;
	private Image c_vide;
	private Image c_rocher;
	private Image c_diamant;
	private Image c_mur;
	private Image c_terre;
	private Image c_sortie_ouverte;
	private Image c_sortie_fermee;

	private Image game_over;
	private Image you_win;

	public Board(JFrame parent, MoteurJeuService mj,int largeur_fenetre, int hauteur_fenetre) {
	//public Board(MoteurJeuService mj,int largeur_fenetre, int hauteur_fenetre) {
		this.mj = mj;
		this.mjClone = mj.copy();
		this.largeur = mj.getTerrain().getLargeur();
		this.hauteur = mj.getTerrain().getHauteur();

		this.largeur_fenetre = largeur_fenetre;
		this.hauteur_fenetre = hauteur_fenetre + 20;
		this.parent =parent;
		this.x_window = parent.getX();
		this.y_window = parent.getY();
		
		
		parent.setSize(largeur_fenetre, hauteur_fenetre);
		parent.addKeyListener(new TAdapter());
		parent.setBackground(Color.black);

		String repCourant = System.getProperty("user.dir" );
		String repImages = repCourant + File.separator + "src" + File.separator + "main";
		ImageIcon iic_h = new ImageIcon(repImages + File.separator + "c_hero.png");
		c_hero = iic_h.getImage();

		ImageIcon iic_v = new ImageIcon(repImages + File.separator + "c_vide.png");
		c_vide = iic_v.getImage();

		ImageIcon iic_r = new ImageIcon(repImages + File.separator + "c_rocher.png");
		c_rocher = iic_r.getImage();

		ImageIcon iic_d = new ImageIcon(repImages + File.separator + "c_diamant.png");
		c_diamant = iic_d.getImage();

		ImageIcon iic_m = new ImageIcon(repImages + File.separator + "c_mur.png");
		c_mur = iic_m.getImage();

		ImageIcon iic_t = new ImageIcon(repImages + File.separator + "c_terre.png");
		c_terre = iic_t.getImage();

		ImageIcon iic_so = new ImageIcon(repImages + File.separator + "c_sortie_ouverte.png");
		c_sortie_ouverte = iic_so.getImage();

		ImageIcon iic_sf = new ImageIcon(repImages + File.separator + "c_sortie_fermee.png");
		c_sortie_fermee = iic_sf.getImage();

		ImageIcon iic_go = new ImageIcon(repImages + File.separator + "game_over.png");
		game_over = iic_go.getImage();

		ImageIcon iic_win = new ImageIcon(repImages + File.separator + "you_win.png");
		you_win = iic_win.getImage();
		
		parent.setFocusable(true);
	//	parent.pack();
		parent.repaint();
		
		initGame();
	}


	public void initGame() {
		timer = new Timer(DELAI, this);
		timer.start();
	}

	public Image imageDeTypeBloc(TypeBloc tb){
		switch(tb){
		case MUR : return c_mur;
		case HERO : return c_hero;
		case ROCHER : return c_rocher;
		case DIAMANT : return c_diamant;
		case TERRE : return c_terre;
		case VIDE : return c_vide;
		case SORTIE_FERMEE : return c_sortie_fermee;
		case SORTIE_OUVERTE : return c_sortie_ouverte;
		default : throw new IllegalArgumentException("Aucun caractére ne correspond au type de bloc  "+tb);
		}
	}



	public void paint(Graphics g) {
		if(afficherAide){
			afficherAide(g);
		} else {
			if (!mj.isPartieTerminee()) {
				TerrainService t = mj.getTerrain();
				for(int y=0;y<hauteur;y++){
					for(int x=0;x<largeur;x++){
						TypeBloc typeBlocCourant = t.getBloc(x, y).getType();
						g.drawImage(imageDeTypeBloc(typeBlocCourant), x * TAILLE_CASE, y *TAILLE_CASE,this);
					}
				}
				printNbPasRestants(g);
				Toolkit.getDefaultToolkit().sync();
				g.dispose();
	
			} else {
				if(mj.isPartieGagnee()){
					youWin(g);
				} else {
					gameOver(g);
				}
			}
		}
	}


	public void gameOver(Graphics g) {
		if(parent.getWidth() != 339 || parent.getHeight() != (203)){ // 153 + 50
			parent.setBounds(x_window,y_window, 339, 203);
		}
		g.clearRect(0, 0, parent.getWidth(), parent.getHeight());
		g.drawImage(game_over, 0, 0,this);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		g.setColor(Color.green);
		g.setFont(small);
		g.drawString("appuyez sur espace pour recommencer!", 4, 170);
		g.dispose();
	}

	public void youWin(Graphics g) {
		if(parent.getWidth() != 339 || parent.getHeight() != 203){
			parent.setBounds(x_window,y_window, 339, 203);
		}
		g.clearRect(0, 0, parent.getWidth(), parent.getHeight());
		g.drawImage(you_win, 0, 0,this);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		g.setColor(Color.green);
		g.setFont(small);
		g.drawString("appuyez sur espace pour recommencer!", 4, 170);
		g.dispose();
		g.dispose();
	}
	
	public void afficherAide(Graphics g) {
		if(parent.getWidth() != 600 || parent.getHeight() != 250){
			parent.setBounds(x_window,y_window, 600, 250);
		}
		Font small = new Font("Helvetica", Font.BOLD, 14);
		parent.setBackground(Color.white);
		g.setColor(Color.black);
		g.setFont(small);
		g.clearRect(0, 0, parent.getWidth(), parent.getHeight());
		String msg[] = {
				 "Le but du jeu est de ramasser tous les diamants et de sortir par la"  ,
				 "porte sans se faire écraser par un diammant ou un rocher, ni épuisé",
				 "le nombre de pas restants.",
				 "" ,
				 "Controles :",
				 "  fléches directionneles : déplacer joueur",
				 "  Barre espace : recommencer le niveau en cours",
				 "  F1 : afficher cette aide"};
		for(int i=0;i<msg.length;i++){
			g.drawString(msg[i], 10, 50 + 17 * i);
		}
		
		g.dispose();
	}
	
	



	public void printNbPasRestants(Graphics g) {
		g.clearRect(0, hauteur*TAILLE_CASE, largeur_fenetre, hauteur_fenetre);
		String msg = ""+mj.getPasRestants();
		Font small = new Font("Helvetica", Font.BOLD, 14);
		parent.setBackground(Color.BLACK);
		g.setColor(Color.green);
		g.setFont(small);
		g.drawString(msg, (TAILLE_CASE*largeur) - 30,(hauteur+ 1)*TAILLE_CASE+5);
	}

	public void actionPerformed(ActionEvent e) {
		if(newAction){
			// si le héros n'est pas sous un objet pouvant chutter, on fait la mise a jour aussitot le héros déplacé
			if( !(!mj.isPartieTerminee() && mj.getTerrain().getBlocVersDirection(mj.getTerrain().getBlocHero(), Direction.HAUT).isTombable())){
				mj.getTerrain().fairePasDeMiseAJour();
			} else {
				timer.restart();
			}
			repaint();
			newAction = false;
		} else {
			mj.getTerrain().fairePasDeMiseAJour();
			repaint();
		}

	}

	private class TAdapter extends KeyAdapter {
		public void deplacerHero(Direction dir) {
			if (!mj.isPartieTerminee() && mj.isDeplacementHeroPossible(dir))
				mj.deplacerHero(dir);
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(afficherAide){
				parent.setSize(largeur_fenetre, hauteur_fenetre);
				afficherAide = false;
			}

			if ((key == KeyEvent.VK_LEFT)) {
				deplacerHero(Direction.GAUCHE);
				newAction = true;
			}
			if ((key == KeyEvent.VK_RIGHT)) {
				deplacerHero(Direction.DROITE);
				newAction = true;
			}
			if ((key == KeyEvent.VK_UP) ) {
				deplacerHero(Direction.HAUT);
				newAction = true;
			}
			if (key == KeyEvent.VK_DOWN) {
				deplacerHero(Direction.BAS);
				newAction = true;

			}

			if (key == KeyEvent.VK_SPACE){
				mj = mjClone;
				mjClone = mj.copy();
				parent.setBounds(x_window,y_window, largeur_fenetre, hauteur_fenetre);
				repaint();
			}
			
			if (key == KeyEvent.VK_F1){
				afficherAide = true;
			}
		}
	}
}
