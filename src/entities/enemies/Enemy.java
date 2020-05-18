package entities.enemies;

import entities.*;
import entities.player.Player;
import main.Program;
import misc.MathUtils;

import java.util.*;

public abstract class Enemy extends Entity {
	public Enemy(double x, double y, double width, double height) {
		super(x,y,width,height);
		team = Entity.Team.ENEMY;
		this.setHasCollision(true);
	}	
	
	private int timeAwayFromPlayer = 0;
	
	public void update() {
		//this is to ensure there is not an over population of enemies in the world
		if (this.distance(this.getGame().player()) > 20)
			this.destroy();
		
		if (this.distance(this.getGame().player()) > 10)
			timeAwayFromPlayer++;
		else
			timeAwayFromPlayer = 0;
		if (timeAwayFromPlayer > Program.TICKS_PER_SECOND*30)
			this.destroy();
		
		super.update();
	}
	
	public void trackAroundTeammates() {
		for (Entity e : this.getGame().entities().get())
			if (e instanceof Enemy)
				this.trackAroundTeammate((Enemy)e);
	}
	
	//this method is to be called in the attacking period of an enemy
	//it sets the velocity such that it will go around the teammate.
	//this method should be called after routine velocity sets and before the "move" method is called
	private void trackAroundTeammate(Enemy teammate) {
		//if the line of sight between the player and this enemy is obstructed by the teammate then move around the teammate
		double thisX = this.getX(), thisY = this.getY();
		double theirWidth = teammate.getWidth(), theirHeight = teammate.getHeight()/2;
		double theirX = teammate.getX(), theirY = teammate.getY()+theirHeight;
		Player player = this.getGame().player();
		double playerWidth = player.getWidth(), playerHeight = player.getHeight();
		double playerX = player.getCenterX(), playerY = player.getCenterY();
		double leftBound = MathUtils.min(theirX, thisX), rightBound = MathUtils.max(theirX, thisX), 
				topBound = MathUtils.min(theirY, thisY), bottomBound = MathUtils.max(theirY, thisY);
		if (!(theirX < rightBound && theirX+theirWidth > leftBound))
			if (!(theirY < bottomBound && theirY+theirHeight < topBound))
				return; //were out of the bounds to even be in the line of sight. so just quit
	}
}
