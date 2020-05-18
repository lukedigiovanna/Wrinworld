package entities.npc;

import java.awt.Image;

import entities.Entity;
import main.Program;

public class John extends NPC {

	public John(double x, double y) {
		super(NPC.Character.JOHN, x, y);
	}

	@Override
	public void individualUpdate() {
		
	}

	@Override
	public Entity replicate() {
		return null;
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/john.png");
	}

}
