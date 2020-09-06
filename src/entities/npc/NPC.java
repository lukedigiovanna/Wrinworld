package entities.npc;

import display.popup.Conversation;
import display.popup.PopupBar;
import entities.*;
import misc.Dimension;

//conversates with the player
//NON-PLAYER CHARACTER -- a character to the story. can be an enemy
public abstract class NPC extends Entity {
	private static final long serialVersionUID = 1L;

	protected String name, title;
	private Conversation convo;
	protected boolean alerted = true;
	
	public NPC(Character c, double x, double y) {
		super(x,y,new Dimension(0.5,1.0));
		this.name = c.name;
		this.convo = c.convo;
		
		//unlimited health
		this.setHealth(new Health(Health.INVULNERABLE));
		this.setInvulnerable(true);
		this.showHealthBar = false;
		
		this.team = Entity.Team.NPC;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void speak() {
		while (!convo.isSpoken())
			PopupBar.popup(convo.getNextBar());
	}
	
	public boolean hasSpoken() {
		if (convo == null)
			return true;
		return convo.isSpoken();
	}
	
	public void update() {
		super.update();
	}
	
	private static final String[] GREETER_0 = {
		"Traveler please help!",
		"Portals have started popping up and evil monsters are spewing out of them!",
		"We don't know how to destroy them but we know the shooting them does no good and only makes them worse!",
		"A warrior like yourself should be able to help us, if you are willing.",
		"Here is a spare sword. I know it is bad but it is all we have; I'm sure you can find something better."
	}; 
	
	private static final String[] JOHN_0 = {
			"i am jon and i am weak"
	};
	
	//holds data for conversations.. not necessarily specific actions or behaviors of extended entities
	public enum Character {
		GREETER("Greeter",GREETER_0),
		JOHN("John",JOHN_0);
		
		String name, title;
		Conversation convo;
		
		Character(String name, String[] msgs) {
			this.name = name;
			this.convo = new Conversation(name,msgs);
		}
	}
}
