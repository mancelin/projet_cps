package pcps.main;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import pcps.enums.Direction;
import pcps.enums.TypeBloc;
import pcps.services.MoteurJeuService;
import pcps.services.TerrainService;


public class Board extends JPanel implements ActionListener {

	private MoteurJeuService mj;
	private int largeur;
	private int hauteur;
	private int nbPas;
	private final int TAILLE_CASE = 16;
	private final int DELAI = 140;
	private int largeur_fenetre;
	private int hauteur_fenetre;

	// private int dots;
	/*
    private int apple_x;
    private int apple_y;
	 */
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean inGame = true;
	private boolean newAction = true;

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

	public Board(MoteurJeuService mj,int largeur_fenetre, int hauteur_fenetre) {
		this.mj = mj;
		this.largeur = mj.getTerrain().getLargeur();
		this.hauteur = mj.getTerrain().getHauteur();
		this.nbPas = mj.getPasRestants();
		
		this.largeur_fenetre = largeur_fenetre;
		this.hauteur_fenetre = hauteur_fenetre;

		addKeyListener(new TAdapter());

		setBackground(Color.black);

		ImageIcon iic_h = new ImageIcon(this.getClass().getResource("c_hero.png"));
		c_hero = iic_h.getImage();

		ImageIcon iic_v = new ImageIcon(this.getClass().getResource("c_vide.png"));
		c_vide = iic_v.getImage();

		ImageIcon iic_r = new ImageIcon(this.getClass().getResource("c_rocher.png"));
		c_rocher = iic_r.getImage();

		ImageIcon iic_d = new ImageIcon(this.getClass().getResource("c_diamant.png"));
		c_diamant = iic_d.getImage();

		ImageIcon iic_m = new ImageIcon(this.getClass().getResource("c_mur.png"));
		c_mur = iic_m.getImage();

		ImageIcon iic_t = new ImageIcon(this.getClass().getResource("c_terre.png"));
		c_terre = iic_t.getImage();

		ImageIcon iic_so = new ImageIcon(this.getClass().getResource("c_sortie_ouverte.png"));
		c_sortie_ouverte = iic_so.getImage();

		ImageIcon iic_sf = new ImageIcon(this.getClass().getResource("c_sortie_fermee.png"));
		c_sortie_fermee = iic_sf.getImage();
		
		ImageIcon iic_go = new ImageIcon(this.getClass().getResource("game_over.png"));
		game_over = iic_go.getImage();


		setFocusable(true);
		initGame();
	}


	public void initGame() {

		timer = new Timer(DELAI, this);
		timer.start();
	}


	//public static Image imageDeTypeBloc(TypeBloc tb){
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
//		super.paint(g);

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


	public void gameOver(Graphics g) {
		setSize(400, 400);
		
		g.clearRect(0, 0, largeur_fenetre, hauteur_fenetre );
	//	System.out.println("game over");
		g.drawImage(game_over, 0, 0,this);
		/*
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 34);
	//	FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, 0,0);
		*/
	}
	
	public void youWin(Graphics g) {
		String msg = "Gagné";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2,
				HEIGHT / 2);
	}
	
	

	public void printNbPasRestants(Graphics g) {
		g.clearRect(0, hauteur*TAILLE_CASE, largeur*TAILLE_CASE, (hauteur+ 1)*TAILLE_CASE);
		String msg = ""+mj.getPasRestants(); //+ "pas restants : ";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.green);
		g.setFont(small);
		g.drawString(msg, (TAILLE_CASE*largeur) - 70,(hauteur+ 1)*TAILLE_CASE+5);
	}



	/*

    public void checkCollision() {

          for (int z = dots; z > 0; z--) {

              if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                  inGame = false;
              }
          }

        if (y[0] > HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] > WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
    }

    public void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));
        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }
	 */
	public void actionPerformed(ActionEvent e) {
		mj.getTerrain().fairePasDeMiseAJour();
		repaint();
		/*
		if (inGame) {
			//System.out.println("game over : false");
			//       
			//          checkCollision();
		}
		*/
		/*
		if(newAction){
			repaint();
	//		System.out.print(mj.toString());
			newAction = false;
		}
		*/
	}


	private class TAdapter extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			
		//	if(!mj.isPartieTerminee()){
	
				int key = e.getKeyCode();
				
				if ((key == KeyEvent.VK_LEFT)) {
					//System.out.println("Gauche");
					mj.deplacerHero(Direction.GAUCHE);
					
				//	newAction = true;
				}
	
				if ((key == KeyEvent.VK_RIGHT)) {
//					System.out.println("Droite");
					mj.deplacerHero(Direction.DROITE);
					
					//newAction = true;
				}
	
				if ((key == KeyEvent.VK_UP) ) {
					mj.deplacerHero(Direction.HAUT);
			//		System.out.println("Haut");
				//	newAction = true;
				}
	
				if (key == KeyEvent.VK_DOWN) {
		//			System.out.println("Bas");
					mj.deplacerHero(Direction.BAS);

//					repaint();
				}
				System.out.printf("nbPas : %d \n", mj.getPasRestants());
		//		repaint();
				/*
				System.out.printf("pos hero : (%d,%d) 		", mj.getTerrain().getPosHero().getX(),mj.getTerrain().getPosHero().getY());
				System.out.printf("nbPas : %d \n", mj.getPasRestants());
				
				System.out.println("--------------");
				System.out.print(mj.toString());
				*/
	//		}
		}
	}

}