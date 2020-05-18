package quests;

import java.io.Serializable;

import entities.player.Player;
import game.Game;

public abstract class QuestBook implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Quest[] quests;
	private Game game;
	private Player player;
	
	public QuestBook(Game game) {
		this.player = game.player();
		this.game = game;
	}
	
	public void update() {
		for (Quest q : quests)
			q.update();
	}
	
	public boolean activateQuest(int index) {
		if (activeQuest() != null) {
			game.console().log("<c=r>Already started quest "+activeQuest().getName());
			return false;
		}
		quests[index].activate();
		return true;
	}
	
	public Quest getQuest(int index) {
		if (index < 0 && index > quests.length-1)
			return null;
		return quests[index];
	}
	
	public Quest activeQuest() {
		for (Quest q : quests) {
			if (q.isActive())
				return q;
		}
		return null;
	}
	
	public int length() {
		return quests.length;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setQuests(Quest[] quests) {
		this.quests = quests;
		for (Quest q : quests) {
			q.setBook(this);
		}
	}
}
