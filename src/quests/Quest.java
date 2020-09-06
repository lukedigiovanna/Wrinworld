package quests;

import java.io.Serializable;

import game.Game;

//problem with this method is youre going to have many, many quests
public class Quest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//all quests have: a name, description, end condition, rewards, activation state
	private String name, description, rewards;
	private Questable q;
	private QuestBook book = null;
	private Game game;
	
	public void setBook(QuestBook book) {
		this.book = book;
		game = book.getGame();
	}
	
	public Quest(String name, String description, String rewards, Questable q) {
		this.name = name;
		this.description = description;
		this.rewards = rewards;
		this.q = q;
	}
	
	private boolean active = false;
	private boolean completed = false;
	
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getRewards() { return rewards; }
	public String getProgress() { return q.progress(); }
	
	public void update() {
		completed = (active && q.completed()) || completed;
		if (active && this.isCompleted()) {
			active = false;
			q.reward();
			game.console().log("<c=y><s=b>Completed quest: "+name);
			game.console().log("<c=g><s=i>Rewards:");
			String[] rewards = getRewards().split("&");
			for (String s : rewards) {
				game.console().log("  <c=b>"+s);
			}
			if (book != null)
				book.getGame().launchFireworks();
		}
	}
	
	public void activate() {
		if (active || completed)
			return;
		active = true;
		q.init();
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public String getStatus() {
		if (!isCompleted()) {
			if (active)
				return "In Progress";
			else
				return "Not Started";
		} else
			return "Completed";
	}
}
