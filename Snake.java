package tp7_snakeGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



/*
 * Cellule(x,y)
 * corpsSerpent = liste de cellule
 * repas = cellule générée aléatoirement
 */

public class Snake extends JFrame
{
	
	int nbCaseX = 40;
	int nbCaseY = 20;
	int largeur = 20;
	int marge = 100;
	int direction = 1;                              //1: right 2: down 3: left 4: up 
	//ArrayList<Cellule> body;
	ArrayList<Cellule> corpsSerpent;
	Cellule repas;
	ArrayList<Cellule> obstacle;
	int freq = 50;
	int score = 0;
	Timer horloge;
	JPanel panneau;
	JLabel gameOver,scoreText;
	
	void initialiserSerpent()
	{
		corpsSerpent = new ArrayList<Cellule>();
		corpsSerpent.add(new Cellule(10,6,new Color(0xE86B4F)));
		for(int i = 9;i >= 5; i--)
			corpsSerpent.add(new Cellule(i,6,new Color(0x67D956)));
		
		
		
	}
	
	void initialiserObstacle()
	{
		obstacle = new ArrayList<Cellule>();
		for(int i = 5; i < 14; i++)
			obstacle.add(new Cellule(30, i, Color.yellow));
	}
	
	void dessinerSerpent(Graphics g)
	{
		for(int i = corpsSerpent.size() - 1; i >= 0; i--)
		{
			dessinerCellule(corpsSerpent.get(i), g);
		}
		
	}
	
	void repasGenerator()
	{
		Random rand = new Random();
		int x,y;
		boolean flag = true;
		
		do {
			x = rand.nextInt(nbCaseX);
			y = rand.nextInt(nbCaseY);
			
			for(Cellule c : corpsSerpent)
			{
				if(x == c.x && y == c.y)      //regenerate if repas on the snake body
					flag = false;
			}
			
			for(Cellule c : obstacle)
			{
				if(x == c.x && y == c.y)      //regenerate if repas on obstacle body
					flag = false;
			}
			
		}while(!flag);
		
		repas = new Cellule(x, y, new Color(0xE63B15));
		
	}
	
	
	
	
	
