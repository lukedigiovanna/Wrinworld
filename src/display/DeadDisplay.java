package display;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import entities.Statistics;
import game.GameController;
import main.Program;
import misc.Graphics2;
import sound.MusicManager;

public class DeadDisplay extends Display {
	private boolean gameOver = false;
	
	public void individualInit() {
		MusicManager.changeSong("gameover");
		
		gameOver = GameController.game().player().getLives() < 0;
		
		this.addListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					// update high score
					if (GameController.game().player().getExperience().getXP() > main.Program.getHighscore()) {
						main.Program.writeHighscore(GameController.game().player().getExperience().getXP());
					}	
					GameController.endGame();
					DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
					break;
				}
			}
		});
		
		this.addListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!gameOver && GameController.game().getEntities().portalCount() > 0) {
					GameController.game().resetPlayer();
					DisplayController.setDisplay(DisplayController.GAME_DISPLAY);
				} else {
					// update high score
					if (GameController.game().player().getExperience().getXP() > main.Program.getHighscore()) {
						main.Program.writeHighscore(GameController.game().player().getExperience().getXP());
					}
					GameController.endGame();
					DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
				}
			}
		});
	}
	
	public void individualUpdate() {
		
	}
	
	@Override
	public void repaint() {
		Graphics g = this.getGraphics();
		this.fillScreen(DisplayController.getDisplay(DisplayController.GAME_DISPLAY));
		this.fillScreen(new Color(125,125,125,125));
		g.setFont(new Font("Arial",Font.BOLD,42));
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial",Font.ITALIC,30));
		if (GameController.game().getEntities().portalCount() <= 0) {
			g.setColor(Color.YELLOW);
			Graphics2.outlineText(g, "YOU WON!", Graphics2.centerX(g, "YOU WON!"), 200, Color.BLACK, 2);
			int playerScore = GameController.game().player().getExperience().getXP();
			String score = "Score: "+playerScore;
			if (playerScore > Program.getHighscore()) 
				score += " | New Highscore!";
			else
				score += " | Highscore: "+Program.getHighscore();
			Graphics2.outlineText(g, score, Graphics2.centerX(g, score), 230, Color.BLACK, 2);
			Statistics stats = GameController.game().player().statistic;
			String[] statStrings = {
				"Distance Traveled: "+Math.round(stats.getDistanceTraveled()),
				"Animals Killed: "+stats.getAnimalsKilled(),
				"Monsters Killed: "+stats.getMonstersKilled(),
				"Damage Dealt: "+Math.round(stats.getDamageDealt()),
				"Damage Taken: "+Math.round(stats.getDamageTaken()),
				"Health Healed: "+Math.round(stats.getHealthHealed()),
				"Lifetime Earnings: "+stats.getLifetimeEarnings(),
				"Entities Killed: "+stats.getEntitiesKilled()
			};
			g.setFont(new Font("Arial",Font.ITALIC,18));
			g.setColor(Color.LIGHT_GRAY);
			for (int i = 0; i < statStrings.length; i++) {
				String s = statStrings[i];
				int x, y;
				if (i < statStrings.length/2) {
					x = this.getWidth()/2-30-g.getFontMetrics().stringWidth(statStrings[0]);
					y = 250 + i * 25;
				} else {
					x = this.getWidth()/2 + 30;
					y = 250 + (i - statStrings.length/2) * 25;
				}
				Graphics2.outlineText(g, s, x, y, Color.BLACK);
			}
			g.setColor(Color.GRAY);
			g.setFont(new Font("Arial",Font.ITALIC,30));
			Graphics2.outlineText(g, "* Click to return home *", Graphics2.centerX(g, "* Click to return home *"), 380, Color.BLACK);
		} else if (!gameOver) {
			g.setColor(Color.RED);
			Graphics2.outlineText(g, "YOU DIED!", Graphics2.centerX(g, "YOU DIED!"), 200, Color.BLACK, 2);
			g.setColor(Color.GRAY);
			Graphics2.outlineText(g, "* Click to respawn *", Graphics2.centerX(g, "* Click to respawn *"), 300, Color.BLACK);
			Graphics2.outlineText(g, "* Press escape to return to home *", Graphics2.centerX(g, "* Press escape to return to home *"), 350, Color.BLACK);
		} else {
			g.setColor(Color.RED);
			Graphics2.outlineText(g, "YOU DIED!", Graphics2.centerX(g, "YOU DIED!"), 200, Color.BLACK, 2);
			g.setColor(Color.GREEN.darker());
			g.setFont(new Font("Arial", Font.BOLD, 17));
			int playerScore = GameController.game().player().getExperience().getXP();
			String score = "Score: "+playerScore;
			if (playerScore > Program.getHighscore()) 
				score += " | New Highscore!";
			else
				score += " | Highscore: "+Program.getHighscore();
			Graphics2.outlineText(g, score, Graphics2.centerX(g, score), 222, Color.BLACK, 2);
			Statistics stats = GameController.game().player().statistic;
			String[] statStrings = {
				"Distance Traveled: "+Math.round(stats.getDistanceTraveled()),
				"Animals Killed: "+stats.getAnimalsKilled(),
				"Monsters Killed: "+stats.getMonstersKilled(),
				"Damage Dealt: "+Math.round(stats.getDamageDealt()),
				"Damage Taken: "+Math.round(stats.getDamageTaken()),
				"Health Healed: "+Math.round(stats.getHealthHealed()),
				"Lifetime Earnings: "+stats.getLifetimeEarnings(),
				"Entities Killed: "+stats.getEntitiesKilled()
			};
			g.setFont(new Font("Arial",Font.ITALIC,18));
			g.setColor(Color.LIGHT_GRAY);
			for (int i = 0; i < statStrings.length; i++) {
				String s = statStrings[i];
				int x, y;
				if (i < statStrings.length/2) {
					x = this.getWidth()/2-30-g.getFontMetrics().stringWidth(statStrings[0]);
					y = 250 + i * 25;
				} else {
					x = this.getWidth()/2 + 30;
					y = 250 + (i - statStrings.length/2) * 25;
				}
				Graphics2.outlineText(g, s, x, y, Color.BLACK);
			}
			g.setColor(Color.GRAY);
			g.setFont(new Font("Arial",Font.ITALIC,30));
			Graphics2.outlineText(g, "* Click to return home *", Graphics2.centerX(g, "* Click to return home *"), 380, Color.BLACK);
		}
	}
}
