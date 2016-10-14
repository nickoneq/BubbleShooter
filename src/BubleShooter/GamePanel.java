package BubleShooter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.util.*;

public class GamePanel extends JPanel implements Runnable{	
	//Field	
	public static int WIDTH = 500;
	public static int HEIGHT = 500;
	
	private Thread thread;
	
	private BufferedImage image;
	private Graphics2D g;
	
	public static GameBack background;
	public static Player player;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static Wave wave;
	
	
	//Constructor	
	public GamePanel(){
		super();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		addKeyListener(new Listeners());
	}
	

	//Functions	
	
	public void start(){
		thread = new Thread(this);
		thread.start();		
	}
	
	@Override
	public void run() {		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		background = new GameBack();
		player = new Player();
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		wave = new Wave();
		
		while(true){
			gameUpdate();
			gameRender();
			gameDraw();
						
			try {
				thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}		
	}
	
	public void gameUpdate(){
		//Background update
		background.update();
		
		//Player update
		player.update();
		
		//Bullets update
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			boolean remove = bullets.get(i).remove();
			if (remove) {
				bullets.remove(i);
				i--;
			}
		}
		
		//Enemies update
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}
		
		//Bullets-enemies collide
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			double ex = e.getX();
			double ey = e.getY();
			
			for (int j = 0; j < bullets.size(); j++) {
				Bullet b = bullets.get(j);
				double bx = b.getX();
				double by = b.getY();
				
				double dx = ex - bx;
				double dy = ey - by;
				double dist = Math.sqrt(dx*dx+dy*dy);
				if ((int)dist <= e.getR()+b.getR()) {
					e.hit();
					bullets.remove(j);
					j--;
					
					boolean remove = e.remove();
					if (remove) {
						enemies.remove(i);
						i--;
						break;
					}
				}
			}						
		}
		
		//Player-enemy collides
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			double ex = e.getX();
			double ey = e. getY();
			
			double px = player.getX();
			double py = player.getY();
			double dx = ex - px;
			double dy = ey - py;
			double dist = Math.sqrt(dx * dx + dy * dy);
			if ((int)dist <= e.getR()+player.getR()) {
				e.hit();
				player.hit();
				boolean remove = e.remove();
				if (remove) {
					enemies.remove(i);
					i--;
				}
			}
		}
		
		//Wave update
		wave.update();
						
	}
	
	public void gameRender(){
		//Background draw
		background.draw(g);
		
		//Player draw
		player.draw(g);
		
		//Bullets draw
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}
		
		//Enemies draw
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		//Wave draw
		if (wave.showWave()){
			wave.draw(g);
		}		
	}	
	
	private void gameDraw(){
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
}