	public Snake()
	{
		this.setTitle("Snake Game");
		this.setSize(nbCaseX*largeur + 2*marge + 10,nbCaseY*largeur + 2*marge +40);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		
		panneau = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				scoreText.setText("Score: " + score);
				scoreText.setBounds(marge + 100,marge - 100 , 200, 100);
				scoreText.setForeground(Color.white);
				add(scoreText);
				
				dessinerGrille(g);
				dessinerSerpent(g);
				dessinerRepas(g);
				dessinerObstacle(g);
			}	
		};	
		
		
		initialiserSerpent();
		initialiserObstacle();
		repasGenerator();
		
		
		horloge = new Timer(freq, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// save last cellule of the snake
				
				int xf = corpsSerpent.get(corpsSerpent.size() - 1).x;
				int yf = corpsSerpent.get(corpsSerpent.size() - 1).y;
				
				// deplacement du corps sauf la premiere
				
				for(int i = corpsSerpent.size() - 1;i > 0 ; i--)
				{
					corpsSerpent.get(i).x = corpsSerpent.get(i-1).x;
					corpsSerpent.get(i).y = corpsSerpent.get(i-1).y;
				
				}
				
				
				
				//calcul pour déplacer la tete
				if(direction == 1) corpsSerpent.get(0).x ++;
				if(direction == 2) corpsSerpent.get(0).y ++;
				if(direction == 3) corpsSerpent.get(0).x --;
				if(direction == 4) corpsSerpent.get(0).y --;
				
				if(corpsSerpent.get(0).x == nbCaseX ) corpsSerpent.get(0).x = 0;
				if(corpsSerpent.get(0).x == -1 ) corpsSerpent.get(0).x =nbCaseX - 1;
				if(corpsSerpent.get(0).y == nbCaseY ) corpsSerpent.get(0).y = 0;
				if(corpsSerpent.get(0).y == -1 ) corpsSerpent.get(0).y = nbCaseY - 1;
				
				
	
				
				
				//when the head eats "repas"
				if(corpsSerpent.get(0).x == repas.x && corpsSerpent.get(0).y == repas.y)
				{
					score ++;
					corpsSerpent.add(new Cellule(xf, yf, new Color(0x67D956)));
					repasGenerator();
				}
				
				
				


				// when the head touchs a cellule from his body

				for(int i =1; i <corpsSerpent.size(); i++)
				{
					if(corpsSerpent.get(0).x == corpsSerpent.get(i).x && corpsSerpent.get(0).y == corpsSerpent.get(i).y)
					{
						
						printEndMessage();
						horloge.stop();
						
					}
					
				}
				
				// when the head touchs l'obstacle if it exits
				if(obstacle != null)
				{
					for(Cellule c : obstacle)
					{
						if(corpsSerpent.get(0).x  == c.x  && corpsSerpent.get(0).y == c.y )
						{
							printEndMessage();
							horloge.stop();
						}
						
					}
				}
				
				
				
				repaint();
				
			}
		});
		
		horloge.start();
		
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_RIGHT && direction != 3) direction = 1;
				if(e.getKeyCode() == KeyEvent.VK_DOWN && direction != 4) direction = 2;
				if(e.getKeyCode() == KeyEvent.VK_LEFT && direction != 1) direction = 3;
				if(e.getKeyCode() == KeyEvent.VK_UP && direction != 2) direction = 4;
				
			}
		});
		
		
		
		
		
		scoreText = new JLabel();
		
		this.setContentPane(panneau);
		panneau.setBackground(new Color(0x022F49));
		this.setVisible(true);
	}
	
	void dessinerCellule(int x, int y, Color c, Graphics g)
	{
		g.setColor(c);
		g.fillRect(marge + x*largeur, marge + y*largeur, largeur, largeur);
		//g.fill3DRect(marge + x*largeur, marge + y*largeur, largeur, largeur, rootPaneCheckingEnabled);
	}
	
	
	void dessinerCellule(Cellule c, Graphics g)
	{
		g.setColor(c.couleur);
		g.fillRect(marge + c.x*largeur, marge + c.y*largeur, largeur, largeur);
		//g.fill3DRect(marge + c.x*largeur, marge + c.y*largeur, largeur, largeur, rootPaneCheckingEnabled);
	}
	
	void dessinerGrille(Graphics g)
	{
		
		
		
		// all lines
		
//		for(int i = 0; i < nbCaseX + 1; i++)
//		{
//			g.drawLine(marge + i*largeur, marge, marge + i*largeur, marge + nbCaseY*largeur);
//		}
//		
//		for(int i = 0; i < nbCaseY + 1; i++)
//		{
//			g.drawLine(marge , marge + i*largeur, marge + nbCaseX*largeur, marge + i*largeur);
//		}
		
		//body = new ArrayList<Cellule>();
		for(int i = 0;i < nbCaseX; i++)
		{
			for(int j = 0; j < nbCaseY; j++)
			{
				if( (i+j)%2 == 0)
				{
					g.setColor(new Color(0x012134));
					g.fillRect(i*largeur +marge, j*largeur + marge, largeur, largeur);
//					Cellule c = new Cellule(i, j, new Color(0x012134));
//					body.add(c);
//					dessinerCellule(c, g);
				}
					
			}
		}
		
		g.setColor(new Color(0xC3D3F2));


		// the 2 extremes vertical lines
		g.fillRect(marge-largeur, marge, largeur , nbCaseY*largeur );
		g.fillRect(marge + nbCaseX*largeur, marge, largeur, nbCaseY*largeur);

		// the 2 extremes horizontal lines
		g.fillRect(marge -largeur , marge - largeur,nbCaseX*largeur + 2*largeur, largeur);
		g.fillRect(marge - largeur ,marge + nbCaseY*largeur , nbCaseX*largeur + 2*largeur, largeur);


	}

	
	
	void dessinerRepas( Graphics g)
	{
		//dessinerCellule(repas, g);
		g.setColor(repas.couleur);
		g.fillOval(marge + repas.x*largeur, marge + repas.y*largeur, largeur, largeur);
		//g.fill3DRect(marge + repas.x*largeur, marge + repas.y*largeur, largeur, largeur, rootPaneCheckingEnabled);
		
		
	}
	
	void dessinerObstacle(Graphics g)
	{
		for(int i = 0;i < obstacle.size(); i++)
			dessinerCellule(obstacle.get(i), g);
	}

	
	
	
	void printEndMessage()
	{
		gameOver = new JLabel();

		gameOver.setText("<html><h1>GAME OVER!&emsp;&emsp;LOOSER!</h1></html>");
		gameOver.setBounds(marge + 350,marge - 100 , 200, 100);
		gameOver.setForeground(Color.red);
		gameOver.setFont(new Font("Verdana", Font.BOLD, 18));
		//gameOver.setOpaque(true);
		//gameOver.setBackground(Color.cyan);
		add(gameOver);
	}
	
	
	public static void main(String[] args)
	{
		Snake s = new Snake();

	}
	
	
	
	
	
	class Cellule
	{
		int x,y;
		Color couleur;
		public Cellule(int x, int y, Color couleur) {
			super();
			this.x = x;
			this.y = y;
			this.couleur = couleur;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public Color getCouleur() {
			return couleur;
		}
		public void setCouleur(Color couleur) {
			this.couleur = couleur;
		}
	}

}
