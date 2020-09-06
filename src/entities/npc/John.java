package entities.npc;

import java.awt.Image;

import entities.Entity;
import main.Program;

public class John extends NPC {
	private static final long serialVersionUID = 1L;

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
