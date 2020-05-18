package entities.player;

import java.io.Serializable;

public class Experience implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int xp, level, curXP, xpToLevelUp;
	
	public Experience() {
		xpToLevelUp = 5;
	}
	
	public int getXP() {
		return xp;
	}
	
	public int getLevelXP() {
		return curXP;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getXpToLevelUp() { 
		return xpToLevelUp;
	}
	
	public void addXP(int amount) {
		this.xp+=amount;
		this.curXP+=amount;
		if (this.curXP >= this.xpToLevelUp)
			levelUp();
	}
	
	public void levelUp() {
		this.curXP%=this.xpToLevelUp;
		this.xpToLevelUp = (int)(Math.pow(level+1,1.8)+10);
		this.level++;
	}
	
	public void addLevel() {
		addXP(this.xpToLevelUp-this.curXP);
	}
	
	public String toString() {
		String s = this.curXP+"/"+this.xpToLevelUp+" XP (lvl. "+this.level+")";
		return s;
	}
}
