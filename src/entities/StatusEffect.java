package entities;

//includes things like regeneration, strength boost, weakness, slowness..
public class StatusEffect {
	private Entity owner;
	private int age = 0, lifeSpan = 0;
	private boolean active = true;
	private Effect effect;
	
	public StatusEffect(Effect effect, int lifeSpan, Entity owner) {
		this.effect = effect;
		this.lifeSpan = lifeSpan;
		this.owner = owner;
	}
	
	public void update() {
		if (age >= lifeSpan) {
			//return entity to normal 
			switch (effect) {
			case SLOWNESS:
				owner.setSpeedMultiplier(owner.getSpeedMultiplier()+0.5);
				break;
			case SPEED:
				owner.setSpeedMultiplier(owner.getSpeedMultiplier()-0.5);
			}
			active = false;
		}
		if (!active)
			return;
		
		switch (effect) {
		case REGENERATION:
			if (this.age%20 == 0)
				owner.heal(2);
			break;
		case HEAL:
			owner.heal(10);
			break;
		case SLOWNESS:
			if (age == 0)
				owner.setSpeedMultiplier(owner.getSpeedMultiplier()-0.5);
			break;
		case SPEED:
			if (age == 0)
				owner.setSpeedMultiplier(owner.getSpeedMultiplier()+0.5);
			break;
		case STRENGTH:
			break;
		case WEAKNESS:
			break;
		}
		
		age++;
	}
	
	public boolean notActive() {
		return !active;
	}
	
	public static enum Effect {
		REGENERATION("Regeneration"), 
		HEAL("Heal"), 
		SLOWNESS("Slowness"), 
		STRENGTH("Strength"), 
		WEAKNESS("Weakness"),
		SPEED("Speed");
		
		String name;
		
		Effect(String name) {
			this.name = name;
		}
	}
}
