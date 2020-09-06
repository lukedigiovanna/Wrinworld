package quests;

import entities.npc.*;
import items.weapons.*;
import game.Game;
import misc.*;

public class MainQuestBook extends QuestBook {
	private static final long serialVersionUID = 1L;

	private Quest[] quests = {
		new Quest("Getting Started","Talk to the greeter at the start","-3xp&-x1 Wooden Sword", 
			new Questable() { 
				public void init() {
					
				}
				
				public boolean completed() {
					NPC greeter = getGame().getNPC("greeter");
					if (greeter != null && greeter.hasSpoken())
						return true;
					return false;
				} 
			
				public void reward() {
					getPlayer().getExperience().addXP(3);
					getPlayer().getInventory().add(new BroadSword(BroadSword.Type.WOODEN));
				} 
				
				public String progress() {
					if (completed())
						return "Talked to greeter";
					return "Not talked to greeter";
				}
			}
		),
		new Quest("Getting an Upgrade","Acquire a stone sword","-5xp&-Tattered Vest",
			new Questable() { 
				public void init() {
			
				}
			 
				public boolean completed() {
					return getPlayer().getInventory().has("stone_sword") > 0;
				}
				
				public void reward() {
					getPlayer().getExperience().addXP(5);
					getPlayer().getInventory().add(null);
				} 	
				
				public String progress() {
					return "Stone swords: "+getPlayer().getInventory().has("stone_sword")+"/1";
				}
			}
		),
		new Quest("Monster Hunter","Kill 5 monsters","-5px&-Bow",
			new Questable() { 
				private int initialCount = -1;
				
				public void init() {
					initialCount = getPlayer().statistic.getMonstersKilled();
				}
				
				public boolean completed() {
					return (getPlayer().statistic.getMonstersKilled()-initialCount >= 5);
				}
				
				public void reward() {
					getPlayer().getExperience().addXP(5);
					getPlayer().getInventory().add(new Bow());
				} 
				
				public String progress() {
					if (initialCount < 0) 
						return "Monsters Killed: 0/5";
					return "Monsters Killed: "+(getPlayer().statistic.getMonstersKilled()-initialCount)+"/5";
				}
			}
		),
		new Quest("Traveler","Walk a total of 200 meters","-15px&-Tattered Boots&-20 coins",
				new Questable() { 
					private double initialCount = -1;
					
					public void init() {
						initialCount = getPlayer().statistic.getDistanceTraveled();
					}
					
					public boolean completed() {
						return (getPlayer().statistic.getDistanceTraveled()-initialCount >= 200);
					}
					
					public void reward() {
						getPlayer().getExperience().addXP(15);
						getPlayer().getInventory().add(new Bow());
						getPlayer().addMoney(20);
					} 
					
					public String progress() {
						if (initialCount < 0)
							return "Distance Traveled: 0/200";
						return "Distance Traveled: "+MathUtils.round(getPlayer().statistic.getDistanceTraveled()-initialCount,0.01)+"/200";
					}
				}
			)
	};
	
	public MainQuestBook(Game game) {
		super(game);
		this.setQuests(quests);
	}
}
