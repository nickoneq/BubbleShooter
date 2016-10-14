package BubleShooter;

import java.awt.*;

public class Wave {
	//Fields
	private int waveNumber;
	
	private long waveTimer;
	private long waveDelay;
	private long waveTimerDiff;
	
	private int waveMultiplier;
	
	private String waveText;
	
	//Constructor
	public Wave(){
		waveNumber = 1;
		waveMultiplier = 5;
		waveTimer = 0;
		waveDelay = 5000;
		waveTimerDiff = 0;
		
		waveText = " Â Î Ë Í À -";
	}
	
	//Function
	public void createEnemies(){
		int enemyCount = waveNumber*waveMultiplier;
		if (waveNumber < 4) {
			while(enemyCount > 0){
				int type = 1;
				int rank = 1;
				GamePanel.enemies.add(new Enemy(type, rank));
				enemyCount -= type*rank;
			}
		}
	}
	
	public void update(){
		if (GamePanel.enemies.size() == 0 & waveTimer == 0) {
			waveTimer = System.nanoTime();
		}
		
		if (waveTimer > 0) {
			waveTimerDiff += (System.nanoTime()-waveTimer)/1000000;
			waveTimer = System.nanoTime();
		}
		
		if (waveTimerDiff > waveDelay) {
			createEnemies();
			waveTimer = 0;
			waveTimerDiff = 0;
		}
	}
	
	public boolean showWave(){
		if (waveTimer != 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public void draw(Graphics2D g){
		double divider = waveDelay/180;
		double alpha = waveTimerDiff/divider;
		alpha = 255*Math.sin(Math.toRadians(alpha));
		
		if (alpha < 0) {
			alpha = 0;
		}
		
		if (alpha > 255) {
			alpha = 255;
		}
		
		g.setFont(new Font("consolas", Font.PLAIN, 20));
		
		g.setColor(new Color(255, 255, 255, (int)alpha));
		g.drawString(waveText, GamePanel.WIDTH/2, GamePanel.HEIGHT/2);
	}
}
